/**
 * Testklasse, die eine unveränderliche Zahl repräsentiert
 * Implementiert Modifiable, wobei Operationen neue Instanzen zurückgeben
 *
 * @invariant Dieses Objekt ist unveränderlich (Immutable). Der interne Wert 'value' ändert sich nach Konstruktion nicht mehr
 */
public class Num implements Modifiable<Num, Num> {

    /**
     * Die unveränderliche Zahl, die dieses Objekt enthält
     */
    private final int value;

    /**
     * Konstruktor, setzt die unveränderliche Zahl
     *
     * @param value Der Wert für dieses Num-Objekt
     */
    public Num(int value) {
        this.value = value;
    }

    /**
     * Privater Getter für den internen Wert
     * Dient dem Zugriff innerhalb der Klasse für Rechenoperationen
     */
    private int getValue() {
        return this.value;
    }

    /**
     * Gibt eine neue Instanz von Num zurück, die die Summe der Zahlen enthält
     *
     * @param y Das Num-Objekt, das addiert werden soll
     * @return Eine neue Instanz von Num mit dem Wert (this.value + y.value)
     * @pre y ist nicht null
     * @post this und y bleiben unverändert
     */
    @Override
    public Num add(Num y) {
        return new Num(this.value + y.getValue());
    }

    /**
     * Gibt eine neue Instanz von Num zurück, die die Differenz der Zahlen enthält
     *
     * @param y Das Num-Objekt, das subtrahiert werden soll
     * @return Eine neue Instanz von Num mit dem Wert (this.value - y.value)
     * @pre y ist nicht null
     * @post this und y bleiben unverändert
     */
    @Override
    public Num subtract(Num y) {
        return new Num(this.value - y.getValue());
    }

    /**
     * Gibt die Zahl als Text zurück
     *
     * @return Die String-Repräsentation des numerischen Werts
     */
    @Override
    public String toString() {
        return "" + this.value;
    }
}