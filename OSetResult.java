/**
 * Das Interface für das von OSet.before() zurückgegebene Objekt.
 * Es bündelt die Anforderungen von Ordered und Modifiable.
 *
 * @param <E> Der Typ der Einträge.
 */
public interface OSetResult<E> extends Ordered<E, Boolean>, Modifiable<E, OSetResult<E>> {
    // Alle Methoden (before, setBefore, add, subtract)
    // werden von den Ober-Interfaces geerbt.
}
