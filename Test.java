/*
Arbeitsaufteilung von allen 8 Aufgaben:
Wir haben alle 8 Aufgaben immer in einem gemeinsamen Meeting besprochen und ausgearbeitet.
Danach haben wir die Programmierarbeit gerecht und gleichmäßig auf alle 3 aufgeteilt. Bei Unklarheiten und komplexen Fragestellungen haben wir vertieft zusammengearbeitet,
außerdem haben wir uns gegenseitig Verbesserungsvorschläge gegeben.
Im Verlauf der Übung ist es zu KEINEN Ausfällen im Team gekommen.

Arbeitsaufteilung Aufgabe 8:
Miriam Reumann: Ich war für die Klassen Worker und ExecuteBA.java zuständig
Simon Oberdörfer: Ich war für die Klassen Test(Testfälle 1-9), BeeBlock.java und BeeLogic.java zuständig
Antonio Molina Gradischnig: Ich war für die Klassen Test(Dateiaufbau und Testfälle 10-32), BeeThread.java und BlockManager.java zuständig
 */

/**
 * Testklasse zur Überprüfung der parallelen Implementierung
 *
 * Diese Klasse fungiert als externer Treiber, der das Gesamtsystem als Blackbox testet
 * Sie validiert, ob die grobgranulare Parallelisierung (Prozesse via ExecuteBA) und die
 * feingranulare Parallelisierung (Threads im Worker) korrekt zusammenspielen
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
        try {
            // Standard-Parameter für die Testläufe
            // t=Runden, b=Blockgröße, n=Scouts, m=Selected, e=Elite, p=SelectedBees, q=EliteBees
            int t = 5; int b = 10; int n = 100; int m = 10; int e = 10; int p = 20; int q = 10;

            // =================================================================
            // TEIL A: PFLICHT-SZENARIEN (Architektur-Validierung)
            // =================================================================

            System.out.println("=== 1. [Pflicht] 4 Prozesse, k=1, Parabel ===");
            // Testet die reine Interprozesskommunikation (IPC) ohne interne Thread-Konkurrenz
            // Jeder Prozess (JVM) bearbeitet ein disjunktes Segment des Suchraums.
            // Hier muss die Pipeline-Kommunikation und die Aggregation durch den Master funktionieren
            double[][] w1 = { {-100, -50}, {-50, 0}, {0, 50}, {50, 100} };
            ExecuteBA.executeBA(w1, b, 1, t, n, m, e, p, q, 0);

            System.out.println("\n=== 2. [Pflicht] 1 Prozess, k=6, Sinus-Mix ===");
            // Testet die interne Synchronisation (Monitor, wait/notify) innerhalb einer JVM
            // Mit k=6 Threads greifen viele Konsumenten konkurrierend auf die 'workQueue' zu
            // Dies prüft die Thread-Sicherheit des BlockManagers gegen Race-Conditions
            double[][] w2 = { {-100, 100} };
            ExecuteBA.executeBA(w2, b, 6, t, n, m, e, p, q, 1);

            System.out.println("\n=== 3. [Pflicht] 2 Prozesse, k=3, Rastrigin ===");
            // Realistischstes Szenario: Mehrere Knoten (Prozesse) nutzen jeweils Multi-Core-Power (Threads)
            // Testet das Zusammenspiel von IPC-Latenz und Thread-Synchronisation
            double[][] w3 = { {-5.12, 0}, {0, 5.12} };
            ExecuteBA.executeBA(w3, b, 3, t, n, m, e, p, q, 2);

            // =================================================================
            // TEIL B: EDGE CASES (Grenzfälle der Parallelisierung)
            // =================================================================

            System.out.println("\n=== 4. [Edge] Minimalismus: 1 Prozess, k=1, t=1, n=b ===");
            // Fall n=b erzeugt exakt einen Block. Prüft, ob der Algorithmus auch ohne echte
            // Verteilung (nur 1 Task) und ohne Iteration (t=1) deadlockfrei terminiert
            double[][] w4 = { {-10, 10} };
            ExecuteBA.executeBA(w4, 10, 1, 1, 10, 10, 10, 10, 10, 0);

            System.out.println("\n=== 5. [Edge] High-Concurrency: 1 Prozess, k=12 ===");
            // Viele Threads (k=12) konkurrieren um wenige Ressourcen.
            // Testet, ob der Synchronisations-Overhead (Context Switches, Monitor-Acquisition)
            // die Ausführung übermäßig verlangsamt oder zu Liveness-Problemen führt
            ExecuteBA.executeBA(w2, b, 12, 3, 120, 20, 10, 20, 10, 1);

            System.out.println("\n=== 6. [Edge] Many-Processes: 8 Prozesse, k=1 ===");
            // Startet viele JVMs. Testet die Grenzen des `ProcessBuilder` und der Pipe-Verwaltung
            // Prüft, ob der Master alle 8 Input-Streams sequenziell korrekt auslesen kann
            double[][] w6 = new double[8][2];
            for(int i=0; i<8; i++) { w6[i][0] = i*10; w6[i][1] = (i+1)*10; }
            ExecuteBA.executeBA(w6, b, 1, 3, n, m, e, p, q, 0);

            System.out.println("\n=== 7. [Edge] Micro-Range: Bereich [0, 0.0001] ===");
            // Sehr kleine Intervalle können bei der Block-Aufteilung zu Rundungsfehlern führen,
            // die Threads in Endlosschleifen schicken oder Bounds-Checks verletzen könnten
            double[][] w7 = { {0, 0.0001} };
            ExecuteBA.executeBA(w7, b, 2, 5, n, m, e, p, q, 0);

            System.out.println("\n=== 8. [Edge] Huge-Range: Bereich [-1M, +1M] ===");
            // Prüft, ob die "Scouts" (zufällige globale Suche) auch riesige Suchräume abdecken,
            // die über Prozessgrenzen hinweg definiert sind
            double[][] w8 = { {-1000000, 1000000} };
            ExecuteBA.executeBA(w8, b, 4, 5, 200, 20, 10, 20, 10, 0);

            System.out.println("\n=== 9. [Edge] Parameter-Konflikt (n=m) ===");
            // Wenn alle Bienen "Selected" sind, gibt es keine Scouts
            // Die Rekrutierungs-Logik muss diesen Fall behandeln, ohne Exceptions zu werfen
            ExecuteBA.executeBA(w3, 10, 2, 3, 50, 50, 20, 20, 10, 2);

            // =================================================================
            // TEIL C: STRUKTUR- & LOGIK-TESTS (Granularität & Load Balancing)
            // =================================================================

            System.out.println("\n=== 10. [Logic] High-Selectivity (Viele Elite, wenig Scouts) ===");
            // Erzeugt viele kleine lokale Suchaufträge. Testet, ob die Worker-Threads
            // effizient viele kleine Tasks aus der Queue abarbeiten ("Pull"-Prinzip)
            ExecuteBA.executeBA(w2, 10, 4, 4, 100, 80, 40, 20, 10, 1);

            System.out.println("\n=== 11. [Logic] High-Exploration (Massive Scouts) ===");
            // Erzeugt zufällig verteilte Aufgaben im gesamten Raum.
            // Testet die Randomisierung und Verteilung ohne lokale Hotspots
            ExecuteBA.executeBA(w2, 10, 4, 4, 200, 20, 10, 20, 10, 1);

            System.out.println("\n=== 12. [Struct] Big-Blocks (b=50) ===");
            // Große Blöcke bedeuten weniger Synchronisation am Monitor (weniger `synchronized` Aufrufe),
            // aber potenziell schlechteres Load-Balancing, wenn ein Thread lange an einem Block rechnet
            ExecuteBA.executeBA(w1, 50, 2, 4, 200, 100, 50, 50, 50, 0);

            System.out.println("\n=== 13. [Struct] Tiny-Blocks (b=1, Overhead Test) ===");
            // Maximale Anzahl an Monitor-Operationen (Lock/Unlock) pro Recheneinheit
            // Extremtest für den Overhead durch Synchronisation. Zeigt, ob Granularität zu fein gewählt wurde
            ExecuteBA.executeBA(w3, 1, 4, 3, 50, 10, 5, 5, 2, 2);

            System.out.println("\n=== 14. [Edge] Zero-Rounds (t=0) ===");
            // Prüft, ob das System sauber hoch- und sofort wieder herunterfährt (Graceful Shutdown),
            // ohne in `waitForRoundCompletion` hängen zu bleiben
            ExecuteBA.executeBA(w1, 10, 1, 0, 50, 10, 10, 10, 10, 0);

            System.out.println("\n=== 15. [Logic] Endurance (t=50 Runden) ===");
            // Langläufer-Test. Prüft, ob Threads über viele Barriere-Zyklen hinweg korrekt synchronisieren
            // und keine "Phantome" (hängende Threads) entstehen
            ExecuteBA.executeBA(w3, 5, 2, 50, 20, 10, 5, 5, 5, 2);

            System.out.println("\n=== 16. [Thread] Odd-Threads (k=7) ===");
            // Bei ungeraden Thread-Zahlen verteilt sich die Last nicht glatt
            // Prüft, ob die dynamische Arbeitsverteilung (Queue-Pull) dies ausgleicht
            ExecuteBA.executeBA(w2, 10, 7, 3, 140, 20, 10, 20, 10, 1);

            System.out.println("\n=== 17. [Num] Deep-Negative [-5000, -4000] ===");
            // Validiert, dass die Fitness-Berechnung und Intervall-Logik auch fernab vom Ursprung funktionieren.
            double[][] w17 = { {-5000, -4000} };
            ExecuteBA.executeBA(w17, 10, 2, 3, 50, 10, 10, 10, 10, 0);

            System.out.println("\n=== 18. [IPC] Disjoint-Search (Getrennte Inseln) ===");
            // Zwei Prozesse suchen in komplett isolierten Bereichen
            // Zeigt die Stärke des Multi-Prozess-Ansatzes bei nicht-zusammenhängenden Suchräumen
            double[][] w18 = { {-100, -90}, {90, 100} };
            ExecuteBA.executeBA(w18, 5, 2, 4, 20, 10, 5, 5, 5, 0);

            System.out.println("\n=== 19. [Logic] Heavy-Local (Hohes q) ===");
            // Viele Bienen pro Elite-Feld erzeugen rechenintensive Blöcke
            // Prüft das Verhalten, wenn einzelne Tasks deutlich länger dauern als andere (Jitter)
            ExecuteBA.executeBA(w3, 10, 4, 3, 200, 20, 10, 20, 50, 2);

            System.out.println("\n=== 20. [Final] The Exam Mix (Complex) ===");
            // Komplexes Zusammenspiel aller Komponenten: Mehrere Prozesse, mehrere Threads,
            // komplexe Funktion (Rastrigin) und gemischte Suchstrategien
            double[][] w20 = { {-10, -5}, {-5, 5}, {5, 10} };
            ExecuteBA.executeBA(w20, 5, 4, 10, 100, 20, 10, 10, 10, 2);


            System.out.println("\n=== 21. [Sync] Extreme Monitor-Contention (b=1, k=16) ===");
            // Ziel: Das System unter maximale Last setzen, was den Zugriff auf den Monitor (BlockManager) angeht
            // Kontext: Bei einer Blockgröße von b=1 muss für jede noch so kleine Berechnung der Monitor
            // gesperrt und entsperrt werden. Dies maximiert die Wahrscheinlichkeit für Race-Conditions
            // und prüft, ob wait()/notifyAll() auch bei extrem häufigen Aufrufen deadlocksicher arbeiten
            double[][] w21 = { {-50, 50} };
            ExecuteBA.executeBA(w21, 1, 16, 3, 64, 16, 8, 4, 4, 1);

            System.out.println("\n=== 22. [Logic] Pure Exploration (m=0, e=0 - Nur Scouts) ===");
            // Ziel: Überprüfung des Verhaltens, wenn keine Rekrutierung stattfindet
            // Kontext: Wenn m=0 und e=0 ist, muss die recruit-Methode (sequenziell) in der Lage sein,
            // rein zufällige neue Blöcke zu generieren, ohne durch leere Listen oder Index-Fehler
            // abzustürzen. Es prüft, ob der Wechsel zwischen paralleler Suche und (in diesem Fall trivialer)
            // sequenzieller Neuverteilung funktioniert
            ExecuteBA.executeBA(w21, 5, 4, 4, 100, 0, 0, 0, 0, 0);

            System.out.println("\n=== 23. [Logic] Pure Exploitation (n=m=e - Keine Scouts) ===");
            // Ziel: Testen der lokalen Suche ohne globale Erkundung
            // Kontext: Alle Bienen werden für die lokale Suche um Elite-Positionen eingesetzt
            // Dies erzeugt potenziell sehr viele kleine Blöcke an denselben Positionen
            // Wir prüfen, ob die Work-Queue im BlockManager auch mit dieser einseitigen Lastverteilung
            // die Threads effizient versorgt, ohne dass Threads "verhungern" (Starvation),
            // während der Main-Thread in der Rekrutierung rechnet
            ExecuteBA.executeBA(w21, 10, 4, 4, 50, 50, 50, 10, 10, 2);

            System.out.println("\n=== 24. [IPC] Max-Process-Stress (8 Prozesse, k=1) ===");
            // Ziel: Belastungstest für die Pipe-Verbindungen und den JVM-Start
            // Kontext: Das Starten vieler JVMs (8 Stück) testet die Grenzen der 'ProcessBuilder'-Verwaltung
            // und der I/O-Streams. Es muss sichergestellt sein, dass die Hauptanwendung nicht blockiert,
            // während sie auf die Ergebnisse von 8 unabhängigen Pipes wartet (Sequentialisierung im IPC-Read)
            double[][] w24 = new double[8][2];
            for(int i=0; i<8; i++) { w24[i][0] = i; w24[i][1] = i+1; }
            ExecuteBA.executeBA(w24, 5, 1, 2, 40, 10, 5, 5, 5, 0);

            System.out.println("\n=== 25. [Stability] Long-Run Simulation (t=30) ===");
            // Ziel: Erkennen von Problemen, die sich erst über die Zeit aufbauen (z.B. nicht freigegebene Ressourcen)
            // Kontext: Bei vielen Runden (t=30) wechseln sich Barriere und Thread-Aktivität oft ab
            // Ein Fehler im 'notifyAll' oder ein verlorenes Signal würde hier zum Deadlock (Timeout) führen
            ExecuteBA.executeBA(w3, 10, 2, 30, 50, 10, 5, 10, 10, 2);

            System.out.println("\n=== 26. [Edge] Ungleiche Lastverteilung (Asymmetric Ranges) ===");
            // Ziel: Verhalten bei unterschiedlich schnell antwortenden Worker-Prozessen
            // Kontext: Prozess 1 hat einen winzigen Suchraum (schnell fertig), Prozess 2 einen riesigen (langsam)
            // ExecuteBA muss korrekt warten, bis ALLE Prozesse fertig sind, bevor aggregiert wird
            // Dies simuliert die Notwendigkeit der Synchronisation auf Ebene der verteilten Prozesse
            double[][] w26 = { {0, 0.1}, {-10000, 10000} };
            ExecuteBA.executeBA(w26, 20, 4, 3, 100, 20, 10, 20, 10, 1);

            System.out.println("\n=== 27. [Robustness] Invalid Function ID ===");
            // Ziel: Prüfen, ob der Worker abstürzt oder weiterläuft, wenn ungültige Parameter kommen
            // Kontext: Ein robustes verteiltes System darf nicht komplett crashen (Broken Pipe),
            // nur weil ein Knoten einen unerwarteten Parameter erhält. Der Default-Case im Switch sollte greifen
            ExecuteBA.executeBA(w1, 10, 2, 2, 50, 10, 5, 10, 10, 999);

            System.out.println("\n=== 28. [Load] Endurance-Test (t=200 Runden) ===");
            // Erhöht die Anzahl der Barriere-Synchronisationen drastisch (200x)
            // Ziel: Prüfen, ob das System auch über längere Laufzeit stabil bleibt und keine
            // kumulativen Verzögerungen (z.B. durch Garbage Collection Stau) entstehen
            // Testet implizit, ob 'wait/notify' auch bei tausendfachem Aufruf deadlocksicher sind
            ExecuteBA.executeBA(w3, 20, 4, 200, 200, 20, 10, 20, 10, 2);

            System.out.println("\n=== 29. [Load] Massive Swarm (n=10.000, 1 Prozess) ===");
            // Eine extrem große Anzahl an Bienen (Daten) wird in die WorkQueue gepumpt
            // Ziel: Testen der Speicherverwaltung und des Queue-Handlings unter Last
            // Da 'n' sehr groß ist, wird die 'recruit'-Methode (sequenziell) zum Flaschenhals (Amdahl's Law),
            // während die parallelen Threads die Queue abarbeiten
            double[][] w29 = { {-100, 100} };
            ExecuteBA.executeBA(w29, 100, 8, 5, 10000, 100, 50, 50, 50, 1);

            System.out.println("\n=== 30. [Load] Distributed Heavy Load (4 Prozesse, n=2.000) ===");
            // Jeder der 4 Prozesse muss eine signifikante Last (2.000 Bienen) bewältigen
            // Ziel: Prüfen, ob die CPU-Kerne durch die multiplen JVMs voll ausgelastet werden,
            // ohne dass der IPC-Overhead (Datenübertragung der Ergebnisse) die Performance frisst
            // Dies simuliert ein rechenintensives Cluster-Szenario
            ExecuteBA.executeBA(w1, 50, 2, 10, 2000, 100, 50, 50, 50, 0);

            System.out.println("\n=== 31. [Stress] High-Frequency Locking (b=2, n=1.000, t=20) ===");
            // Kombination aus hoher Last (n=1000) und extrem feiner Granularität (b=2)
            // Führt zu tausenden von Zugriffen auf den synchronisierten BlockManager pro Sekunde
            // Ziel: Stresstest für den Java-Monitor (Intrinsic Lock). Wenn die Synchronisation
            // nicht effizient ist, bricht hier die Performance durch "Thread Contention" ein
            ExecuteBA.executeBA(w3, 2, 4, 20, 1000, 50, 20, 10, 10, 2);

            System.out.println("\n=== 32. [Final] The 5-Second Challenge (Complex Mix) ===");
            // Versucht, eine Laufzeit von mehreren Sekunden zu erzwingen, um Timeouts zu provozieren
            // Mischt viele Runden (t=50) mit vielen Bienen (n=1000) und komplexer Funktion (Rastrigin)
            // Validiert, dass der Prozess-Builder und die Pipes auch warten können, wenn die Berechnung dauert
            double[][] w32 = { {-50, 0}, {0, 50} };
            ExecuteBA.executeBA(w32, 25, 4, 50, 1000, 100, 20, 50, 20, 2);

        } catch (Exception ex) {
            System.err.println("!!! TEST ABBRUCH WEGEN FEHLER !!!");
            ex.printStackTrace();
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("\n>>> Gesamte Testsuite beendet in " + duration + " ms <<<");

        // Zeitlimit-Check gemäß Angabe (max 20s für alle Tests)
        if (duration > 20000) {
            System.err.println("WARNUNG: Zeitlimit (20s) überschritten! Optimierung nötig.");
        } else {
            System.out.println("ZEIT-CHECK: OK - Bereit für Abgabe.");
        }
    }
}