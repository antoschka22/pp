/**
 * Diese Klasse stellt eine allgemeine Bienenbeobachtung dar und zudem dient sie als Basisklasse
 * für verschiedene spezielle Bienenarten wie Honeybee und Wildbee.
 */
public class Bee {

    private final String descriptionOfObs;

    /**
     * Konstruktor, der eine neue Bee-Beobachtung mit der gegebenen Beschreibung.
     * @param descriptionOfObs Die Beschreibung der Bienenbeobachtung.
     */
    public Bee(String descriptionOfObs) {
        this.descriptionOfObs = descriptionOfObs;
    }

    /**
     * Gibt die Beschreibung der Bienenbeobachtung zurück.
     * @return Der Rückgabewert ist die Beschreibung der Bienenbeobachtung.
     */
    @Override
    public String toString() {
        return descriptionOfObs;
    }
}
