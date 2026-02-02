/**
 * Diese Klasse dient als abstrakte Darstellung der Honigbienenpopulation, bei dem die Population
 * täglich upgedated werden soll und die Ruhephase simulieren soll. Die Honigbiene fungiert als
 * Konkurrentin zu Wildbienen und als Bestäuber. Im Vergleich zu Wildbienen, sind Honigbienen
 * stabiler, weniger abhängig vom Nahrungsangebot, tragen aber weniger effektiv zur Bestäubung bei.
 * * STYLE: Objektorientiert
 * * @invariant this.population >= 0
 * @invariant this.restPhaseDistribution != null
 * GOOD: Diese Klasse zeigt einen hohen Klassenzusammenhalt (High Cohesion).
 * Alle Methoden (updateDaily, updateRestPhase, getPopulation) und Felder (population, MAX_RANGE)
 * beziehen sich direkt auf das *eine* Konzept: die Verwaltung des Zustands
 * einer Honigbienenpopulation. Es gibt keine "fremden" Methoden, die sich z.B.
 * um das Wetter oder Pflanzen kümmern. Eine Alternative mit niedrigerem
 * Zusammenhalt wäre, wenn die Simulation-Klasse die Berechnungen
 * von 'updateDaily' und 'updateRestPhase' selbst durchführen würde,
 * was die Simulation-Klasse überladen und die Kapselung brechen würde.
 */
public class HoneyBeePopulation implements IBeePopulation {

    private double population; // Populationsgröße
    private final static double MAX_RANGE = 700.0; //Flugreichweite dieser Art
    private final IDistribution restPhaseDistribution; // Wahrscheinlichkeitsverteilung


    /**
     * @pre population >= 0
     * @pre restPhaseDistribution != null
     * @post Ein neues HoneyBeePopulation-Objekt ist erstellt und initialisiert.
     */
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
     * @pre foodSupply >= 0
     * @post this.population wurde basierend auf der foodSupply-Logik angepasst.
     * @post this.population >= 0 (stellt sicher, dass die Population nicht negativ wird, obwohl die Formel dies zulassen könnte, wenn foodSupply sehr klein ist)
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
     * @pre N/A
     * @post this.population wurde mit einem Überlebensfaktor (0.6 - 0.7) multipliziert.
     * @post this.population >= 0
     */
    @Override
    public void updateRestPhase() {
        double survivalFactor = restPhaseDistribution.nextDouble(0.6, 0.7);
        population *= survivalFactor;
    }

    /**
     * Gibt die aktuelle Populationsgröße der Honigbienen zurück.
     * @return Populationsgröße
     * @pre N/A
     * @post Gibt den Wert von this.population zurück.
     */
    @Override
    public double getPopulation() {
        return this.population;
    }

    /**
     * @pre N/A
     * @post Gibt den String "Honigbienen" zurück.
     */
    @Override
    public String getName() {
        return "Honigbienen";
    }

    /**
     * @pre N/A
     * @post Gibt den Wert von MAX_RANGE (700.0) zurück.
     */
    @Override
    public double getMaxRange() {
        return MAX_RANGE;
    }

    /**
     * @pre capacity >= 0 (gemäß Interface IBeePopulation)
     * @post this.population == capacity
     * * BAD: Die Objektkopplung und der Vertrag sind hier nicht ideal.
     * Das Interface IBeePopulation definiert die Vorbedingung (capacity >= 0).
     * Diese Implementierung setzt den Wert jedoch einfach (`this.population = capacity`),
     * ohne ihn zu validieren. Wenn ein Client (z.B. die Simulation-Klasse)
     * fälschlicherweise einen negativen Wert übergibt, wird der Vertrag des Clients
     * gebrochen, aber die Methode lässt es zu. Dies führt dazu, dass die Klasseninvariante
     * (population >= 0) bricht.
     * Eine bessere (robustere) Lösung wäre:
     * `this.population = Math.max(0, capacity);`
     * oder eine `IllegalArgumentException` zu werfen, um den fehlerhaften Client
     * sofort zu benachrichtigen.
     */
    @Override
    public void setPopulation(double capacity) {
        this.population = capacity;
    }
}
