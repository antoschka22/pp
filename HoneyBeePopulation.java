/**
 * Diese Klasse dient als abstrakte Darstellung der Honigbienenpopulation, bei dem die Population
 * täglich upgedated werden soll und die Ruhephase simulieren soll. Die Honigbiene fungiert als
 * Konkurrentin zu Wildbienen und als Bestäuber. Im Vergleich zu Wildbienen, sind Honigbienen
 * stabiler, weniger abhängig vom Nahrungsangebot, tragen aber weniger effektiv zur Bestäubung bei.
 */
public class HoneyBeePopulation implements IBeePopulation {

    private double population; // Populationsgröße
    private final static double MAX_RANGE = 700.0; //Flugreichweite dieser Art
    private final IDistribution restPhaseDistribution; // Wahrscheinlichkeitsverteilung


    public HoneyBeePopulation(double population, IDistribution restPhaseDistribution) {
        // Population muss >= 0 sein
        if(population < 0){
            throw new IllegalArgumentException(
                    "Population ist ungültig. Sollte: population >= 0. Eingegebener Wert: Population=" + population);
        }

        if(restPhaseDistribution == null){
            throw new IllegalArgumentException(
                    "restPhaseDistribution ist ungültig. Sollte nicht null sein");
        }

        this.population = population;
        this.restPhaseDistribution = restPhaseDistribution;
    }

    /**
     * Passt täglich die Honigbienenpopulation während der Vegetationsperiode an.
     * Honigbienen sind weniger abhängig vom Nahrungsangebot als Wildbienen und
     * somit wird ein stabiles Wachstum und weniger Schwankungen erzielt.
     *
     * @param foodSupply aktuelles Nahrungsangebot an diesem Tag
     */
    @Override
    public void updateDaily(double foodSupply) {
        if(foodSupply >= population){
            population *= 1.01; // Population wird nur um 1 % erhöht → langsameres Wachstum
        } else {
            double changeQuote = (4 * foodSupply / population) - 2; // schwächer abhängig von foodSupply → schwächeres Wachstum
            population *= 1.0 + (changeQuote / 100.0);
        }
    }

    /**
     * Simuliert die Ruhephase der Honigbienenpopulation.
     * Hierbei soll simuliert werden, dass ein Anteil der Honigbienenpopulation den Winter nicht überstehen,
     * aber dennoch überwintern Honigbienen besser als Wildbienen und sind dementsprechend stabiler.
     */
    @Override
    public void updateRestPhase() {
        double survivalFactor = restPhaseDistribution.nextDouble(0.6, 0.7);
        population *= survivalFactor;
    }

    /**
     * Gibt die aktuelle Populationsgröße der Honigbienen zurück.
     * @return Populationsgröße
     */
    @Override
    public double getPopulation() {
        return this.population;
    }

    @Override
    public String getName() {
        return "Honigbienen";
    }

    @Override
    public double getMaxRange() {
        return MAX_RANGE;
    }

    @Override
    public void setPopulation(double capacity) {
        this.population = capacity;
    }
}
