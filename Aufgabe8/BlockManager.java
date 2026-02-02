import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Zentraler Monitor für die Thread-Synchronisation innerhalb eines Prozesses
 * Diese Klasse fungiert als Schnittstelle zwischen dem sequenziellen Main-Thread (Worker)
 * und den parallelen BeeThreads. Sie implementiert zwei wesentliche Konzepte der Nebenläufigkeit:
 * 1. Producer-Consumer: Der Main-Thread produziert Blöcke, BeeThreads konsumieren sie
 * 2. Barriere: Der Main-Thread wartet, bis alle Blöcke einer Runde verarbeitet wurden
 * Alle Methoden sind 'synchronized', um Race Conditions auf dem gemeinsamen Zustand
 * (WorkQueue, Zähler) zu verhindern
 */
public class BlockManager {

    // Gemeinsam genutzte Ressourcen (Shared Mutable State)
    // Die Queue dient als Puffer zwischen Produzent und Konsumenten
    private static final Queue<BeeBlock> workQueue = new LinkedList<>();

    // Sammelstelle für Ergebnisse, auf die threadsicher zugegriffen werden muss
    private static final List<BeeBlock> finishedBlocks = new LinkedList<>();

    // Synchronisations-Zustände für die Barriere
    private static int totalBlocksInRound = 0;
    private static int processedBlocksInRound = 0;

    // Flag für das kontrollierte Beenden (Graceful Shutdown) der Threads
    private static boolean isShutdown = false;

    /**
     * Produzenten-Methode: Stellt neue Arbeit für die Threads bereit
     * Wird vom Main-Thread (Worker) zu Beginn einer Runde aufgerufen
     * Setzt die Barriere-Zähler zurück und weckt wartende Threads auf
     */
    public static synchronized void addBlocks(List<BeeBlock> newBlocks) {
        workQueue.addAll(newBlocks);
        totalBlocksInRound = newBlocks.size();
        processedBlocksInRound = 0; // Reset für die neue Runde
        finishedBlocks.clear();     // Alte Ergebnisse verwerfen

        // notifyAll() weckt alle BeeThreads auf, die in getNextBlock()
        // im wait()-Zustand schlafen, da die Queue leer war
        BlockManager.class.notifyAll();
    }

    /**
     * Konsumenten-Methode: Threads holen sich hier Arbeit (Pull-Prinzip)
     * Implementiert das "Guarded Block" Muster:
     * Solange keine Arbeit da ist, wird gewartet (wait), um CPU-Zyklen zu sparen (kein Busy-Waiting)
     *
     * @return Der nächste zu bearbeitende Block oder null, wenn der Thread enden soll
     */
    public static synchronized BeeBlock getNextBlock() {
        // While-Schleife ist zwingend nötig wegen "Spurious Wakeups"
        // (Threads können ohne Grund aufwachen) und um die Bedingung erneut zu prüfen
        while (workQueue.isEmpty() && !isShutdown) {
            try {
                // Thread legt sich schlafen und gibt den Monitor (Lock) frei
                BlockManager.class.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        // Beendigungsbedingung: Wenn Shutdown signalisiert wurde UND keine Arbeit mehr da ist
        if (isShutdown && workQueue.isEmpty()) {
            return null;
        }

        return workQueue.poll();
    }

    /**
     * Meldet ein Teilergebnis zurück und prüft die Barriere-Bedingung
     * Wird von BeeThreads am Ende ihrer Berechnung aufgerufen
     */
    public static synchronized void reportFinishedBlock(BeeBlock block) {
        finishedBlocks.add(block);
        processedBlocksInRound++;

        // Barriere-Check:
        // Wenn der letzte Block der Runde verarbeitet wurde, wecken wir den Main-Thread auf
        if (processedBlocksInRound == totalBlocksInRound) {
            // Weckt den Worker-Main-Thread, der in waitForRoundCompletion() blockiert
            BlockManager.class.notifyAll();
        }
    }

    /**
     * Implementierung der Barriere für den Main-Thread
     * Der Main-Thread blockiert hier, solange die parallele Verarbeitung noch läuft
     * Dies stellt sicher, dass die Rekrutierungsphase erst startet, wenn alle Ergebnisse vorliegen
     */
    public static synchronized void waitForRoundCompletion() throws InterruptedException {
        while (processedBlocksInRound < totalBlocksInRound) {
            BlockManager.class.wait();
        }
    }

    /**
     * Gibt eine Kopie der Ergebnisse zurück (Thread-Safety für den Zugriff im Main-Thread)
     */
    public static synchronized List<BeeBlock> getFinishedBlocks() {
        return new LinkedList<>(finishedBlocks);
    }

    /**
     * Signalisiert allen Threads, dass keine neue Arbeit mehr kommt und sie sich beenden sollen
     */
    public static synchronized void stopThreads() {
        isShutdown = true;
        // Weckt alle Threads auf, damit sie das isShutdown-Flag prüfen und terminieren können
        BlockManager.class.notifyAll();
    }
}