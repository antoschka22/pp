/**
 * Generelles Interface für eine simulierte Population von Bienenarten.
 * Definiert die notwendigen Methoden für die Interaktion mit der Hauptsimulation,
 * um tägliche Updates und Ruhephasen-Updates durchzuführen.
 */
public interface IBeePopulation {

    /**
     * Während der Vegetationsperiode wird population täglich angepasst:
     * Je nachdem um welche Bienenart es sich handelt, wird die Population anders angepasst
     *
     * @param foodSupply Die Anzahl an foodSupply im Moment in der Simulation
     */
    void updateDaily(double foodSupply);

    /**
     * Simuliert die Ruhephase wo population angepasst wird
     */
    void updateRestPhase();

    /**
     * Gibt den aktuellen Populationswert zurück.
     * Bei Bienen ist dies die Anzahl der Individuen.
     * Bei Pflanzen ist dies die Wuchskraft (vigor).
     *
     * @return Der aktuelle Populationswert.
     */
    double getPopulation();

    /**
     * Gibt einen beschreibenden Namen für die Population zurück.
     *
     * @return Der Name der Population (z.B. "Wildbienen" oder "Honigbiebe").
     */
    String getName();

    /**
     * Gibt die maximale Flugweite einer Population an
     * @return maximale Flugweite
     */
    double getMaxRange();

    /**
     * Setzt die Anzahl der Individuen der Population auf Capacity.
     * @param capacity neuer Populationswert.
     */
    void setPopulation(double capacity);
}