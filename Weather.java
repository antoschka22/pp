import java.util.Random;

/**
 * Diese Klasse modelliert die täglichen Wetterbedingungen (Sonnenscheindauer und Bodenfeuchte) während der Vegetationsperiode.
 */
public class Weather {

    private double sunHoursToday; // Sonnenscheindauer d
    private double accumulatedSunHours; // aufsummierte Sonnenstunden h
    private double soilMoisture; // Bodenfeuchte f (0 ≤ f ≤ 1)
    private Random random; // Zufallszahl generieren

    /**
     * Konstruktor initialisiert ein neues Weather-Objekt.
     *
     * @param random der Zufallszahlengenerator
     * @throws IllegalArgumentException Wenn die Zufallszahl null ist.
     */
    public Weather(Random random) {
        if(random == null){
            throw new IllegalArgumentException("Random must not be null");
        }

        this.random = random;
        initializeForVegetationPeriod();
    }

    /**
     * Initialisierung der Wetterwerte am Beginn der Vegetationsperiode.
     * Die Summe der Sonnenstunden startet bei 0.0 und die Bodenfeuchte f wird zufällig gewählt.
     */
    public void initializeForVegetationPeriod(){
        sunHoursToday = 0.0;
        accumulatedSunHours = 0.0;
        soilMoisture = random.nextDouble(); // 0 (inklusive) ≤ f < 1 (exklusive)
        if(random.nextBoolean()) soilMoisture = 1.0; // damit Wert 1 ebenfalls inklusive ist
    }

    /**
     * Simulation der täglichen Wetterveränderungen.
     * - Die Sonnenscheindauer d soll eine Zufallszahl zwischen 0 und 12 sein.
     * - Die aufsummierten Werte der Sonnenscheindauer ab Beginn der Vegetationsperiode ergeben die Sonnenstunden h.
     * - Die Bodenfeuchte f mit 0 ≤ f ≤ 1 wird am Beginn der Vegetationsperiode zufällig gewählt und kann sich täglich zufällig um 10% verändern.
     */
    public void simulateDailyChange(){
        this.sunHoursToday = random.nextDouble() * 12.0; // Zufallszahl zwischen 0 und 12
        this.accumulatedSunHours += sunHoursToday;

        // zufällige Veränderung der Bodenfeuchte f um bis zu 10%
        double changeSoilMoisture = (random.nextDouble() * 0.2) - 0.1;
        soilMoisture += changeSoilMoisture;

        // Überprüfung der Intervallgröße der Bodenfeuchte f (0 ≤ f ≤ 1)
        if(this.soilMoisture < 0.0) this.soilMoisture = 0.0;
        if(this.soilMoisture > 1.0) this.soilMoisture = 1.0;
    }

    /**
     * Getter-Methode der Sonnenscheindauer d
     * @return Sonnenscheindauer des jeweiligen Tages
     */
    public double getSunHoursToday(){
        return this.sunHoursToday;
    }

    /**
     * Getter-Methode der Sonnenstunden h
     * @return aufsummierten Sonnenstunden ab Beginn der Vegetationsperiode
     */
    public double getAccumulatedSunHours(){
       return this.accumulatedSunHours;
    }

    /**
     * Getter-Methode der Bodenfeuchte f
     * @return aktuelle Bodenfeuchte, deren Wert zwischen 0 und 1 liegt
     */
    public double getSoilMoisture(){
        return this.soilMoisture;
    }

    /**
     * Gibt eine lesbare Darstellung der aktuellen Wetterbedingungen des jeweiligen Tages.
     * @return Ein String mit den täglichen Wetterbedingungen.
     */
    @Override
    public String toString(){
        return String.format("Heutige Sonnenscheindauer: %.2f Sonnenstunden, Summe: %.2f Sonnenstunden, Bodenfeuchte: %.2f",
                this.sunHoursToday,
                this.accumulatedSunHours,
                this.soilMoisture);
    }
}
