/**
 * Person C: BeeThread
 * Repräsentiert einen Thread innerhalb eines Worker-Prozesses.
 * Holt sich wiederholt Blöcke vom BlockManager und lässt sie bearbeiten.
 */
public class BeeThread extends Thread {

    private final int threadId;

    public BeeThread(int threadId) {
        this.threadId = threadId;
    }

    @Override
    public void run() {
        try {
            // Solange es Blöcke in dieser Runde gibt...
            while (true) {
                // Kritischer Abschnitt: Nächsten Block holen
                BeeBlock block = BlockManager.getNextBlock();

                // Wenn null zurückkommt, sind alle Blöcke der Runde erledigt
                if (block == null) {
                    break;
                }

                // --- Hier wird die Logik von Person B aufgerufen ---
                // Da wir BeeLogic hier nicht haben, simulieren wir die Arbeit:
                // BeeLogic.process(block); 
                simulateProcessing(block);
            }
        } catch (Exception e) {
            System.err.println("Fehler in Thread " + threadId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Platzhalter für den Aufruf von BeeLogic (Person B).
     */
    private void simulateProcessing(BeeBlock block) {
        // Simulation: Wir tun so, als würden wir Bienen berechnen.
        // In der echten Lösung steht hier: BeeLogic.calculateBlock(block);

        // Simuliert Rechenzeit basierend auf Blockgröße (Person A/B Parameter)
        // Achtung: In der echten Abgabe hier KEIN Thread.sleep verwenden, sondern rechnen!
        // Dies ist nur, damit man beim Testen sieht, dass was passiert.
        double dummy = 0;
        for (int i = 0; i < 1000; i++) {
            dummy += Math.sin(i) * Math.cos(i);
        }
        block.setProcessed(true); // Markieren als erledigt
    }
}