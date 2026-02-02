/**
 * Diese Klasse dient als Abstrakte Darstellung der Wildbienepopulation,
 * bei dem die Population täglich upgedated werden soll
 * und die Ruhephase simulierensoll
 * STYLE: Objektorientiert
 * @invariant this.population >= 0 && this.restPhaseDistribution != null
 */
public class WildbeePopulation implements IBeePopulation {
    private double population; // Population ist das x auf der Angabe
    private final static double MAX_RANGE = 600.0; //Flugreichweite dieser Art
    private final IDistribution restPhaseDistribution; // Wahrscheinlichkeitsverteilung

    /**
     * @pre population >= 0 && restPhaseDistribution != null
     * @post Ein neues WildbeePopulation-Objekt ist erstellt und initialisiert.
     */
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
     * Ist foodSupply >= population -> wird population um 3% erhöht
     * Sonst wird population um ((6*foodSply/x)-3)% erhöht/verringert
     *
     * @pre foodSupply >= 0
     * @post this.population wurde basierend auf der foodSupply-Logik angepasst.
     * @post this.population >= 0
     * BAD: Diese Methode benutzt eine verschachtelte Logik, da in der
     *      if-else-Verzweigung mehrere Berechnungen erfolgen, welche schwer
     *      verständlich sein können. Beispielsweise ist die Berechnung von
     *      changeQuote nicht sofort klar, weswegen man dafür eine separate
     *      Hilfsmethode erstellen könnte, um mehr Klarheit zu schaffen.
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
     * @pre N/A
     * @post this.population wurde mit einem Überlebensfaktor (0.1 - 0.3) multipliziert.
     * @post this.population >= 0
     */
    @Override
    public void updateRestPhase() {
        double survivalFactor = restPhaseDistribution.nextDouble(0.1, 0.3);
        population *= survivalFactor;
    }

    // ---------------------------------- GETTER ----------------------------------------------
    /**
     * @pre N/A
     * @post Gibt den Wert von this.population zurück.
     */
    public double getPopulation() {
        return population;
    }

    /**
     * @pre N/A
     * @post Gibt den String "Wildbienen" zurück.
     */
    @Override
    public String getName() {
        return "Wildbienen";
    }

    /**
     * @pre N/A
     * @post Gibt den Wert von MAX_RANGE (600.0) zurück.
     */
    @Override
    public double getMaxRange() {
        return MAX_RANGE;
    }

    /**
     * @pre capacity >= 0 (gemäß Interface IBeePopulation)
     * @post this.population == capacity
     * BAD: Identisches Problem wie in HoneyBeePopulation.
     * Die Methode validiert capacity nicht. Wenn ein Client einen negativen
     * Wert übergibt (Vertragsbruch des Clients), akzeptiert diese Methode den
     * Wert trotzdem und bricht damit die eigene Klasseninvariante (population >= 0).
     * Dies kann zu unvorhersehbarem Verhalten führen, zB zu einer
     * Division by Zero (NaN) in updateDaily.
     * Eine robuste Implementierung würde den Wert prüfen.
     */
    @Override
    public void setPopulation(double capacity) {
        this.population = capacity;
    }
}
