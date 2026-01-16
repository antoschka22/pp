import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enthält die algorithmische Logik ("Business Logic") des Bienenalgorithmus
 * PARADIGMA-KONTEXT:
 * Diese Klasse ist rein funktional aufgebaut (statische Methoden). Sie trennt die
 * Berechnung (parallelisierbar) von der Koordination (sequenziell)
 * * Es gibt zwei Hauptphasen:
 * 1. `processBlock`: Die rechenintensive Phase, die parallel auf den Worker-Threads läuft
 * Hier wird "Thread Confinement" angewendet – jeder Thread arbeitet nur auf seinen lokalen Daten
 * 2. `recruit`: Die Organisationsphase, die sequenziell im Main-Thread läuft
 * Hier wird globales Wissen genutzt, um die Arbeit für die nächste Runde zu verteilen
 */
public class BeeLogic {

    /**
     * Führt die eigentliche Arbeit eines Worker-Threads aus (Parallele Phase)
     * Verarbeitet einen kompletten Block von `b` Bienen
     * PERFORMANCE-HINWEIS:
     * Statt für jede Biene einen eigenen Task zu starten (zu feingranular, hoher Overhead),
     * bündeln wir `b` Bienen. Das reduziert Context-Switches und Queue-Contention drastisch
     * * THREAD-SAFETY:
     * Wir nutzen eine lokale `Random`-Instanz statt einer globalen statischen Instanz,
     * um "Contention" (Wettstreit um Ressourcen) im Multi-Threading-Kontext zu vermeiden
     * `java.util.Random` ist thread-safe, aber synchronisiert, was bei vielen Threads bremst
     *
     * @param block Der zu bearbeitende Aufgabenblock (Input & Output Container)
     */
    public static void processBlock(BeeBlock block) {
        // Lokale Random Instanz für diesen Thread/Aufruf vermeidet Synchronisations-Overhead
        Random rand = new Random();

        double range = block.end - block.start;

        // Iteration über alle Bienen im Block (Granularität b)
        for (int i = 0; i < block.numBees; i++) {
            double pos = block.start + (rand.nextDouble() * range);

            // Bounds Check: Sicherstellen, dass wir im global erlaubten Intervall bleiben
            if (pos < Worker.wStart) pos = Worker.wStart;
            if (pos > Worker.wEnd) pos = Worker.wEnd;

            // Teure Fitness-Berechnung
            double fitness = calculateFitness(pos, Worker.functionId);

            // Speichern des lokal besten Ergebnisses direkt im Block-Objekt
            // Da nur EIN Thread diesen Block bearbeitet, ist hier keine Synchronisation nötig
            if (fitness > block.bestFitness) {
                block.bestFitness = fitness;
                block.bestPosition = pos; // Wichtig für die Rekrutierung
            }
        }

        // Markierung für Debugging/Status
        block.setProcessed(true);
    }

    /**
     * Erstellt die neuen Aufgabenblöcke für die nächste Runde (Sequenzielle Phase)
     * ARCHITEKTUR-HINWEIS:
     * Diese Methode wird exklusiv vom Main-Thread ausgeführt, NACHDEM alle Worker-Threads
     * ihre Arbeit beendet haben (Barriere im BlockManager)
     * Da sie sequenziell läuft, können wir hier sicher auf die gesammelten Ergebnisse zugreifen
     * und komplexe Sortierlogik anwenden, ohne Race-Conditions zu fürchten
     *
     * @param oldBlocks Ergebnisse der vorherigen Runde
     * @param n,m,e,p,q,b Parameter des Algorithmus
     * @return Eine Liste neuer Blöcke für die Work-Queue
     */
    public static List<BeeBlock> recruit(List<BeeBlock> oldBlocks, int n, int m, int e, int p, int q, int b) {
        List<BeeBlock> newBlocks = new ArrayList<>();

        // 1. Sortieren der Ergebnisse nach Fitness (Globales Wissen nutzen)
        // Wir kopieren die Liste, um Seiteneffekte zu vermeiden
        List<BeeBlock> sortedBlocks = new ArrayList<>(oldBlocks);
        sortedBlocks.sort((b1, b2) -> Double.compare(b2.bestFitness, b1.bestFitness));

        // 2. Elite-Stellen (Die besten e Orte)
        // Hier wird intensiv gesucht: Viele Bienen (q) auf engem Raum
        // Division durch b (q/b) stellt sicher, dass wir saubere Blöcke erzeugen
        int blocksPerElite = Math.max(1, q / b);
        double neighborhood = (Worker.wEnd - Worker.wStart) * 0.05; // 5% lokale Nachbarschaft

        for (int i = 0; i < e && i < sortedBlocks.size(); i++) {
            BeeBlock elite = sortedBlocks.get(i);
            createLocalSearchBlocks(newBlocks, elite, blocksPerElite, b, neighborhood);
        }

        // 3. Ausgewählte Stellen (Die besten m-e Orte)
        // Hier wird moderat gesucht: Weniger Bienen (p) auf engem Raum
        int blocksPerSelected = Math.max(1, p / b);
        for (int i = e; i < m && i < sortedBlocks.size(); i++) {
            BeeBlock selected = sortedBlocks.get(i);
            createLocalSearchBlocks(newBlocks, selected, blocksPerSelected, b, neighborhood);
        }

        // 4. Scouts (Globale Suche)
        // Der Rest der Bienenpopulation (n - bereits vergebene) wird zufällig verteilt,
        // um lokale Optima zu verlassen
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

    /**
     * Hilfsmethode zur Erzeugung lokaler Suchblöcke um einen Punkt (Exploitation)
     */
    private static void createLocalSearchBlocks(List<BeeBlock> targetList, BeeBlock origin, int numBlocks, int b, double neighborhood) {
        for (int k = 0; k < numBlocks; k++) {
            // Zentriere die Suche um die beste gefundene Position
            double center = origin.bestPosition;
            double start = center - (neighborhood / 2.0);
            double end = center + (neighborhood / 2.0);

            // Erzeuge einen neuen Block. Die `processBlock` Methode kümmert sich später
            // darum, dass die zufälligen Punkte auch wirklich in [start, end] liegen
            targetList.add(new BeeBlock(start, end, b));
        }
    }

    /**
     * Die zu optimierende Zielfunktion
     * Enthält eine künstliche Verzögerung (Schleife mit cos/sin), um Rechenlast zu simulieren
     * Dies ist wichtig für die Parallelisierung, da bei zu trivialen Funktionen der
     * Verwaltungsoverhead (Threads starten/stoppen) den Gewinn durch Parallelität auffressen würde
     */
    private static double calculateFitness(double x, int funcId) {
        // Last-Simulation (CPU Burner)
        double dummy = 0;
        for(int k=0; k<200; k++) { dummy += Math.cos(x * k); }

        double result = 0;
        switch (funcId) {
            case 0: result = -(x * x); break; // Parabel (Maximum bei 0)
            case 1: result = -(x * x) + Math.sin(5 * x) * 10; break; // Sinus-Mix (Viele lokale Maxima)
            case 2: result = -( (x*x) + 10.0 - (10.0 * Math.cos(2 * Math.PI * x)) ); break; // Rastrigin (Komplex)
            default: result = -(x * x);
        }

        // Verhindert, dass der JIT-Compiler die Dummy-Schleife wegoptimiert ("Dead Code Elimination")
        return result + (dummy * 0.0000001);
    }
}