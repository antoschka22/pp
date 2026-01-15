import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Diese Klasse führt einen Teil des Bienenalgorithmus in einem eigenen JVM-Prozess aus.
 * Sie verwaltet die Threads und die Runden-Synchronisation.
 */
public class Worker {

    // Synchronisations-Variablen
    private static int currRound = 0;
    private static int finishedThreads = 0;
    private static final Object barrierLock = new Object();

    // Globale Parameter (für Zugriff durch BeeLogic/BlockManager)
    public static double wStart, wEnd;
    public static int b, k, t, n, m, e, p, q;

    public static void main(String[] args) throws Exception {
        BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));

        // 1. BA-Parameter einlesen (Pipeline)
        String lineOfParams = bR.readLine();
        if (lineOfParams == null) {
            System.err.println("Worker: Keine Parameter empfangen. Abbruch.");
            return;
        }

        // Parsing mit Strichpunkt (passend zu ExecuteBA)
        try {
            String[] params = lineOfParams.split(";");
            wStart = Double.parseDouble(params[0]);
            wEnd   = Double.parseDouble(params[1]);
            b      = Integer.parseInt(params[2]); // Bienen pro Block
            k      = Integer.parseInt(params[3]); // Threads
            t      = Integer.parseInt(params[4]); // Runden
            n      = Integer.parseInt(params[5]);
            m      = Integer.parseInt(params[6]);
            e      = Integer.parseInt(params[7]);
            p      = Integer.parseInt(params[8]);
            q      = Integer.parseInt(params[9]);
        } catch (Exception ex) {
            System.err.println("Worker: Fehler beim Parsen der Parameter: " + ex.getMessage());
            return;
        }

        // Initialisierung der Blöcke VOR dem Start der Threads
        // Wir übergeben den Wertebereich (wStart, wEnd) und die Anzahl (n)
        BlockManager.init(wStart, wEnd, b, n);

        // 2. Worker-Threads erstellen & starten
        Thread[] workers = new Thread[k];
        for (int i = 0; i < k; i++) {
            workers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < t; j++) {
                        // Abarbeitung der Blöcke in dieser Runde
                        processBlocksOfRound();

                        // Warten an der Barriere bis alle Threads fertig sind
                        synchronized (barrierLock) {
                            finishedThreads++;
                            // Wenn dies der letzte Thread ist, wecke den Main-Thread (Koordinator)
                            if (finishedThreads == k) {
                                barrierLock.notifyAll();
                            }

                            // Warten, bis der Main-Thread die nächste Runde freigibt (currRound erhöht)
                            int activeRound = j;
                            while (currRound <= activeRound && j < t - 1) { // j < t-1 verhindert Warten nach der allerletzten Runde
                                barrierLock.wait();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            workers[i].start();
        }

        // 3. Koordinierung der Runden (Main Thread agiert als Koordinator)
        for (int round = 0; round < t; round++) {
            // Warten, bis alle k Threads ihre Arbeit für diese Runde beendet haben
            synchronized (barrierLock) {
                while (finishedThreads < k) {
                    barrierLock.wait();
                }
            }

            // --- SEQUENTIELLE PHASE ---
            // Wenn wir nicht in der allerletzten Runde sind, müssen wir rekrutieren.
            if (round < t - 1) {
                // TODO (Person B): Rekrutierungs-Logik aufrufen
                // List<Results> bestResults = BlockManager.getBestResults();
                // BlockManager.createNewBlocks(bestResults, m, e, p, q, ...);
            }

            // Reset für nächste Runde
            synchronized (barrierLock) {
                finishedThreads = 0;
                currRound++;       // Runde hochzählen
                barrierLock.notifyAll(); // Alle Worker-Threads aufwecken
            }
        }

        // Sicherstellen, dass alle Threads sauber beendet sind
        for (Thread thread : workers) {
            thread.join();
        }

        // 4. Ergebnis zurücksenden
        // TODO (Person B): Bestes globales Ergebnis aus dem BlockManager holen
        double bestResult = 0.0; // Platzhalter
        System.out.println("Ergebnis Bereich [" + wStart + " - " + wEnd + "]: " + bestResult);
    }

    /**
     * Diese Methode wird von jedem Worker-Thread in jeder Runde aufgerufen.
     * Sie holt sich so lange Arbeitspakete (Blöcke), bis keine mehr da sind.
     */
    private static void processBlocksOfRound() {
        while (true) {
            // 1. Thread-sicher den nächsten Block holen (kritischer Abschnitt im BlockManager)
            // Hinweis: BlockManager muss importiert oder im selben Package sein.
            BeeBlock block = BlockManager.getNextBlock();

            // 2. Prüfen, ob Arbeit da war
            if (block == null) {
                // Keine Blöcke mehr in der Queue -> Thread ist fertig mit dieser Runde
                return;
            }

            // 3. Die eigentliche Arbeit verrichten (Logik von Person B)
            // Hier wird die Berechnung ausgeführt.
            // WICHTIG: Falls BeeLogic noch nicht existiert, hier den Code direkt einfügen oder Dummy nutzen.
            // Beispielaufruf:
            try {
                BeeLogic.processBlock(block);
            } catch (Exception e) {
                System.err.println("Fehler bei der Blockverarbeitung: " + e.getMessage());
            }

            // 4. Den fertigen Block zurückmelden (für die Rekrutierung in der nächsten Runde)
            BlockManager.reportFinishedBlock(block);
        }
    }
}