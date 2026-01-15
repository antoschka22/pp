import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ExecuteBA {

    public static void executeBA(double[][] w, int b, int k, int t,
                                 int n, int m, int e, int p, int q,
                                 int functionId) throws Exception {

        int numOfProcesses = w.length;
        Process[] processes = new Process[numOfProcesses];
        BufferedReader[] readers = new BufferedReader[numOfProcesses];

        // Classpath holen
        String classpath = System.getProperty("java.class.path");

        System.out.println("ExecuteBA: Starte " + numOfProcesses + " Worker-Prozesse (FuncID: " + functionId + ")...");

        for (int i = 0; i < numOfProcesses; i++) {
            // HINWEIS: Wenn du packages nutzt (z.B. package antoschka22.pp;),
            // musst du hier "antoschka22.pp.Worker" schreiben!
            ProcessBuilder pB = new ProcessBuilder("java", "-cp", classpath, "Worker");

            // WICHTIG: Wir entfernen Redirect.INHERIT, um den Fehler selbst zu lesen
            // pB.redirectError(ProcessBuilder.Redirect.INHERIT);

            Process proc = pB.start();
            processes[i] = proc;

            PrintWriter out = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()), true);
            out.println(w[i][0] + ";" + w[i][1] + ";" + b + ";" + k + ";" + t +
                    ";" + n + ";" + m + ";" + e + ";" + p + ";" + q + ";" + functionId);

            readers[i] = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        }

        System.out.println("ExecuteBA: Warte auf Ergebnisse...");
        List<String> resultsBA = new ArrayList<>();

        for (int i = 0; i < numOfProcesses; i++) {
            String s = readers[i].readLine();

            if (s != null) {
                resultsBA.add(s);
            } else {
                // Diagnose: Wenn s null ist, ist der Prozess abgestürzt. ErrorStream lesen!
                BufferedReader errReader = new BufferedReader(new InputStreamReader(processes[i].getErrorStream()));
                StringBuilder errLog = new StringBuilder();
                String line;
                while ((line = errReader.readLine()) != null) {
                    errLog.append(line).append("\n");
                }
                resultsBA.add("FEHLER (Prozess " + i + "): " + errLog.toString());
            }

            processes[i].waitFor();
        }

        System.out.println("\n---- Ergebnisübersicht ----");
        for (int i = 0; i < resultsBA.size(); i++) {
            System.out.println("Worker-Prozess " + i + ": " + resultsBA.get(i));
        }
    }
}