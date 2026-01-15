import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Worker {

    // Globale Parameter (public static für einfachen Zugriff)
    public static double wStart, wEnd;
    public static int b, k, t, n, m, e, p, q;
    public static int functionId;

    public static void main(String[] args) {
        try {
            BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
            String lineOfParams = bR.readLine();
            if (lineOfParams == null) return;

            // Parameter parsen (Peinlich genau auf Trennzeichen achten!)
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

            // 1. Initialisierung (Scouts)
            List<BeeBlock> initialBlocks = new ArrayList<>();
            int numScoutBlocks = n / b;
            double step = (wEnd - wStart) / numScoutBlocks;
            for (int i = 0; i < numScoutBlocks; i++) {
                double s = wStart + i * step;
                initialBlocks.add(new BeeBlock(s, s + step, b));
            }

            // BlockManager füllen
            BlockManager.addBlocks(initialBlocks);

            // 2. Threads erzeugen und starten
            BeeThread[] threads = new BeeThread[k];
            for (int i = 0; i < k; i++) {
                threads[i] = new BeeThread(i);
                threads[i].start();
            }

            // Variable für das beste globale Ergebnis
            double bestGlobalFitness = -Double.MAX_VALUE;
            double bestGlobalPos = 0.0;

            // 3. Schleife über t Iterationen
            for (int round = 0; round < t; round++) {
                // Warten bis alle Threads die aktuelle Runde fertig haben (Barriere)
                BlockManager.waitForRoundCompletion();

                // Ergebnisse holen
                List<BeeBlock> results = BlockManager.getFinishedBlocks();

                // Bestes Ergebnis dieser Runde finden
                for (BeeBlock blk : results) {
                    if (blk.bestFitness > bestGlobalFitness) {
                        bestGlobalFitness = blk.bestFitness;
                        bestGlobalPos = blk.bestPosition;
                    }
                }

                // Falls nicht die letzte Runde: Rekrutierung
                if (round < t - 1) {
                    List<BeeBlock> newBlocks = BeeLogic.recruit(results, n, m, e, p, q, b);
                    BlockManager.addBlocks(newBlocks); // Startet die Threads implizit wieder (notifyAll)
                }
            }

            // 4. Threads sauber beenden
            BlockManager.stopThreads();
            for (BeeThread th : threads) {
                th.join();
            }

            // 5. Ergebnis ausgeben (Format: Fitness;Position)
            System.out.println(bestGlobalFitness + ";" + bestGlobalPos);

        } catch (Exception ex) {
            // Fehler in System.err schreiben, damit ExecuteBA es via getErrorStream lesen kann
            System.err.println("CRASH in Worker: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}