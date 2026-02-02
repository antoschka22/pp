/**
 * Generisches Interface für Objekte, die eine partielle Ordnungsbeziehung definieren
 *
 * @param <E> Der Typ der Elemente in der Ordnung
 * @param <R> Der Ergebnistyp der 'before'-Methode
 */
public interface Ordered<E, R> {

    /**
     * Prüft, ob x in der Ordnung vor y kommt
     *
     * @param x Das erste Element (Vorgänger)
     * @param y Das zweite Element (Nachfolger)
     * @return Ein Ergebnis vom Typ R ungleich null, wenn x in der Ordnung vor y steht
     * Gibt null zurück, wenn keine Ordnungsbeziehung besteht oder x nicht vor y steht
     * @pre x und y sind ungleich null
     * @post Der Zustand des Objekts (this) sowie von x und y bleibt unverändert (Nebeneffektfreiheit)
     */
    R before(E x, E y);

    /**
     * Versucht, die Ordnung so zu ändern, dass x vor y steht
     *
     * @param x Das erste Element
     * @param y Das zweite Element
     * @throws IllegalArgumentException wenn:
     * 1. x und y identisch sind (x == y)
     * 2. Die Beziehung einen Zyklus verursachen würde (dh y steht bereits vor x)
     * 3. Ein gesetzter Prüfer (Checker c) diese Beziehung verbietet
     * @post x und y sind im Container enthalten
     * @post x steht in der Ordnung vor y (this.before(x, y) != null)
     */
    void setBefore(E x, E y);
}