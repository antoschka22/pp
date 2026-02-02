import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert einen eigenständigen Worker-Prozess (JVM)
 * Diese Klasse fungiert als lokaler Koordinator:
 * 1. IPC-Schnittstelle: Empfängt Konfiguration vom Master-Prozess ('ExecuteBA') via Stdin
 * 2. Thread-Management: Startet und verwaltet den Pool an BeeThread's
 * 3. Phasen-Steuerung: Synchronisiert den Wechsel zwischen paralleler Suche (Threads)
 * und sequenzieller Rekrutierung (Main-Thread) mittels einer Barriere
 */
public class Worker {

    // Globale Parameter (Shared Memory innerhalb dieses Prozesses)
    // public static erlaubt den effizienten Zugriff durch BeeLogic/BeeThread ohne
    // ständiges Herumreichen von Objekten
    public static double wStart, wEnd;
    public static int b, k, t, n, m, e, p, q;
    public static double s;

    public static int functionId;

    public static void main(String[] args) {
        // Lokaler Bestwert dieses Prozesses
        double bestGlobalFitness = -Double.MAX_VALUE;
        double bestGlobalPos = 0.0;

        BeeThread[] threads = null;

        try {
            // IPC (Input Phase): Parameter lesen
            // Wir lesen die Konfiguration, die ExecuteBA in unsere Pipe (System.in) schreibt
            BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
            String lineOfParams = bR.readLine();
            if (lineOfParams == null) return;

            // Robustes Parsing der CSV-Daten
            try {
                String[] params = lineOfParams.split(";");
                wStart = Double.parseDouble(params[0]);
                wEnd = Double.parseDouble(params[1]);
                b = Integer.parseInt(params[2]);
                k = Integer.parseInt(params[3]);
                t = Integer.parseInt(params[4]);
                n = Integer.parseInt(params[5]);
                m = Integer.parseInt(params[6]);
                e = Integer.parseInt(params[7]);
                p = Integer.parseInt(params[8]);
                q = Integer.parseInt(params[9]);
                functionId = Integer.parseInt(params[10]);
                s = Double.parseDouble(params[11]);

            } catch (Exception parseEx) {
                System.err.println("Worker Parameter Error: " + parseEx.getClass().getSimpleName() + " - " + parseEx.getMessage());
                return;
            }

            // Initialisierung
            // Erzeugen der initialen Scouts (Gleichverteilung im Suchraum)
            // Dies ist der erste "Job", den die Worker erledigen müssen
            List<BeeBlock> initialBlocks = new ArrayList<>();
            int numScoutBlocks = Math.max(1, n / b);
            double step = (wEnd - wStart) / numScoutBlocks;

            for (int i = 0; i < numScoutBlocks; i++) {
                double start = wStart + i * step;
                double end = start + step;
                if (end > wEnd) end = wEnd; // Korrektur für Fließkomma-Ungenauigkeiten
                initialBlocks.add(new BeeBlock(start, end, b));
            }

            // Füllen des Monitors (Producer-Schritt)
            BlockManager.addBlocks(initialBlocks);

            // Starten des Thread-Pools
            // Die Threads laufen sofort los und bedienen sich an der Queue
            threads = new BeeThread[k];
            for (int i = 0; i < k; i++) {
                threads[i] = new BeeThread(i);
                threads[i].start();
            }

            //Hauptschleife (Phasen-Synchronisation)

            // Warten auf Abschluss der Initial-Runde
            // Der Main-Thread blockiert hier, solange die Worker-Threads rechnen
            BlockManager.waitForRoundCompletion();

            // Ergebnisse einsammeln (Thread-Safe Zugriff via Monitor)
            List<BeeBlock> results = BlockManager.getFinishedBlocks();

            // Bestwert aktualisieren
            for (BeeBlock blk : results) {
                if (blk.bestFitness > bestGlobalFitness) {
                    bestGlobalFitness = blk.bestFitness;
                    bestGlobalPos = blk.bestPosition;
                }
            }

            // Evolutions-Schleife (t-1 mal)
            // Hier wechselt sich sequenzielle Logik (Main) und parallele Arbeit (Threads) ab
            for (int round = 1; round < t; round++) {

                // 1: Sequenzielle Rekrutierung ---
                // "Da die Rekrutierungsphase sequentiell abgearbeitet wird..."
                // Wir erstellen neue Blöcke basierend auf den alten Ergebnissen
                // Da alle Worker an der Barriere (in getNextBlock -> wait) schlafen,
                // ist dieser Zugriff exklusiv und sicher
                List<BeeBlock> newBlocks = BeeLogic.recruit(results, n, m, e, p, q, s, b);

                // 2: Arbeit verteilen (Producer) ---
                // Fügt Blöcke hinzu und ruft intern notifyAll() auf, um die Worker zu wecken
                BlockManager.addBlocks(newBlocks);

                // 3: Parallele Verarbeitung (Barriere) ---
                // Der Main-Thread legt sich schlafen, bis die Queue leer UND alle Blöcke fertig sind
                BlockManager.waitForRoundCompletion();

                // 4: Auswertung ---
                results = BlockManager.getFinishedBlocks();

                for (BeeBlock blk : results) {
                    if (blk.bestFitness > bestGlobalFitness) {
                        bestGlobalFitness = blk.bestFitness;
                        bestGlobalPos = blk.bestPosition;
                    }
                }
            }

            // Shutdown & Reporting (Output Phase)

            // Graceful Shutdown: Threads signalisieren, dass sie terminieren sollen
            BlockManager.stopThreads();
            for (BeeThread th : threads) {
                th.join(); // Warten, bis alle Threads wirklich tot sind
            }

            // IPC Output: Ergebnis zurück an den Master-Prozess (ExecuteBA) senden
            // Format: Fitness;Position
            System.out.println(bestGlobalFitness + ";" + bestGlobalPos);

        } catch (Exception ex) {
            System.err.println("CRASH in Worker Main: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}