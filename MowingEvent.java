import java.util.List;

/**
 * Ein Event, das eine Mahd (z.B. einer Wiese) simuliert.
 * Es reduziert die Wuchskraft (vigor) und den Blütenanteil (bloomProportion)
 * aller Pflanzenpopulationen um einen bestimmten Faktor.
 */
public class MowingEvent implements IPopulationEvent{

    private final double vigorReductionFactor; // z.B. 0.5 (reduziert auf 50%)
    private final double bloomReductionFactor; // z.B. 0.1 (reduziert auf 10%)

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
     */
    @Override
    public String getName(){
        return "Mowing";
    }
}