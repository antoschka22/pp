/**
 * Interface, die den dynamischen Zustand und das Verhalten einer bestimmten Pflanzenpopulation beschreibt.
 * Sie definiert, wie Wuchskraft, Blütenanteil und Samenqualität auf Wetterereignisse
 * und saisonale Zyklen reagieren.
 */
public interface IPlantPopulation {

    /**
     * Setzt den Zustand der Population für eine neue Vegetationsperiode zurück
     * (z.B. Zurücksetzen von Blütenanteil, Samenqualität und Blühstatus)
     */
    void resetForNewVegetationPeriod();

    /**
     * Aktualisiert den Zustand der Population basierend auf dem täglichen Wetter und Umweltfaktoren
     *
     * @param randomWeather         Die täglichen Wetterdaten.
     * @param beePopulation   Die aktuelle Bienenpopulation.
     * @param totalFoodSupply Das gesamte Nahrungsangebot aller Populationen.
     */
    void updateDaily(IWeather randomWeather, double beePopulation, double totalFoodSupply);

    /**
     * Aktualisiert die Wuchskraft für die Ruhephase basierend auf der Samenqualität und einem zufälligen Vermehrungsfaktor
     */
    void updateRestPhase();

    /**
     * Setzt die Wuchskraft der Population direkt
     * Wird von externen Ereignissen (z.B. MowingEvent) genutzt
     *
     * @param newVigor Der neue Wert für die Wuchskraft
     */
    void setVigor(double newVigor);

    /**
     * Setzt den Blütenanteil der Population direkt
     * Wird von externen Ereignissen (z.B. MowingEvent) genutzt
     *
     * @param newBloomProportion Der neue Wert für den Blütenanteil
     */
    void setBloomProportion(double newBloomProportion);

    /**
     * Ruft die geografischen Koordinaten der Population ab.
     *
     * @return Die Koordinaten.
     */
    Coordinates getCoordinates();

    /**
     * Ruft den Namen der Pflanzenart ab.
     *
     * @return Der Name der Art.
     */
    Object getSpeciesName();

    /**
     * Berechnet das aktuelle Nahrungsangebot, das von dieser Population bereitgestellt wird.
     *
     * @return Das aktuelle Nahrungsangebot (Wuchskraft * Blütenanteil).
     */
    double getCurrentFoodSupply();

    /**
     * Ruft die aktuelle Wuchskraft der Population ab.
     *
     * @return Die aktuelle Wuchskraft.
     */
    double getVigor();

    /**
     * Ruft den aktuellen Blütenanteil der Population ab.
     *
     * @return Der aktuelle Blütenanteil (ein Wert zwischen 0.0 und 1.0).
     */
    double getBloomProportion();

    /**
     * Liefert eine String-Repräsentation des aktuellen Zustands der Population.
     *
     * @return Ein formatierter String mit Zustandsdetails (Art, Wuchskraft, Blüte, Samen).
     */
    @Override
    String toString();
}