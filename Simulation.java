import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Diese Klasse dient als Simulation des Wildbienenwachstums über
 * eine gewisse Anzahl an übergebenen Jahren. Logs können über Parameter aktiviert werden,
 * um den Wachstum täglich oder/und jährlich zu verfolgen
 *
 */
public class Simulation {
    private final int totalYears;
    private final BeePopulation beePopulation;
    private final List<Plantpopulation> plantPopulations;
    private final Weather weather;

    // Für detallierte Ausgabe
    private final StringBuilder yearlyLog = new StringBuilder();
    private final StringBuilder dailyLog = new StringBuilder();

    /**
     @param totalYears Anzahl an Jahre zum Simulieren
     @param initialBeePopulation Anfangspopulation von Wildbienen
     @param plantPopulations eine Liste von Plantspecies um eine Population zu erstellen
     */
    public Simulation(int totalYears,
                      double initialBeePopulation,
                      List<Plantspecies> plantPopulations,
                      Random random,
                      double vigor) {
        this.totalYears = totalYears;
        this.beePopulation = new BeePopulation(initialBeePopulation, random);
        this.plantPopulations = new ArrayList<>();
        for(Plantspecies plantspecies : plantPopulations){
            this.plantPopulations.add(new Plantpopulation(plantspecies, vigor, random));
        }
        this.weather = new Weather(random);
    }

    /**
    Startet die Simulation für totalYears
    @param dailyLogging Log für jeden Tag der Simuliert wird
    @param yearlyLogging Log für jedes Jahr was simuliert wird
     */
    public void run(boolean dailyLogging, boolean yearlyLogging){
        for(int year = 1; year <= totalYears; year++){
            runVegetationPeriod(dailyLogging, year);
            runRestPhase();
            if(yearlyLogging) logYearlyState(year);
        }
    }

    /**
    Simuliert die 240 Tage der Vegatationsperiode
    @param dailyLogging Log für jeden Tag der Simuliert wird
    @param currentYear Int vom aktuellen Jahr für Log
     */
    private void runVegetationPeriod(boolean dailyLogging, int currentYear) {
        for(Plantpopulation p : plantPopulations){
            p.resetForNewVegetationPeriod();
        }
        weather.initializeForVegetationPeriod();

        int vegetationDays = 240; // März bis Oktober
        for(int day = 1; day <= vegetationDays; day++){
            weather.simulateDailyChange();

            //Nahrungsangebot ausrechnen summe(yi * bi)
            double totalFoodSupply = 0.0;
            for(Plantpopulation p : plantPopulations){
                totalFoodSupply += p.getCurrentFoodSupply();
            }

            //Population updaten
            beePopulation.updateDaily(totalFoodSupply);
            for(Plantpopulation p : plantPopulations){
                p.updateDaily(weather, beePopulation.getPopulation(), totalFoodSupply);
            }

            if(dailyLogging){
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
        dailyLog.append(String.format("Jahr: %d, Tag: %d, %s, Bienenpopulation: %.2f%n",
                currentYear,
                day,
                weather.toString(),
                beePopulation.getPopulation()));

        for(Plantpopulation p : plantPopulations){
            dailyLog.append(p.toString()).append(System.lineSeparator());
        }
    }

    /**
     * Erfasst den Zustand am Ende des Jahres nach der Vegetationsperiode und der Ruhephase.
     * @param year das aktuell erfasste Jahr
     */
    private void logYearlyState(int year) {
        yearlyLog.append(String.format("Jahr: %d, Bienenpopulation: %.2f%n",
                year,
                beePopulation.getPopulation()));

        for(Plantpopulation p : plantPopulations){
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
     *
     * Nach dem Aufruf dieser Methode erhält man alle Startwerte für die nächste Vegetationsperiode im neuen Jahr.
     */
    private void runRestPhase() {
        // Ruhephase der Wildbienenpopulation
        beePopulation.updateRestPhase();

        // Ruhephase der Blütenpflanzenpopulation
        for(Plantpopulation p : plantPopulations){
            p.updateRestPhase();
        }
    }

    /**
     * Gibt die finalen simulierten Werte der Bienenpopulation und der Blütenpflanzenpopulation nach Ablauf aller Jahre aus.
     */
    public void printFinalResults(){
        System.out.println("Die finalen Werte nach " + totalYears + " Jahren betragen: ");

        System.out.printf("Wildbienenpopulation:  %.2f%n",  beePopulation.getPopulation());

        System.out.println("Blütenpflanzenpopulation: ");
        for(Plantpopulation p : plantPopulations){
            System.out.println(p.toString());
        }
    }

    /**
     * Gibt zuerst die täglichen Logs während der Vegetationsperiode aus.
     * Anschließend werden die jährlichen Logs nach der Vegetationsperiode und Ruhephase ausgegeben.
     */
    public void printDetailedResults(){
        // 1) Tägliche Logs
        System.out.println("// Tägliche Logs //");
        System.out.println(dailyLog.toString());

        // 2) Jährliche Logs
        System.out.println("// Jährliche Logs // ");
        System.out.println(yearlyLog.toString());
    }
}