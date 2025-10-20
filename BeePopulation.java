import java.util.Random;

/**
 * Diese Klasse dient als Abstrakte Darstellung der Wildbienepopulation,
 * bei dem die Population täglich upgedated werden soll
 * und die Ruhephase simulierensoll
 */
public class BeePopulation {
    private double population; // Population ist das x auf der Angabe
    private final Random rand; // Random für die Ruhephase

    public BeePopulation(double population, Random random){
        // Population muss >= 0 sein
        if(population < 0){
            throw new IllegalArgumentException(
                    "Population ist ungültig. Sollte: population >= 0. Eingegebener Wert: Population=" + population);
        }

        this.population = population;
        this.rand = random;
    }

    /**
    Während der Vegetationsperiode wird populaiton täglich angepasst:
    Ist foodSuply >= population -> wird population um 3% erhöht
    Sonst wird population um ((6*foodSply/x)-3)% erhöht/verringert
     */
    public void updateDaily(double foodSuply){
        if(foodSuply >= population){
            population *= 1.03;
        } else {
            double changeQuote = (6 * foodSuply / population) - 3;
            population *= 1.0 + (changeQuote / 100.0);
        }
    }

    /**
    Simuliert die Ruhephase wo population mit einer Random Zahl,
    zwischen 0.1 und 0.3 multipliziert wird
     */
    public void updateRestPhase() {
        //Math.nextUp wird verwendet um den Upperbound-Fall 0.3 erreichen zu können
        //Somit wird eine Random Zahl zwischen [0.1-0.3] garantiert
        double survivalFactor = 0.1 + rand.nextDouble() * (Math.nextUp(0.3) - 0.1);
        population *= survivalFactor;
    }

    // ---------------------------------- GETTER ----------------------------------------------
    public double getPopulation() {
        return population;
    }
}
