import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enthält die algorithmische Logik ("Business Logic") des Bienenalgorithmus
 * Diese Klasse ist rein funktional aufgebaut. Sie trennt die
 * Berechnung (parallelisierbar) von der Koordination (sequenziell)
 * Es gibt zwei Hauptphasen:
 * 1. 'processBlock': Die rechenintensive Phase, die parallel auf den Worker-Threads läuft
 * Hier wird "Thread Confinement" angewendet – jeder Thread arbeitet nur auf seinen lokalen Daten
 * 2. 'recruit': Die Organisationsphase, die sequenziell im Main-Thread läuft
 * Hier wird globales Wissen genutzt, um die Arbeit für die nächste Runde zu verteilen
 */
public class BeeLogic {

    /**
     * Führt die eigentliche Arbeit eines Worker-Threads aus (Parallele Phase)
     * Verarbeitet einen kompletten Block von b Bienen
     * Statt für jede Biene einen eigenen Task zu starten (zu feingranular, hoher Overhead),
     * bündelt man b Bienen. Das reduziert Context-Switches und Queue-Contention drastisch
     * Man nutzt eine lokale 'Random'-Instanz statt einer globalen statischen Instanz,
     * um "Contention" (Wettstreit um Ressourcen) im Multi-Threading-Kontext zu vermeiden
     * 'java.util.Random' ist thread-safe, aber synchronisiert, was bei vielen Threads bremst
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
            // Da nur ein Thread diesen Block bearbeitet, ist hier keine Synchronisation nötig
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
     * Diese Methode wird exklusiv vom Main-Thread ausgeführt, nachdem alle Worker-Threads
     * ihre Arbeit beendet haben (Barriere im BlockManager)
     * Da sie sequenziell läuft, können wir hier sicher auf die gesammelten Ergebnisse zugreifen
     * und komplexe Sortierlogik anwenden, ohne Race-Conditions zu fürchten
     *
     * @param oldBlocks Ergebnisse der vorherigen Runde
     * @param n Gesamtanzahl Kundschafter
     * @param m Anzahl der besten Felder
     * @param e Anzahl der exzellenten Felder
     * @param p Rekrutierte Bienen für exzellente Felder (intensiv)
     * @param q Rekrutierte Bienen für ausgewählte Felder (weniger intensiv)
     * @param s Größe des Feldes relativ zum Suchraum (z.B. 0.05 für 5%)
     * @param b Blockgröße
     * @return Eine Liste neuer Blöcke für die Work-Queue
     */
    public static List<BeeBlock> recruit(List<BeeBlock> oldBlocks, int n, int m, int e, int p, int q, double s, int b) {
        List<BeeBlock> newBlocks = new ArrayList<>();

        // Sortieren der Ergebnisse nach Fitness (Globales Wissen nutzen)
        // Wir kopieren die Liste, um Seiteneffekte zu vermeiden
        List<BeeBlock> sortedBlocks = new ArrayList<>(oldBlocks);
        sortedBlocks.sort((b1, b2) -> Double.compare(b2.bestFitness, b1.bestFitness));

        // Variable Nachbarschaftsgröße basierend auf Parameter s
        double neighborhood = (Worker.wEnd - Worker.wStart) * s;

        // Elite-Stellen (Die besten e Orte)
        // Division durch b (p/b) stellt sicher, dass wir saubere Blöcke erzeugen
        int blocksPerElite = Math.max(1, p / b);

        for (int i = 0; i < e && i < sortedBlocks.size(); i++) {
            BeeBlock elite = sortedBlocks.get(i);
            createLocalSearchBlocks(newBlocks, elite, blocksPerElite, b, neighborhood);
        }

        // Ausgewählte Stellen (Die besten m-e Orte)
        int blocksPerSelected = Math.max(1, q / b);
        for (int i = e; i < m && i < sortedBlocks.size(); i++) {
            BeeBlock selected = sortedBlocks.get(i);
            createLocalSearchBlocks(newBlocks, selected, blocksPerSelected, b, neighborhood);
        }

        // Scouts (Globale Suche)
        // Feste Anzahl Scouts basierend auf n - m (restliche Kundschafter)
        // Unabhängig davon, wie viele Bienen wir oben bereits rekrutiert haben.
        // Das garantiert, dass die globale Suche (Exploration) nie ganz ausfällt.
        int numScouts = n - m;

        if (numScouts > 0) {
            int scoutBlocksNeeded = Math.max(1, numScouts / b);
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

        double result = switch (funcId) {
            case 0 -> -(x * x); // Parabel (Maximum bei 0)
            case 1 -> -(x * x) + Math.sin(5 * x) * 10; // Sinus-Mix (Viele lokale Maxima)
            case 2 -> -((x * x) + 10.0 - (10.0 * Math.cos(2 * Math.PI * x))); // Rastrigin (Komplex)
            default -> -(x * x);
        };

        // Verhindert, dass der JIT-Compiler die Dummy-Schleife wegoptimiert ("Dead Code Elimination")
        return result + (dummy * 0.0000001);
    }
}