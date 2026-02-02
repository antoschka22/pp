/*
Miriam Reumann hat in der Testklasse Scenario 2-6 geschrieben und bei der Implementierung
von BeesAlgorithm mitgeholfen, indem ich die Testfälle implementiert habe und den Algorithmus bewertet und
auf Fehlern hingewiesen habe.
Antonio Molina Gradischnig hat die Klasse BeesAlgorithm geschrieben. Und in der Test Klasse Scenario1 geschrieben.
Außerdem habe ich mit Miriam Reumann die Testklasse für die Fehlersuche mitgeholfen.
Simon Oberdörfer hat die folgenden Klassen und Interfaces geschrieben: AlgorithmConfig, ConstraintValidator, LocalSearch,
NeighborhoodGenerator und ObjectiveFunction.
*/

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Test {

    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("--- Starte Tests für Programmieraufgabe 7 ---");

        // Sequentielle Abarbeitung der definierten Testszenarien zur Validierung des Algorithmus
        // Erfassung der Zeitdifferenz zur Ermittlung der Gesamtperformanz
        long totalTimeStart = System.currentTimeMillis();
        scenario1();
        scenario2();
        scenario3();
        scenario4();
        scenario5();
        scenario6();
        long totalTime = System.currentTimeMillis() - totalTimeStart;

        // Transformation der Laufzeitdauer und Ausgabe als Seiteneffekt
        double totalTimeSeconds = totalTime / 1000.0;

        System.out.println("\n>>> Die Gesamtlaufzeit der Szenarien beträgt: " + totalTime + " ms (= " + totalTimeSeconds + " s).");
    }

    /*
     * Definition Szenario 1: Parametrisierung für Sinus-Funktion (1D)
     * Ziel: Maximierung des Funktionswerts
     */
    private static void scenario1(){
        System.out.println("\n>>> Starte Optimierung 1: Sinus Funktion (1D)");

        // Definition der Dimensionsgrenzen des Suchraums
        double[][] boundsSine = {{-1800.0, 1800.0}};

        // Zusammenfassung der Hyperparameter in einem unveränderlichen Konfigurationsobjekt
        // (a=1, t=4000, n=100, m=30, e=10, p=30, q=15, s=0.01, r=10)
        AlgorithmConfig configSine = new AlgorithmConfig(
                1, 4000, 100, 30, 10, 30, 15, 0.01, 10, boundsSine
        );

        // Definition der Abbildung: Eingabewert -> Sinus des Winkels
        ObjectiveFunction fSine = inputs -> Math.sin(Math.toRadians(inputs[0]));

        // Definition der Ordnungsrelation: Höhere Werte werden bevorzugt (Maximierung)
        Comparator<Solution> maxComparator = Comparator.comparingDouble(Solution::value);

        // Übergabe der funktionalen Komponenten an die Messmethode
        runAndMeasure("Sinus-Funktion (Maxima)", configSine, fSine, validator, generator, maxComparator);
    }

    /*
     * Definition Szenario 2: Parametrisierung für Cosinus-Funktion (1D)
     * Ziel: Maximierung des Funktionswerts
     */
    private static void scenario2(){
        System.out.println("\n>>> Starte Optimierung 2: Cosinus Funktion (1D)");

        // Definition der Dimensionsgrenzen des Suchraums
        double[][] boundsCos = {{-1440.0, 1440.0}};

        // Zusammenfassung der Hyperparameter
        // (a=1, t=6000, n=200, m=50, e=15, p=40, q=20, s=0.05, r=10)
        AlgorithmConfig configCos = new AlgorithmConfig(
                1, 6000, 200, 50, 15, 40, 20, 0.05, 10, boundsCos
        );

        // Definition der Abbildung: Eingabewert -> Cosinus des Winkels
        ObjectiveFunction fCos = inputs -> Math.cos(Math.toRadians(inputs[0]));

        // Definition der Ordnungsrelation: Höhere Werte werden bevorzugt (Maximierung)
        Comparator<Solution> maxComparator = Comparator.comparingDouble(Solution::value);

        // Übergabe der funktionalen Komponenten an die Messmethode
        runAndMeasure("Cosinus-Funktion (Maxima)", configCos, fCos, validator, generator, maxComparator);
    }

    /*
     * Definition Szenario 3: Parametrisierung für Wellen-Funktion (2D)
     * Ziel: Maximierung der Summe zweier Cosinus-Schwingungen
     */
    private static void scenario3(){
        System.out.println("\n>>> Starte Optimierung 3: Wellen Funktion (2D)");

        // Definition der Dimensionsgrenzen des Suchraums (2D)
        double[][] bounds2D = {{-20.0, 20.0}, {-20.0, 20.0}};

        // Zusammenfassung der Hyperparameter
        // (a=2, t=8000, n=300, m=80, e=20, p=60, q=30, s=0.05, r=10)
        AlgorithmConfig config2D = new AlgorithmConfig(
                2, 8000, 300, 80, 20, 60, 30, 0.05, 10, bounds2D
        );

        // Definition der Abbildung: Vektor(x,y) -> cos(x) + cos(y)
        ObjectiveFunction fWave = inputs -> Math.cos(inputs[0]) + Math.cos(inputs[1]);

        // Definition der Ordnungsrelation: Höhere Werte werden bevorzugt (Maximierung)
        Comparator<Solution> maxComparator = Comparator.comparingDouble(Solution::value);

        // Übergabe der funktionalen Komponenten an die Messmethode
        runAndMeasure("2D-Wellen-Funktion (Maxima)", config2D, fWave, validator, generator, maxComparator);
    }

    /*
     * Definition Szenario 4: Parametrisierung für Himmelblau-Funktion (2D)
     * Ziel: Minimierung des Funktionswerts
     */
    private static void scenario4(){
        System.out.println("\n>>> Starte Optimierung 4: Himmelblau Funktion (2D)");

        // Definition der Dimensionsgrenzen des Suchraums
        double[][] bounds2D = {{-6.0, 6.0}, {-6.0, 6.0}};

        // Zusammenfassung der Hyperparameter
        // (a=2, t=10000, n=300, m=80, e=20, p=60, q=30, s=0.08, r=10)
        AlgorithmConfig config2D = new AlgorithmConfig(
                2, 10000, 300, 80, 20, 60, 30, 0.08, 10, bounds2D
        );

        // Definition der Abbildung: Berechnung des Himmelblau-Polynoms
        ObjectiveFunction fHimmelblau = inputs -> {
            double x = inputs[0];
            double y = inputs[1];
            return Math.pow((x * x) + y - 11, 2) + Math.pow(x + (y * y) - 7, 2);
        };

        // Definition der Ordnungsrelation: Niedrigere Werte werden bevorzugt (Minimierung)
        Comparator<Solution> minComparator = Comparator.comparingDouble(Solution::value);

        // Übergabe der funktionalen Komponenten an die Messmethode
        runAndMeasure("Himmelblau-Funktion (Minima)", config2D, fHimmelblau, validator, generator, minComparator);
    }

    /*
     * Definition Szenario 5: Parametrisierung für Rastrigin-Funktion (3D)
     * Ziel: Minimierung des Funktionswerts
     */
    private static void scenario5(){
        System.out.println("\n>>> Starte Optimierung 5: Rastrigin Funktion (3D)");

        // Definition der Dimensionsgrenzen des Suchraums
        double[][] bounds3D = {{-5.12, 5.12}, {-5.12, 5.12}, {-5.12, 5.12}} ;

        // Zusammenfassung der Hyperparameter
        // (a=3, t=15000, n=400, m=150, e=40, p=120, q=60, s=0.05, r=10)
        AlgorithmConfig config3D = new AlgorithmConfig(
                3 , 15000, 400, 150, 40, 120, 60, 0.05, 10, bounds3D
        );

        // Definition der Abbildung: Summation der Rastrigin-Terme über alle Dimensionen
        ObjectiveFunction rastrigin3D = inputs -> {
            double sum = 10.0 * inputs.length;
            for(double x : inputs) {
                sum += x * x - 10.0 * Math.cos(2 * Math.PI * x);
            }
            return sum;
        };

        // Definition der Ordnungsrelation: Niedrigere Werte werden bevorzugt (Minimierung)
        Comparator<Solution> minComparator = Comparator.comparingDouble(Solution::value);

        // Übergabe der funktionalen Komponenten an die Messmethode
        runAndMeasure("Rastrigin-Funktion (Minima)", config3D, rastrigin3D, validator, generator, minComparator);
    }

    /*
     * Definition Szenario 6: Parametrisierung für Schwefel-Funktion (3D)
     * Ziel: Minimierung des Funktionswerts
     */
    private static void scenario6(){
        System.out.println("\n>>> Starte Optimierung 6: Schwefel Funktion (3D)");

        // Definition der Dimensionsgrenzen des Suchraums
        double[][] bounds3D = {{-500.0, 500.0}, {-500.0, 500.0}, {-500.0, 500.0}};

        // Zusammenfassung der Hyperparameter
        // (a=3, t=12000, n=350, m=100, e=30, p=80, q=40, s=0.03, r=10)
        AlgorithmConfig config3D = new AlgorithmConfig(
                3, 12000, 350, 100, 30, 80, 40, 0.03, 10, bounds3D
        );

        // Definition der Abbildung: Summation der Schwefel-Terme über alle Dimensionen
        ObjectiveFunction fSchwefel = inputs -> {
            double sum = 418.9829 * inputs.length;
            for(double x : inputs) {
                sum -= x * Math.sin(Math.sqrt(Math.abs(x)));
            }
            return sum;
        };

        // Definition der Ordnungsrelation: Niedrigere Werte werden bevorzugt (Minimierung)
        Comparator<Solution> minComparator = Comparator.comparingDouble(Solution::value);

        // Übergabe der funktionalen Komponenten an die Messmethode
        runAndMeasure("Schwefel-Funktion (Minima)", config3D, fSchwefel, validator, generator, minComparator);
    }

    /*
     * Koordination von Berechnung, Zeitmessung und Ausgabe der Ergebnisse (Seiteneffekte)
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

        // Aufruf der reinen Funktion solve und Materialisierung des Ergebnisses
        List<Solution> results = BeesAlgorithm.solve(config, f, validator, generator, comparator);

        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.println("Fertig in " + duration + " ms.");
        System.out.println("Top " + config.r() + " Ergebnisse für " + name + ":");
        results.forEach(System.out::println);
    }

    /*
     * Prädikat zur Validierung: Prüft mittels Stream-Reduktion, ob alle Dimensionen im zulässigen Intervall liegen
     * (Verwendung von Indizes zur Referenzierung der Dimensionen in w)
     */
    private static final ConstraintValidator validator = (inputs, w) ->
            IntStream.range(0, inputs.length)
                    .allMatch(i -> inputs[i] >= w[i][0] && inputs[i] <= w[i][1]);

    /*
     * Abbildung eines Positionsvektors auf einen Nachbarvektor durch komponentenweise
     * Zufallsverschiebung innerhalb definierter Reichweiten
     */
    private static final NeighborhoodGenerator generator = (center, ranges) ->
            IntStream.range(0, center.length)
                    .mapToDouble(i -> {
                        double offset = (random.nextDouble() - 0.5) * ranges[i];
                        return center[i] + offset;
                    })
                    .toArray();
}