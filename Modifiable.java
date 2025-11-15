/**
 * Das ist ein generisches Interface für Objekte, die Objektveränderungen zurückgeben können,
 * ohne das Objekt selbst zu verändern.
 *
 * @param <X> der Typparameter, der für die Objektveränderung verwendet wird.
 * @param <T> der Typ des Ergebnisses.
 */
public interface Modifiable <X, T>{

    /**
     * Gibt ein neues Objekt vom Typ T zurück, das this um x erweitert hat.
     * This und x bleiben dabei unverändert.
     *
     * @param x Der Wert, der für die Erweiterung verwendet wird.
     * @post Es wird ein um x erweitertes Objekt des Typs T oder this, falls
     * this nicht durch x erweitert werden kann, zurückgegeben.
     */
    T add (X x);

    /**
     * Gibt ein neues Objekt vom Typ T zurück, aus dem x entfernt wurde.
     * This und x bleiben dabei unverändert.
     *
     * @param x Der Wert, der für die Entfernung genutzt wird.
     * @post Es wird ein Objekt des Typs T aus dem x entfernt wurde
     * oder this, falls x nicht aus this entfernt werden kann, zurückgegeben.
     */
    T subtract (X x);
}
