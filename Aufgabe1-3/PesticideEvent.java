/**
 * Ein Event, das einen Pestizideinsatz simuliert.
 * Es reduziert die Population von Bienen (in diesem Fall die 'BeePopulation')
 * um einen bestimmten Faktor.
 * STYLE: Objektorientiert
 * @invariant this.reductionFactor >= 0.0
 */
public class PesticideEvent implements IBeeEvent{

    private final double reductionFactor; // z.B. 0.7 (reduziert auf 70%)

    /**
     * @pre reductionFactor >= 0.0 (Werte > 1.0 sind erlaubt, sie erhöhen die Population)
     * @post Ein neues PesticideEvent ist erstellt, this.reductionFactor ist initialisiert.
     */
    public PesticideEvent(double reductionFactor) {
        this.reductionFactor = reductionFactor;
    }

    /**
     * Die Bienenpopulation wird verringert, um einen reductionFactor
     *
     * @param currentPopulation Die Liste aller Populationen in der Simulation
     * @param weather Die Weather Instanz in der Simulation
     * @return Neue Population der Bienen
     * @pre currentPopulation >= 0 && weather != null
     * @post Gibt das Ergebnis von currentPopulation * this.reductionFactor zurück.
     * Der Zustand von 'weather' wird nicht verändert.
     * Der Rückgabewert ist >= 0.
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
     * @pre N/A
     * @post Gibt den String "Pesticide" zurück.
     */
    @Override
    public String getName(){
        return "Pesticide";
    }

}