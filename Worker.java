import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Diese Klasse führt einen Teil des Bienenalgorithmus in einem eigenen JVM-Prozess aus.
 * Sie verwaltet die Threads und die Runden-Synchronisation.
 */
public class Worker {

    // Synchronisations-Variablen
    private static int currRound = 0;
    private static int finishedThreads = 0;
    private static final Object barrierLock = new Object();

    // Globale Parameter (public für Zugriff durch andere Klassen im Package)
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

        // Parsing
        try {
            String[] params = lineOfParams.split(";");
            wStart = Double.parseDouble(params[0]);
            wEnd   = Double.parseDouble(params[1]);
            b      = Integer.parseInt(params[2]);
            k      = Integer.parseInt(params[3]);
            t      = Integer.parseInt(params[4]);
            n      = Integer.parseInt(params[5]);
            m      = Integer.parseInt(params[6]);
            e      = Integer.parseInt(params[7]);
            p      = Integer.parseInt(params[8]);
            q      = Integer.parseInt(params[9]);
        } catch (Exception ex) {
            System.err.println("Worker: Fehler beim Parsen der Parameter: " + ex.getMessage());
            return;
        }

        // Initialisierung (Runde 0: n Scouts zufällig verteilen)
        BlockManager.init(wStart, wEnd, b, n);

        // 2. Worker-Threads erstellen & starten
        Thread[] workers = new Thread[k];
        for (int i = 0; i < k; i++) {
            workers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < t; j++) {
                        // Abarbeitung der Blöcke
                        processBlocksOfRound();

                        // Barriere
                        synchronized (barrierLock) {
                            finishedThreads++;
                            if (finishedThreads == k) {
                                barrierLock.notifyAll(); // Weckt den Main-Thread
                            }

                            // Warten auf Start der nächsten Runde
                            int activeRound = j;
                            while (currRound <= activeRound && j < t - 1) {
                                barrierLock.wait();
                            }
                        }
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            });
            workers[i].start();
        }

        // 3. Koordinator (Main Thread)
        // Läuft t Runden lang
        // Das beste globale Ergebnis über alle Runden speichern wir hier
        double globalBestFitness = -Double.MAX_VALUE;
        double globalBestPos = 0.0;

        for (int round = 0; round < t; round++) {
            // Warten bis alle Threads fertig sind
            synchronized (barrierLock) {
                while (finishedThreads < k) {
                    barrierLock.wait();
                }
            }

            // --- SEQUENTIELLE PHASE (Rekrutierung) ---
            // 1. Ergebnisse holen
            List<BeeBlock> results = BlockManager.getFinishedBlocks();

            // Globales Optimum aktualisieren (nur für Ausgabe am Ende)
            for (BeeBlock blk : results) {
                if (blk.bestFitness > globalBestFitness) {
                    globalBestFitness = blk.bestFitness;
                    globalBestPos = blk.bestPosition;
                }
            }

            // Wenn wir noch nicht in der letzten Runde sind, neue Blöcke bauen
            if (round < t - 1) {
                // Sortieren: Bestes Ergebnis zuerst (absteigend)
                results.sort((o1, o2) -> Double.compare(o2.bestFitness, o1.bestFitness));

                List<BeeBlock> newBlocks = new ArrayList<>();

                // Neighborhood Size: Wie weit suchen die rekrutierten Bienen um den Punkt herum?
                // Heuristik: 1/10 des Suchraums, wird pro Runde kleiner (optional)
                double neighborhood = (wEnd - wStart) / (10.0 + round);

                // Die m besten Felder auswählen
                int sitesToVisit = Math.min(results.size(), m);

                for (int i = 0; i < sitesToVisit; i++) {
                    BeeBlock bestBlock = results.get(i);

                    // Ist es ein Elite-Feld? (die ersten e)
                    int beesToSend = (i < e) ? p : q;

                    // Neuer Suchbereich um das gefundene Maximum herum
                    // Achtung: Grenzen des Prozesses (wStart/wEnd) einhalten!
                    double center = bestBlock.bestPosition;
                    double start = Math.max(wStart, center - (neighborhood / 2.0));
                    double end   = Math.min(wEnd, center + (neighborhood / 2.0));

                    // Neuen Block erstellen
                    // Hinweis: Wenn p oder q sehr groß sind, könnte man das auf mehrere Blöcke
                    // aufteilen (je b Bienen), aber laut Angabe reicht ein Block mit 'beesToSend'.
                    // Wir müssen sicherstellen, dass beesToSend ein Vielfaches von b ist oder
                    // wir teilen es in (beesToSend / b) Blöcke auf.
                    // Der Einfachheit halber (und laut PDF Text: "b Bienen ... zu einem Block")
                    // erstellen wir hier mehrere Blöcke der Größe b, bis beesToSend erreicht ist.

                    int blocksToCreate = beesToSend / b;
                    if (blocksToCreate < 1) blocksToCreate = 1;

                    for (int x = 0; x < blocksToCreate; x++) {
                        newBlocks.add(new BeeBlock(start, end, b));
                    }
                }

                // Optional: Zufällige Scouts für den Rest (Global Search),
                // um nicht in lokalen Optima stecken zu bleiben (n - m Bienen).
                // Die Angabe fordert das nicht explizit in der Rekrutierungsphase,
                // aber es gehört zum Bees Algorithm. Wir fügen ein paar hinzu:
                int randomScouts = n - (sitesToVisit); // Einfache Heuristik
                if (randomScouts > 0) {
                    int blocks = (randomScouts / b);
                    for(int x=0; x<blocks; x++) {
                        newBlocks.add(new BeeBlock(wStart, wEnd, b));
                    }
                }

                // BlockManager resetten und füllen
                BlockManager.clearFinished();
                BlockManager.addBlocks(newBlocks);
            }

            // Reset für nächste Runde & Threads aufwecken
            synchronized (barrierLock) {
                finishedThreads = 0;
                currRound++;
                barrierLock.notifyAll();
            }
        }

        // Auf Threads warten
        for (Thread thread : workers) {
            thread.join();
        }

        // 4. Ergebnis zurücksenden
        System.out.println(globalBestPos + " -> Fitness: " + globalBestFitness);
    }

    private static void processBlocksOfRound() {
        while (true) {
            BeeBlock block = BlockManager.getNextBlock();
            if (block == null) {
                return;
            }
            // Echte Logik aufrufen
            BeeLogic.processBlock(block);

            BlockManager.reportFinishedBlock(block);
        }
    }
}