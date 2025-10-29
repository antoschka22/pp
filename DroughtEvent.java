import java.util.List;

/**
 * Ein Event, das eine Dürre simuliert.
 * Es setzt die Bodenfeuchte für den aktuellen Tag auf einen
 * vordefinierten, sehr niedrigen Wert.
 */
public class DroughtEvent implements IPopulationEvent{

    private final double droughtMoistureLevel;

    /**
     * Erstellt ein neues Dürre-Event.
     * @param droughtMoistureLevel Der Wert, auf den die Bodenfeuchte gesetzt wird (z.B. 0.05).
     */
    public DroughtEvent(double droughtMoistureLevel) {
        this.droughtMoistureLevel = droughtMoistureLevel;
    }

    /**
     * Wendet die Dürre an, indem es dem Wetterobjekt befiehlt,
     * die Bodenfeuchte für diesen Tag zu überschreiben.
     *
     * @param populations Die Liste aller Populationen in der Simulation
     * @param weather Die Weather Instanz in der Simulation
     */
    @Override
    public void apply(List<IPlantPopulation> populations, IWeather weather) {
        // Greift auf das Wetter-Objekt im Kontext zu und erzwingt einen neuen Wert
        weather.forceSoilMoisture(this.droughtMoistureLevel);
    }

    /**
     * Gibt den Namen des Events für Logging-Zwecke zurück.
     * @return Name des Events
     */
    @Override
    public String getName(){
        return "Drought";
    }
}