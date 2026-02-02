/**
 * Interface für die Modellierung von täglichen Wetterbedingungen.
 */
public interface IWeather {

    /**
     * Initialisierung der Wetterwerte am Beginn der Vegetationsperiode.
     * @post soilMoisture, sunHoursToday, accumulatedSunHours und temperature werden gesetzt.
     *
     */
    void initializeForVegetationPeriod();

    /**
     * Simulation der täglichen Wetterveränderungen, bestehend aus Sonnenscheindauer d, der aufsummierten Sonnenstunden h,
     * der Bodenfeuchte f und der Temperatur t.
     *
     * @param day aktueller Tag der Vegetationsperiode
     *
     * @pre day ist ein gültiger Tag der Vegetationsperiode.
     * @post die internen Zustandsvariablen werden aktualisiert.
     */
    void simulateDailyChange(int day);

    /**
     * Getter-Methode der Sonnenscheindauer d
     * @return Sonnenscheindauer des jeweiligen Tages
     *
     * @post Der zurückgegebene Wert ist die Sonnenscheindauer des aktuellen Tages und ist >= 0.0.
     */
    double getSunHoursToday();
    /**
     * Getter-Methode der Sonnenstunden h
     * @return aufsummierten Sonnenstunden ab Beginn der Vegetationsperiode
     *
     * @post Der zurückgegebene Wert ist die akkumulierte Sonnenscheindauer und ist >= 0.0.
     */
    double getAccumulatedSunHours();
    /**
     * Getter-Methode der Bodenfeuchte f
     * @return aktuelle Bodenfeuchte, deren Wert zwischen 0 und 1 liegt
     *
     * @post Der zurückgegebene Wert ist die Bodenfeuchte und liegt im gültigen Bereich [0.0, 1.0].
     */
    double getSoilMoisture();
    /**
     * Getter-Methode der Temperatur
     * @return aktuelle Temperatur
     *
     * @post Der zurückgegebene Wert ist die aktuelle Temperatur.
     */
    double getTemperature();
    /**
     * Erzwingt einen bestimmten Bodenfeuchtewert für den *aktuellen* Tag.
     * @param newMoisture Der zu setzende Feuchtewert.
     *
     * @pre newMoisture im Bereich [0.0, 1.0].
     * @post: soilMoistureOverride ist auf newMoisture gesetzt.
     */
    void forceSoilMoisture(double newMoisture);


}
