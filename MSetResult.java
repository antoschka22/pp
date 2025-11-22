/**
 * Das Interface für das von MSet.before() zurückgegebene Objekt.
 * Es erbt von OSetResult, damit MSet von OSet erben kann.
 * Durch die Vererbung von OSetResult ist es automatisch auch 'Ordered' und 'Modifiable'.
 * Wir überschreiben add/subtract, um den spezifischeren Rückgabetyp (MSetResult) zu garantieren.
 *
 * @param <E> Der Typ der Einträge.
 */
public interface MSetResult<E> extends OSetResult<E> {

    @Override
    MSetResult<E> add(E x);

    @Override
    MSetResult<E> subtract(E x);
}