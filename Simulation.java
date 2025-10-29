import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient als Simulation des Bienenwachstums über
 * eine gewisse Anzahl an übergebenen Jahren. Logs können über Parameter aktiviert werden,
 * um den Wachstum täglich oder/und jährlich zu verfolgen
 *
 */
public class Simulation {
    private final int totalYears;
    private final List<IPlantPopulation> plantPopulations;
    private final IWeather weather;
    private final List<NestingSite> nestingSites;

    private final double maxX;
    private final double maxY;

    // Eine Liste aller Events, die in dieser Simulation auftreten könnten
    private final List<IPopulationEvent> possiblePlantEvents;
    // Eine Variable mit der einzigen Bee Event was es gibt
    private final IBeeEvent possibleBeeEvent;

    // Ein separater Random-Generator nur für das Auslösen von Events
    private final IDistribution eventRandom;

    // Die tägliche Wahrscheinlichkeit, dass irgendein Event ausgelöst wird.
    private static final double DAILY_EVENT_PROBABILITY = 0.01;

    // Für detaillierte Ausgabe
    private final StringBuilder yearlyLog = new StringBuilder();
    private final StringBuilder dailyLog = new StringBuilder();

    /**
     *  STYLE: Objektorientiert (Kapselung). Der Konstruktor nimmt
     *  Abhängigkeiten entgegen und speichert sie in 'private' Feldern.
     *  Von außen ist der Zustand der Simulation (z.B. die Liste der
     *  Populationen) nicht mehr direkt manipulierbar, sondern nur
     *  über die Methoden des 'Simulation'-Objekts (z.B. 'run()')
     *
     * @param totalYears Anzahl an Jahre zum Simulieren
     * @param plantPopulations eine Liste von Plantspecies um eine Population zu erstellen
     */
    public Simulation(int totalYears,
                      List<NestingSite> nestingSites,
                      List<IPlantPopulation> plantPopulations,
                      IWeather weather,
                      GaussianDistribution gaussianDistribution,
                      double maxX,
                      double maxY,
                      List<IPopulationEvent> possibleEvents,
                      IBeeEvent possibleBeeEvent) {
        this.totalYears = totalYears;
        this.nestingSites = nestingSites;
        this.plantPopulations = plantPopulations;
        this.maxX = maxX;
        this.maxY = maxY;
        this.weather = weather;
        this.eventRandom = gaussianDistribution;
        this.possiblePlantEvents = (possibleEvents != null) ? possibleEvents : new ArrayList<>();
        this.possibleBeeEvent = possibleBeeEvent;

        //testet, ob die Nistplätze und die Pflanzen innerhalb des definierten Felds liegen
        testCoordinates(nestingSites, plantPopulations);
    }

    /**
     * Startet die Simulation für totalYears
     *
     * @param dailyLogging  Log für jeden Tag der Simuliert wird
     * @param yearlyLogging Log für jedes Jahr was simuliert wird
     */
    public void run(boolean dailyLogging, boolean yearlyLogging) {
        for (int year = 1; year <= totalYears; year++) {
            runVegetationPeriod(dailyLogging, year);
            runRestPhase();
            if (yearlyLogging) logYearlyState(year);
        }
    }

    /**
     * Simuliert die 240 Tage der Vegatationsperiode
     * Zuerst wird das Nahrungsangebot ausgerechnet
     * Dann die Bienenpopulation pro Nistplatz
     * Dann wird überprüft ob ein Event passieren könnte, bei dem die Population veringert wird
     * Dann Mach ein tägliches Update der Pflanzenpopulation
     * @param dailyLogging Log für jeden Tag der Simuliert wird
     * @param currentYear Int vom aktuellen Jahr für Log
     */
    private void runVegetationPeriod(boolean dailyLogging, int currentYear) {
        for(IPlantPopulation p : plantPopulations){
            p.resetForNewVegetationPeriod();
        }
        weather.initializeForVegetationPeriod();

        int vegetationDays = 240; // März bis Oktober
        for (int day = 1; day <= vegetationDays; day++) {
            weather.simulateDailyChange(day);


            //Nahrungsangebot berechnen
            double totalFoodSupply = 0.0;
            for(IPlantPopulation p : plantPopulations){
                totalFoodSupply += p.getCurrentFoodSupply();
            }

            //Bienenpopulation pro Nistplatz
            double totalBeePopulation = 0.0;
            for (NestingSite nest : nestingSites) {
                IBeePopulation bees = nest.getBeePopulation();

                //Erreichbares Futter für dieses Nest berechnen
                double foodInRange = 0.0;
                for (IPlantPopulation plant : plantPopulations) {
                    double dist = nest.getCoordinates().distanceTo(plant.getCoordinates());

                    if (dist <= bees.getMaxRange()) {
                        // Effizienz: abhängig von der Entfernung zum Nistplatz
                        double efficiency = 1.0 - (dist / bees.getMaxRange());
                        foodInRange += plant.getCurrentFoodSupply() * efficiency;
                    }
                }

                //Population updaten
                bees.updateDaily(foodInRange);
                //Nistplatz-Kapazität erzwingen
                if (bees.getPopulation() > nest.getCapacity()) {
                    bees.setPopulation(nest.getCapacity());
                }

                //Gesamtpopulation aufsummieren
                totalBeePopulation += bees.getPopulation();
            }

            // Prüfe ob ein Event ausgelöst wird
            if (!possiblePlantEvents.isEmpty() && eventRandom.nextDouble(0, 1) <= DAILY_EVENT_PROBABILITY) {
                // Wähle ein zufälliges Event aus der Liste, wenn die random Zahl = die Länge der Liste,
                // dann wende das einzige Event von Bienen
                int index = eventRandom.nextInt(0, possiblePlantEvents.size());
                String eventName;
                // Wende das Event der Bienen an
                if(index == possiblePlantEvents.size()){
                    totalBeePopulation = possibleBeeEvent.apply(totalBeePopulation, weather);
                    eventName = possibleBeeEvent.getName();
                }else{
                    // Wende ein Event der Pflanzen an
                    IPopulationEvent triggeredEvent = possiblePlantEvents.get(index);

                    triggeredEvent.apply(plantPopulations, weather);
                    eventName = triggeredEvent.getName();
                }

                // Logge das Event (falls dailyLogging aktiv ist)
                if (dailyLogging) {
                    dailyLog.append(String.format("  *** EVENT AM TAG %d: %s wurde ausgelöst! ***%n", day, eventName));
                }
            }

            for (IPlantPopulation p : plantPopulations) {
                p.updateDaily(weather, totalBeePopulation, totalFoodSupply);
            }

            if (dailyLogging) {
                logDailyState(currentYear, day);
            }
        }
    }

    /**
     * Erfasst den Zustand eines Tags während der Vegetationsperiode.
     * @param currentYear aktuelle Jahr
     * @param day aktuelle Tag während der Vegetationsperiode
     */
    private void logDailyState(int currentYear, int day) {
        dailyLog.append(String.format("Jahr: %d, Tag: %d, %s%n",
                currentYear,
                day,
                weather.toString()));
        double totalBees = 0;
        for (NestingSite nest : nestingSites) {dailyLog.append(String.format("  Nistplatz (%.1f, %.1f): Bienen: %.2f / %.0f (%s)%n",
                nest.getCoordinates().x(),
                nest.getCoordinates().y(),
                nest.getBeePopulation().getPopulation(),
                nest.getCapacity(),
                nest.getBeePopulation().getName()));
            totalBees += nest.getBeePopulation().getPopulation();
        }
        dailyLog.append(String.format("  GESAMTE Bienenpopulation: %.2f%n", totalBees));
        for (IPlantPopulation p : plantPopulations) {
            dailyLog.append(p.toString()).append(System.lineSeparator());
        }
    }

    /**
     * Erfasst den Zustand am Ende des Jahres nach der Vegetationsperiode und der Ruhephase.
     * @param year das aktuell erfasste Jahr
     */
    private void logYearlyState(int year) {
        yearlyLog.append(String.format("Jahr: %d%n",
                year));
        double totalBees = 0;
        for (NestingSite nest : nestingSites) {
            yearlyLog.append(String.format("  Nistplatz (%.1f, %.1f): Bienen: %.2f / %.0f (%s)%n",
                    nest.getCoordinates().x(),
                    nest.getCoordinates().y(),
                    nest.getBeePopulation().getPopulation(),
                    nest.getCapacity(),
                    nest.getBeePopulation().getName()));
            totalBees += nest.getBeePopulation().getPopulation();
        }
        yearlyLog.append(String.format("  GESAMTE Bienenpopulation: %.2f%n", totalBees));

        for (IPlantPopulation p : plantPopulations) {
            yearlyLog.append(p.toString()).append(System.lineSeparator());
        }

        yearlyLog.append(System.lineSeparator()); // Zeilenumbrüche
    }

    /**
     * Nach den 240 Tagen der Vegetationsperiode beginnt die Ruhephase, deren Bewertung in einem einzigen Schritt erfolgt.
     * - Ruhephase der Wildbienenpopulation:
     *   Während einer Ruhephase wird diese Population mit einer Zufallszahl zwischen 0.1 und 0.3 multipliziert,
     *   um zu simulieren, dass viele Wildbienen den Winter nicht überstehen.
     * <p>
     * - Ruhephase der Blütenpflanzenpopulation:
     *   Hierbei wird die neue Wuchskraft für jede Blütenpflanze bestimmt, die durch eine Multiplikation
     *   von ihrer Samenqualität und einer Zufallszahl berechnet wird, um die Vermehrung zu simulieren.
     * Nach dem Aufruf dieser Methode erhält man alle Startwerte für die nächste Vegetationsperiode im neuen Jahr.
     */
    private void runRestPhase() {
        // Ruhephase für jede Bienenpopulation in jedem Nest
        for (NestingSite nest : nestingSites) {
            nest.getBeePopulation().updateRestPhase();
        }

        // Ruhephase der Blütenpflanzenpopulation
        for (IPlantPopulation p : plantPopulations) {
            p.updateRestPhase();
        }
    }

    /**
     * Gibt die finalen simulierten Werte der Bienenpopulation und der Blütenpflanzenpopulation nach Ablauf aller Jahre aus.
     */
    public void printFinalResults() {
        System.out.println("Die finalen Werte nach " + totalYears + " Jahren betragen: ");

        double totalBees = 0;
        for (NestingSite nest : nestingSites) {
            System.out.printf("  Nistplatz (%.1f, %.1f): Bienen: %.2f / %.0f (%s)%n",
                    nest.getCoordinates().x(),
                    nest.getCoordinates().y(),
                    nest.getBeePopulation().getPopulation(),
                    nest.getCapacity(),
                    nest.getBeePopulation().getName());
            totalBees += nest.getBeePopulation().getPopulation();
        }
        System.out.printf("GESAMTE Bienenpopulation:  %.2f%n",  totalBees);

        System.out.println("Blütenpflanzenpopulation: ");
        for (IPlantPopulation p : plantPopulations) {
            System.out.println(p.toString());
        }
    }

    /**
     * Gibt zuerst die täglichen Logs während der Vegetationsperiode aus.
     * Anschließend werden die jährlichen Logs nach der Vegetationsperiode und Ruhephase ausgegeben.
     */
    public void printDetailedResults() {
        // 1) Tägliche Logs
        System.out.println("// Tägliche Logs //");
        System.out.println(dailyLog);

        // 2) Jährliche Logs
        System.out.println("// Jährliche Logs // ");
        System.out.println(yearlyLog);
    }

    /**
     * Kontrolliert, ob die Nistplätze und die Pflanzen innerhalb des definierten Felds liegen.
     * @param sites Liste der Nistplätze
     * @param plants Liste der Pflanzen
     */
    private void testCoordinates(List<NestingSite> sites, List<IPlantPopulation> plants){
        //Grenzen des Felds
        double minX = 0;
        double minY = 0;
        for(NestingSite nest : sites){
            Coordinates c = nest.getCoordinates();
            if(c.x() < minX || c.x() > maxX || c.y() < minY ||c.y() > maxY){
                throw new IllegalArgumentException("Invalid coordinates for NestingSite");
            }
        }
        for (IPlantPopulation plant : plants){
            Coordinates c = plant.getCoordinates();
            if(c.x() < minX || c.x() > maxX || c.y() < minY ||c.y() > maxY){
                throw new IllegalArgumentException("Pflanze %s liegt außerhalb des Felds".formatted(plant.getSpeciesName()));
            }
        }
    }
}