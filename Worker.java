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
    // Globale Parameter
    public static int b, k, t, n, m, e, p, q;
    public static int functionId; // NEU: ID der Funktion

    public static void main(String[] args) throws Exception {
        BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));

        // 1. BA-Parameter einlesen
        String lineOfParams = bR.readLine();
        if (lineOfParams == null) return;

        // Parsing
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
            // NEU: Einlesen der functionId an Index 10
            functionId = Integer.parseInt(params[10]);
        } catch (Exception ex) {
            System.err.println("Worker: Fehler Parameter: " + ex.getMessage());
            return;
        }

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
