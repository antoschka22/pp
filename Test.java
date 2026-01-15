/**
 * Person C: Test
 * Führt die drei geforderten Testläufe durch.
 * * Aufgabenverteilung:
 * Person A: Prozessverwaltung, IPC (ExecuteBA, Worker-Gerüst)
 * Person B: Bienen-Logik, Rekrutierung (BeeLogic, BeeBlock)
 * Person C: Threading, Synchronisation, Test (BeeThread, BlockManager, Test)
 */
public class Test {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        System.out.println(">>> Start der Tests (Zeitlimit: 20s) <<<");

        try {
            // Gemeinsame Parameter für die Tests (so gewählt, dass es schnell geht)
            // Funktion f (implizit in BeeLogic): z.B. f(x) = -(x^2) (Maximum bei 0)
            int t = 5;   // Wenige Runden für Speed
            int b = 10;  // Kleine Blockgröße
            int n = 100; // Scouts
            int m = 10;  // Sites
            int e = 4;   // Elite
            int p = 20;  // Recruited Elite
            int q = 10;  // Recruited Normal

            // ----------------------------------------------------------------
            // Testlauf 1: 4 Prozesse, k=1 (Ein Thread pro Prozess) [cite: 48]
            // Wertebereich aufgeteilt auf 4 Prozesse
            // ----------------------------------------------------------------
            System.out.println("\n=== Testlauf 1: 4 Prozesse, k=1 ===");
            double[][] w1 = {
                    {-100, -50}, {-50, 0}, {0, 50}, {50, 100}
            };
            int k1 = 1;
            ExecuteBA.executeBA(w1, b, k1, t, n, m, e, p, q);


            // ----------------------------------------------------------------
            // Testlauf 2: 1 Prozess, k=6 (Sechs Threads in einem Prozess) [cite: 49]
            // ----------------------------------------------------------------
            System.out.println("\n=== Testlauf 2: 1 Prozess, k=6 ===");
            double[][] w2 = {
                    {-100, 100}
            };
            int k2 = 6;
            ExecuteBA.executeBA(w2, b, k2, t, n, m, e, p, q);


            // ----------------------------------------------------------------
            // Testlauf 3: 2 Prozesse, k=3 (Gemischt) [cite: 50]
            // ----------------------------------------------------------------
            System.out.println("\n=== Testlauf 3: 2 Prozesse, k=3 ===");
            double[][] w3 = {
                    {-100, 0}, {0, 100}
            };
            int k3 = 3;
            ExecuteBA.executeBA(w3, b, k3, t, n, m, e, p, q);


        } catch (Exception ex) {
            System.err.println("Kritischer Fehler im Testablauf: " + ex.getMessage());
            ex.printStackTrace();
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\n>>> Alle Tests beendet in " + duration + " ms <<<");
        if (duration > 20000) {
            System.err.println("ACHTUNG: Zeitlimit von 20s überschritten!");
        }
    }
}