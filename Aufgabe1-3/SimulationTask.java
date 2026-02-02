import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Diese Klasse führt einen einzelnen Simulationslauf aus.
 */
public class SimulationTask implements Callable<SimulationResult> {
    private final int seed;
    private final int groupIndex;
    private final int runIndex;
    private final List<IPopulationEvent> possibleEvents;
    private final IBeeEvent possibleBeeEvent;
    private final boolean useCsvWeather;
    private final String csvWeatherPath;
    private final double fieldMaxX;
    private final double fieldMaxY;

    /**
     * Initialisert ein neues SimulationTask-Objekt.
     * @param seed verwendeter Seed
     * @param groupIndex Gruppenindex
     * @param j Laufindex
     * @param possibleEvents mögliche Pflanzen-Events
     * @param possibleBeeEvent mögliche Bienen-Events
     * @param useCsvWeather Angabe, ob CSV-Wetter verwendet werden soll
     * @param csvWeatherPath Pfad zu CSV-Datei
     * @param fieldMaxX Länge des Feldes
     * @param fieldMaxY Höhe des Feldes
     *
     * @pre seed, j >= 0
     * @pre groupIndex im Bereich [0, 2]
     * @pre fieldMaxX, fieldMaxY
     * @pre csvWeatherPath stellt einen gültigen Pfad zu einer CSV-Datei dar.
     * @pre possibleEvents und possibleBeeEvent sind nicht null
     */
    public SimulationTask(int seed, int groupIndex, int j,
                          List<IPopulationEvent> possibleEvents, IBeeEvent possibleBeeEvent,
                          boolean useCsvWeather,
                          String csvWeatherPath,
                          double fieldMaxX, double fieldMaxY) {
        this.seed = seed;
        this.groupIndex = groupIndex;
        this.runIndex = j;
        this.possibleEvents = possibleEvents;
        this. possibleBeeEvent = possibleBeeEvent;
        this.useCsvWeather = useCsvWeather;
        this.csvWeatherPath = csvWeatherPath;
        this.fieldMaxX = fieldMaxX;
        this.fieldMaxY = fieldMaxY;
    }

    /**
     * Diese Methode führt einen einzelnen, vollständigen Simulationslauf durch.
     * Es werden die Distribution, die Pflanzengruppen und das Wettermodell initialisiert
     * und der Simulation übergeben.
     * @return Ein SimulationResult-Objekt, das die Erfolgsdaten oder eine Fehlermeldung enthält.
     * @throws Exception Intern werden alle Exceptions abgefangen und
     * in ein SimulationResult.failure-Objekt umgewandelt.
     *
     */
    @Override
    public SimulationResult call() throws Exception {
        try {
            Random random = new Random(this.seed);
            GaussianDistribution distribution = new GaussianDistribution(random);

            List<NestingSite> nests = Test.createSites(random);

            List<IPlantPopulation> currentGroup;
            if(groupIndex == 0){
                currentGroup = Test.createPlantsGroup1(random);
            } else if(groupIndex == 1){
                currentGroup = Test.createPlantsGroup2(random);
            } else {
                currentGroup = Test.createPlantsGroup3(random);
            }

            IWeather weather;
            if(useCsvWeather){
                try{
                    weather = new CsvWeather(csvWeatherPath, distribution);
                } catch(RuntimeException e){
                    System.err.printf("Fehler beim Einlesen der CSV-Datei (Lauf %d, Gruppe %d). Zufallswetter wird verwendet. Fehler: %s%n", this.runIndex, this.groupIndex, e.getMessage());
                    weather = new RandomWeather(distribution);
                }

            } else {
                weather = new RandomWeather(distribution);
            }
            Simulation sim = new Simulation(
                    Test.YEARS,
                    nests,
                    currentGroup,
                    weather,
                    distribution,
                    fieldMaxX,
                    fieldMaxY,
                    possibleEvents,
                    possibleBeeEvent);

            sim.run(false, false);
            String results = generateResultString(nests, currentGroup);

            return SimulationResult.success(this.runIndex, this.groupIndex, this.seed, results);

        } catch (Exception e) {
            System.err.printf("Fehler beim Simulationslauf (Lauf %d, Gruppe %d, Seed %d). Fehler: %s%n", this.runIndex, this.groupIndex, this.seed, e.getMessage());
            return SimulationResult.failure(this.runIndex, this.groupIndex, this.seed, e.getMessage());
        }
    }

    /**
     * Diese Methode erstellt einen formatierten String mit den Endergebnissen einer Simulation.
     * @param nests Liste der Nistplätze
     * @param plants Liste der Pflanzenpopulationen
     * @return einen formatierten String
     *
     * @pre nests und plants sind nicht null
     * @post gibt eine formatierte Zusammenfassung aller Nistplätze und Pflanzenpopulationen in einem String zurück
     */
    private String generateResultString(List<NestingSite> nests, List<IPlantPopulation> plants) {

        StringBuilder sb = new StringBuilder();

        double totalBees = 0;
        for (NestingSite nest : nests) {
            sb.append(String.format("  Nistplatz (%.1f, %.1f): Bienen: %.2f / %.0f (%s)%n",
                    nest.getCoordinates().x(),
                    nest.getCoordinates().y(),
                    nest.getBeePopulation().getPopulation(),
                    nest.getCapacity(),
                    nest.getBeePopulation().getName()));
            totalBees += nest.getBeePopulation().getPopulation();
        }
        sb.append(String.format("GESAMTE Bestäuberpopulation:  %.2f%n",  totalBees));

        sb.append("Blütenpflanzenpopulation: \n");
        for (IPlantPopulation p : plants) {
            sb.append("  ").append(p.toString()).append(System.lineSeparator());
        }

        return sb.toString();
    }
}
