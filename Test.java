/*
Miriam Reumann hat einen Teil der Testklasse implementiert.
Antonio Molina Gradischnig
Simon Oberdörfer
*/

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Test {

    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("--- Starte Tests für Programmieraufgabe 7 ---");

        // Aufruf der einzelnen Szenarien
        // Berechnung der Gesamtlaufzeit der Szenarien
        long totalTimeStart = System.currentTimeMillis();
        scenario1();
        scenario2();
        scenario3();
        scenario4();
        scenario5();
        scenario6();
        long totalTime = System.currentTimeMillis() - totalTimeStart;

        // Umrechnung von Millisekunden in Sekunden
        double totalTimeSeconds = totalTime / 1000.0;

        // Ausgabe der Gesamtlaufzeit
        System.out.println("\n>>> Die Gesamtlaufzeit der Szenarien beträgt: " + totalTime + " ms (= " + totalTimeSeconds + " s).");
    }

    /**
     * Szenario 1:
     * Optimierung der 1D-Sinus-Funktion. Dabei ist das Ziel, das Maxima der Funktion zu finden.
     */
    private static void scenario1(){
        System.out.println("\n>>> Starte Optimierung 1: Sinus Funktion (1D)");

        // Wertebereich festlegen
        double[][] boundsSine = {{-1800.0, 1800.0}};

        // Konfiguration: a=1, t=500, n=50, m=15, e=5, p=10, q=5, s=0.01, r=10
        AlgorithmConfig configSine = new AlgorithmConfig(
                1, 500, 50, 15, 5, 10, 5, 0.01, 10, boundsSine
        );

        // Definition der 1D-Sinus-Objektivfunktion: f(x) = sin(x)
        // KORREKTUR: 'inputs' statt 'args', um Namenskonflikt zu vermeiden
        ObjectiveFunction fSine = inputs -> Math.sin(Math.toRadians(inputs[0]));

        // Comparator: Wir suchen MAXIMA (größer ist besser)
        Comparator<Solution> maxComparator = Comparator.comparingDouble(Solution::value);

        // Ausführung & Zeitmessung
        runAndMeasure("Sinus-Funktion (Maxima)", configSine, fSine, validator, generator, maxComparator);
    }

    /**
     * Szenario 2:
     * Optimierung der 1D-Cosinus-Funktion. Dabei ist das Ziel, das Maxima der Funktion zu finden.
     */
    private static void scenario2(){
        System.out.println("\n>>> Starte Optimierung 2: Cosinus Funktion (1D)");

        // Wertebereich festlegen
        double[][] boundsCos = {{-1440.0, 1440.0}};

        // Konfiguration: a=1, t=800, n=80, m=30, e=8, p=20, q=10, s=0.05, r=10
        AlgorithmConfig configSine = new AlgorithmConfig(
                1, 800, 80, 30, 8, 20, 10, 0.05, 10, boundsCos
        );

        // Definition der 1D-Cosinus-Objektivfunktion: f(x) = cos(x)
        ObjectiveFunction fCos = inputs -> Math.cos(Math.toRadians(inputs[0]));

        // Comparator: Wir suchen MAXIMA (größer ist besser)
        Comparator<Solution> maxComparator = Comparator.comparingDouble(Solution::value);

        // Ausführung & Zeitmessung
        runAndMeasure("Cosinus-Funktion (Maxima)", configSine, fCos, validator, generator, maxComparator);
    }

    /**
     * Szenario 3:
     * Optimierung einer multimodalen 2D-Funktion (z.B. cos(x) + cos(y)). Dabei ist das Ziel, das Maxima der Funktion zu finden.
     */
    private static void scenario3(){
        System.out.println("\n>>> Starte Optimierung 3: Wellen Funktion (2D)");

        // Wertebereich festlegen
        double[][] bounds2D = {{-20.0, 20.0}, {-20.0, 20.0}};

        // Konfiguration: a=2, t=1000, n=100, m=20, e=5, p=20, q=10, s=0.05, r=10
        AlgorithmConfig config2D = new AlgorithmConfig(
                2, 1000, 100, 20, 5, 20, 10, 0.05, 10, bounds2D
        );

        // Definition der 2D-Wellen-Objektivfunktion: f(x,y) = cos(x) + cos(y)
        ObjectiveFunction fWave = inputs -> Math.cos(inputs[0]) + Math.cos(inputs[1]);

        // Comparator: Wir suchen MAXIMA (größer ist besser)
        Comparator<Solution> maxComparator = Comparator.comparingDouble(Solution::value);

        // Ausführung & Zeitmessung
        runAndMeasure("2D-Wellen-Funktion (Maxima)", config2D, fWave, validator, generator, maxComparator);
    }

    /**
     * Szenario 4:
     * Optimierung der multimodalen 2D-Himmelblau-Funktion. Dabei ist das Ziel, das Minima der Funktion zu finden.
     */
    private static void scenario4(){
        System.out.println("\n>>> Starte Optimierung 4: Himmelblau Funktion (2D)");

        // Wertebereich festlegen
        double[][] bounds2D = {{-6.0, 6.0}, {-6.0, 6.0}};

        // Konfiguration: a=2, t=1200, n=70, m=25, e=5, p=15, q=7, s=0.08, r=10
        AlgorithmConfig config2D = new AlgorithmConfig(
                2, 1200, 70, 25, 5, 15, 7, 0.08, 10, bounds2D
        );

        // Definition der 2D-Himmelblau-Objektivfunktion: f(x,y) = (((x^2) + y - 11)^2) + ((x + (y^2) - 7)^2)
        ObjectiveFunction fHimmelblau = inputs -> {
                double x = inputs[0];
                double y = inputs[1];
                return Math.pow((x * x) + y - 11, 2) + Math.pow(x + (y * y) - 7, 2);
        };

        // Comparator: Wir suchen MINIMA
        Comparator<Solution> minComparator = Comparator.comparingDouble(Solution::value);

        // Ausführung & Zeitmessung
        runAndMeasure("Himmelblau-Funktion (Minima)", config2D, fHimmelblau, validator, generator, minComparator);
    }

    /**
     * Szenario 5:
     * Optimierung der multimodalen 3D-Rastrigin-Funktion. Dabei ist das Ziel, das Minima der Funktion zu finden.
     */
    private static void scenario5(){
        System.out.println("\n>>> Starte Optimierung 5: Rastrigin Funktion (3D)");

        // Wertebereich festlegen
        double[][] bounds3D = {{-5.12, 5.12}, {-5.12, 5.12}, {-5.12, 5.12}} ;

        // Konfiguration: a=3, t=2000, n=200, m=80, e=20, p=40, q=20, s=0.05, r=10
        AlgorithmConfig config3D = new AlgorithmConfig(
                3 , 2000, 120, 80, 20, 60, 50, 0.05, 10, bounds3D
        );

        // Definition der 3D-Rastrigin-Objektivfunktion: f(x) = 10*d + ∑((x_i)^2 - 10*cos(2πx_i))
        ObjectiveFunction rastrigin3D = inputs -> {
            double sum = 10.0 * inputs.length;
            for(double x : inputs) {
                sum += x * x - 10.0 * Math.cos(2 * Math.PI * x);
            }
            return sum;
        };

        // Comparator: Wir suchen MINIMA, daher wird kleinerer Funktionswert bevorzugt
        Comparator<Solution> minComparator = Comparator.comparingDouble(Solution::value);

        // Ausführung & Zeitmessung
        runAndMeasure("Rastrigin-Funktion (Minima)", config3D, rastrigin3D, validator, generator, minComparator);
    }

    /**
     * Szenario 6:
     * Optimierung der 3D-Schwefel-Funktion. Dabei ist das Ziel, das Minima der Funktion zu finden.
     */
    private static void scenario6(){
        System.out.println("\n>>> Starte Optimierung 6: Schwefel Funktion (3D)");

        // Wertebereich festlegen
        double[][] bounds3D = {{-500.0, 500.0}, {-500.0, 500.0}, {-500.0, 500.0}};

        // Konfiguration: a=3, t=1600, n=60, m=20, e=8, p=16, q=10, s=0.03, r=10
        AlgorithmConfig config3D = new AlgorithmConfig(
                3, 1600, 60, 20, 8, 16, 10, 0.03, 10, bounds3D
        );

        // Definition der 3D-Schwefel-Objektivfunktion: f(x) = 418.9829*d - ∑(x_i * sin(sqrt(|x_i|)))
        ObjectiveFunction fSchwefel = inputs -> {
            double sum = 418.9829 * inputs.length;
            for(double x : inputs) {
                sum -= x * Math.sin(Math.sqrt(Math.abs(x)));
            }
            return sum;
        };

        // Comparator: Wir suchen MINIMA
        Comparator<Solution> minComparator = Comparator.comparingDouble(Solution::value);

        // Ausführung & Zeitmessung
        runAndMeasure("Schwefel-Funktion (Minima)", config3D, fSchwefel, validator, generator, minComparator);
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

    /**
     * Der Validator prüft, ob ein Wert innerhalb der gegebenen Grenzen liegt
     */
    // KORREKTUR: 'inputs' statt 'args'
    private static final ConstraintValidator validator = (inputs, w) ->
            IntStream.range(0, inputs.length)
                    .allMatch(i -> inputs[i] >= w[i][0] && inputs[i] <= w[i][1]);

    /**
     * NeighbourhoodGenerator erzeugt einen zufälligen Punkt im Bereich center +/- range/2
     */
    private static final NeighborhoodGenerator generator = (center, ranges) -> {
        double[] newPos = new double[center.length];
        for (int i = 0; i < center.length; i++) {
            double offset = (random.nextDouble() - 0.5) * ranges[i];
            newPos[i] = center[i] + offset;
        }
        return newPos;
    };
}