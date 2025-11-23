import java.util.Iterator;

/**
 * ISet implementiert OrdSet. Erbt die meiste Logik von AbstractOrdSet.
 * Die 'before'-Methode gibt einen Iterator zurück.
 *
 * @param <E> Der Typ der Einträge.
 */
public class ISet<E> extends AbstractOrdSet<E, Iterator<E>> {

    /**
     * Erstellt ein neues ISet und setzt das Prüfobjekt für erlaubte Ordnungsbeziehungen.
     * @param c Das Objekt zur Prüfung erlaubter Ordnungsbeziehungen (kann null sein).
     * @post Der Container ist leer.
     */
    public ISet(Ordered<? super E, ?> c) {
        super(c);
    }

    /**
     * Gibt einen Iterator zurück, der über alle Elemente iteriert, die in der Ordnung nach x und vor y stehen.
     * Die Reihenfolge der zurückgegebenen Elemente ist nicht festgelegt.
     *
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @return Ein Iterator über alle Elemente z, für die gilt: x vor z und z vor y; andernfalls null.
     * @post x und y bleiben unverändert. Der zurückgegebene Iterator erlaubt keine remove-Operation.
     */
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

    /**
     * Versucht, eine Ordnungsbeziehung herzustellen, sodass x in der Ordnung vor y steht.
     *
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @pre x und y dürfen nicht identisch sein.
     * @pre Die Ordnungsbeziehung muss durch this.c erlaubt sein (falls c != null).
     * @pre Es darf keine Ordnungsbeziehung y vor x existieren.
     * @post Wenn keine Ausnahme ausgelöst wird, sind x und y im Container enthalten und x steht in der Ordnung vor y.
     * @throws IllegalArgumentException Wenn eine der Vorbedingungen verletzt ist.
     */
    @Override
    public void setBefore(E x, E y) {
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

    /**
     * Erzeugt einen String des ISet, der alle Elemente und ihre Ordnungsbeziehungen enthält.
     *
     * @return Eine beschreibende Zeichenkette des Containers.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ISet:\n");
        sb.append("  Elements: { ");
        for (ElementNode n = elementHead; n != null; n = n.next) {
            sb.append(n.element.toString());
            if (n.next != null) sb.append(", ");
        }
        sb.append(" }\n");

        sb.append("  Relations: { ");
        for (RelationNode r = relationHead; r != null; r = r.next) {
            sb.append(r.from.toString()).append(" -> ").append(r.to.toString());
            if (r.next != null) sb.append(", ");
        }
        sb.append(" }\n");
        return sb.toString();
    }
}