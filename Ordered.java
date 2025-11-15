/**
 * Generisches Interface für Objekte, die eine Ordnungsbeziehung definieren.
 *
 * @param <E> Der Typ der Elemente in der Ordnung.
 * @param <R> Der Ergebnistyp der 'before'-Methode.
 */
public interface Ordered<E, R> {

    /**
     * Prüft, ob x in der Ordnung vor y kommt.
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @return Ein Ergebnis ungleich null, wenn x vor y kommt, sonst null[cite: 14].
     */
    R before(E x, E y);

    /**
     * Ändert die Ordnung, sodass x vor y steht.
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @throws IllegalArgumentException wenn die Ordnung nicht hergestellt werden kann[cite: 18].
     */
    void setBefore(E x, E y);
}