/**
 * Implementiert OrdSet, ähnlich wie OSet, aber mit Modifiable-Elementen.
 *
 * @param <E> Der Typ der Einträge, muss Modifiable sein
 * @param <X> Der Modifikationstyp für die E-Elemente
 */
public class MSet<E extends Modifiable<X, E>, X> extends AbstractOrdSet<E, MSet<E, X>> {
    /** Das Prüfobjekt (package-private für MSetResultImpl) */
    /**
     * Konstruktor, der das Prüfobjekt c setzt.
     *
     * @param c Das Objekt zur Prüfung (kann null sein).
     */
    public MSet(Ordered<? super E, ?> c) {
        super(c);
    }

    // --- MSet-spezifische Methoden (plus/minus) ---

    @Override
    public MSet<E, X> before(E x, E y) {
        return null;
    }

    @Override
    public void setBefore(E x, E y) {

    }

    public void plus(X x) {
    }

    public void minus(X x) {
    }

    @Override
    public String toString() {
        return "";
    }
}