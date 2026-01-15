import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Steuerungsklasse für die grobgranulare Parallelisierung auf Prozessebene.
 * <p>
 * Diese Klasse ist verantwortlich für:
 * 1. Das Aufteilen des gesamten Suchraums auf mehrere unabhängige JVM-Prozesse (SPMD-Ansatz).
 * 2. Den Start der Worker-Prozesse mittels ProcessBuilder.
 * 3. Die Interprozesskommunikation (IPC) über Standard-Input/Output Streams (Pipelines).
 * 4. Die Aggregation der Teilergebnisse am Ende der Berechnung.
 */
public class ExecuteBA {

    /**
     * Startet die parallele Berechnung durch Erzeugen mehrerer Worker-Prozesse.
     *
     * @param w           Array der Wertebereiche (ein Bereich pro Prozess).
     * @param b,k,t,...   Parameter für den Bienenalgorithmus.
     * @param functionId  ID der zu optimierenden Funktion.
     */
    public static void executeBA(double[][] w, int b, int k, int t,
                                 int n, int m, int e, int p, int q,
                                 int functionId) throws Exception {

        int numOfProcesses = w.length;
        Process[] processes = new Process[numOfProcesses];
        BufferedReader[] readers = new BufferedReader[numOfProcesses];

        // Ermittelt den Classpath der aktuellen JVM, um Worker korrekt zu starten
        String classpath = System.getProperty("java.class.path");

        System.out.println("ExecuteBA: Starte " + numOfProcesses + " Worker-Prozesse (FuncID: " + functionId + ")...");

        // --- Phase 1: Prozess-Erzeugung und Initialisierung (IPC-Write) ---
        for (int i = 0; i < numOfProcesses; i++) {
            // Startet eine neue JVM für jeden Teilbereich (Prozess-Parallelität)
            ProcessBuilder pB = new ProcessBuilder("java", "-cp", classpath, "Worker");
            Process proc = pB.start();
            processes[i] = proc;

            // IPC: Schreiben der Konfigurationsparameter in den Standard-Input (System.in) des Kindprozesses.
            // Die Kommunikation erfolgt textbasiert (CSV-Format), da keine Shared-Memory zwischen Prozessen existiert.
            PrintWriter out = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()), true);
            out.println(w[i][0] + ";" + w[i][1] + ";" + b + ";" + k + ";" + t +
                    ";" + n + ";" + m + ";" + e + ";" + p + ";" + q + ";" + functionId);

            // Vorbereiten des Lesens vom Standard-Output des Kindprozesses
            readers[i] = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        }

        System.out.println("ExecuteBA: Warte auf Ergebnisse...");
        List<String> resultsBA = new ArrayList<>();

        // --- Phase 2: Ergebnis-Aggregation und Synchronisation (IPC-Read) ---
        for (int i = 0; i < numOfProcesses; i++) {
            // Blockierendes Lesen: Wir warten hier, bis der jeweilige Prozess fertig ist und sein Ergebnis sendet.
            // Dies wirkt als implizite Barriere für das Ende der Gesamtberechnung.
            String s = readers[i].readLine();

            if (s != null) {
                resultsBA.add(s);
            } else {
                // Fehlerbehandlung: Auslesen des ErrorStreams bei Prozessabsturz
                BufferedReader errReader = new BufferedReader(new InputStreamReader(processes[i].getErrorStream()));
                StringBuilder errLog = new StringBuilder();
                String line;
                while ((line = errReader.readLine()) != null) {
                    errLog.append(line).append("\n");
                }
                resultsBA.add("FEHLER (Prozess " + i + "): " + errLog.toString());
            }

            // Sicherstellen, dass der Prozess vollständig terminiert ist (Ressourcenfreigabe)
            processes[i].waitFor();
        }

        System.out.println("\n---- Ergebnisübersicht ----");
        for (int i = 0; i < resultsBA.size(); i++) {
            System.out.println("Worker-Prozess " + i + ": " + resultsBA.get(i));
        }
    }
}