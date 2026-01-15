import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BeeLogic {

    // Zufallsgenerator für Positionierung
    private static final Random rand = new Random();

    /**
     * Verarbeitet einen einzelnen Block (Berechnet Fitness für alle Bienen darin).
     */
    public static void processBlock(BeeBlock block) {
        double range = block.end - block.start;

        // Falls der Block neu ist und noch keine "bestPosition" hat (z.B. Scouts),
        // suchen wir im ganzen Block-Bereich.
        // Bei rekrutierten Blöcken ist die Suche lokal um die Startposition herum.
        // Die Logik hier ist vereinfacht: Wir suchen einfach N Bienen im Intervall [start, end].

        for (int i = 0; i < block.numBees; i++) {
            double pos = block.start + (rand.nextDouble() * range);

            // Grenzen beachten (falls durch lokale Suche der Bereich den globalen Bereich verlässt)
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
     * Erstellt neue Blöcke basierend auf den Ergebnissen der Vorrunde.
     */
    public static List<BeeBlock> recruit(List<BeeBlock> oldBlocks, int n, int m, int e, int p, int q, int b) {
        List<BeeBlock> newBlocks = new ArrayList<>();

        // 1. Sortieren nach Fitness (absteigend, beste zuerst)
        oldBlocks.sort((b1, b2) -> Double.compare(b2.bestFitness, b1.bestFitness));

        // 2. Elite-Stellen (die besten e) -> intensive lokale Suche (q Bienen)
        // Wir nehmen an, 'q' ist die Anzahl Bienen PRO Elite-Stelle.
        // Da wir in Blöcken arbeiten, müssen wir q Bienen in Blöcke der Größe b aufteilen.
        int blocksPerElite = q / b;

        // Suchradius für lokale Suche (dynamisch oder fest, hier fest 5% des Gesamtbereichs)
        double neighborhood = (Worker.wEnd - Worker.wStart) * 0.05;

        for (int i = 0; i < e && i < oldBlocks.size(); i++) {
            BeeBlock elite = oldBlocks.get(i);
            createLocalSearchBlocks(newBlocks, elite, blocksPerElite, b, neighborhood);
        }

        // 3. Ausgewählte Stellen (die nächsten m - e) -> weniger intensive Suche (p Bienen)
        int blocksPerSelected = p / b;
        for (int i = e; i < m && i < oldBlocks.size(); i++) {
            BeeBlock selected = oldBlocks.get(i);
            createLocalSearchBlocks(newBlocks, selected, blocksPerSelected, b, neighborhood);
        }

        // 4. Den Rest (Scouts) auffüllen -> Globale Suche
        // Wir zählen, wie viele Bienen wir schon haben
        int currentBees = newBlocks.stream().mapToInt(block -> block.numBees).sum();
        int beesMissing = n - currentBees;

        // Daraus neue Scout-Blöcke machen
        int scoutBlocksNeeded = beesMissing / b;
        double globalRange = (Worker.wEnd - Worker.wStart) / Math.max(1, scoutBlocksNeeded);

        for (int i = 0; i < scoutBlocksNeeded; i++) {
            // Scouts decken idealerweise den Raum ab oder sind rein zufällig.
            // Hier: Einfache Aufteilung oder Zufallsbereiche.
            double start = Worker.wStart + (i * globalRange);
            double end = start + globalRange;
            if (end > Worker.wEnd) end = Worker.wEnd;

            newBlocks.add(new BeeBlock(start, end, b));
        }

        return newBlocks;
    }

    private static void createLocalSearchBlocks(List<BeeBlock> targetList, BeeBlock origin, int numBlocks, int b, double neighborhood) {
        for (int k = 0; k < numBlocks; k++) {
            double center = origin.bestPosition;
            double start = center - (neighborhood / 2.0);
            double end = center + (neighborhood / 2.0);

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
        return result + (dummy * 0.0000001);
    }
}