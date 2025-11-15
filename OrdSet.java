import java.util.Iterator;

/**
 * Generisches Interface, das Iterable und Ordered erweitert. Es beschreibt einen Container für Objekte vom
 * Typ E, die in einer bestimmten Reihenfolge gespeichert werden. Bei dieser Reihenfolge handelt es sich um
 * eine partielle, zyklenfreie Ordnung.
 *
 * @param <E> Der Typ der im Container gespeicherten Elemente.
 * @param <R> Der Ergebnistyp der 'before'-Methode.
 */
public interface OrdSet<E, R> extends Iterable<E>, Ordered<E, R> {

    /**
     * Setzt ein neues Prüfobjekt c, falls es mit allen bestehenden Ordnungen kompatibel ist
     * @param c Das neue Prüfobjekt.
     * @throws IllegalArgumentException Wenn c nicht mit einer bestehenden Ordnungsbeziehung kompatibel ist.
     */
    void check(Ordered<? super E, ?> c);

    /**
     * Legt das neue Prüfobjekt zwingend fest und entfernt alle Ordnungsbeziehungen, die für das neue c nicht mehr
     * erlaubt sind.
     * @param c Das neue Prüfobjekt.
     */
    void checkForced(Ordered<? super E, ?> c);

    /**
     * Gibt einen Iterator zurück, der über alle Elemente des OrdSets läuft.
     * @return Der Rückgabewert ist ein Iterator, der alle gespeicherten Elemente durchläuft.
     */
    Iterator<E> iterator();

    /**
     * Gibt die Anzahl der im Container enthaltenen Objekte zurück.
     * @return Der Rückgabewert ist die Größe des OrdSets.
     */
    int size();
}
