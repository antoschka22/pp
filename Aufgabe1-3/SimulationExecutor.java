import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutionException;

/**
 * STYLE: Parallel / Nebenläufig
 * Diese Klasse orchestriert die parallele Ausführung von Simulations-Tasks.
 * Ziel: Maximale CPU-Auslastung durch parallele Abarbeitung unabhängiger Simulationen (Parallelität)
 * und Gewährleistung einer sauberen, thread-sicheren Konsolenausgabe (Nebenläufigkeit).
 * Umsetzung:
 * - Parallel: Ein FixedThreadPool (taskExecutor) führt SimulationTask-Objekte aus.
 * - Nebenläufig: Ein SingleThreadExecutor (loggerExecutor) betreibt einen Logger-Task.
 * Dieser liest Nachrichten von einer thread-sicheren BlockingQueue und ist der
 * einzige Thread, der auf System.out schreibt, um Race Conditions zu verhindern.
 * Synchronisation: Der Hauptthread sammelt Ergebnisse via Future.get() (blockierend)
 * und stellt formatierte Strings in die logQueue.
 */
public class SimulationExecutor {

    private final ExecutorService taskExecutor; // Führt die Simulationen aus
    private final ExecutorService loggerExecutor; // Führt den Konsolen-Logger aus
    private final BlockingQueue<String> logQueue; // Thread-sichere Nachrichten-Warteschlange
    private static final String POISON_PILL = "::STOP_LOGGING::"; // Signal zum Beenden des Loggers
    private final List<Future<SimulationResult>> taskFutures = new ArrayList<>();

    // Konfigurationsparameter, die an die Tasks weitergegeben werden
    private final List<IPopulationEvent> possibleEvents;
    private final IBeeEvent possibleBeeEvent;
    private static final boolean USE_CSV_WEATHER = true;
    private static final String CSV_WEATHER_PATH = "wetterwien.csv";
    private static final double FIELD_MAX_X = 1000.0;
    private static final double FIELD_MAX_Y = 1000.0;

    /**
     * Konstruktor für den SimulationExecutor.
     *
     * @param possibleEvents     Liste der möglichen Pflanzen-Events.
     * @param possibleBeeEvent   Das mögliche Bienen-Event.
     */
    public SimulationExecutor(List<IPopulationEvent> possibleEvents, IBeeEvent possibleBeeEvent) {
        this.possibleEvents = possibleEvents;
        this.possibleBeeEvent = possibleBeeEvent;

        // Erstelle einen Thread-Pool mit so vielen Threads, wie CPU-Kerne vorhanden sind
        int coreCount = Runtime.getRuntime().availableProcessors();
        this.taskExecutor = Executors.newFixedThreadPool(coreCount);

        // Erstelle einen separaten Thread nur für die Konsolenausgabe
        this.loggerExecutor = Executors.newSingleThreadExecutor();

        // Erstelle die Warteschlange für die Log-Nachrichten
        this.logQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Startet den nebenläufigen Logger-Thread.
     * Dieser Thread wartet auf Nachrichten in der logQueue und gibt sie aus.
     */
    public void startLogger() {
        Runnable loggerTask = () -> {
            try {
                while (true) {
                    // Wartet blockierend, bis eine Nachricht verfügbar ist
                    String message = logQueue.take();

                    // Prüft auf das Beendigungs-Signal
                    if (POISON_PILL.equals(message)) {
                        break; // Schleife beenden und Thread sterben lassen
                    }
                    System.out.println(message);
                }
            } catch (InterruptedException e) {
                // Thread wurde unterbrochen, Status wiederherstellen
                Thread.currentThread().interrupt();
            }
        };
        // Starte den Logger-Task auf seinem eigenen Executor
        loggerExecutor.submit(loggerTask);
    }

    /**
     * Stellt eine Nachricht thread-sicher in die Log-Warteschlange.
     *
     * @param message Die auszugebende Nachricht.
     */
    private void log(String message) {
        try {
            logQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Erstellt und startet alle Simulations-Tasks für eine bestimmte Gruppe.
     *
     * @param groupIndex Der Index der Pflanzengruppe (0, 1 oder 2).
     * @param numRuns    Die Anzahl der Simulationsläufe (z.B. 10).
     * @param seeds      Das Array mit den Seeds für die Läufe.
     */
    public void submitAllTasks(int groupIndex, int numRuns, int[] seeds) {
        log(String.format("\n--- Starte %d parallele Simulationen für Gruppe %d ---", numRuns, groupIndex + 1));

        // Alte Ergebnisse löschen, falls der Executor wiederverwendet wird
        taskFutures.clear();

        for (int j = 0; j < numRuns; j++) {
            Callable<SimulationResult> task = new SimulationTask(
                    seeds[j],
                    groupIndex,
                    j,
                    this.possibleEvents,
                    this.possibleBeeEvent,
                    USE_CSV_WEATHER,
                    CSV_WEATHER_PATH,
                    FIELD_MAX_X,
                    FIELD_MAX_Y
            );

            // Reiche die Task beim Executor ein und speichere das Future-Objekt
            Future<SimulationResult> future = taskExecutor.submit(task);
            taskFutures.add(future);
        }
    }

    /**
     * Wartet auf die Beendigung aller eingereichten Tasks,
     * sammelt ihre Ergebnisse und loggt sie.
     */
    public void collectAndLogResults() {
        int successCount = 0;
        int failCount = 0;

        // Iteriere durch die Future-Objekte und warte auf jedes Ergebnis
        for (Future<SimulationResult> future : taskFutures) {
            try {
                // future.get() blockiert, bis der Task fertig ist
                SimulationResult result = future.get();

                if (result.success()) {
                    // Formatiere das erfolgreiche Ergebnis für den Log
                    String logMessage = String.format(
                            "\n[OK] Simulationslauf %d - Gruppe %d (Seed %d)\n%s",
                            result.runIndex() + 1,
                            result.groupIndex() + 1,
                            result.seed(),
                            result.resultSummary()
                    );
                    log(logMessage);
                    successCount++;
                } else {
                    // Formatiere das fehlerhafte Ergebnis für den Log
                    String logMessage = String.format(
                            "\n[FEHLER] Simulationslauf %d - Gruppe %d (Seed %d)\n   > Fehler: %s",
                            result.runIndex() + 1,
                            result.groupIndex() + 1,
                            result.seed(),
                            result.errorMessage()
                    );
                    log(logMessage);
                    failCount++;
                }

            } catch (InterruptedException | ExecutionException e) {
                // Ein Task ist katastrophal fehlgeschlagen
                log("[FATALER FEHLER] Ein Task konnte nicht ausgeführt oder abgefragt werden: " + e.getMessage());
                e.printStackTrace(); // Gibt den Stacktrace auf stderr aus
                failCount++;
            }
        }

        log(String.format("\n--- Alle Simulationen für diese Gruppe abgeschlossen. (Erfolgreich: %d, Fehler: %d) ---",
                successCount, failCount));
    }

    /**
     * Fährt alle Executor-Services sauber herunter.
     */
    public void shutdown() {
        log("Fahre Simulations-Manager herunter...");

        // Sagt dem Task-Executor, keine neuen Tasks anzunehmen
        taskExecutor.shutdown();

        // Sende das "Gift-Signal" an den Logger-Thread, damit er sich beendet
        log(POISON_PILL);

        // Sagt dem Logger-Executor, sich nach dem letzten Task zu beenden
        loggerExecutor.shutdown();
    }
}