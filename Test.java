/**
 * Person C: Test (Erweitert)
 * Führt eine umfassende Testsuite durch (9+ Szenarien).
 *
 * Enthalten sind:
 * 1-3. Die Pflicht-Szenarien laut Angabe.
 * 4.   Minimal-Test (Grenzfall: Alles auf 1/Minimum).
 * 5.   High-Concurrency (Viele Threads in einem Prozess).
 * 6.   Many-Processes (Viele Prozesse, wenig Threads).
 * 7.   Präzisions-Test (Sehr kleiner Wertebereich).
 * 8.   Weitbereichs-Test (Sehr großer Wertebereich).
 * 9.   Stress-Test (Hohe Bienen-Dichte, wenige Runden).
 * 10. High-Selectivity: Fast alle Bienen sind Elite/Selected (wenig Scouts).
 * 11. High-Exploration: Sehr viele Scouts (hohes n, kleines m).
 * 12. Big-Blocks: Großes b (50). Prüft, ob grobe Granularität funktioniert.
 * 13. Tiny-Blocks: b=1. Maximale Granularität (hoher Overhead, prüft Thread-Scheduling).
 * 14. Zero-Rounds: t=0. Prüft, ob die Initialisierung allein schon ein Ergebnis liefert (Sanity Check).
 * 15. Endurance: Viele Runden (t=50), aber wenig Bienen. Prüft Stabilität über Zeit.
 * 16. Odd-Threads: Ungerade Thread-Anzahl (k=7), um "saubere" Teiler zu brechen.
 * 17. Deep-Negative: Bereich weit im Negativen (Offset-Prüfung).
 * 18. Disjoint-Search: Zwei Prozesse suchen in komplett getrennten Inseln (Intervall-Check).
 * 19. Heavy-Local: Sehr viele Bienen pro Elite-Stelle (q hoch).
 * 20. The "Exam" Mix: Eine komplexe Mischung aus allem (3 Prozesse, Parabel & Rastrigin Mix)
 */

public class Test {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println(">>> Start der MASTER Testsuite (20 Fälle) <<<\n");

        try {
            // Standard-Parameter
            int t = 5; int b = 10; int n = 100; int m = 10; int e = 10; int p = 20; int q = 10;

            // =================================================================
            // TEIL A: PFLICHT-TESTS (1-3)
            // =================================================================
            System.out.println("=== 1. [Pflicht] 4 Prozesse, k=1, Parabel ===");
            double[][] w1 = { {-100, -50}, {-50, 0}, {0, 50}, {50, 100} };
            ExecuteBA.executeBA(w1, b, 1, t, n, m, e, p, q, 0);

            System.out.println("\n=== 2. [Pflicht] 1 Prozess, k=6, Sinus-Mix ===");
            double[][] w2 = { {-100, 100} };
            ExecuteBA.executeBA(w2, b, 6, t, n, m, e, p, q, 1);

            System.out.println("\n=== 3. [Pflicht] 2 Prozesse, k=3, Rastrigin ===");
            double[][] w3 = { {-5.12, 0}, {0, 5.12} };
            ExecuteBA.executeBA(w3, b, 3, t, n, m, e, p, q, 2);

            // =================================================================
            // TEIL B: EDGE CASES (4-9)
            // =================================================================
            System.out.println("\n=== 4. [Edge] Minimalismus: 1 Prozess, k=1, t=1, n=b ===");
            double[][] w4 = { {-10, 10} };
            // n=10, b=10 -> Nur 1 Block
            ExecuteBA.executeBA(w4, 10, 1, 1, 10, 10, 10, 10, 10, 0);

            System.out.println("\n=== 5. [Edge] High-Concurrency: 1 Prozess, k=12 ===");
            ExecuteBA.executeBA(w2, b, 12, 3, 120, 20, 10, 20, 10, 1);

            System.out.println("\n=== 6. [Edge] Many-Processes: 8 Prozesse, k=1 ===");
            double[][] w6 = new double[8][2];
            for(int i=0; i<8; i++) { w6[i][0] = i*10; w6[i][1] = (i+1)*10; }
            ExecuteBA.executeBA(w6, b, 1, 3, n, m, e, p, q, 0);

            System.out.println("\n=== 7. [Edge] Micro-Range: Bereich [0, 0.0001] ===");
            double[][] w7 = { {0, 0.0001} };
            ExecuteBA.executeBA(w7, b, 2, 5, n, m, e, p, q, 0);

            System.out.println("\n=== 8. [Edge] Huge-Range: Bereich [-1M, +1M] ===");
            double[][] w8 = { {-1000000, 1000000} };
            ExecuteBA.executeBA(w8, b, 4, 5, 200, 20, 10, 20, 10, 0);

            System.out.println("\n=== 9. [Edge] Parameter-Konflikt (n=m) ===");
            // Alle Bienen sind in der Auswahl, keine reinen Scouts
            ExecuteBA.executeBA(w3, 10, 2, 3, 50, 50, 20, 20, 10, 2);

            // =================================================================
            // TEIL C: NEUE TESTS (10-20)
            // =================================================================
            System.out.println("\n=== 10. [Logic] High-Selectivity (Viele Elite, wenig Scouts) ===");
            // n=100, m=80. Fokus auf Ausbeutung guter Stellen.
            // b=10. Alle Params müssen Vielfache von 10 sein.
            ExecuteBA.executeBA(w2, 10, 4, 4, 100, 80, 40, 20, 10, 1);

            System.out.println("\n=== 11. [Logic] High-Exploration (Massive Scouts) ===");
            // n=200, m=20. 180 Bienen suchen zufällig.
            ExecuteBA.executeBA(w2, 10, 4, 4, 200, 20, 10, 20, 10, 1);

            System.out.println("\n=== 12. [Struct] Big-Blocks (b=50) ===");
            // Wenige große Aufgabenpakete. Testet Lastverteilung.
            // Params müssen durch 50 teilbar sein!
            ExecuteBA.executeBA(w1, 50, 2, 4, 200, 100, 50, 50, 50, 0);

            System.out.println("\n=== 13. [Struct] Tiny-Blocks (b=1, Overhead Test) ===");
            // Maximale Anzahl an Blöcken. Stresstest für Queue/Synchronisation.
            // n=50 -> 50 Blöcke.
            ExecuteBA.executeBA(w3, 1, 4, 3, 50, 10, 5, 5, 2, 2);

            System.out.println("\n=== 14. [Edge] Zero-Rounds (t=0) ===");
            // Sollte nur Initialisierung machen und sofort Ergebnisse liefern.
            ExecuteBA.executeBA(w1, 10, 1, 0, 50, 10, 10, 10, 10, 0);

            System.out.println("\n=== 15. [Logic] Endurance (t=50 Runden) ===");
            // Viele Runden, kleine Population.
            ExecuteBA.executeBA(w3, 5, 2, 50, 20, 10, 5, 5, 5, 2);

            System.out.println("\n=== 16. [Thread] Odd-Threads (k=7) ===");
            // Ungerade Zahl, bricht 2er Potenzen.
            ExecuteBA.executeBA(w2, 10, 7, 3, 140, 20, 10, 20, 10, 1);

            System.out.println("\n=== 17. [Num] Deep-Negative [-5000, -4000] ===");
            // Testet, ob Logik auch weit weg von 0 funktioniert.
            double[][] w17 = { {-5000, -4000} };
            ExecuteBA.executeBA(w17, 10, 2, 3, 50, 10, 10, 10, 10, 0);

            System.out.println("\n=== 18. [IPC] Disjoint-Search (Getrennte Inseln) ===");
            // Prozess 1 sucht links, Prozess 2 sucht rechts. Nichts in der Mitte.
            double[][] w18 = { {-100, -90}, {90, 100} };
            ExecuteBA.executeBA(w18, 5, 2, 4, 20, 10, 5, 5, 5, 0);

            System.out.println("\n=== 19. [Logic] Heavy-Local (Hohes q) ===");
            // Viele Bienen pro Elite-Feld. q=50 bei b=10 -> 5 Blöcke pro Elite.
            ExecuteBA.executeBA(w3, 10, 4, 3, 200, 20, 10, 20, 50, 2);

            System.out.println("\n=== 20. [Final] The Exam Mix (Complex) ===");
            // 3 Prozesse, k=4, t=10. Ein realistisches "schweres" Szenario.
            double[][] w20 = { {-10, -5}, {-5, 5}, {5, 10} };
            ExecuteBA.executeBA(w20, 5, 4, 10, 100, 20, 10, 10, 10, 2);

        } catch (Exception ex) {
            System.err.println("!!! TEST ABBRUCH WEGEN FEHLER !!!");
            ex.printStackTrace();
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\n>>> Gesamte Testsuite (20 Fälle) beendet in " + duration + " ms <<<");

        if (duration > 20000) {
            System.err.println("WARNUNG: Zeitlimit (20s) überschritten! Optimierung nötig.");
        } else {
            System.out.println("ZEIT-CHECK: OK - Bereit für Abgabe.");
        }
    }
}