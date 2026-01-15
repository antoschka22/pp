/**
 * Worker-Thread im "Thread-Pool"-Muster.
 * <p>
 * Dieser Thread arbeitet nach dem Pull-Prinzip:
 * Er holt sich selbstständig Aufgaben (Blöcke) vom BlockManager.
 * Wenn keine Arbeit da ist, legt er sich schlafen (wait), statt aktiv zu warten (busy waiting),
 * was CPU-Ressourcen spart.
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
                // Blockierender Aufruf:
                // Versucht, einen Block aus der Queue zu holen.
                // Wartet (wait()) im Monitor des BlockManagers, falls die Queue leer ist,
                // aber die Simulation noch nicht beendet wurde.
                BeeBlock block = BlockManager.getNextBlock();

                // Beendigungsbedingung:
                // null signalisiert, dass das Programm beendet wird (Shutdown-Flag gesetzt).
                if (block == null) {
                    break;
                }

                // Eigentliche Berechnung ohne Synchronisation (lokale Daten)
                BeeLogic.processBlock(block);

                // Ergebnis zurückmelden:
                // Dies kann potenziell den Main-Thread aufwecken (notify),
                // falls dieser Block der letzte der aktuellen Runde war (Barriere).
                BlockManager.reportFinishedBlock(block);
            }
        } catch (Exception e) {
            System.err.println("BeeThread " + id + " crashed: " + e.getMessage());
        }
    }
}