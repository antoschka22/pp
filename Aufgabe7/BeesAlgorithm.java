import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * Funktionale Sammlung von Operationen zur Durchführung des Bienenalgorithmus
 * Enthält keine Zustände, sondern reine Transformationsvorschriften
 */
public class BeesAlgorithm {

    private static final Random random = new Random();

    /*
     * Transformation der Eingangsparameter (Konfiguration, Funktionen) in eine Liste
     * optimaler Lösungen durch iterative Evolution einer Population
     */
    public static List<Solution> solve(
            AlgorithmConfig config,
            ObjectiveFunction f,
            ConstraintValidator validator,
            NeighborhoodGenerator generator,
            Comparator<Solution> comparator
    ) {
        // Generierung eines unendlichen Stroms an Zufallslösungen,
        // Begrenzung auf Populationsgröße n und Materialisierung als Startliste
        List<Solution> initialPopulation = Stream.generate(() -> generateRandomSolution(config, f, validator))
                .limit(config.n())
                .collect(Collectors.toList());

        // Erzeugung einer Sequenz von Populationen (Stream von Listen), wobei jede Population
        // durch Anwendung der nextGeneration-Funktion aus der vorherigen hervorgeht
        // Der Strom wird auf t Schritte begrenzt und auf das letzte Element (die finale Population) reduziert
        List<Solution> finalPopulation = Stream.iterate(initialPopulation,
                        currentPop -> nextGeneration(currentPop, config, f, validator, generator, comparator))
                .limit(config.t() + 1)
                .reduce((first, second) -> second)
                .orElse(initialPopulation);

        // Sortierung der finalen Population nach Güte und Projektion auf die r besten Elemente
        return finalPopulation.stream()
                .sorted(comparator.reversed())
                .limit(config.r())
                .collect(Collectors.toList());
    }

    /*
     * Abbildung einer bestehenden Population auf eine neue Generation durch
     * Anwendung von lokaler Suche auf besten Kandidaten und Erneuerung (Scouts)
     * des Rests
     */
    private static List<Solution> nextGeneration(
            List<Solution> currentPopulation,
            AlgorithmConfig config,
            ObjectiveFunction f,
            ConstraintValidator validator,
            NeighborhoodGenerator generator,
            Comparator<Solution> comparator
    ) {
        // Sortierung des Eingabestroms, um die Verarbeitungspriorität festzulegen
        // (Beste Lösungen kommen an den Anfang für die Partitionierung)
        List<Solution> sortedPopulation = currentPopulation.stream()
                .sorted(comparator.reversed())
                .toList();

        // Teilstrom 1: Abbildung der Top-e Lösungen auf verbesserte Lösungen
        // durch intensive lokale Suche (p Bienen)
        Stream<Solution> eliteBees = sortedPopulation.stream()
                .limit(config.e())
                .map(bee -> LocalSearch.explorePatch(bee, config.p(), config, generator, f, validator, comparator));

        // Teilstrom 2: Abbildung der nachfolgenden (m-e) Lösungen auf verbesserte Lösungen
        // durch weniger intensive lokale Suche (q Bienen)
        Stream<Solution> selectedBees = sortedPopulation.stream()
                .skip(config.e())
                .limit(config.m() - config.e())
                .map(bee -> LocalSearch.explorePatch(bee, config.q(), config, generator, f, validator, comparator));

        // Teilstrom 3: Generierung neuer Zufallslösungen (Scouts) für den Rest der Population (n-m),
        // um lokale Optima zu verlassen (Exploration)
        int scoutsCount = config.n() - config.m();
        Stream<Solution> scoutBees = Stream.generate(() -> generateRandomSolution(config, f, validator))
                .limit(scoutsCount);

        // Konkatenation der drei Teilströme zu einer neuen Gesamtpopulation
        return Stream.concat(
                Stream.concat(eliteBees, selectedBees),
                scoutBees
        ).collect(Collectors.toList());
    }

    /*
     * Erzeugung einer validen Zufallslösung durch Suche in einem unendlichen Strom
     * von Zufallsparametern bis zur ersten Gültigkeit
     */
    private static Solution generateRandomSolution(
            AlgorithmConfig config,
            ObjectiveFunction f,
            ConstraintValidator validator
    ) {
        // Erzeugung eines Stroms von Parameter-Arrays, Filterung ungültiger Arrays
        // und Abbildung auf Solution-Objekte
        // Die Pipeline terminiert beim Finden des ersten validen Elements
        return Stream.generate(() ->
                        IntStream.range(0, config.a())
                                .mapToDouble(i -> {
                                    double min = config.w()[i][0];
                                    double max = config.w()[i][1];
                                    return min + random.nextDouble() * (max - min);
                                })
                                .toArray()
                )
                .filter(params -> validator.isValid(params, config.w()))
                .map(params -> new Solution(params, f.apply(params)))
                .findFirst()
                .orElseThrow();
    }
}