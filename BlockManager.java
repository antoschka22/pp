import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Person C: BlockManager
 * Verwaltet die Aufgabenblöcke (BeeBlocks) für die Worker-Threads.
 * Thread-safe Implementierung.
 */
public class BlockManager {

    // Queue für die noch zu bearbeitenden Blöcke
    private static final Queue<BeeBlock> workQueue = new LinkedList<>();

    // Liste für die fertigen Ergebnisse (für die Rekrutierung durch Person B)
    private static final List<BeeBlock> finishedBlocks = new LinkedList<>();

    /**
     * Initialisiert den Manager (wird von Worker.java main aufgerufen).
     * Kann auch genutzt werden, um neue Blöcke für eine neue Runde bereitzustellen.
     */
    public static synchronized void addBlocks(List<BeeBlock> newBlocks) {
        workQueue.addAll(newBlocks);
    }

    /**
     * Setzt den Manager für eine neue Runde zurück (falls nötig).
     */
    public static synchronized void clearFinished() {
        finishedBlocks.clear();
    }

    /**
     * Gibt den nächsten zu bearbeitenden Block zurück.
     * Thread-safe Zugriff für BeeThreads.
     * @return BeeBlock oder null, wenn keine Arbeit mehr da ist.
     */
    public static synchronized BeeBlock getNextBlock() {
        return workQueue.poll();
    }

    /**
     * Nimmt einen bearbeiteten Block entgegen (optional, falls Ergebnisse gesammelt werden müssen).
     * Wird typischerweise von BeeThread oder BeeLogic aufgerufen.
     */
    public static synchronized void reportFinishedBlock(BeeBlock block) {
        finishedBlocks.add(block);
    }

    /**
     * Gibt alle bearbeiteten Blöcke zurück (für Person B zur Rekrutierung).
     */
    public static synchronized List<BeeBlock> getFinishedBlocks() {
        return new LinkedList<>(finishedBlocks);
    }

    /**
     * Initialisiert die erste Generation von Bienen (Scouts).
     * @param wStart Start des Wertebereichs dieses Prozesses
     * @param wEnd Ende des Wertebereichs dieses Prozesses
     * @param b Anzahl der Bienen pro Block
     * @param n Gesamtanzahl der Kundschafter (Scouts)
     */
    public static synchronized void init(double wStart, double wEnd, int b, int n) {
        workQueue.clear();
        finishedBlocks.clear();

        // Laut Angabe ist n ein Vielfaches von b.
        int numberOfBlocks = n / b;

        // Falls durch Rundungsfehler oder ungünstige Parameter 0 herauskommt, mindestens 1 Block
        if (numberOfBlocks < 1) numberOfBlocks = 1;

        List<BeeBlock> initialBlocks = new java.util.ArrayList<>();

        // Wir erstellen die Blöcke.
        // WICHTIG: Die eigentliche zufällige Positionierung der Bienen im Bereich [wStart, wEnd]
        // passiert typischerweise erst in der BeeLogic (wenn der Block das erste Mal verarbeitet wird)
        // oder wir geben dem Block den Bereich mit.
        for (int i = 0; i < numberOfBlocks; i++) {
            // Wir erzeugen einen Block, der 'b' Bienen repräsentiert.
            // Wir übergeben den Bereich, damit die Bienen wissen, wo sie starten dürfen.
            initialBlocks.add(new BeeBlock(wStart, wEnd, b));
        }

        workQueue.addAll(initialBlocks);
    }
}