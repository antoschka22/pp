/**
 * Ein Worker-Thread, der Blöcke abarbeitet.
 */
public class BeeThread extends Thread {

    private final int id;

    public BeeThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Holt nächsten Block (wartet, falls Queue leer ist aber noch nicht Shutdown)
                BeeBlock block = BlockManager.getNextBlock();

                // Wenn null kommt, bedeutet das Shutdown
                if (block == null) {
                    break;
                }

                // Logik ausführen
                BeeLogic.processBlock(block);

                // Ergebnis melden (kann Worker aufwecken, wenn Runde fertig)
                BlockManager.reportFinishedBlock(block);
            }
        } catch (Exception e) {
            System.err.println("BeeThread " + id + " crashed: " + e.getMessage());
        }
    }
}