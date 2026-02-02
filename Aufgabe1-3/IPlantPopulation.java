/**
 * Interface, das den dynamischen Zustand und das Verhalten einer bestimmten Pflanzenpopulation beschreibt.
 * Es definiert, wie Wuchskraft, Blütenanteil und Samenqualität auf Wetterereignisse
 * und saisonale Zyklen reagieren.
 */
public interface IPlantPopulation {

    /**
     * Setzt den Zustand der Population für eine neue Vegetationsperiode zurück
     * (z.B. Zurücksetzen von Blütenanteil, Samenqualität und Blühstatus)
     *
     * @post interne Variablen werden auf Anfangswerte zurückgesetzt.
     */
    void resetForNewVegetationPeriod();

    /**
     * Aktualisiert den Zustand der Population basierend auf dem täglichen Wetter und Umweltfaktoren
     *
     * @param weather         Die täglichen Wetterdaten.
     * @param beePopulation   Die aktuelle Bienenpopulation.
     * @param totalFoodSupply Das gesamte Nahrungsangebot aller Populationen.
     *
     * @pre weather ist nicht null, beePopulation >= 0.0 und totalFoodSupply >= 0.0.
     * @post interne Variablen werden aktualisiert.
     */
    void updateDaily(IWeather weather, double beePopulation, double totalFoodSupply);

    /**
     * Aktualisiert die Wuchskraft für die Ruhephase basierend auf der Samenqualität und einem zufälligen Vermehrungsfaktor
     *
     * @post Wuchskraft wird auf >= 0.0 aktualisiert.
     */
    void updateRestPhase();

    /**
     * Setzt die Wuchskraft der Population direkt.
     * Wird von externen Ereignissen (z.B. MowingEvent) genutzt
     *
     * @param newVigor Der neue Wert für die Wuchskraft
     *
     * @pre: newVigor >= 0.0.
     * @post: Wuchskraft wird auf newVigor aktualisiert.
     */
    void setVigor(double newVigor);

    /**
     * Setzt den Blütenanteil der Population direkt
     * Wird von externen Ereignissen (z.B. MowingEvent) genutzt.
     *
     * @param newBloomProportion Der neue Wert für den Blütenanteil
     *
     * @pre: newBloomProportion im Bereich [0.0, 1.0].
     * @post: Blühanteil wird auf newBloomProportion aktualisiert.
     */
    void setBloomProportion(double newBloomProportion);

    /**
     * Ruft die geografischen Koordinaten der Population ab.
     *
     * @return Die Koordinaten.
     *
     * @post gibt ein gültiges Coordinates-Objekt, ungleich null, zurück
     */
    Coordinates getCoordinates();

    /**
     * Ruft den Namen der Pflanzenart ab.
     *
     * @return Der Name der Art.
     *
     * @post der zurückgegebene String ist ungleich null.
     */
    Object getSpeciesName();

    /**
     * Berechnet das aktuelle Nahrungsangebot, das von dieser Population bereitgestellt wird.
     *
     * @return Das aktuelle Nahrungsangebot (Wuchskraft * Blütenanteil).
     *
     * @post gibt das Nahrungsangebot, >= 0.0, zurück.
     */
    double getCurrentFoodSupply();

    /**
     * Ruft die aktuelle Wuchskraft der Population ab.
     *
     * @return Die aktuelle Wuchskraft.
     *
     * @post gibt die Wuchskraft, >= 0.0, zurück.
     */
    double getVigor();

    /**
     * Ruft den aktuellen Blütenanteil der Population ab.
     *
     * @return Der aktuelle Blütenanteil (ein Wert zwischen 0.0 und 1.0).
     *
     * @post gibt den Blütenanteil im Bereich [0.0, 1.0] zurück
     */
    double getBloomProportion();


    /**
     * Ruft die aktuelle Samenqualität der Population ab.
     *
     * @return Die aktuelle Samenqualität (ein Wert zwischen 0.0 und 1.0).
     *
     * @post gibt die Samenqualität im Bereich [0.0, 1.0] zurück
     */
    double getSeedQuality();

    /**
     * Liefert eine String-Repräsentation des aktuellen Zustands der Population.
     *
     * @return Ein formatierter String mit Zustandsdetails (Art, Wuchskraft, Blüte, Samen).
     *
     * @post gibt einen nicht-leeren String zurück
     */
    @Override
    String toString();
}