/**
 * Diese Klasse modelliert die täglichen Wetterbedingungen (Sonnenscheindauer und Bodenfeuchte) während der Vegetationsperiode anhand von Zufallszahlen.
 */
public class RandomWeather implements IWeather{

    private double sunHoursToday; // Sonnenscheindauer d
    private double accumulatedSunHours; // aufsummierte Sonnenstunden h
    private double soilMoisture; // Bodenfeuchte f (0 ≤ f ≤ 1)
    private double temperature; //
    private final IDistribution restPhaseDistribution;// Zufallszahl generieren
    private double soilMoistureOverride = -1.0;


    /**
     * Konstruktor initialisiert ein neues Weather-Objekt.
     *
     * @param restPhaseDistribution der Zufallszahlengenerator
     * @throws IllegalArgumentException Wenn die Zufallszahl null ist.
     */
    public RandomWeather(IDistribution restPhaseDistribution) {
        if(restPhaseDistribution == null){
            throw new IllegalArgumentException("restPhaseDistribution must not be null");
        }
        this.restPhaseDistribution = restPhaseDistribution;
        initializeForVegetationPeriod();
    }

    /**
     * Initialisierung der Wetterwerte am Beginn der Vegetationsperiode.
     * Die Summe der Sonnenstunden startet bei 0.0 und die Bodenfeuchte f wird zufällig gewählt.
     */
    @Override
    public void initializeForVegetationPeriod(){
        sunHoursToday = 0.0;
        accumulatedSunHours = 0.0;
        soilMoisture = restPhaseDistribution.nextDouble(0,1); // 0 (inklusive) ≤ f < 1 (exklusive)
    }
    /**
     * Simulation der täglichen Wetterveränderungen.
     * - Die Sonnenscheindauer d soll eine Zufallszahl zwischen 0 und 12 sein.
     * - Die aufsummierten Werte der Sonnenscheindauer ab Beginn der Vegetationsperiode ergeben die Sonnenstunden h.
     * - Die Bodenfeuchte f mit 0 ≤ f ≤ 1 wird am Beginn der Vegetationsperiode zufällig gewählt und kann sich täglich zufällig um 10% verändern.
     * - Die Temperatur ergibt sich auch der Sinuswelle über die Vegetationsperiode plus dem täglichen Rauschen.
     *
     * @param day aktuelle Tag der Vegetationsperiode
     *
     * STYLE: Prozedural → Innerhalb dieser Methode werden schrittweise die täglichen Wetterveränderungen berechnet.
     *        Die einzelnen Schritte hängen voneinander ab, da beispielsweise zuerst sunHoursToday berechnet werden
     *        muss, um accumulatedSunHours berechnen zu können.
     */
    @Override
    public void simulateDailyChange(int day){
        this.sunHoursToday = restPhaseDistribution.nextDouble(0.0, 12.0); // Zufallszahl zwischen 0 und 12
        this.accumulatedSunHours += sunHoursToday;

        // zufällige Veränderung der Bodenfeuchte f um bis zu 10%
        double changeSoilMoisture = restPhaseDistribution.nextDouble(0, 0.1);
        soilMoisture += changeSoilMoisture;

        // Überprüfung der Intervallgröße der Bodenfeuchte f (0 ≤ f ≤ 1)
        if(this.soilMoisture < 0.0) this.soilMoisture = 0.0;
        if(this.soilMoisture > 1.0) this.soilMoisture = 1.0;

        // Berechnung der Temperatur
        double avgTemp = 15.0;
        double amplitudeTemp = 10.0; // maximale Abweichung

        // Temperatur kann als Sinuswelle über die 240 Tage mit täglichem Rauschen dargestellt werden
        double seasonalTemp = avgTemp + amplitudeTemp * Math.sin(2 * Math.PI * day / 240.0);
        double noiseTemp = restPhaseDistribution.nextDouble(-2.0, 2.0); // Rauschen von +/- 2 °C
        temperature = seasonalTemp + noiseTemp;
    }

    /**
     * Getter-Methode der Sonnenscheindauer d
     * @return Sonnenscheindauer des jeweiligen Tages
     */
    @Override
    public double getSunHoursToday(){
        return this.sunHoursToday;
    }

    /**
     * Getter-Methode der Sonnenstunden h
     * @return aufsummierten Sonnenstunden ab Beginn der Vegetationsperiode
     */
    @Override
    public double getAccumulatedSunHours(){
       return this.accumulatedSunHours;
    }

    /**
     * Getter-Methode der Bodenfeuchte f
     * @return aktuelle Bodenfeuchte, deren Wert zwischen 0 und 1 liegt
     */
    @Override
    public double getSoilMoisture(){
        return this.soilMoisture;
    }

    /**
     * Getter-Methode der Temperatur
     * @return aktuelle Temperatur
     */
    @Override
    public double getTemperature(){
        return this.temperature;
    }

    /**
     * Gibt eine lesbare Darstellung der aktuellen Wetterbedingungen des jeweiligen Tages.
     * @return Ein String mit den täglichen Wetterbedingungen.
     */
    @Override
    public String toString(){
        return String.format("Heutige Sonnenscheindauer: %.2f Sonnenstunden, Summe: %.2f Sonnenstunden, Bodenfeuchte: %.2f, Temperatur: %.2f °C",
                this.sunHoursToday,
                this.accumulatedSunHours,
                this.soilMoisture,
                this.temperature);
    }

    /**
     * Erzwingt einen bestimmten Bodenfeuchtewert für den *aktuellen* Tag.
     * Wird von Events wie DroughtEvent aufgerufen.
     * @param newMoisture Der zu setzende Feuchtewert (sollte zwischen 0 und 1 liegen).
     */
    @Override
    public void forceSoilMoisture(double newMoisture) {
        this.soilMoistureOverride = newMoisture;
    }

}
