/**
 * Person C: BeeThread
 */
public class BeeThread extends Thread {

    private final int threadId;

    public BeeThread(int threadId) {
        this.threadId = threadId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Kritischer Abschnitt: Nächsten Block holen
                BeeBlock block = BlockManager.getNextBlock();

                // Wenn null zurückkommt, sind alle Blöcke der Runde erledigt
                if (block == null) {
                    break;
                }

                // --- KORREKTUR: Echte Logik statt Simulation ---
                // Ruft die Berechnung von Person B auf
                BeeLogic.processBlock(block);
            }
        } catch (Exception e) {
            System.err.println("Fehler in Thread " + threadId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}