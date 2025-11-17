/**
 * Implementierung des von MSet before() zurückgegebenen Containers.
 * Sie implementiert die Logik für den "Zwischen"-Container, indem sie
 * selbst ein AbstractOrdSet ist.
 */
class MSetResultImpl<E> extends AbstractOrdSet<E, Boolean> implements MSetResult<E> {

    /**
     * Konstruktor.
     * @param c Das Prüfobjekt, das vom übergeordneten Set geerbt wird.
     */
    public MSetResultImpl(Ordered<? super E, ?> c) {
        super(c);
    }

    /**
     * Prüft, ob x vor y steht, und gibt Boolean.TRUE oder null zurück.
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @return TRUE wenn x vor y ist, sonst null.
     */
    @Override
    public Boolean before(E x, E y) {
        // Nutzt die geerbte isBefore-Logik von AbstractOrdSet
        if (this.isBefore(x, y)) {
            return Boolean.TRUE;
        }
        return null;
    }

    /**
     * Stellt eine Ordnungsbeziehung her.
     * Die Logik ist identisch zu ISet.setBefore, basierend auf der Angabe
     * @param x Das erste Element.
     * @param y Das zweite Element.
     */
    @Override
    public void setBefore(E x, E y) {
        if (!containsElement(x) || !containsElement(y)) {
            throw new IllegalArgumentException("In MSetResult müssen beide Elemente bereits vorhanden sein.");
        }

        if (x == y) {
            throw new IllegalArgumentException("Elemente x und y dürfen nicht identisch sein.");
        }

        if (this.c != null && this.c.before(x, y) == null) {
            throw new IllegalArgumentException("Ordnungsbeziehung ist durch 'c' nicht erlaubt.");
        }

        // prüft auf Zyklen (this.before(y, x))
        if (this.isBefore(y, x)) {
            throw new IllegalArgumentException("Ordnungsbeziehung würde einen Zyklus erstellen.");
        }

        addElementIfNeeded(x);
        addElementIfNeeded(y);
        addRelationIfNeeded(x, y);
    }

    /**
     * Hilfsmethode, um zu prüfen, ob ein Element im Container ist.
     * Wird von MSet.before() benötigt.
     * @param e Das zu prüfende Element.
     * @return true, wenn das Element vorhanden ist, sonst false.
     */
    protected boolean containsElement(E e) {
        for (ElementNode current = elementHead; current != null; current = current.next) {
            if (current.element == e) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt ein neues MSetResult zurück, das dieses um 'e' erweitert.
     * Erstellt eine Kopie dieses Containers und fügt 'e' hinzu.
     * @param e Das hinzuzufügende Element.
     * @return Ein neuer MSetResult-Container
     */
    @Override
    public MSetResult<E> add(E e) {
        MSetResultImpl<E> newSet = new MSetResultImpl<>(this.c);

        // Kopiert alle Elemente
        for (ElementNode n = this.elementHead; n != null; n = n.next) {
            newSet.addElementIfNeeded(n.element);
        }

        // Kopiert alle Relationen
        for (RelationNode r = this.relationHead; r != null; r = r.next) {
            newSet.addRelationIfNeeded(r.from, r.to);
        }

        // Fügt das neue Element hinzu (falls noch nicht vorhanden)
        newSet.addElementIfNeeded(e);

        return newSet;
    }

    /**
     * Gibt ein neues MSetResult zurück, aus dem 'e' entfernt wurde.
     * Erstellt eine Kopie und entfernt 'e' sowie alle Relationen mit 'e'.
     * @param e Das zu entfernende Element.
     * @return Ein neuer MSetResult-Container
     */
    @Override
    public MSetResult<E> subtract(E e) {
        MSetResultImpl<E> newSet = new MSetResultImpl<>(this.c);

        // Kopiert alle Elemente AUSSER 'e'
        for (ElementNode n = this.elementHead; n != null; n = n.next) {
            if (n.element != e) {
                newSet.addElementIfNeeded(n.element);
            }
        }

        // Kopiert alle Relationen, die 'e' NICHT enthalten
        for (RelationNode r = this.relationHead; r != null; r = r.next) {
            if (r.from != e && r.to != e) {
                newSet.addRelationIfNeeded(r.from, r.to);
            }
        }

        return newSet;
    }
}