/**
 * Interface für die Modellierung von täglichen Wetterbedingungen.
 */
public interface IWeather {

    /**
     * Initialisierung der Wetterwerte am Beginn der Vegetationsperiode.
     *
     */
    void initializeForVegetationPeriod();

    /**
     * Simulation der täglichen Wetterveränderungen, bestehend aus Sonnenscheindauer d, der aufsummierten Sonnenstunden h,
     * der Bodenfeuchte f und der Temperatur t.
     *
     * @param day aktueller Tag der Vegetationsperiode
     */
    void simulateDailyChange(int day);

    /**
     * Getter-Methode der Sonnenscheindauer d
     * @return Sonnenscheindauer des jeweiligen Tages
     */
    double getSunHoursToday();
    /**
     * Getter-Methode der Sonnenstunden h
     * @return aufsummierten Sonnenstunden ab Beginn der Vegetationsperiode
     */
    double getAccumulatedSunHours();
    /**
     * Getter-Methode der Bodenfeuchte f
     * @return aktuelle Bodenfeuchte, deren Wert zwischen 0 und 1 liegt
     */
    double getSoilMoisture();
    /**
     * Getter-Methode der Temperatur
     * @return aktuelle Temperatur
     */
    double getTemperature();
    /**
     * Erzwingt einen bestimmten Bodenfeuchtewert für den *aktuellen* Tag.
     * @param newMoisture Der zu setzende Feuchtewert.
     */
    void forceSoilMoisture(double newMoisture);


}
