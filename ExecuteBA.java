import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse ist für die parallele Ausführung des Bienenalgorithmus auf mehreren JVM-Prozessen zuständig.
 */
public class ExecuteBA {

    /**
     * Diese Methode führt den Bienenalgorithmus parallel aus.
     *
     * @param w Array der zu untersuchenden Wertebereiche aller Argumente
     * @param b Anzahl der Bienen in einem Block
     * @param k Anzahl der Threads in einem Prozess
     * @param t Anzahl der Suchschritte nach denen abgebrochen wird
     * @param n Anzahl der Kundschafterinnen
     * @param m Anzahl der Felder, die (weiter) untersucht werden
     * @param e Anzahl exzellenter Felder, die sehr genau untersucht werden
     * @param p Anzahl der für ein exzellentes Feld rekrutierten Bienen
     * @param q Anzahl der für ein anderes Feld rekrutierten Bienen
     * @throws Exception Bei Auftreten von Fehlern in der Prozessverwaltung oder IPC wird eine Exception geworfen.
     */
    public static void executeBA(double [][] w, int b, int k, int t,
                                        int n, int m, int e, int p, int q) throws Exception {
        int numOfProcesses = w.length;
        Process[] processes = new Process[numOfProcesses];
        BufferedReader[] readers = new BufferedReader[numOfProcesses];

        // Prozesse erstellen & Parameter senden
        for(int i = 0; i < numOfProcesses; i++) {
            ProcessBuilder pB = new ProcessBuilder("java", "Worker");
            pB.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process proc = pB.start();
            processes[i] = proc;

            PrintWriter out = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()), true);
            out.println(w[i][0] + ";" + w[i][1] + ";" + b + ";" + k + ";" + t +
                    ";" + n + ";" + m + ";" + e + ";" + p + ";" + q);

            readers[i] = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        }

        // Ergebnisse sammeln
        System.out.println("ExecuteBA: Auf Ergebnisse der " + numOfProcesses + " Prozesse warten...");
        List<String> resultsBA = new ArrayList<>();
        for(int i = 0; i < numOfProcesses; i++) {
            String s = readers[i].readLine();
            resultsBA.add(s != null ? s : "Kein Ergebnis empfangen!");
            processes[i].waitFor();
        }

        // alle Ergebnisse zusammenfassen
        System.out.println("\n ---- Ergebnisübersicht ----");
        for(int i = 0; i < resultsBA.size(); i++) {
            System.out.println("Worker-Prozess " + i + ": " + resultsBA.get(i));
        }
    }
}
