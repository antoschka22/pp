/**
 * Diese Klasse dient als Abstrakte Darstellung der Wildbienepopulation,
 * bei dem die Population täglich upgedated werden soll
 * und die Ruhephase simulierensoll
 */
public class WildbeePopulation implements IBeePopulation {
    private double population; // Population ist das x auf der Angabe
    private final static double MAX_RANGE = 600.0; //Flugreichweite dieser Art
    private final IDistribution restPhaseDistribution; // Wahrscheinlichkeitsverteilung

    public WildbeePopulation(double population, IDistribution restPhaseDistribution){
        // Population muss >= 0 sein
        if(population < 0){
            throw new IllegalArgumentException(
                    "Population ist ungültig. Sollte: population >= 0. Eingegebener Wert: Population=" + population);
        }

        this.population = population;
        this.restPhaseDistribution = restPhaseDistribution;
    }

    /**
     * Während der Vegetationsperiode wird populaiton täglich angepasst:
     * Ist foodSuply >= population -> wird population um 3% erhöht
     * Sonst wird population um ((6*foodSply/x)-3)% erhöht/verringert
     *
     *
     */
    @Override
    public void updateDaily(double foodSupply) {
        if(foodSupply >= population){
            population *= 1.03;
        } else {
            double changeQuote = (6 * foodSupply / population) - 3;
            population *= 1.0 + (changeQuote / 100.0);
        }
    }

    /**
     * Simuliert die Ruhephase wo population mit einer Random Zahl,
     * zwischen 0.1 und 0.3 multipliziert wird
     */
    @Override
    public void updateRestPhase() {
        double survivalFactor = restPhaseDistribution.nextDouble(0.1, 0.3);
        population *= survivalFactor;
    }

    // ---------------------------------- GETTER ----------------------------------------------
    public double getPopulation() {
        return population;
    }

    @Override
    public String getName() {
        return "Wildbienen";
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
