import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementierung von Bees Algorithm im funktionalen Stil
 * Diese Klasse speichert keinen Zustand und nutzt ausschließlich statische Methoden
 * sowie Java Streams
 */
public class BeesAlgorithm {

    // Ein Random-Objekt für die Erzeugung von Zufallszahlen
    private static final Random random = new Random();

    /**
     * Führt den Bienenalgorithmus aus, um eine optimale Lösung für die gegebene Zielfunktion zu finden
     *
     * @param config    Die Konfiguration des Algorithmus (Parameter n, m, e, etc.)
     * @param f         Die zu optimierende Zielfunktion
     * @param validator Prüft, ob eine Lösung innerhalb der Grenzen liegt
     * @param generator Erzeugt Nachbarschaftslösungen
     * @param comparator Vergleicht zwei Lösungen (definiert, was "besser" ist)
     * @return Eine Liste der r besten gefundenen Lösungen
     */
    public static List<Solution> solve(
            AlgorithmConfig config,
            ObjectiveFunction f,
            ConstraintValidator validator,
            NeighborhoodGenerator generator,
            Comparator<Solution> comparator
    ) {
        // 1. Erzeuge n zufällige Startlösungen
        List<Solution> initialPopulation = Stream.generate(() -> generateRandomSolution(config, f, validator))
                .limit(config.n())
                .collect(Collectors.toList());

        // 2. Iteriere t Schritte
        // Stream.iterate erzeugt eine unendliche Sequenz von Populationen, basierend auf der vorherigen
        // Wir begrenzen dies auf t + 1 (Startpopulation + t Schritte) und nehmen das letzte Element
        List<Solution> finalPopulation = Stream.iterate(initialPopulation,
                        currentPop -> nextGeneration(currentPop, config, f, validator, generator, comparator))
                .limit(config.t() + 1)
                .reduce((first, second) -> second) // Reduktion auf den letzten Zustand (nach t Schritten)
                .orElse(initialPopulation);

        // 3. Die r besten Lösungen der finalen Population werden zurückgegeben
        return finalPopulation.stream()
                .sorted(comparator.reversed()) // Beste zuerst
                .limit(config.r())
                .collect(Collectors.toList());
    }

    /**
     * Berechnet die nächste Generation von Bienen (einen Iterationsschritt).
     *
     * @param currentPopulation Die aktuelle Liste von Lösungen (Bienen).
     * @return Die neue Liste von Lösungen nach globaler und lokaler Suche.
     */
    private static List<Solution> nextGeneration(
            List<Solution> currentPopulation,
            AlgorithmConfig config,
            ObjectiveFunction f,
            ConstraintValidator validator,
            NeighborhoodGenerator generator,
            Comparator<Solution> comparator
    ) {
        // Schritt A: Sortieren der Population (Beste zuerst)
        // Wir nutzen comparator.reversed(), da der Comparator typischerweise aufsteigend sortiert (kleiner < größer).
        // Für 'max' bedeutet das, das größte Element steht hinten. Wir wollen es vorne haben.
        List<Solution> sortedPopulation = currentPopulation.stream()
                .sorted(comparator.reversed())
                .toList();

        // Schritt B: Aufteilung und lokale Suche

        // 1. Exzellente Felder (Top e): Rekrutiere p Bienen für lokale Suche
        Stream<Solution> eliteBees = sortedPopulation.stream()
                .limit(config.e())
                .map(bee -> LocalSearch.explorePatch(bee, config.p(), config, generator, f, validator, comparator));

        // 2. Gute Felder (Nächste m - e): Rekrutiere q Bienen für lokale Suche
        Stream<Solution> selectedBees = sortedPopulation.stream()
                .skip(config.e())
                .limit(config.m() - config.e())
                .map(bee -> LocalSearch.explorePatch(bee, config.q(), config, generator, f, validator, comparator));

        // 3. Globale Suche (Rest n - m): Ersetze die restlichen Bienen durch komplett neue Scouts
        // Diese suchen rein zufällig im gesamten Suchraum (nicht in einer Nachbarschaft).
        int scoutsCount = config.n() - config.m();
        Stream<Solution> scoutBees = Stream.generate(() -> generateRandomSolution(config, f, validator))
                .limit(scoutsCount);

        // Schritt C: Zusammenführen der Ergebnisse
        // concat kann immer nur zwei Streams verbinden, daher verschachteln.
        return Stream.concat(
                Stream.concat(eliteBees, selectedBees),
                scoutBees
        ).collect(Collectors.toList());
    }

    /**
     * Hilfsmethode: Erzeugt eine einzelne zufällige, valide Lösung im gesamten Suchraum
     * Wird für die Initialisierung und die globalen Scouts verwendet
     */
    private static Solution generateRandomSolution(
            AlgorithmConfig config,
            ObjectiveFunction f,
            ConstraintValidator validator
    ) {
        // Wir probieren so lange, bis wir eine valide Lösung finden (Simple Rejection Sampling für die Validierung)
        // Hinweis: In einem rein funktionalen Kontext ohne Rekursionslimit könnte dies theoretisch hängen,
        // ist aber praktisch für diese Aufgabe die Standardlösung.
        return Stream.generate(() -> {
                    double[] params = new double[config.a()];
                    for (int i = 0; i < config.a(); i++) {
                        double min = config.w()[i][0];
                        double max = config.w()[i][1];
                        params[i] = min + random.nextDouble() * (max - min);
                    }
                    return params;
                })
                .filter(params -> validator.isValid(params, config.w()))
                .map(params -> new Solution(params, f.apply(params)))
                .findFirst()
                .orElseThrow(); // Sollte nicht passieren, da Stream unendlich
    }
}