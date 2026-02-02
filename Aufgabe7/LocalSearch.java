import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Statische Funktionssammlung für lokale Suche.
 * Implementiert Logik zur Verbesserung der bestehenden Lösung durch intensive Suche in der Nachbarschaft.
 * Die Klasse ist funktional und ohne Zustand.
 */
public class LocalSearch {
    public static Solution explorePatch(
            Solution center,
            int numberOfBees,
            AlgorithmConfig config,
            NeighborhoodGenerator generator,
            ObjectiveFunction f,
            ConstraintValidator validator,
            Comparator<Solution> comparator
    ){
        //Projektion des Skalierungsfaktors s auf die Dimensionen des Suchraums
        double [] ranges  = IntStream.range(0, config.a())
                .mapToDouble(i -> {
                    double min = config.w()[i][0];
                    double max = config.w()[i][1];
                    return (max - min) * config.s();
                })
                .toArray();

        Optional<Solution> bestNeighbor = IntStream.range(0, numberOfBees)
                //Abbildung eines Index auf eine Position im Suchraum
                .mapToObj(i -> generator.generate(center.params(), ranges))
                //Filterung ungültiger Zustände
                .filter(pos -> validator.isValid(pos, config.w()))
                //Transformation in Ergebnisraum
                .map(pos -> new Solution(pos, f.apply(pos)))
                //Reduktion des Datenstroms auf bestes Element laut comparator
                .max(comparator);

        //garantiert ein mindestens so gutes Ergebnis wie der Eingangszustand
        if(bestNeighbor.isPresent() && comparator.compare(bestNeighbor.get(), center) > 0){
            return bestNeighbor.get();
        } else {
            return center;
        }
    }
}
