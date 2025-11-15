/**
 * Das Interface für das von MSet.before() zurückgegebene Objekt.
 * Es bündelt die Anforderungen von Ordered und Modifiable.
 *
 * @param <E> Der Typ der Einträge.
 */
public interface MSetResult<E> extends Ordered<E, Boolean>, Modifiable<E, MSetResult<E>> {
    // Alle Methoden (before, setBefore, add, subtract)
    // werden von den Ober-Interfaces geerbt.
}