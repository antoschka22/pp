/**
 * OSet implementiert OrdSet.
 * Erbt die meiste Logik von AbstractOrdSet.
 * Implementiert 'before' durch Rückgabe eines 'SubSet'-Objekts.
 *
 * @param <E> Der Typ der Einträge.
 */
public class OSet<E> extends AbstractOrdSet<E, OSetResult<E>> {


    public OSet(Ordered<? super E, ?> c) {
        super(c);
    }

    @Override
    public OSetResult<E> before(E x, E y) {
        if (!isBefore(x, y)) {
            return null;
        }
        // Alle Elemente, die strikt zwischen x und y liegen, werden gesammelt.
        ElementNode newSubHead = null;
        for (ElementNode current = this.elementHead; current != null; current = current.next) {
            E z = current.element;
            if (z != x && z != y && isBefore(x, z) && isBefore(z, y)) {
                newSubHead = new ElementNode(z, newSubHead);
            }
        }

        // Relationen von allen Elementen sammeln, die strikt zwischen x und y liegen
        RelationNode newSubRelationHead = null;
        for (RelationNode current = this.relationHead; current != null; current = current.next) {
            E from = current.from;
            E to = current.to;

            if (listContains(newSubHead, from) && listContains(newSubHead, to)) {
                newSubRelationHead = new RelationNode(from, to, newSubRelationHead);
            }

        }
        return new SubSet(newSubHead, newSubRelationHead);

    }

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

    private boolean listContains(ElementNode head, E element) {
        for (ElementNode current = head; current != null; current = current.next) {
            if (current.element == element) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("OSet:\n");
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

    private class SubSet implements OSetResult<E> {

        private ElementNode elementHead;
        private RelationNode relationHead;

        public SubSet(ElementNode subElements, RelationNode subRelations) {
            this.elementHead = subElements;
            this.relationHead = subRelations;
        }

        @Override
        public OSetResult<E> add(E e) {
            if (listContains(this.elementHead, e)) return this;
            // Listen kopieren
            ElementNode newElementHead = copyElements(this.elementHead);
            RelationNode newRelationHead = copyRelations(this.relationHead);

            // E e in kopierte Liste einfügen
            newElementHead = new ElementNode(e, newElementHead);

            return new SubSet(newElementHead, newRelationHead);
        }

        @Override
        public OSetResult<E> subtract(E e) {
            // e ist nicht im Container
            if (!listContains(this.elementHead, e)) return this;
            // e wird aus beiden Listen entfernt
            ElementNode newElementHead = copyElementsWithout(this.elementHead, e);
            RelationNode newRelationHead = copyRelationsWithout(this.relationHead, e);
            return new SubSet(newElementHead, newRelationHead);
        }

        @Override
        public Boolean before(E x, E y) {
            if (OSet.this.isBefore(x, y)) {
                return true;
            } else {
                return null;
            }
        }

        @Override
        public void setBefore(E x, E y) {
            if (!listContains(elementHead, x) || !listContains(elementHead, y)) {
                throw new IllegalArgumentException("x oder y sind nicht im Subset enthalten");
            }
            if (this.before(y, x) != null) {
                throw new IllegalArgumentException("y ist vor x, kein Zyklus erlaubt");
            }
            // Relation wird in OSet eingefügt
            OSet.this.setBefore(x, y);
            // Relation wird in SubSet eingefügt
            if (!containRelation(x, y)) this.relationHead = new RelationNode(x, y, this.relationHead);
        }
        private boolean containRelation(E x, E y){
            for (RelationNode current = this.relationHead; current != null; current = current.next){
                if(current.from == x && current.to == y){
                    return true;
                }
            }
            return false;
        }

        private ElementNode copyElements(ElementNode current) {
            //Basisfall
            if (current == null) return null;
            ElementNode copy = new ElementNode(current.element, null);
            copy.next = copyElements(current.next);
            return copy;
        }

        private RelationNode copyRelations(RelationNode current) {
            //Basisfall
            if (current == null) return null;
            RelationNode copy = new RelationNode(current.from, current.to, null);
            copy.next = copyRelations(current.next);
            return copy;
        }

        private ElementNode copyElementsWithout(ElementNode current, E subtractElement) {
            //Basisfall
            if (current == null) return null;
            if (current.element == subtractElement) {
                return copyElementsWithout(current.next, subtractElement);
            } else {
                ElementNode copy = new ElementNode(current.element, null);
                copy.next = copyElementsWithout(current.next, subtractElement);
                return copy;
            }
        }

        private RelationNode copyRelationsWithout(RelationNode current, E subtractElement){
            //Basisfall
            if (current == null) return null;
            // Element überspringen
            if(current.from == subtractElement || current.to == subtractElement){
                return copyRelationsWithout(current.next, subtractElement);
            } else {
                RelationNode copy = new RelationNode(current.from, current.to, null);
                copy.next = copyRelationsWithout(current.next, subtractElement);
                return copy;
            }
        }
    }
}