import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Test {

    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("--- Starte Tests für Programmieraufgabe 7 ---");

        // --- Szenario 1: Sinus Funktion (1D) ---
        System.out.println("\n>>> Starte Optimierung 1: Sinus Funktion (1D)");

        // Konfiguration: a=1, t=500, n=50, m=15, e=5, p=10, q=5, s=0.01, r=10
        double[][] boundsSine = {{-1800.0, 1800.0}};
        AlgorithmConfig configSine = new AlgorithmConfig(
                1, 500, 50, 15, 5, 10, 5, 0.01, 10, boundsSine
        );

        // Zielfunktion: sin(x)
        // KORREKTUR: 'inputs' statt 'args', um Namenskonflikt zu vermeiden
        ObjectiveFunction fSine = inputs -> Math.sin(Math.toRadians(inputs[0]));

        // Validator: Prüft Grenzen
        // KORREKTUR: 'inputs' statt 'args'
        ConstraintValidator validator = (inputs, w) ->
                IntStream.range(0, inputs.length)
                        .allMatch(i -> inputs[i] >= w[i][0] && inputs[i] <= w[i][1]);

        // Nachbarschaft: Zufälliger Punkt im Bereich center +/- range/2
        NeighborhoodGenerator generator = (center, ranges) -> {
            double[] newPos = new double[center.length];
            for (int i = 0; i < center.length; i++) {
                double offset = (random.nextDouble() - 0.5) * ranges[i];
                newPos[i] = center[i] + offset;
            }
            return newPos;
        };

        // Comparator: Wir suchen MAXIMA (größer ist besser)
        Comparator<Solution> maxComparator = Comparator.comparingDouble(Solution::value);

        // Ausführung & Zeitmessung
        runAndMeasure("Sinus Maxima", configSine, fSine, validator, generator, maxComparator);


        // --- Szenario 2: Multimodale 2D Funktion (z.B. cos(x) + cos(y)) ---
        System.out.println("\n>>> Starte Optimierung 2: Wellen Funktion (2D)");

        double[][] bounds2D = {{-20.0, 20.0}, {-20.0, 20.0}};
        AlgorithmConfig config2D = new AlgorithmConfig(
                2, 1000, 100, 20, 5, 20, 10, 0.05, 10, bounds2D
        );

        // f(x,y) = cos(x) + cos(y)
        // KORREKTUR: 'inputs' statt 'args'
        ObjectiveFunction fWave = inputs -> Math.cos(inputs[0]) + Math.cos(inputs[1]);

        runAndMeasure("Wellen 2D", config2D, fWave, validator, generator, maxComparator);
    }

    /**
     * Führt den Algorithmus aus, misst die Zeit und gibt Ergebnisse aus.
     */
    private static void runAndMeasure(
            String name,
            AlgorithmConfig config,
            ObjectiveFunction f,
            ConstraintValidator validator,
            NeighborhoodGenerator generator,
            Comparator<Solution> comparator
    ) {
        System.out.println("Start...");
        long start = System.currentTimeMillis();

        List<Solution> results = BeesAlgorithm.solve(config, f, validator, generator, comparator);

        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.println("Fertig in " + duration + " ms.");
        System.out.println("Top " + config.r() + " Ergebnisse für " + name + ":");
        results.forEach(System.out::println);
    }
}