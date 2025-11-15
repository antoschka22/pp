import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * ISet implementiert OrdSet. Erbt die meiste Logik von AbstractOrdSet.
 * Die 'before'-Methode gibt einen Iterator zurück.
 *
 * @param <E> Der Typ der Einträge.
 */
public class ISet<E> extends AbstractOrdSet<E, Iterator<E>> {


    public ISet(Ordered<? super E, ?> c) {
        super(c);
    }

    @Override
    public Iterator<E> before(E x, E y) {
        // x ist nicht vor y
        if(!isBefore(x, y)){
            return null;
        }
        ElementNode resultsHead = null;

        for(ElementNode current = this.elementHead; current != null; current = current.next){
            E z = current.element;
            // z muss strikt zwischen x und y liegen
            if(z != x && z != y && isBefore(x, z) && isBefore(z, y)){
                resultsHead = new ElementNode(z, resultsHead);
            }
        }
        return new OrdSetIterator(resultsHead);
    }

    @Override
    public void setBefore(E x, E y) {
        // Hier werden die 3 Regeln aus der Angabe geprüft.

        // x und y identisch?
        if (x == y) {
            throw new IllegalArgumentException("Elemente x und y dürfen nicht identisch sein.");
        }

        // 'c'-Bedingung
        if (this.c != null && this.c.before(x, y) == null) {
            throw new IllegalArgumentException("Ordnungsbeziehung ist durch 'c' nicht erlaubt.");
        }

        // prüft auf this.before(y,x)
        if (this.before(y, x) != null) {
            throw new IllegalArgumentException("Ordnungsbeziehung würde einen Zyklus erstellen.");
        }
        addElementIfNeeded(x);
        addElementIfNeeded(y);
        addRelationIfNeeded(x, y);
    }
}