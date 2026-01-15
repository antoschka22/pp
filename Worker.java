import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Worker {

    // Globale Parameter (public static für einfachen Zugriff aus BeeLogic)
    public static double wStart, wEnd;
    public static int b, k, t, n, m, e, p, q;
    public static int functionId;

    public static void main(String[] args) {
        // Variable für das beste globale Ergebnis
        double bestGlobalFitness = -Double.MAX_VALUE;
        double bestGlobalPos = 0.0;

        // Thread-Array hier deklarieren, damit wir im finally-Block darauf zugreifen könnten (optional)
        BeeThread[] threads = null;

        try {
            BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
            String lineOfParams = bR.readLine();
            if (lineOfParams == null) return;

            // --- 4. Exception Handling: Robustes Parsing ---
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
            } catch (Exception parseEx) {
                System.err.println("Worker Parameter Error: " + parseEx.getClass().getSimpleName() + " - " + parseEx.getMessage());
                return; // Abbruch, da ohne Parameter kein Start möglich
            }

            // --- Initialisierung ---
            // 1. Initiale Blöcke (Scouts) erstellen
            List<BeeBlock> initialBlocks = new ArrayList<>();
            // Falls n < b, mindestens einen Block erzeugen, um Division durch Null/Leere Listen zu vermeiden
            int numScoutBlocks = Math.max(1, n / b);
            double step = (wEnd - wStart) / numScoutBlocks;

            for (int i = 0; i < numScoutBlocks; i++) {
                double s = wStart + i * step;
                double end = s + step;
                if (end > wEnd) end = wEnd; // Floating Point Korrektur
                initialBlocks.add(new BeeBlock(s, end, b));
            }

            // BlockManager füllen
            BlockManager.addBlocks(initialBlocks);

            // 2. Threads erzeugen und starten
            threads = new BeeThread[k];
            for (int i = 0; i < k; i++) {
                threads[i] = new BeeThread(i);
                threads[i].start();
            }

            // --- 1. Der t=0 Fix (Logik-Umbau) ---

            // Schritt A: Initiale Auswertung (MUSS immer passieren, auch bei t=0)
            // Wir warten, bis die Scouts fertig sind.
            BlockManager.waitForRoundCompletion();

            // Ergebnisse der Initialisierung holen
            List<BeeBlock> results = BlockManager.getFinishedBlocks();

            // Bestwert aktualisieren
            for (BeeBlock blk : results) {
                if (blk.bestFitness > bestGlobalFitness) {
                    bestGlobalFitness = blk.bestFitness;
                    bestGlobalPos = blk.bestPosition;
                }
            }

            // Schritt B: Schleife für Rekrutierungs-Runden (t-1 mal, falls t > 0)
            // Wenn t=1 ist, haben wir Schritt A gemacht (1 Runde), loop läuft nicht. Passt.
            // Wenn t=0 ist, haben wir Schritt A gemacht (Init-Check), loop läuft nicht. Passt.
            // Wenn t=5 ist, haben wir Schritt A (1) + Loop (4) = 5 Runden. Passt.

            for (int round = 1; round < t; round++) {
                // Rekrutierung basierend auf den Ergebnissen der VORHERIGEN Runde
                List<BeeBlock> newBlocks = BeeLogic.recruit(results, n, m, e, p, q, b);
                BlockManager.addBlocks(newBlocks); // Weckt Worker auf

                // Warten bis Runde fertig
                BlockManager.waitForRoundCompletion();

                // Neue Ergebnisse holen
                results = BlockManager.getFinishedBlocks();

                // Bestwert aktualisieren
                for (BeeBlock blk : results) {
                    if (blk.bestFitness > bestGlobalFitness) {
                        bestGlobalFitness = blk.bestFitness;
                        bestGlobalPos = blk.bestPosition;
                    }
                }
            }

            // 4. Threads sauber beenden
            BlockManager.stopThreads();
            if (threads != null) {
                for (BeeThread th : threads) {
                    th.join();
                }
            }

            // 5. Ergebnis ausgeben
            System.out.println(bestGlobalFitness + ";" + bestGlobalPos);

        } catch (Exception ex) {
            System.err.println("CRASH in Worker Main: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}