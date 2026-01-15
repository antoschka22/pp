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
            int t = 5; int b = 10; int n = 100; int m = 10; int e = 4; int p = 20; int q = 10;

            // ----------------------------------------------------------------
            // Testlauf 1: 4 Prozesse, k=1. Funktion: Parabel (ID 0)
            // ----------------------------------------------------------------
            System.out.println("\n=== Testlauf 1: 4 Prozesse, k=1, Parabel ===");
            double[][] w1 = { {-100, -50}, {-50, 0}, {0, 50}, {50, 100} };
            int k1 = 1;
            int funcId1 = 0; // Parabel
            ExecuteBA.executeBA(w1, b, k1, t, n, m, e, p, q, funcId1);

            // ----------------------------------------------------------------
            // Testlauf 2: 1 Prozess, k=6. Funktion: Sinus-Mix (ID 1)
            // ----------------------------------------------------------------
            System.out.println("\n=== Testlauf 2: 1 Prozess, k=6, Sinus-Mix ===");
            double[][] w2 = { {-100, 100} };
            int k2 = 6;
            int funcId2 = 1; // Original-Funktion
            ExecuteBA.executeBA(w2, b, k2, t, n, m, e, p, q, funcId2);

            // ----------------------------------------------------------------
            // Testlauf 3: 2 Prozesse, k=3. Funktion: Rastrigin (ID 2)
            // ----------------------------------------------------------------
            System.out.println("\n=== Testlauf 3: 2 Prozesse, k=3, Rastrigin ===");
            double[][] w3 = { {-5.12, 0}, {0, 5.12} }; // Rastrigin Bereich oft [-5.12, 5.12]
            int k3 = 3;
            int funcId3 = 2; // Rastrigin
            ExecuteBA.executeBA(w3, b, k3, t, n, m, e, p, q, funcId3);


        } catch (Exception ex) {
            System.err.println("Kritischer Fehler im Testablauf: " + ex.getMessage());
            ex.printStackTrace();
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\n>>> Alle Tests beendet in " + duration + " ms <<<");
    }
}