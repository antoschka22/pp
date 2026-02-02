/*
Miriam Reumann
    Aufgabe 2: hat die Klasse HoneyBeePopulation implementiert, um Honigbienen als Konkurrenten und Bestäuber hinzuzufügen. Um die sprunghaften 1%/3%
    Vigor-Reduktionen in Plantpopulation zu ersetzen, habe ich die updateVigor-Methode refaktoriert, indem ich eine kontinuierliche Stressfunktion
    eingeführt habe. Außerdem habe ich die Blühlogik bei der updateBloom()-Methode erweitert, damit die Blühlogik nicht mehr starr von aufsummierten
    Sonnenstunden abhängt. Dabei habe ich neue Variablen in der Plantspecies-Klasse und der RandomWeather-Klasse erstellt und dort ebenfalls die
    simulateChange-Methode geändert, sodass die der Verlauf der Temperatur mithilfe einer Sinusfunktion über die 240 Tage + täglichen Rauschen simuliert
    wird. Anschließend habe ich in der Test-Klasse fehlende Werte eingetragen bei jeder Pflanzengruppe und 2 STYLE Kommentare hinzugefügt, an Stellen,
    die ich implementiert habe.
    Aufgabe 3: hat die Zusicherungen für die folgenden Klassen geschrieben: Plantspecies und Plantpopulation. Ich habe 2 GOOD und 2 BAD Kommentare
    geschrieben. Zudem habe ich die SimulationAnalytics-Klasse implementiert, die eine neue Funktionalität im funktionalen Stil implementiert.
    Implementiert ein Statistik-Modul für die Simulation. Dieses Modul ändert keinen Zustand, sondern berechnet nur Ergebnisse. Das passt perfekt
    zur referentiellen Transparenz. Dementsprechend habe ich in der Test-Klasse die Ausgabe für die neu erstellten Methoden gemacht, entsprechend
    den bisherigen Testfällen.
Antonio Molina Gradischnig
    Aufgabe 2: hat die alle Interfaces implementiert, außer IWeather. Mit den implementieren der Interfaces musste ich auch
    die Klassen die damit betroffen sind ändern, damit alles strukturiert ist. Ich habe das System mit den Events erstellt,
    wo es zwei Pflanzenpopulation Events gibt und eine Bienenpopulation Event und eins davon täglich mit einer Wahrscheinlichkeit
    von 0.1% auftreten kann. Und als letztes habe ich das System mit der neuen Wahrscheinlichkeitsverteilungen erstellt und alle Methoden,
    die davor mit einem Random Wert gearbeitet haben, geändert. In der Klasse Test und Simulation habe ich nur ein Paar Stellen ergänzt,
    um meine neue implementierte Logik zu implementiere. Zb Events und Wahrscheinlichkeitsverteilungen. 3-4 STYLE Kommentare wurden an
    Stellen, die ich implementiert habe, erstellt
    Aufgabe 3: hat die die Zusicherung für folgende Klassen geschrieben: DroughtEvent, GaussianDistribution, HoneyBeePopulation, IBeeEvent,
    IBeePopulation, IDistribution, MowingEvent, NestingSite, PesticideEvent und WildbeePopulation. Außerdem habe ich die Klasse SimulationExecutor
    und den Record SimulationResult erstellt. Die Executor Klasse ist da um die Tasks parallel zu starten und die Ergebnisse sammelt und dabei
    nebenläufig den Fortschritt und die Ergebnisse sicher auf der Konsole auszugeben. Außerdem habe ich 2 Good und 2 Bad kommentare zum Thema
    prozedual geschrieben und ca 4 Good und 3 Bad Kommentare im Thema OO geschrieben
Simon Oberdörfer
    Aufgabe 2: hat den Record Coordinates erstellt, um allen Pflanzen und Nistplätzen einen Standort zu geben.
    Er hat die Klasse NestingSide erstellt und die Nistplatzlogik in der runVegetationPeriod-Methode der Simulation-Klasse implementiert.
    Außerdem hat er eine maximale Flugweite bei den Bienenklassen integriert und in der Simulation eine Effizienz eingeführt
    (Bienen erhalten weniger Nahrung von Pflanzen, die weiter vom Nistplatz entfernt sind, immer abnehmend bis zur maximalen Flugweite).
    Des Weiteren hat er das Interface IWeather erstellt und die Klasse CSVWeather implementiert, da diese Klasse eine CSV-Datei
    mit realen Wetterdaten benötigt, hat er die wetterwien.csv Datei erstellt, erarbeitet und bereinigt.
    In der Test-Klasse wurden die createSites-Methode und die Wetterauswahl (zwischen CSV-Datei und Random) implementiert.
    Es wurden 2 STYLE-Kommentare erstellt.
    Aufgabe 3: hat die Zusicherungen für folgende Klassen geschrieben: Coordinates, CsvWeather, IPlantPopulation, IPopulationEvent,
    IWeather, RandomWeather, Simulation und Test. Außerdem hat er die SimulationTask-Klasse implementiert, die einen einzelnen Simulationslauf
    ausführt (stellt einen Teil der parallelen Simulation dar). Zudem hat er die main-Methode umgeschrieben, damit die parallele Simulation
    korrekt gestartet wird und der Exekutor erstellt wird. Er hat 2 BAD- und 2 GOOD-Kommentare verfasst.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Diese Klasse dient als Einstiegspunkt der Simulation. Es werden drei zu testende Pflanzengruppen definiert und in
 * 10 Läufen mit unterschiedlicher Witterung (gesteuert über Seeds für einen fairen Vergleich)
 * über 25 Jahre simuliert.
 * Außerdem werden 2 zusätzliche Debug-Läufe durchgeführt: einmal jährlich über 25 Jahre und einmal täglich über 1 Jahr.
 *
 * @invariant YEARS >= 1
 * @invariant SIMULATION_RUNS >= 1
 * @invariant CSV_WEATHER_PATH ist ein gültiger Pfad zu einer CSV-Datei
 * @invariant START_BEE_Population >= 1
 */
public class Test {

    public static final int YEARS = 25;
    private static final int SIMULATION_RUNS = 10;
    private static final double START_BEE_Population = 3000;

    //Konfiguration der Wetterauswahl
    private static final boolean USE_CSV_WEATHER = true;
    private static final String CSV_WEATHER_PATH = "wetterwien.csv";


    private static final double FIELD_MAX_X = 1000.0;
    private static final double FIELD_MAX_Y = 1000.0;
    private static final int[] SEEDS = {43, 568, 93, 345, 21, 6, 34, 86, 9, 11};


    /**
     * STYLE: Prozedural. Die main-Methode dient als Haupteinstiegspunkt
     * und steuert den gesamten Ablauf der Simulation von oben nach unten
     * (Top-Down-Kontrollfluss). Sie ruft nacheinander statische
     * Helfermethoden (z.B. createPlantsGroup1) und die Simulations-
     * objekte auf, ohne selbst wesentlichen Zustand zu kapseln.
     * GOOD: (Prozedural) Der Kontrollfluss in 'main' ist sehr gut gelungen.
     * Er folgt einem klaren Top-Down-Ansatz:
     * 1. Setup (Events erstellen)
     * 2. Hauptlogik (runSummary aufrufen)
     * 3. Debug-Logik (runDebugSimulation aufrufen)
     * 4. Abschluss (Print-Statement)
     * Es gibt keine Sprünge (goto) oder komplexen Schleifen, was die
     * Nachvollziehbarkeit extrem erleichtert.
     */
    public static void main(String[] args) {
        System.out.println("=== Wildbienen-Simulation ===");

        // Erstellt die Events, die auftreten könnten
        List<IPopulationEvent> possibleEvents = new ArrayList<>();
        possibleEvents.add(new DroughtEvent(0.05));
        possibleEvents.add(new MowingEvent(0.5, 0.1));
        IBeeEvent possibleBeeEven = new PesticideEvent(0.5);

        Random random = new Random();
        // Group 1 und 2 werden nicht verwendet, weil runDebugSimulation nur für Group 3 verwendet wird
        List<IPlantPopulation> plantPopulationsGroup1 = createPlantsGroup1(random);
        List<IPlantPopulation> plantPopulationsGroup2 = createPlantsGroup2(random);
        List<IPlantPopulation> plantPopulationsGroup3 = createPlantsGroup3(random);

        // Erstellt den Executor
        SimulationExecutor executor = new SimulationExecutor(
                possibleEvents,
                possibleBeeEven);

        // Startet den Logger Thread
        executor.startLogger();

        // Startet alle Simulations-Tasks für eine bestimmte Gruppe
        for (int i = 0; i < 3; i++) {
            executor.submitAllTasks(i, SIMULATION_RUNS, SEEDS);
            executor.collectAndLogResults();
        }
        executor.shutdown();

        // Debugging der Gruppen
        // runDebugSimulation(0, possibleEvents, possibleBeeEven, plantPopulationsGroup1);
        // runDebugSimulation(1, possibleEvents, possibleBeeEven, plantPopulationsGroup2);
        runDebugSimulation(2, possibleEvents, possibleBeeEven, plantPopulationsGroup3);

        System.out.println("\n=== Ende der Simulation ===");

    }

    /**
     * Überprüft das korrekte Vorgehen für einen Simulationslauf mit jährlichen Zwischenergebnissen und für ein Jahr täglichen Zwischenergebnissen.
     *
     * @pre i im Bereich [0,2]
     * @pre possibleEvents, possibleBeeEven != null
     * BAD: (Prozedural) Diese Methode und runSummary
     * enthalten sehr viel duplizierten Code. Der gesamte Block von
     * Random random = ... bis Simulation sim = ... ist fast identisch.
     * In guter prozeduraler Programmierung würde man diesen Block in eine
     * eigene Helfer-Prozedur auslagern, zB
     * Simulation createSimulation(int seed, int groupIndex, List<IPopulationEvent> events, ...)
     * und diese von beiden Methoden aufrufen, um die Codeduplizierung
     * (Copy-Paste-Fehlerquelle) zu vermeiden.
     */
    private static void runDebugSimulation(int i, List<IPopulationEvent> possibleEvents, IBeeEvent possibleBeeEven, List<IPlantPopulation> plantPopulations) {
        System.out.println("\n\n\n----------------------------------------------");
        System.out.println("------ Debugging Ausgabe ------");
        System.out.println("----------------------------------------------");

        System.out.println("\n----- Jährliches Zwischenergebnis -----");
        Random yearlyRandom = new Random(SEEDS[0]);
        GaussianDistribution yearlyDist = new GaussianDistribution(yearlyRandom);


        List<IPlantPopulation> debuggingPlantsGroup;
        if (i == 0) {
            debuggingPlantsGroup = createPlantsGroup1(yearlyRandom);
        } else if (i == 1) {
            debuggingPlantsGroup = createPlantsGroup2(yearlyRandom);
        } else {
            debuggingPlantsGroup = createPlantsGroup3(yearlyRandom);
        }

        List<NestingSite> yearlyNests = createSites(yearlyRandom);

        IWeather yearlyWeather;
        if (USE_CSV_WEATHER) {
            yearlyWeather = new CsvWeather(CSV_WEATHER_PATH, yearlyDist);
        } else {
            yearlyWeather = new RandomWeather(yearlyDist);
        }

        Simulation yearlySimulation = new Simulation(YEARS, yearlyNests, debuggingPlantsGroup, yearlyWeather, yearlyDist, FIELD_MAX_X, FIELD_MAX_Y, possibleEvents, possibleBeeEven);
        yearlySimulation.run(false, true);
        yearlySimulation.printDetailedResults();

        System.out.println("\n----- tägliches Zwischenergebnis (für ein Jahr) -----\n");
        Random dailyRandom = new Random(SEEDS[0]);
        GaussianDistribution dailyDist = new GaussianDistribution(dailyRandom); // NEU

        List<NestingSite> dailyNests = createSites(dailyRandom);

        IWeather dailyWeather;
        if (USE_CSV_WEATHER) {
            dailyWeather = new CsvWeather(CSV_WEATHER_PATH, yearlyDist);
        } else {
            dailyWeather = new RandomWeather(yearlyDist);
        }
        Simulation dailySimulation = new Simulation(1, dailyNests, debuggingPlantsGroup, dailyWeather, dailyDist, FIELD_MAX_X, FIELD_MAX_Y, possibleEvents, possibleBeeEven);


        dailySimulation.run(true, false);
        dailySimulation.printDetailedResults();


        // Ergebnisse der funktionalen Statistik-Methoden
        System.out.println("// Simulation Gruppe " + i + " //");

        double totalFoodSupply = SimulationAnalytics.calculateTotalFoodSupply(plantPopulations);

        for (int day = 0; day < 240; day++) {
            for (IPlantPopulation p : plantPopulations) {
               p.updateDaily(dailyWeather, START_BEE_Population, totalFoodSupply);
            }
        }

        double averageVigor = SimulationAnalytics.calculateAverageVigor(plantPopulations);
        double averageSeedQuality = SimulationAnalytics.avgSeedQuality(plantPopulations);
        List<IPlantPopulation> sufficientSpeciesForBees = SimulationAnalytics.findSufficientSpeciesForBees(plantPopulations, START_BEE_Population);
        Map<String, Double> averageSeedQualityPerSpecies = SimulationAnalytics.getAverageSeedQualityPerSpecies(plantPopulations);
        List<IPlantPopulation> plantsUnderStress = SimulationAnalytics.findPlantsUnderStress(plantPopulations, 0.5);
        List<IPlantPopulation> topSeedQualitySpecies = SimulationAnalytics.getTopSeedQualitySpecies(plantPopulations, 4);
        List<IPlantPopulation> bloomingSpecies = SimulationAnalytics.getBloomingSpecies(plantPopulations);

        System.out.println("Durchschnittliche Wuchskraft: " + averageVigor);
        System.out.println("Durchschnittliche Samenqualität: " + averageSeedQuality);
        System.out.println("Durchschnittliche Samenqualität pro Pflanzenspezies : " + averageSeedQualityPerSpecies);
        System.out.println("Pflanzen unter Stress : " + plantsUnderStress);
        System.out.println("Top Pflanzen mit der höchsten Samenqualität: " + topSeedQualitySpecies);
        System.out.println("Blühende Pflanzen: " + bloomingSpecies);
        System.out.println("Gesamte Nahrung: " + totalFoodSupply);
        System.out.println("Pflanzen mit ausreichend Nahrung für Bienen: " + sufficientSpeciesForBees);

        System.out.println("// Ender der Simulation Gruppe " + i + " //");
    }

    /**
     * Erstellt die Pflanzen für Gruppe 1.
     * STYLE: Prozedural. Diese 'static'-Methode ist eine reine "Helfer"-Prozedur.
     * Ihr einziger Zweck ist es, eine komplexe Datenstruktur (List<Plantpopulation>)
     * zu erstellen und zurückzugeben. Sie ist zustandslos und operiert nur
     * auf ihren Eingaben, um ein Ergebnis zu produzieren.
     *
     * @return Liste der Pflanzen in Gruppe 1.
     * @pre random != null
     * @post Liste mit gültigen IPlantPopulation-Objekten.
     * GOOD: (Prozedural) Diese Methode ist ein gutes Beispiel für eine
     * "reine" prozedurale Hilfsfunktion. Sie hat eine klare Eingabe (random),
     * eine klare Ausgabe (List<IPlantPopulation>) und modifiziert
     * keinen globalen Zustand. Sie ist leicht zu testen und wiederzuverwenden.
     *
     */
    public static List<IPlantPopulation> createPlantsGroup1(Random random) {
        List<IPlantPopulation> plants = new ArrayList<>();
        try {
            GaussianDistribution plantDist = new GaussianDistribution(random);

            // 15 Pflanzen
            plants.add(new Plantpopulation(new Plantspecies("Rose", 1.3, 2.2, 0.05, 0.99, 1.0 / 200.0, 1.0 / 1000.0, 0.05, 10, 50, 10), 100, plantDist, new Coordinates(10, 40)));
            plants.add(new Plantpopulation(new Plantspecies("Tulpe", 2.5, 3.0, 0.05, 0.97,1.0 / 250.0, 1.0 / 1000.0, 0.02, 8, 25, 3), 100, plantDist, new Coordinates(30, 80)));
            plants.add(new Plantpopulation(new Plantspecies("Narzisse", 2.5, 4.3, 0.05, 0.95, 1.0 / 300.0, 1.0 / 1300.0, 0.02, 7, 25, 3), 100, plantDist, new Coordinates(50, 120)));
            plants.add(new Plantpopulation(new Plantspecies("Sonnenblume", 2.9, 5.3, 0.05, 0.97, 1.0 / 350.0, 1.0 / 1500.0, 0.02, 18, 50, 6), 100, plantDist, new Coordinates(70, 30)));
            plants.add(new Plantpopulation(new Plantspecies("Lilie", 1.8, 3.7, 0.05, 0.95, 1.0 / 400.0, 1.0 / 1100.0, 0.04, 12, 35, 4), 100, plantDist, new Coordinates(90, 200)));
            plants.add(new Plantpopulation(new Plantspecies("Nelke", 1.3, 4.0, 0.05, 0.96, 1.0 / 220.0, 1.0 / 750.0, 0.05, 12, 30, 3), 100, plantDist, new Coordinates(110, 129)));
            plants.add(new Plantpopulation(new Plantspecies("Veilchen", 1.4, 3.4, 0.05, 0.95, 1.0 / 280.0, 1.0 / 850.0, 0.04, 8, 20, 3), 100, plantDist, new Coordinates(130, 283)));
            plants.add(new Plantpopulation(new Plantspecies("Geranie", 1.3, 2.8, 0.05, 0.99, 1.0 / 300.0, 1.0 / 900.0, 0.05, 12, 25, 4), 100, plantDist, new Coordinates(150, 842)));
            plants.add(new Plantpopulation(new Plantspecies("Ringelblume", 1.7, 3.5, 0.05, 0.99, 1.0 / 330.0, 1.0 / 1000.0, 0.02, 12, 30, 4), 100, plantDist, new Coordinates(170, 463)));
            plants.add(new Plantpopulation(new Plantspecies("Mohn", 4.5, 6.9, 0.02, 0.98, 1.0 / 350.0, 1.0 / 1000.0, 0.03, 15, 40, 5), 100, plantDist, new Coordinates(190, 127)));
            plants.add(new Plantpopulation(new Plantspecies("Sonnenhut", 1.4, 3.6, 0.05, 0.95, 1.0 / 280.0, 1.0 / 850.0, 0.03, 14, 25, 5), 100, plantDist, new Coordinates(210, 468)));
            plants.add(new Plantpopulation(new Plantspecies("Schleierkraut", 1.6, 3.7, 0.03, 0.97, 1.0 / 250.0, 1.0 / 1000.0, 0.03, 10, 30, 4), 100, plantDist, new Coordinates(230, 473)));
            plants.add(new Plantpopulation(new Plantspecies("Mädchenauge", 1.6, 4.8, 0.03, 0.98, 1.0 / 500.0, 1.0 / 1000.0, 0.03, 10, 30, 4), 100, plantDist, new Coordinates(250, 983)));
            plants.add(new Plantpopulation(new Plantspecies("Indianernessel", 2.9, 6.9, 0.01, 0.95, 1.0 / 400.0, 1.0 / 1100.0, 0.01, 14, 35, 5), 100, plantDist, new Coordinates(270, 20)));
            plants.add(new Plantpopulation(new Plantspecies("Duftwicke", 8.9, 13.8, 0.045, 0.94,1.0 / 250.0, 1.0 / 1100.0, 0.07, 14, 35, 5), 100, plantDist, new Coordinates(290, 251)));
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler beim Erstellen der Pflanzengruppe 1: " + e.getMessage());
        }
        return plants;
    }

    /**
     * Erstellt die Pflanzen für Gruppe 2.
     *
     * @return Liste der Pflanzen in Gruppe 2.
     *
     * @pre random != null
     * @post Liste mit gültigen IPlantPopulation-Objekten.
     * BAD: (Prozedural) Diese Methode (`createPlantsGroup2`) und die
     * Methoden `createPlantsGroup1` und `createPlantsGroup3` sind
     * fast identisch (Copy-Paste-Programmierung). Das ist ein klassischer
     * prozeduraler Designfehler, der die Wartbarkeit massiv erschwert.
     * Wenn sich die Signatur von `Plantpopulation` ändert, muss die
     * Änderung an drei Stellen nachgezogen werden. Besser wäre eine
     * datengesteuerte Prozedur, die eine Konfiguration (z.B. ein Array
     * von Pflanzen-Spezifikationen) entgegennimmt und daraus die Liste erstellt.
     *
     */
    public static List<IPlantPopulation> createPlantsGroup2(Random random) {
        List<IPlantPopulation> plants = new ArrayList<>();
        try {
            GaussianDistribution plantDist = new GaussianDistribution(random);

            //17 Pflanzen
            plants.add(new Plantpopulation(new Plantspecies("Krokus", 3.8, 6.25, 0.04, 0.88, 1.0 / 240.0, 1.0 / 700.0, 0.03, 5, 20, 2), 100, plantDist, new Coordinates(30, 30)));
            plants.add(new Plantpopulation(new Plantspecies("Margerite", 5.9, 8.3, 0.02, 0.86, 1.0 / 300.0, 1.0 / 1200.0, 0.025, 10, 30, 4), 100, plantDist, new Coordinates(50, 60)));
            plants.add(new Plantpopulation(new Plantspecies("Begonie", 4.8, 14.3, 0.04, 0.97, 1.0 / 300.0, 1.0 / 1100.0, 0.05, 16, 40, 5), 100, plantDist, new Coordinates(94, 90)));
            plants.add(new Plantpopulation(new Plantspecies("Lupine", 2.55, 4.25, 0.015, 0.94, 1.0 / 300.0, 1.0 / 1300.0, 0.05, 15, 35, 5), 100, plantDist, new Coordinates(110, 120)));
            plants.add(new Plantpopulation(new Plantspecies("Zinnie", 19.85, 31.2, 0.05, 0.99, 1.0 / 300.0, 1.0 / 900.0, 0.04, 14, 25, 6), 100, plantDist, new Coordinates(130, 150)));
            plants.add(new Plantpopulation(new Plantspecies("Petunie", 3.5, 5.1, 0.03, 0.87, 1.0 / 300.0, 1.0 / 800.0, 0.045, 14, 30, 6), 100, plantDist, new Coordinates(149, 180)));
            plants.add(new Plantpopulation(new Plantspecies("Glockenheide", 5.25, 7.15, 0.025, 0.87, 1.0 / 350.0, 1.0 / 1500.0, 0.045, 15, 30, 5), 100, plantDist, new Coordinates(189, 249)));
            plants.add(new Plantpopulation(new Plantspecies("Astern", 5.9, 7.2, 0.02, 0.96,1.0 / 350.0, 1.0 / 1100.0, 0.03, 12, 35, 4), 100, plantDist, new Coordinates(207, 30)));
            plants.add(new Plantpopulation(new Plantspecies("Rittersporn", 2.1, 2.8, 0.01, 0.93, 1.0 / 250.0, 1.0 / 800.0, 0.045, 15, 35, 5), 100, plantDist, new Coordinates(250, 400)));
            plants.add(new Plantpopulation(new Plantspecies("Passionsblume", 5.9, 10.3, 0.045, 0.85, 1.0 / 280.0, 1.0 / 900.0, 0.045, 16, 45, 6), 100, plantDist, new Coordinates(280, 200)));
            plants.add(new Plantpopulation(new Plantspecies("Schneeglöckchen", 2.55, 4.05, 0.04, 0.97,1.0 / 350.0, 1.0 / 750.0, 0.045, 4, 15, 1), 100, plantDist, new Coordinates(348, 500)));
            plants.add(new Plantpopulation(new Plantspecies("Anemone", 2.85, 5.2, 0.03, 0.96,1.0 / 220.0, 1.0 / 1100.0, 0.045, 6, 25, 2), 100, plantDist, new Coordinates(398, 100)));
            plants.add(new Plantpopulation(new Plantspecies("Kalla", 13.95, 21.15, 0.035, 0.95,1.0 / 280.0, 1.0 / 1100.0, 0.045, 16, 40, 5), 100, plantDist, new Coordinates(432, 20)));
            plants.add(new Plantpopulation(new Plantspecies("Wicken", 5.9, 12.3, 0.05, 0.94,1.0 / 300.0, 1.0 / 1200.0, 0.045, 14, 25, 4), 100, plantDist, new Coordinates(489, 700)));
            plants.add(new Plantpopulation(new Plantspecies("Spornblume", 2.1, 3.75, 0.055, 0.99,1.0 / 330.0, 1.0 / 1000.0, 0.03, 14, 30, 5), 100, plantDist, new Coordinates(578, 599)));
            plants.add(new Plantpopulation(new Plantspecies("Steppenkerze", 2.9, 4.7, 0.025, 0.96,1.0 / 200.0, 1.0 / 800.0, 0.045, 15, 35, 5), 100, plantDist, new Coordinates(692, 302)));
            plants.add(new Plantpopulation(new Plantspecies("Buschwindröschen", 1.6, 3.3, 0.02, 0.97,1.0 / 200.0, 1.0 / 1200.0, 0.045, 5, 25, 2), 100, plantDist, new Coordinates(746, 742)));
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler beim Erstellen der Pflanzengruppe 2: " + e.getMessage());
        }
        return plants;
    }

    /**
     * Erstellt die Pflanzen für Gruppe 3.
     *
     * @return Liste der Pflanzen in Gruppe 3.
     *
     * @pre random != null
     * @post Liste mit gültigen IPlantPopulation-Objekten.
     */
    public static List<IPlantPopulation> createPlantsGroup3(Random random) {
        List<IPlantPopulation> plants = new ArrayList<>();
        try {
            GaussianDistribution plantDist = new GaussianDistribution(random);

            // 20 Pflanzen
            plants.add(new Plantpopulation(new Plantspecies("Rose", 1.8, 2.55, 0.05, 0.98,1.0 / 250.0, 1.0 / 1250.0, 0.05, 15, 40, 5), 100, plantDist, new Coordinates(49, 43)));
            plants.add(new Plantpopulation(new Plantspecies("Tulpe", 2.8, 3.2, 0.042, 0.95,1.0 / 200.0, 1.0 / 1000.0, 0.05, 8, 25, 3), 100, plantDist, new Coordinates(334, 858)));
            plants.add(new Plantpopulation(new Plantspecies("Narzisse", 3.5, 5.1, 0.03, 0.96,1.0 / 220.0, 1.0 / 1100.0, 0.05, 7, 25, 3), 100, plantDist, new Coordinates(853, 50)));
            plants.add(new Plantpopulation(new Plantspecies("Sonnenblume", 5.5, 7.35, 0.034, 0.9,1.0 / 350.0, 1.0 / 1500.0, 0.02, 18, 50, 6), 100, plantDist, new Coordinates(339, 384)));
            plants.add(new Plantpopulation(new Plantspecies("Wicken", 5.9, 12.3, 0.05, 0.94,1.0 / 300.0, 1.0 / 1200.0, 0.045, 14, 25, 4), 100, plantDist, new Coordinates(20, 348)));
            plants.add(new Plantpopulation(new Plantspecies("Lilie", 1.7, 4.3, 0.05, 0.96,1.0 / 500.0, 1.0 / 930.0, 0.04, 12, 35, 4), 100, plantDist, new Coordinates(498, 95)));
            plants.add(new Plantpopulation(new Plantspecies("Veilchen", 2.4, 3.2, 0.05, 0.93,1.0 / 200.0, 1.0 / 900.0, 0.04, 8, 20, 3), 100, plantDist, new Coordinates(234, 38)));
            plants.add(new Plantpopulation(new Plantspecies("Geranie", 3.1, 3.3, 0.065, 0.92,1.0 / 330.0, 1.0 / 1000.0, 0.05, 12, 25, 4), 100, plantDist, new Coordinates(953, 854)));
            plants.add(new Plantpopulation(new Plantspecies("Ringelblume", 2.5, 4.4, 0.035, 0.94,1.0 / 300.0, 1.0 / 1300.0, 0.035, 12, 30, 4), 100, plantDist, new Coordinates(384, 328)));
            plants.add(new Plantpopulation(new Plantspecies("Mohn", 3.9, 8.1, 0.04, 0.96,1.0 / 500.0, 1.0 / 1200.0, 0.03, 15, 40, 5), 100, plantDist, new Coordinates(384, 383)));
            plants.add(new Plantpopulation(new Plantspecies("Sonnenhut", 3.2, 5.1, 0.05, 0.85, 1.0 / 320.0, 1.0 / 790.0, 0.03, 14, 25, 5), 100, plantDist, new Coordinates(238, 95)));
            plants.add(new Plantpopulation(new Plantspecies("Schleierkraut", 2.3, 3.9, 0.02, 0.99,1.0 / 242.0, 1.0 / 1500.0, 0.03, 10, 30, 4), 100, plantDist, new Coordinates(843, 854)));
            plants.add(new Plantpopulation(new Plantspecies("Mädchenauge", 3.4, 6.6, 0.05, 0.96,1.0 / 600.0, 1.0 / 1100.0, 0.03, 10, 30, 4), 100, plantDist, new Coordinates(374, 482)));
            plants.add(new Plantpopulation(new Plantspecies("Indianernessel", 1.2, 3.1, 0.025, 0.97,1.0 / 200.0, 1.0 / 1200.0, 0.025, 14, 35, 5), 100, plantDist, new Coordinates(873, 384)));
            plants.add(new Plantpopulation(new Plantspecies("Duftwicke", 8.4, 13.9, 0.05, 0.92,1.0 / 250.0, 1.0 / 1100.0, 0.07, 14, 35, 5), 100, plantDist, new Coordinates(743, 843)));
            plants.add(new Plantpopulation(new Plantspecies("Salbei", 1.6, 3.9, 0.05, 0.97,1.0 / 250.0, 1.0 / 1000.0, 0.04, 12, 30, 4), 100, plantDist, new Coordinates(374, 834)));
            plants.add(new Plantpopulation(new Plantspecies("Aster", 4.2, 9.2, 0.001, 0.99,1.0 / 350.0, 1.0 / 950.0, 0.03, 12, 35, 4), 100, plantDist, new Coordinates(103, 217)));
            plants.add(new Plantpopulation(new Plantspecies("Krokus", 3.8, 5.2, 0.02, 0.8,1.0 / 560.0, 1.0 / 1100.0, 0.03, 5, 20, 2), 100, plantDist, new Coordinates(732, 24)));
            plants.add(new Plantpopulation(new Plantspecies("Löwenzahn", 4.4, 13.8, 0.032, 0.95,1.0 / 350.0, 1.0 / 1100.0, 0.045, 10, 25, 3), 100, plantDist, new Coordinates(34, 385)));
            plants.add(new Plantpopulation(new Plantspecies("Lavendel", 4.2, 6.5, 0.002, 0.9,1.0 / 450.0, 1.0 / 2000.0, 0.045, 14, 35, 5), 100, plantDist, new Coordinates(384, 238)));
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler beim Erstellen der Pflanzengruppe 3: " + e.getMessage());
        }
        return plants;
    }

    /**
     * Erstellt eine Liste an Nistplätzen mit einer Bienenpopulation.
     * STYLE: Prozedural. Kontrollstrukturen basierend auf strukturierter Programmierung.
     * Die Methode ist eine klare Abfolge von Befehlen. Die Datenstruktur wird hier explizit manipuliert.
     * Der Abstraktionsgrad ist niedrig.
     * @param random eine Zufallszahl
     * @return ein Liste von Nistplätzen
     *
     * @pre random != null
     * @post gültige Liste mit NestingSite-Objekten.
     *
     */
    public static List<NestingSite> createSites(Random random) {
        List<NestingSite> sites = new ArrayList<>();
        try {
            // Nistplatz 1
            IBeePopulation bees1 = new WildbeePopulation(START_BEE_Population, new GaussianDistribution(random));
            sites.add(new NestingSite(bees1,
                    4000,
                    new Coordinates(50, 50)));
            // Nistplatz 2
            IBeePopulation bees2 = new HoneyBeePopulation(START_BEE_Population, new GaussianDistribution(random));
            sites.add(new NestingSite(bees2,
                    4000,
                    new Coordinates(150, 120)));
            // Nistplatz 3
            IBeePopulation bees3 = new WildbeePopulation(START_BEE_Population, new GaussianDistribution(random));
            sites.add(new NestingSite(bees3,
                    6000,
                    new Coordinates(100, 20)));
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler beim Erstellen der Nistplätze: " + e.getMessage());
        }
        return sites;
    }
}
