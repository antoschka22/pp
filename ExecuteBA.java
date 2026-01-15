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
     * @param w Array der zu untersuchenden Wertebereiche aller Argumente [ProzessID][0=Start, 1=Ende]
     * @param b Anzahl der Bienen in einem Block
     * @param k Anzahl der Threads in einem Prozess
     * @param t Anzahl der Suchschritte nach denen abgebrochen wird
     * @param n Anzahl der Kundschafterinnen
     * @param m Anzahl der Felder, die (weiter) untersucht werden
     * @param e Anzahl exzellenter Felder, die sehr genau untersucht werden
     * @param p Anzahl der für ein exzellentes Feld rekrutierten Bienen
     * @param q Anzahl der für ein anderes Feld rekrutierten Bienen
     * @throws Exception Bei Auftreten von Fehlern in der Prozessverwaltung oder IPC.
     */
    public static void executeBA(double[][] w, int b, int k, int t,
                                 int n, int m, int e, int p, int q) throws Exception {

        int numOfProcesses = w.length;
        Process[] processes = new Process[numOfProcesses];
        BufferedReader[] readers = new BufferedReader[numOfProcesses];

        System.out.println("ExecuteBA: Starte " + numOfProcesses + " Worker-Prozesse...");

        // Prozesse erstellen & Parameter senden
        for (int i = 0; i < numOfProcesses; i++) {
            // Prozess starten
            ProcessBuilder pB = new ProcessBuilder("java", "Worker");

            // WICHTIG: Fehler-Stream erben, damit man Exceptions im Worker auf der Konsole sieht
            pB.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process proc = pB.start();
            processes[i] = proc;

            // Parameter an den Worker senden (Pipeline IN)
            // WICHTIG: Konsistentes Trennzeichen (;) verwenden!
            PrintWriter out = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()), true);
            out.println(w[i][0] + ";" + w[i][1] + ";" + b + ";" + k + ";" + t +
                    ";" + n + ";" + m + ";" + e + ";" + p + ";" + q);

            // Reader für die Ergebnisse vorbereiten (Pipeline OUT)
            readers[i] = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        }

        // Ergebnisse sammeln (Blockierend warten)
        System.out.println("ExecuteBA: Warte auf Ergebnisse...");
        List<String> resultsBA = new ArrayList<>();

        for (int i = 0; i < numOfProcesses; i++) {
            // Liest die letzte Zeile, die der Worker sendet (das Ergebnis)
            String s = readers[i].readLine();
            resultsBA.add(s != null ? s : "FEHLER: Kein Ergebnis von Prozess " + i + " empfangen!");

            // Sicherstellen, dass der Prozess wirklich beendet ist
            processes[i].waitFor();
        }

        // Alle Ergebnisse zusammenfassen und ausgeben
        System.out.println("\n---- Ergebnisübersicht ----");
        for (int i = 0; i < resultsBA.size(); i++) {
            System.out.println("Worker-Prozess " + i + ": " + resultsBA.get(i));
        }
    }
}