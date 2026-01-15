import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Diese Klasse führt einen Teil des Bienenalgorithmus in einem eigenen JVM-Prozess aus.
 */
public class Worker {

    private static int currRound = 0;
    private static int finishedThreads = 0;
    private static final Object barrierLock = new Object();

    public static void main(String[] args) throws Exception {
        BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));

        // BA-Parameter einlesen
        String lineOfParams = bR.readLine();
        if(lineOfParams == null) return;

        String[] params = lineOfParams.split(" ");
        double wStart = Double.parseDouble(params[0]);
        double wEnd = Double.parseDouble(params[1]);

        int b = Integer.parseInt(params[2]);
        int k = Integer.parseInt(params[3]);
        int t = Integer.parseInt(params[4]);

        // Worker-Threads erstellen & starten
        Thread[] workers = new Thread[k];
        for(int i = 0; i < k; i++) {
            workers[i] = new Thread(() -> {
                try {
                    for(int j = 0; j < t; j++){
                        processBlocksOfRound();

                        // Warten auf das Ende der Runde
                        synchronized(barrierLock) {
                            finishedThreads++;
                            barrierLock.notifyAll();

                            int activeRound = j;
                            while(currRound <= activeRound) barrierLock.wait();
                        }
                    }
                } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            });
            workers[i].start();
        }

        // Koordinierung der Runden
        for(int i = 0; i < t; i++) {
            synchronized(barrierLock) {
                while(finishedThreads < k) barrierLock.wait();
            }

            // Rekrutierungs-Logik

            finishedThreads = 0;
            currRound++;
            barrierLock.notifyAll();
        }

        // sicherstellen, dass alle Worker-Threads die Runde abgeschlossen haben
        for(Thread thread : workers) thread.join();

        // Ausgabe des besten gefundene Ergebnis
        System.out.println("Ergebnis für den Wertebereich [" + wStart + ", " + wEnd + "]");
    }

    /**
     * Hilfsmethode, die für die parallele Abarbeitung der Bienen-Blöcke innerhalb einer Runde
     * zuständig ist.
     */
    private static void processBlocksOfRound(){
     // parallele Block-Verarbeitung
    }
}
