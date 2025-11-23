/**
 * OSet implementiert OrdSet.
 * Erbt die meiste Logik von AbstractOrdSet.
 * Implementiert 'before' durch Rückgabe eines 'SubSet'-Objekts.
 *
 * @param <E> Der Typ der Einträge.
 */
public class OSet<E> extends AbstractOrdSet<E, OSetResult<E>> {

    /**
     * Erstellt ein neues OSet und setzt das Prüfobjekt für erlaubte Ordnungsbeziehungen.
     * @param c Das Objekt zur Prüfung erlaubter Ordnungsbeziehungen (kann null sein).
     * @post Der Container ist leer.
     */
    public OSet(Ordered<? super E, ?> c) {
        super(c);
    }

    /**
     * Gibt einen Container (SubSet) zurück, der alle Elemente enthält, die strikt zwischen x und y stehen.
     *
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @return Ein Objekt vom Typ OSetResult<E>, wenn x vor y kommt, sonst null. Dieses Objekt vereint Ordered und Modifiable.
     * @post x und y bleiben unverändert. Das Ergebnis ist eine Teilmenge des OSet, die die Ordnung des OSet beibehält.
     */
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

    /**
     * Stellt eine Ordnungsbeziehung zwischen x und y her, falls dies möglich ist.
     *
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @pre x und y dürfen nicht identisch sein.
     * @pre Die Ordnungsbeziehung muss durch this.c erlaubt sein (falls c != null).
     * @pre Es darf keine Ordnungsbeziehung y vor x existieren (Zyklen sind verboten).
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
     * Prüft, ob ein Element in der gegebenen Liste enthalten ist.
     *
     * @param head Kopf der zu durchsuchenden Elementliste.
     * @param element Das gesuchte Element.
     * @return true, wenn das Element gefunden wurde, sonst false.
     * @pre Die Liste ist eine verkettete Liste von ElementNode-Objekten.
     */
    private boolean listContains(ElementNode head, E element) {
        for (ElementNode current = head; current != null; current = current.next) {
            if (current.element == element) {
                return true;
            }
        }
        return false;
    }

    /**
     * Erzeugt eine String-Repräsentation des OSet, die alle Elemente und ihre direkten Ordnungsbeziehungen enthält.
     *
     * @return Eine beschreibende Zeichenkette des Containers.
     */
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

    /**
     * Private Implementierung der Teilmenge (Subset), die die Interfaces Ordered und Modifiable implementiert.
     * Dieses Objekt wird von before() zurückgegeben und repräsentiert eine gefilterte Sicht auf das OSet.
     */
    private class SubSet implements OSetResult<E> {

        private ElementNode elementHead;
        private RelationNode relationHead;

        /**
         * Erstellt ein neues SubSet aus den übergebenen Element- und Relationslisten.
         * @param subElements Die Liste der Elemente im Subset.
         * @param subRelations Die Liste der Relationen im Subset.
         */
        public SubSet(ElementNode subElements, RelationNode subRelations) {
            this.elementHead = subElements;
            this.relationHead = subRelations;
        }

        /**
         * Gibt ein neues SubSet zurück, das um das Element e erweitert ist, falls e noch nicht enthalten ist.
         * Das SubSet selbst und der Parameter e bleiben unverändert.
         * @param e Das hinzuzufügende Element.
         * @return Ein neues SubSet-Objekt mit erweitertem Inhalt oder this, wenn e bereits enthalten war.
         * @post this bleibt unverändert.
         */
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

        /**
         * Gibt ein neues SubSet zurück, aus dem das Element e entfernt wurde, falls es enthalten war.
         * Das SubSet selbst und der Parameter e bleiben unverändert.
         * @param e Das zu entfernende Element.
         * @return Ein neues SubSet-Objekt mit reduziertem Inhalt (inklusive Entfernung aller Relationen von/zu e) oder this.
         * @post this bleibt unverändert.
         */
        @Override
        public OSetResult<E> subtract(E e) {
            // e ist nicht im Container
            if (!listContains(this.elementHead, e)) return this;
            // e wird aus beiden Listen entfernt
            ElementNode newElementHead = copyElementsWithout(this.elementHead, e);
            RelationNode newRelationHead = copyRelationsWithout(this.relationHead, e);
            return new SubSet(newElementHead, newRelationHead);
        }

        /**
         * Prüft, ob x in der Ordnung vor y kommt. Delegiert die Prüfung an den übergeordneten OSet-Container.
         * @param x Das erste Element.
         * @param y Das zweite Element.
         * @return true, wenn x vor y im OSet kommt, sonst null.
         * @post this, x und y bleiben unverändert.
         */
        @Override
        public Boolean before(E x, E y) {
            if (OSet.this.isBefore(x, y)) {
                return true;
            } else {
                return null;
            }
        }

        /**
         * Stellt eine Ordnungsbeziehung her. Die Änderung wird sowohl im SubSet als auch im übergeordneten OSet vorgenommen.
         * @param x Das erste Element.
         * @param y Das zweite Element.
         * @pre x und y müssen bereits im SubSet enthalten sein.
         * @pre Die Beziehung darf keinen Zyklus innerhalb des OSet erstellen.
         * @post Die Relation x -> y ist im OSet und im SubSet vorhanden.
         * @throws IllegalArgumentException Wenn x oder y nicht im SubSet sind oder ein Zyklus entsteht.
         */
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

        /**
         * Prüft, ob eine direkte Relation (x -> y) lokal im SubSet vorhanden ist.
         * @pre Die Relationen sind eine verkettete Liste von RelationNode-Objekten.
         */
        private boolean containRelation(E x, E y){
            for (RelationNode current = this.relationHead; current != null; current = current.next){
                if(current.from == x && current.to == y){
                    return true;
                }
            }
            return false;
        }

        /**
         * Kopiert rekursiv die Elementliste des Subsets.
         */
        private ElementNode copyElements(ElementNode current) {
            //Basisfall
            if (current == null) return null;
            ElementNode copy = new ElementNode(current.element, null);
            copy.next = copyElements(current.next);
            return copy;
        }

        /**
         * Kopiert rekursiv die Relationsliste des Subsets.
         */
        private RelationNode copyRelations(RelationNode current) {
            //Basisfall
            if (current == null) return null;
            RelationNode copy = new RelationNode(current.from, current.to, null);
            copy.next = copyRelations(current.next);
            return copy;
        }

        /**
         * Kopiert rekursiv die Elementliste, wobei ein bestimmtes Element ausgelassen wird.
         */
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

        /**
         * Kopiert rekursiv die Relationsliste, wobei alle Relationen, die das zu entfernende Element betreffen, ausgelassen werden.
         */
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