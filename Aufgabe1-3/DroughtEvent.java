import java.util.List;

/**
 * Ein Event, das eine Dürre simuliert.
 * Es setzt die Bodenfeuchte für den aktuellen Tag auf einen
 * vordefinierten, sehr niedrigen Wert.
 * STYLE: Objektorientiert
 * @invariant this.droughtMoistureLevel >= 0.0 && this.droughtMoistureLevel <= 1.0
 */
public class DroughtEvent implements IPopulationEvent{

    private final double droughtMoistureLevel;

    /**
     * Erstellt ein neues Dürre-Event.
     * @param droughtMoistureLevel Der Wert, auf den die Bodenfeuchte gesetzt wird (z.B. 0.05).
     * @pre droughtMoistureLevel >= 0.0 && droughtMoistureLevel <= 1.0
     * @post Ein neues DroughtEvent ist erstellt, this.droughtMoistureLevel ist initialisiert.
     * BAD: Diese Methode erzwingt ihren Vertrag (Vorbedingung) nicht.
     * Ein Client könnte ein DroughtEvent mit `new DroughtEvent(5.0)` erstellen,
     * was die Klasseninvariante verletzen würde. Der Konstruktor sollte
     * ungültige Werte abfangen (`if (level < 0.0 || level > 1.0) throw ...`),
     * um die Robustheit zu gewährleisten und den Client sofort über den
     * fehlerhaften Aufruf zu informieren.
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
     * @pre populations != null && weather != null
     * @post weather.forceSoilMoisture() wurde mit this.droughtMoistureLevel aufgerufen.
     * Der Zustand von 'populations' wird nicht verändert.
     */
    @Override
    public void apply(List<IPlantPopulation> populations, IWeather weather) {
        // Greift auf das Wetter-Objekt im Kontext zu und erzwingt einen neuen Wert
        weather.forceSoilMoisture(this.droughtMoistureLevel);
    }

    /**
     * Gibt den Namen des Events für Logging-Zwecke zurück.
     * @return Name des Events
     * @pre N/A
     * @post Gibt den String "Drought" zurück.
     */
    @Override
    public String getName(){
        return "Drought";
    }
}