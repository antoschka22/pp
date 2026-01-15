import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Verwaltet die Aufgabenblöcke und die Synchronisation zwischen Worker (Main) und BeeThreads.
 * Implementiert die Barriere mit wait() und notifyAll().
 */
public class BlockManager {

    private static final Queue<BeeBlock> workQueue = new LinkedList<>();
    private static final List<BeeBlock> finishedBlocks = new LinkedList<>();

    // Synchronisations-Zustände
    private static int totalBlocksInRound = 0;
    private static int processedBlocksInRound = 0;
    private static boolean isShutdown = false;

    /**
     * Wird vom Worker aufgerufen, um neue Blöcke für eine Runde bereitzustellen.
     */
    public static synchronized void addBlocks(List<BeeBlock> newBlocks) {
        workQueue.addAll(newBlocks);
        totalBlocksInRound = newBlocks.size();
        processedBlocksInRound = 0; // Reset für die neue Runde
        finishedBlocks.clear();     // Alte Ergebnisse verwerfen (wurden schon verarbeitet)

        // Alle wartenden Threads aufwecken, da es neue Arbeit gibt
        BlockManager.class.notifyAll();
    }

    /**
     * Threads rufen dies auf, um Arbeit zu holen.
     * WARTET, wenn Queue leer ist, aber das Programm noch nicht beendet ist.
     */
    public static synchronized BeeBlock getNextBlock() {
        while (workQueue.isEmpty() && !isShutdown) {
            try {
                // Warten, bis der Main-Thread neue Blöcke nachlegt oder shutdown signalisiert
                BlockManager.class.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        // Wenn wir aufgewacht sind und shutdown ist true (und queue leer), dann null zurückgeben -> Thread beendet sich
        if (isShutdown && workQueue.isEmpty()) {
            return null;
        }

        return workQueue.poll();
    }

    /**
     * Wird vom Thread aufgerufen, wenn ein Block fertig berechnet ist.
     */
    public static synchronized void reportFinishedBlock(BeeBlock block) {
        finishedBlocks.add(block);
        processedBlocksInRound++;

        // Prüfen, ob die Runde komplett fertig ist
        if (processedBlocksInRound == totalBlocksInRound) {
            // Worker-Thread (Main) aufwecken, der in waitForRoundCompletion wartet
            BlockManager.class.notifyAll();
        }
    }

    /**
     * Der Main-Worker ruft dies auf, um zu warten, bis alle Threads fertig sind (Barriere).
     */
    public static synchronized void waitForRoundCompletion() throws InterruptedException {
        while (processedBlocksInRound < totalBlocksInRound) {
            BlockManager.class.wait();
        }
    }

    /**
     * Gibt die Ergebnisse der aktuellen Runde zurück (für BeeLogic).
     */
    public static synchronized List<BeeBlock> getFinishedBlocks() {
        return new LinkedList<>(finishedBlocks);
    }

    /**
     * Signalisierte allen Threads, dass sie sich beenden sollen.
     */
    public static synchronized void stopThreads() {
        isShutdown = true;
        BlockManager.class.notifyAll(); // Alle aufwecken, damit sie merken "isShutdown == true"
    }
}