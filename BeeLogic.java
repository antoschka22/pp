import java.util.Random;

/**
 * Person B: BeeLogic
 * Enthält die mathematische Logik und die Verarbeitung eines Blocks.
 */
public class BeeLogic {

    /**
     * Verarbeitet einen Block von Bienen.
     * Jede "Biene" wählt einen zufälligen Punkt im Bereich des Blocks und prüft die Fitness.
     */
    public static void processBlock(BeeBlock block) {
        // Random Instanz hier erstellen, um Thread-Safety Probleme zu vermeiden
        Random rand = new Random();

        // Wir suchen im Bereich [block.start, block.end]
        double range = block.end - block.start;

        for (int i = 0; i < block.numBees; i++) {
            // Zufällige Position im Block-Bereich bestimmen
            double pos = block.start + (rand.nextDouble() * range);

            // Berechnung der Fitness an dieser Stelle
            double fitness = calculateFitness(pos);

            // Prüfen, ob wir ein neues Optimum innerhalb dieses Blocks gefunden haben
            // Da jeder Block nur von einem Thread gleichzeitig bearbeitet wird,
            // brauchen wir hier kein synchronized.
            if (fitness > block.bestFitness) {
                block.bestFitness = fitness;
                block.bestPosition = pos; // WICHTIG: Position merken!
            }
        }

        // Block als bearbeitet markieren
        block.setProcessed(true);
    }

    /**
     * Die Zielfunktion f(x), die optimiert werden soll.
     * Beispiel: Eine Funktion mit vielen lokalen Maxima, damit der Algorithmus arbeiten muss.
     * f(x) = -(x^2) + 10 * sin(5x)
     */
    private static double calculateFitness(double x) {
        // --- 1. Künstliche Rechenlast (Laut Angabe gefordert) ---
        // Damit man die Parallelisierung auch spürt (kein Thread.sleep verwenden!)
        double dummy = 0;
        for(int k=0; k<200; k++) {
            dummy += Math.cos(x * k);
        }

        // --- 2. Die eigentliche mathematische Funktion ---
        // -(x^2) sorgt für ein globales Maximum bei 0.
        // sin(5x) sorgt für viele kleine "Hügel" (lokale Maxima).
        double result = -(x * x) + Math.sin(5 * x) * 10;

        // Dummy-Wert minimal einrechnen, damit der Compiler die Schleife nicht wegoptimiert
        return result + (dummy * 0.0000001);
    }
}