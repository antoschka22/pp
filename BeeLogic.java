import java.util.Random;

/**
 * Person B: BeeLogic
 */
public class BeeLogic {

    public static void processBlock(BeeBlock block) {
        Random rand = new Random();
        double range = block.end - block.start;

        for (int i = 0; i < block.numBees; i++) {
            double pos = block.start + (rand.nextDouble() * range);

            // KORREKTUR: Wir rufen calculateFitness auf.
            // Die functionId holen wir uns statisch vom Worker (Person A Teil),
            // da diese für den gesamten Prozess gleich ist.
            double fitness = calculateFitness(pos, Worker.functionId);

            if (fitness > block.bestFitness) {
                block.bestFitness = fitness;
                block.bestPosition = pos;
            }
        }
        block.setProcessed(true);
    }

    /**
     * Wählt die Funktion basierend auf der ID aus.
     * ID 0: Einfache Parabel (Unimodal)
     * ID 1: Originalfunktion (Multimodal)
     * ID 2: Rastrigin-ähnlich (Sehr viele lokale Maxima)
     */
    private static double calculateFitness(double x, int funcId) {
        // 1. Künstliche Rechenlast (bleibt gleich)
        double dummy = 0;
        for(int k=0; k<200; k++) {
            dummy += Math.cos(x * k);
        }

        double result = 0;

        // 2. Auswahl der Funktion
        switch (funcId) {
            case 0: // Test 1: Einfache Parabel (-x^2). Max bei 0.
                result = -(x * x);
                break;

            case 1: // Test 2: Die bisherige Funktion ("Sinus-Mix").
                result = -(x * x) + Math.sin(5 * x) * 10;
                break;

            case 2: // Test 3: Rastrigin-basiert (invertiert für Maximierung).
                // Hat sehr viele lokale Optima.
                // Form: -(x^2 + 10 - 10cos(2*pi*x))
                result = -( (x*x) + 10.0 - (10.0 * Math.cos(2 * Math.PI * x)) );
                break;

            default: // Fallback
                result = -(x * x);
        }

        return result + (dummy * 0.0000001);
    }
}