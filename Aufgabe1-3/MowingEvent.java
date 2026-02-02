import java.util.List;

/**
 * Ein Event, das eine Mahd (z.B. einer Wiese) simuliert.
 * Es reduziert die Wuchskraft (vigor) und den Blütenanteil (bloomProportion)
 * aller Pflanzenpopulationen um einen bestimmten Faktor.
 * STYLE: Objektorientiert
 * @invariant this.vigorReductionFactor >= 0.0 && this.vigorReductionFactor <= 1.0
 * @invariant this.bloomReductionFactor >= 0.0 && this.bloomReductionFactor <= 1.0
 */
public class MowingEvent implements IPopulationEvent{

    private final double vigorReductionFactor; // z.B. 0.5 (reduziert auf 50%)
    private final double bloomReductionFactor; // z.B. 0.1 (reduziert auf 10%)

    /**
     * @pre vigorReductionFactor >= 0.0 && vigorReductionFactor <= 1.0
     * @pre bloomReductionFactor >= 0.0 && bloomReductionFactor <= 1.0
     * @post Ein neues MowingEvent ist erstellt, Reduktionsfaktoren sind initialisiert.
     */
    public MowingEvent(double vigorReductionFactor, double bloomReductionFactor) {
        this.vigorReductionFactor = vigorReductionFactor;
        this.bloomReductionFactor = bloomReductionFactor;
    }

    /**
     * Geht alle Populationen durch, filtert die Pflanzenpopulationen
     * und reduziert deren Wuchskraft und Blütenanteil.
     *
     * @param populations Die Liste aller Populationen in der Simulation
     * @param weather Die Weather Instanz in der Simulation
     * * @pre populations != null (kann leer sein)
     * @pre weather != null
     * @post Für jedes Element pop in populations, das eine Instanz von Plantpopulation ist:
     * pop.setVigor() und pop.setBloomProportion() wurden mit reduzierten Werten aufgerufen.
     * Der Zustand von weather wird nicht verändert.
     * BAD: Diese Implementierung verletzt das Open/Closed-Prinzip und zeigt eine
     * Form von starker Kopplung durch instanceof.
     * Die apply-Methode muss wissen, was die konkrete Implementierung
     * von IPlantPopulation ist (also Plantpopulation),
     * um den Cast Plantpopulation plantPop durchzuführen.
     * Wenn wir eine neue Art von Pflanze einführen würden (zB TreePopulation),
     * würde dieses Event für sie nicht funktionieren, und wir müssten diese Klasse ändern.
     * Eine bessere Lösung wäre, wenn das Interface IPlantPopulation eine Methode
     * applyMowing(double vigorFactor, double bloomFactor) anbieten würde.
     * Dann könnte MowingEvent.apply einfach pop.applyMowing(...) aufrufen,
     * ohne die konkrete Klasse kennen zu müssen (dynamische Bindung).
     */
    @Override
    public void apply(List<IPlantPopulation> populations, IWeather weather) {
        for (IPlantPopulation pop : populations) {
            // Prüft, ob es sich um eine Pflanzenpopulation handelt
            if (pop instanceof Plantpopulation plantPop) {
                // Hole alte Werte
                double currentVigor = plantPop.getVigor();
                double currentBloom = plantPop.getBloomProportion();

                // Wende Reduktion an (setzt modifizierte Klassen voraus)
                plantPop.setVigor(currentVigor * this.vigorReductionFactor);
                plantPop.setBloomProportion(currentBloom * this.bloomReductionFactor);
            }
        }
    }

    /**
     * Gibt den Namen des Events für Logging-Zwecke zurück.
     * @return Name des Events
     * @pre N/A
     * @post Gibt den String "Mowing" zurück.
     */
    @Override
    public String getName(){
        return "Mowing";
    }
}