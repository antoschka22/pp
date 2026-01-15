/**
 * Master-Testsuite zur Überprüfung der parallelen Implementierung.
 *
 * PARADIGMA-KONTEXT:
 * Diese Klasse fungiert als externer Treiber ("Driver"), der das Gesamtsystem als Blackbox testet.
 * Sie validiert, ob die grobgranulare Parallelisierung (Prozesse via ExecuteBA) und die
 * feingranulare Parallelisierung (Threads im Worker) korrekt zusammenspielen.
 *
 * Die Tests sind so gewählt, dass sie spezifische Aspekte der Nebenläufigkeit isolieren:
 * - Skalierung über Prozesse (Distributed Memory Ansatz)
 * - Skalierung über Threads (Shared Memory Ansatz)
 * - Synchronisations-Overhead (Viele kleine Blöcke)
 * - Robustheit bei Race-Conditions (Hohe Nebenläufigkeit)
 */
public class Test {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println(">>> Start der MASTER Testsuite (20 Fälle) <<<\n");

        try {
            // Standard-Parameter für die Testläufe
            // t=Runden, b=Blockgröße, n=Scouts, m=Selected, e=Elite, p=SelectedBees, q=EliteBees
            int t = 5; int b = 10; int n = 100; int m = 10; int e = 10; int p = 20; int q = 10;

            // =================================================================
            // TEIL A: PFLICHT-SCENARIOS (Laut Angabe Aufgabe 8)
            // Diese Tests prüfen die grundlegenden Anforderungen an die Architektur.
            // =================================================================

            // Szenario 1: Fokus auf Multi-Process (4 Prozesse), Single-Threaded pro Prozess (k=1).
            // ZIEL: Überprüfung der Interprozesskommunikation (IPC) und der Aufteilung des
            // Suchraums (Distributed Memory). Hier gibt es keine Thread-Konkurrenz im Worker.
            System.out.println("=== 1. [Pflicht] 4 Prozesse, k=1, Parabel ===");
            double[][] w1 = { {-100, -50}, {-50, 0}, {0, 50}, {50, 100} };
            ExecuteBA.executeBA(w1, b, 1, t, n, m, e, p, q, 0);

            // Szenario 2: Fokus auf Multi-Threading (k=6) innerhalb eines einzigen Prozesses.
            // ZIEL: Überprüfung der internen Synchronisation (Monitor, Queue, Barriere).
            // Stresstest für `BlockManager` und Race-Conditions auf Shared Memory.
            System.out.println("\n=== 2. [Pflicht] 1 Prozess, k=6, Sinus-Mix ===");
            double[][] w2 = { {-100, 100} };
            ExecuteBA.executeBA(w2, b, 6, t, n, m, e, p, q, 1);

            // Szenario 3: Hybrider Ansatz (2 Prozesse, je 3 Threads).
            // ZIEL: Realistisches Szenario, das beide Ebenen der Parallelisierung kombiniert.
            // Prüft, ob IPC und Threading gleichzeitig stabil laufen.
            System.out.println("\n=== 3. [Pflicht] 2 Prozesse, k=3, Rastrigin ===");
            double[][] w3 = { {-5.12, 0}, {0, 5.12} };
            ExecuteBA.executeBA(w3, b, 3, t, n, m, e, p, q, 2);

            // =================================================================
            // TEIL B: EDGE CASES (Grenzfälle der Parallelisierung)
            // =================================================================

            System.out.println("\n=== 4. [Edge] Minimalismus: 1 Prozess, k=1, t=1, n=b ===");
            // Minimal-Konfiguration: n=b bedeutet genau 1 Block.
            // Prüft: Funktioniert der Algorithmus auch ohne echte Iterationsschleife (t=1)?
            double[][] w4 = { {-10, 10} };
            ExecuteBA.executeBA(w4, 10, 1, 1, 10, 10, 10, 10, 10, 0);

            System.out.println("\n=== 5. [Edge] High-Concurrency: 1 Prozess, k=12 ===");
            // Viele Threads (k=12) auf wenig Arbeit.
            // Prüft: Thread-Contention am Monitor (`BlockManager`) und Overhead durch Context-Switches.
            ExecuteBA.executeBA(w2, b, 12, 3, 120, 20, 10, 20, 10, 1);

            System.out.println("\n=== 6. [Edge] Many-Processes: 8 Prozesse, k=1 ===");
            // Viele Prozesse.
            // Prüft: Overhead durch JVM-Startzeiten und IPC-Pipes.
            double[][] w6 = new double[8][2];
            for(int i=0; i<8; i++) { w6[i][0] = i*10; w6[i][1] = (i+1)*10; }
            ExecuteBA.executeBA(w6, b, 1, 3, n, m, e, p, q, 0);

            System.out.println("\n=== 7. [Edge] Micro-Range: Bereich [0, 0.0001] ===");
            // Numerische Stabilität bei sehr kleinen Intervallen.
            double[][] w7 = { {0, 0.0001} };
            ExecuteBA.executeBA(w7, b, 2, 5, n, m, e, p, q, 0);

            System.out.println("\n=== 8. [Edge] Huge-Range: Bereich [-1M, +1M] ===");
            // Großer Suchraum. Prüft, ob Scouts den Raum ausreichend abdecken.
            double[][] w8 = { {-1000000, 1000000} };
            ExecuteBA.executeBA(w8, b, 4, 5, 200, 20, 10, 20, 10, 0);

            System.out.println("\n=== 9. [Edge] Parameter-Konflikt (n=m) ===");
            // Logik-Check: Was passiert, wenn alle Bienen "Selected" sind und keine reinen Scouts übrig bleiben?
            ExecuteBA.executeBA(w3, 10, 2, 3, 50, 50, 20, 20, 10, 2);

            // =================================================================
            // TEIL C: STRUKTUR- & LOGIK-TESTS
            // =================================================================

            System.out.println("\n=== 10. [Logic] High-Selectivity (Viele Elite, wenig Scouts) ===");
            // Prüft die Rekrutierungslogik bei Fokus auf Ausbeutung (Exploitation).
            ExecuteBA.executeBA(w2, 10, 4, 4, 100, 80, 40, 20, 10, 1);

            System.out.println("\n=== 11. [Logic] High-Exploration (Massive Scouts) ===");
            // Prüft die Logik bei Fokus auf Erkundung (Exploration).
            ExecuteBA.executeBA(w2, 10, 4, 4, 200, 20, 10, 20, 10, 1);

            System.out.println("\n=== 12. [Struct] Big-Blocks (b=50) ===");
            // Coarse-Grained Tasking: Wenige, aber rechenintensive Blöcke.
            // Prüft Lastverteilung (Load Balancing) bei wenigen Aufgabenpaketen.
            ExecuteBA.executeBA(w1, 50, 2, 4, 200, 100, 50, 50, 50, 0);

            System.out.println("\n=== 13. [Struct] Tiny-Blocks (b=1, Overhead Test) ===");
            // Fine-Grained Tasking: Maximale Anzahl an Blöcken (b=1).
            // Extremtest für den Synchronisations-Overhead im `BlockManager` (Lock-Contention).
            ExecuteBA.executeBA(w3, 1, 4, 3, 50, 10, 5, 5, 2, 2);

            System.out.println("\n=== 14. [Edge] Zero-Rounds (t=0) ===");
            // Boundary-Test: t=0 sollte zumindest die Initialisierung durchführen und ein Ergebnis liefern.
            ExecuteBA.executeBA(w1, 10, 1, 0, 50, 10, 10, 10, 10, 0);

            System.out.println("\n=== 15. [Logic] Endurance (t=50 Runden) ===");
            // Langläufer-Test: Prüft auf Speicherlecks oder Deadlocks über längere Zeit.
            ExecuteBA.executeBA(w3, 5, 2, 50, 20, 10, 5, 5, 5, 2);

            System.out.println("\n=== 16. [Thread] Odd-Threads (k=7) ===");
            // Prüft, ob die Block-Verteilung auch bei "krummen" Thread-Zahlen sauber aufgeht.
            ExecuteBA.executeBA(w2, 10, 7, 3, 140, 20, 10, 20, 10, 1);

            System.out.println("\n=== 17. [Num] Deep-Negative [-5000, -4000] ===");
            // Prüft Offset-Berechnungen im negativen Zahlenraum.
            double[][] w17 = { {-5000, -4000} };
            ExecuteBA.executeBA(w17, 10, 2, 3, 50, 10, 10, 10, 10, 0);

            System.out.println("\n=== 18. [IPC] Disjoint-Search (Getrennte Inseln) ===");
            // Zwei Prozesse suchen in komplett getrennten Intervallen.
            double[][] w18 = { {-100, -90}, {90, 100} };
            ExecuteBA.executeBA(w18, 5, 2, 4, 20, 10, 5, 5, 5, 0);

            System.out.println("\n=== 19. [Logic] Heavy-Local (Hohes q) ===");
            // Viele Bienen pro Elite-Feld -> Erzeugt viele lokale Suchblöcke an derselben Stelle.
            ExecuteBA.executeBA(w3, 10, 4, 3, 200, 20, 10, 20, 50, 2);

            System.out.println("\n=== 20. [Final] The Exam Mix (Complex) ===");
            // Komplexes Abschlussszenario: Mischung aus Parabel/Rastrigin, 3 Prozesse.
            double[][] w20 = { {-10, -5}, {-5, 5}, {5, 10} };
            ExecuteBA.executeBA(w20, 5, 4, 10, 100, 20, 10, 10, 10, 2);

        } catch (Exception ex) {
            System.err.println("!!! TEST ABBRUCH WEGEN FEHLER !!!");
            ex.printStackTrace();
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\n>>> Gesamte Testsuite (20 Fälle) beendet in " + duration + " ms <<<");

        // Zeitlimit-Check gemäß Angabe (max 20s für alle Tests)
        if (duration > 20000) {
            System.err.println("WARNUNG: Zeitlimit (20s) überschritten! Optimierung nötig.");
        } else {
            System.out.println("ZEIT-CHECK: OK - Bereit für Abgabe.");
        }
    }
}