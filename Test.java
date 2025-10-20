/*
Miriam Reumann hat die Klasse Weather geschrieben sowie in der Klasse Simulation die Methoden logDailyState, logYearlyState, runRestPhase, printFinalResults und printDetailedResults erstellt.
Antonio Molina Gradischnig hat die Klassen Plantspecies und Beepopulation geschrieben sowie in der Klasse Simulation die Variabeln, den Konstruktor und die Methoden run und runVegetationPeriod erstellt.
Simon Oberdörfer hat die Klassen Plantpopulation und Test vollständig geschrieben.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Diese Klasse dient als Einstiegspunkt der Simulation. Es werden drei zu testende Pflanzengruppen definiert und in
 * 10 Läufen mit unterschiedlicher Witterung (gesteuert über Seeds für einen fairen Vergleich)
 * über 25 Jahre simuliert.
 * Außerdem werden 2 zusätzliche Debug-Läufe durchgeführt: einmal jährlich über 25 Jahre und einmal täglich über 1 Jahr.
 */
public class Test {

    private static final int YEARS = 25;
    private static final int SIMULATION_RUNS = 10;

    private static final double START_BEE_Population = 3000;
    private static final int[] SEEDS = {43, 568, 93, 345, 21, 6, 34, 86, 9, 11};


    public static void main(String[] args) {
        System.out.println("=== Wildbienen-Simulation ===");

        // Die Pflanzengruppen werden definiert
        List<Plantspecies> plantsGroup1 = createPlantsGroup1();
        List<Plantspecies> plantsGroup2 = createPlantsGroup2();
        List<Plantspecies> plantsGroup3 = createPlantsGroup3();
        List<List<Plantspecies>> plantsGroups = List.of(plantsGroup1, plantsGroup2, plantsGroup3);

        // die 10 gleichen Simulationsläufe für alle 3 Gruppen
        runSummary(plantsGroups);

        // Debugging der Gruppen
        //runDebugSimulation(plantsGroup1);
        //runDebugSimulation(plantsGroup2);
        //runDebugSimulation(plantsGroup3);

        System.out.println("\n=== Ende der Simulation ===");

    }

    /**
     * Führt die 10 Simulationsläufe für alle 3 Gruppen durch.
     * @param plantsGroups Liste der verschiedenen Pflanzenzusammensetzungen
     */
    private static void runSummary(List<List<Plantspecies>> plantsGroups) {
        System.out.println("\n=== Simulationsergebnisse ===");

        for (int i = 0; i < plantsGroups.size(); i++) {
            List<Plantspecies> currentGroup = plantsGroups.get(i);
            System.out.printf("\n--- Ergebnis für Gruppe %d ---\n", (i + 1));

            // Führe 10 Simulationsläufe für Gruppe (i + 1) durch
            for (int j = 0; j < SIMULATION_RUNS; j++) {
                Random random = new Random(SEEDS[j]);
                Simulation sim = new Simulation(YEARS, START_BEE_Population, currentGroup, random, 100);
                sim.run(false, false);
                System.out.printf("\n    Simulationslauf %d - Gruppe %d (Seed %d)\n", (j + 1), (i + 1), SEEDS[j]);
                sim.printFinalResults();
            }
        }
    }

    /**
     *
     * Überprüft das korrekte Vorgehen für einen Simulationslauf mit jährlichen Zwischenergebnissen und für ein Jahr täglichen Zwischenergebnissen.
     */
    private static void runDebugSimulation(List<Plantspecies> debuggingPlantsGroup) {
        System.out.println("\n\n\n----------------------------------------------");
        System.out.println("------ Debugging Ausgabe ------");
        System.out.println("----------------------------------------------");

        System.out.println("\n----- Jährliches Zwischenergebnis -----");
        Random yearlyRandom = new Random(SEEDS[0]);
        Simulation yearlySimulation = new Simulation(YEARS, START_BEE_Population, debuggingPlantsGroup, yearlyRandom, 100);
        yearlySimulation.run(false, true);
        yearlySimulation.printDetailedResults();

        System.out.println("\n----- tägliches Zwischenergebnis (für ein Jahr) -----\n");
        Random dailyRandom = new Random(SEEDS[0]);
        Simulation dailySimulation = new Simulation(1, START_BEE_Population, debuggingPlantsGroup, dailyRandom, 100);
        dailySimulation.run(true, false);
        dailySimulation.printDetailedResults();


    }

    /**
     * Erstellt die Pflanzen für Gruppe 1.
     * @return Liste der Pflanzen in Gruppe 1.
     */
    private static List<Plantspecies> createPlantsGroup1() {
        List<Plantspecies> plants = new ArrayList<>();
        try {
            // 15 Pflanzen
            plants.add(new Plantspecies("Rose", 1.3, 2.2, 0.05, 0.99, 300, 1200, 1.0 / 200.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Tulpe", 2.5, 3.0, 0.05, 0.97, 800, 1600, 1.0 / 250.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Narzisse", 2.5, 4.3, 0.05, 0.95, 700, 1800, 1.0 / 300.0, 1.0 / 1300.0));
            plants.add(new Plantspecies("Sonnenblume", 2.9, 5.3, 0.05, 0.97, 800, 2100, 1.0 / 350.0, 1.0 / 1500.0));
            plants.add(new Plantspecies("Lilie", 1.8, 3.7, 0.05, 0.95, 700, 1600, 1.0 / 400.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Nelke", 1.3, 4.0, 0.05, 0.96, 150, 700, 1.0 / 220.0, 1.0 / 750.0));
            plants.add(new Plantspecies("Veilchen", 1.4, 3.4, 0.05, 0.95, 250, 900, 1.0 / 280.0, 1.0 / 850.0));
            plants.add(new Plantspecies("Geranie", 1.3, 2.8, 0.05, 0.99, 700, 1400, 1.0 / 300.0, 1.0 / 900.0));
            plants.add(new Plantspecies("Ringelblume", 1.7, 3.5, 0.05, 0.99, 800, 1600, 1.0 / 330.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Mohn", 4.5, 6.9, 0.02, 0.98, 1100, 1900, 1.0 / 350.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Sonnenhut", 1.4, 3.6, 0.05, 0.95, 250, 900, 1.0 / 280.0, 1.0 / 850.0));
            plants.add(new Plantspecies("Schleierkraut", 1.6, 3.7, 0.03, 0.97, 750, 1550, 1.0 / 250.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Mädchenauge", 1.6, 4.8, 0.03, 0.98, 800, 1700, 1.0 / 500.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Indianernessel", 2.9, 6.9, 0.01, 0.95, 1000, 2000, 1.0 / 400.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Duftwicke", 8.9, 13.8, 0.045, 0.94, 1200, 2200, 1.0 / 250.0, 1.0 / 1100.0));
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler beim Erstellen der Pflanzengruppe 1: " + e.getMessage());
        }
        return plants;
    }

    /**
     * Erstellt die Pflanzen für Gruppe 2.
     * @return Liste der Pflanzen in Gruppe 2.
     */
    private static List<Plantspecies> createPlantsGroup2() {
        List<Plantspecies> plants = new ArrayList<>();
        try {
            //17 Pflanzen
            plants.add(new Plantspecies("Krokus", 3.8, 6.25, 0.04, 0.88, 300, 850, 1.0 / 240.0, 1.0 / 700.0));
            plants.add(new Plantspecies("Margerite", 5.9, 8.3, 0.02, 0.86, 1000, 2000, 1.0 / 300.0, 1.0 / 1200.0));
            plants.add(new Plantspecies("Begonie", 4.8, 14.3, 0.04, 0.97, 1150, 2050, 1.0 / 300.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Lupine", 2.55, 4.25, 0.015, 0.94, 700, 1800, 1.0 / 300.0, 1.0 / 1300.0));
            plants.add(new Plantspecies("Zinnie", 19.85, 31.2, 0.05, 0.99, 1300, 2000, 1.0 / 300.0, 1.0 / 900.0));
            plants.add(new Plantspecies("Petunie", 3.5, 5.1, 0.03, 0.87, 350, 1000, 1.0 / 300.0, 1.0 / 800.0));
            plants.add(new Plantspecies("Glockenheide", 5.25, 7.15, 0.025, 0.87, 800, 2100, 1.0 / 350.0, 1.0 / 1500.0));
            plants.add(new Plantspecies("Astern", 5.9, 7.2, 0.02, 0.96, 1100, 2000, 1.0 / 350.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Rittersporn", 2.1, 2.8, 0.01, 0.93, 650, 1300, 1.0 / 250.0, 1.0 / 800.0));
            plants.add(new Plantspecies("Passionsblume", 5.9, 10.3, 0.045, 0.85, 1100, 1900, 1.0 / 280.0, 1.0 / 900.0));
            plants.add(new Plantspecies("Schneeglöckchen", 2.55, 4.05, 0.04, 0.97, 400, 950, 1.0 / 350.0, 1.0 / 750.0));
            plants.add(new Plantspecies("Anemone", 2.85, 5.2, 0.03, 0.96, 900, 1800, 1.0 / 220.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Kalla", 13.95, 21.15, 0.035, 0.95, 1250, 2150, 1.0 / 280.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Wicken", 5.9, 12.3, 0.05, 0.94, 1100, 2100, 1.0 / 300.0, 1.0 / 1200.0));
            plants.add(new Plantspecies("Spornblume", 2.1, 3.75, 0.055, 0.99, 800, 1600, 1.0 / 330.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Steppenkerze", 2.9, 4.7, 0.025, 0.96, 180, 750, 1.0 / 200.0, 1.0 / 800.0));
            plants.add(new Plantspecies("Buschwindröschen", 1.6, 3.3, 0.02, 0.97, 600, 1600, 1.0 / 200.0, 1.0 / 1200.0));
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler beim Erstellen der Pflanzengruppe 2: " + e.getMessage());
        }
        return plants;
    }

    /**
     * Erstellt die Pflanzen für Gruppe 3.
     * @return Liste der Pflanzen in Gruppe 3.
     */
    private static List<Plantspecies> createPlantsGroup3() {
        List<Plantspecies> plants = new ArrayList<>();
        try {
            // 20 Pflanzen
            plants.add(new Plantspecies("Rose", 1.8, 2.55, 0.05, 0.98, 350, 1200, 1.0 / 250.0, 1.0 / 1250.0));
            plants.add(new Plantspecies("Tulpe", 2.8, 3.2, 0.042, 0.95, 700, 1600, 1.0 / 200.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Narzisse", 3.5, 5.1, 0.03, 0.96, 900, 1900, 1.0 / 220.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Sonnenblume", 5.5, 7.35, 0.034, 0.9, 850, 2100, 1.0 / 350.0, 1.0 / 1500.0));
            plants.add(new Plantspecies("Wicken", 5.9, 12.3, 0.05, 0.94, 1100, 2100, 1.0 / 300.0, 1.0 / 1200.0));
            plants.add(new Plantspecies("Lilie", 1.7, 4.3, 0.05, 0.96, 235, 850, 1.0 / 500.0, 1.0 / 930.0));
            plants.add(new Plantspecies("Veilchen", 2.4, 3.2, 0.05, 0.93, 720, 1250, 1.0 / 200.0, 1.0 / 900.0));
            plants.add(new Plantspecies("Geranie", 3.1, 3.3, 0.065, 0.92, 800, 1600, 1.0 / 330.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Ringelblume", 2.5, 4.4, 0.035, 0.94, 650, 1800, 1.0 / 300.0, 1.0 / 1300.0));
            plants.add(new Plantspecies("Mohn", 3.9, 8.1, 0.04, 0.96, 1000, 2000, 1.0 / 500.0, 1.0 / 1200.0));
            plants.add(new Plantspecies("Sonnenhut", 3.2, 5.1, 0.05, 0.85, 320, 860, 1.0 / 320.0, 1.0 / 790.0));
            plants.add(new Plantspecies("Schleierkraut", 2.3, 3.9, 0.02, 0.99, 660, 1340, 1.0 / 242.0, 1.0 / 1500.0));
            plants.add(new Plantspecies("Mädchenauge", 3.4, 6.6, 0.05, 0.96, 950, 1830, 1.0 / 600.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Indianernessel", 1.2, 3.1, 0.025, 0.97, 600, 1600, 1.0 / 200.0, 1.0 / 1200.0));
            plants.add(new Plantspecies("Duftwicke", 8.4, 13.9, 0.05, 0.92, 1200, 2200, 1.0 / 250.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Salbei", 1.6, 3.9, 0.05, 0.97, 750, 1550, 1.0 / 250.0, 1.0 / 1000.0));
            plants.add(new Plantspecies("Aster", 4.2, 9.2, 0.001, 0.99, 1100, 2000, 1.0 / 350.0, 1.0 / 950.0));
            plants.add(new Plantspecies("Krokus", 3.8, 5.2, 0.02, 0.8, 600, 1600, 1.0 / 560.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Löwenzahn", 4.4, 13.8, 0.032, 0.95, 1100, 2000, 1.0 / 350.0, 1.0 / 1100.0));
            plants.add(new Plantspecies("Lavendel", 4.2, 6.5, 0.002, 0.9, 800, 1900, 1.0 / 450.0, 1.0 / 2000.0));
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler beim Erstellen der Pflanzengruppe 3: " + e.getMessage());
        }
        return plants;
    }
}
