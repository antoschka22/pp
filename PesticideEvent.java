/**
 * Ein Event, das einen Pestizideinsatz simuliert.
 * Es reduziert die Population von Bienen (in diesem Fall die 'BeePopulation')
 * um einen bestimmten Faktor.
 */
public class PesticideEvent implements IBeeEvent{

    private final double reductionFactor; // z.B. 0.7 (reduziert auf 70%)

    public PesticideEvent(double reductionFactor) {
        this.reductionFactor = reductionFactor;
    }

    /**
     * Die Bienenpopulation wird verringert, um einen reductionFactor
     *
     * @param currentPopulation Die Liste aller Populationen in der Simulation
     * @param weather Die Weather Instanz in der Simulation
     * @return Neue Population der Bienen
     */
    @Override
    public double apply(double currentPopulation, IWeather weather) {
        if(currentPopulation < 0){
            throw new IllegalArgumentException(
                    "currentPopulation ist ungültig. Sollte >= 0. Eingegebener Wert: currentPopulation=" + currentPopulation);
        }

        return currentPopulation * this.reductionFactor;
    }

    /**
     * Gibt den Namen des Events für Logging-Zwecke zurück.
     * @return Name des Events
     */
    @Override
    public String getName(){
        return "Pesticide";
    }

}