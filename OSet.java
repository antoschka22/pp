/**
 * OSet implementiert OrdSet.
 * Erbt die meiste Logik von AbstractOrdSet.
 * Implementiert 'before' durch Rückgabe eines 'SubSet'-Objekts.
 *
 * @param <E> Der Typ der Einträge.
 */
public class OSet<E> extends AbstractOrdSet<E, OSet<E>> {


    public OSet(Ordered<? super E, ?> c) {
        super(c);
    }

    @Override
    public OSet<E> before(E x, E y) {
        return null;
    }

    @Override
    public void setBefore(E x, E y) {

    }
}