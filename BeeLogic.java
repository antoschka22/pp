import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeeLogic {

    // 3. Random Performance Fix: KEIN static Random mehr!
    // private static final Random rand = new Random(); <-- GELÖSCHT

    /**
     * Verarbeitet einen einzelnen Block (Berechnet Fitness für alle Bienen darin).
     */
    public static void processBlock(BeeBlock block) {
        // Lokale Random Instanz für diesen Thread/Aufruf vermeiden Contention
        Random rand = new Random();

        double range = block.end - block.start;

        for (int i = 0; i < block.numBees; i++) {
            double pos = block.start + (rand.nextDouble() * range);

            // Grenzen beachten
            if (pos < Worker.wStart) pos = Worker.wStart;
            if (pos > Worker.wEnd) pos = Worker.wEnd;

            double fitness = calculateFitness(pos, Worker.functionId);

            if (fitness > block.bestFitness) {
                block.bestFitness = fitness;
                block.bestPosition = pos;
            }
        }
        block.setProcessed(true);
    }

    /**
     * Rekrutierungs-Logik (Evolutionärer Schritt).
     */
    public static List<BeeBlock> recruit(List<BeeBlock> oldBlocks, int n, int m, int e, int p, int q, int b) {
        List<BeeBlock> newBlocks = new ArrayList<>();

        // 1. Sortieren nach Fitness (absteigend)
        // Hinweis: Wir erstellen eine kopierbare Liste, falls oldBlocks unmodifiable wäre (hier nicht der Fall, aber sicher)
        List<BeeBlock> sortedBlocks = new ArrayList<>(oldBlocks);
        sortedBlocks.sort((b1, b2) -> Double.compare(b2.bestFitness, b1.bestFitness));

        // 2. Elite-Stellen
        int blocksPerElite = Math.max(1, q / b); // Safety Check div 0
        double neighborhood = (Worker.wEnd - Worker.wStart) * 0.05; // 5% Nachbarschaft

        for (int i = 0; i < e && i < sortedBlocks.size(); i++) {
            BeeBlock elite = sortedBlocks.get(i);
            createLocalSearchBlocks(newBlocks, elite, blocksPerElite, b, neighborhood);
        }

        // 3. Ausgewählte Stellen
        int blocksPerSelected = Math.max(1, p / b);
        for (int i = e; i < m && i < sortedBlocks.size(); i++) {
            BeeBlock selected = sortedBlocks.get(i);
            createLocalSearchBlocks(newBlocks, selected, blocksPerSelected, b, neighborhood);
        }

        // 4. Scouts (Rest auffüllen)
        int currentBees = newBlocks.stream().mapToInt(blk -> blk.numBees).sum();
        int beesMissing = n - currentBees;

        if (beesMissing > 0) {
            int scoutBlocksNeeded = Math.max(1, beesMissing / b);
            double globalRange = (Worker.wEnd - Worker.wStart) / scoutBlocksNeeded;

            for (int i = 0; i < scoutBlocksNeeded; i++) {
                double start = Worker.wStart + (i * globalRange);
                double end = start + globalRange;
                if (end > Worker.wEnd) end = Worker.wEnd;
                newBlocks.add(new BeeBlock(start, end, b));
            }
        }

        return newBlocks;
    }

    private static void createLocalSearchBlocks(List<BeeBlock> targetList, BeeBlock origin, int numBlocks, int b, double neighborhood) {
        for (int k = 0; k < numBlocks; k++) {
            double center = origin.bestPosition;
            double start = center - (neighborhood / 2.0);
            double end = center + (neighborhood / 2.0);

            // Optional: Bounds Check hier schon, oder im processBlock lassen.
            // processBlock fängt es ab, daher hier ok.

            targetList.add(new BeeBlock(start, end, b));
        }
    }

    private static double calculateFitness(double x, int funcId) {
        // Rechenlast simulieren
        double dummy = 0;
        for(int k=0; k<200; k++) { dummy += Math.cos(x * k); }

        double result = 0;
        switch (funcId) {
            case 0: result = -(x * x); break; // Parabel
            case 1: result = -(x * x) + Math.sin(5 * x) * 10; break; // Sinus-Mix
            case 2: result = -( (x*x) + 10.0 - (10.0 * Math.cos(2 * Math.PI * x)) ); break; // Rastrigin
            default: result = -(x * x);
        }
        return result + (dummy * 0.0000001); // Verhindert Wegoptimieren der Schleife
    }
}