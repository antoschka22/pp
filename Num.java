/**
 * Testklasse, die eine unveränderliche Zahl enthält.
 * Implementiert Modifiable mit geeigneten Typen (Num, Num).
 */
public class Num implements Modifiable<Num, Num> {

    /**
     * Die unveränderliche Zahl, die dieses Objekt enthält.
     */
    private final int value;

    /**
     * Konstruktor, setzt die unveränderliche Zahl.
     *
     * @param value Der Wert für dieses Num-Objekt.
     */
    public Num(int value) {
        this.value = value;
    }

    /**
     * Privater Getter, der von add/subtract verwendet wird.
     * (Könnte auch direkt auf y.value zugreifen).
     */
    private int getValue() {
        return this.value;
    }

    /**
     * Gibt eine neue Instanz von Num zurück, die die Summe der Zahlen enthält.
     * Das Originalobjekt (this) und der Parameter (y) bleiben unverändert.
     *
     * @param y Das Num-Objekt, das addiert werden soll.
     * @return Eine neue Instanz von Num mit dem Summenwert.
     */
    @Override
    public Num add(Num y) {
        // Gibt eine neue Instanz zurück, die die Summe enthält
        return new Num(this.value + y.getValue());
    }

    /**
     * Gibt eine neue Instanz von Num zurück, die die Differenz der Zahlen enthält.
     * Das Originalobjekt (this) und der Parameter (y) bleiben unverändert.
     *
     * @param y Das Num-Objekt, das subtrahiert werden soll.
     * @return Eine neue Instanz von Num mit dem Differenzwert.
     */
    @Override
    public Num subtract(Num y) {
        // Gibt eine neue Instanz zurück, die die Differenz enthält
        return new Num(this.value - y.getValue());
    }

    /**
     * Gibt die Zahl als Text zurück
     *
     * @return Die Zahl als String.
     */
    @Override
    public String toString() {
        return "" + this.value;
    }
}