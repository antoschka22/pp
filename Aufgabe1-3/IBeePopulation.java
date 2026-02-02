/**
 * Generelles Interface für eine simulierte Population von Bienenarten.
 * Definiert die notwendigen Methoden für die Interaktion mit der Hauptsimulation,
 * um tägliche Updates und Ruhephasen-Updates durchzuführen.
 * STYLE: Objektorientiert
 */
public interface IBeePopulation {

    /**
     * Während der Vegetationsperiode wird population täglich angepasst:
     * Je nachdem um welche Bienenart es sich handelt, wird die Population anders angepasst
     *
     * @param foodSupply Die Anzahl an foodSupply im Moment in der Simulation
     * @pre foodSupply >= 0
     * @post Die Bienenpopulation wurde basierend auf dem foodSupply aktualisiert.
     * Die Population (abrufbar über getPopulation()) ist >= 0.
     */
    void updateDaily(double foodSupply);

    /**
     * Simuliert die Ruhephase wo population angepasst wird
     * @pre N/A
     * @post Die Bienenpopulation wurde für die Ruhephase aktualisiert (typischerweise reduziert).
     * Die Population (abrufbar über getPopulation()) ist >= 0.
     */
    void updateRestPhase();

    /**
     * Gibt den aktuellen Populationswert zurück.
     * Bei Bienen ist dies die Anzahl der Individuen.
     *
     * @return Der aktuelle Populationswert.
     * @pre N/A
     * @post Gibt die aktuelle Populationsgröße als double zurück. Der Wert ist >= 0.
     */
    double getPopulation();

    /**
     * Gibt einen beschreibenden Namen für die Population zurück.
     *
     * @return Der Name der Population (z.B. "Wildbienen" oder "Honigbiebe").
     * @pre N/A
     * @post Gibt einen nicht-leeren String zurück, der die Bienenart beschreibt.
     */
    String getName();

    /**
     * Gibt die maximale Flugweite einer Population an
     * @return maximale Flugweite
     * @pre N/A
     * @post Gibt die maximale Flugweite (in Metern o.ä.) als double zurück. Der Wert ist > 0.
     */
    double getMaxRange();

    /**
     * Setzt die Anzahl der Individuen der Population auf Capacity.
     * Wird z.B. von NestingSite verwendet, um die Kapazität zu begrenzen.
     * @param capacity neuer Populationswert.
     * @pre capacity >= 0
     * @post getPopulation() gibt nun 'capacity' zurück.
     */
    void setPopulation(double capacity);
}