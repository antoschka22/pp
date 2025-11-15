import java.util.Iterator;

/**
 * Iterator für MSet. Läuft über die EntryNode-Liste.
 * Diese Klasse ist package-private und greift auf MSet.EntryNode zu.
 */
class MSetIterator<E> implements Iterator<E> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
        return null;
    }
}