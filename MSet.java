/**
 * MSet erweitert AbstractOrdSet und verwaltet Elemente, die das Interface Modifiable implementieren.
 * Die Klasse definiert die generischen Parameter für das OrdSet-Verhalten so, dass die
 * 'before'-Methode ein MSetResult zurückgibt.
 *
 * @param <E> Der Typ der Einträge, muss Modifiable<X, E> sein.
 * @param <X> Der Typ des Parameters für die Modifikationen (add/subtract).
 */
public class MSet<E extends Modifiable<X, E>, X> extends AbstractOrdSet<E, MSetResult<E>> {

    /**
     * Konstruktor, der das Prüfobjekt c setzt.
     *
     * @param c Das Objekt zur Prüfung (kann null sein).
     */
    public MSet(Ordered<? super E, ?> c) {
        super(c);
    }

    /**
     * Gibt einen Container (MSetResult) zurück, der alle Elemente 'z' enthält,
     * für die (x -> z) und (z -> y) gilt.
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @return Ein MSetResult-Container oder null.
     */
    @Override
    public MSetResult<E> before(E x, E y) {
        if (!isBefore(x, y)) {
            return null;
        }

        // Erstellt den Ergebnis-Container.
        MSetResultImpl<E> resultSet = new MSetResultImpl<>(this.c);

        // Alle Elemente 'z' finden, die strikt zwischen x und y liegen
        // und zum resultSet hinzufügen.
        for (ElementNode current = this.elementHead; current != null; current = current.next) {
            E z = current.element;
            // z muss zwischen x und y liegen
            if (z != x && z != y && isBefore(x, z) && isBefore(z, y)) {
                resultSet.addElementIfNeeded(z);
            }
        }

        // Alle Relationen zwischen den Elementen im resultSet kopieren,
        // damit die Ordnung erhalten bleibt.
        for (RelationNode r = this.relationHead; r != null; r = r.next) {
            // Prüft, ob BEIDE Enden der Relation im neuen Set enthalten sind.
            if (resultSet.containsElement(r.from) && resultSet.containsElement(r.to)) {
                resultSet.addRelationIfNeeded(r.from, r.to);
            }
        }

        return resultSet;
    }

    /**
     * Stellt eine Ordnungsbeziehung her.
     * Die Logik ist identisch zu ISet.setBefore, basierend auf der Angabe
     * @param x Das erste Element.
     * @param y Das zweite Element.
     */
    @Override
    public void setBefore(E x, E y) {
        if (x == y) {
            throw new IllegalArgumentException("Elemente x und y dürfen nicht identisch sein.");
        }

        if (this.c != null && this.c.before(x, y) == null) {
            throw new IllegalArgumentException("Ordnungsbeziehung ist durch 'c' nicht erlaubt.");
        }

        //prüft auf Zyklen (this.before(y, x))
        if (this.isBefore(y, x)) {
            throw new IllegalArgumentException("Ordnungsbeziehung würde einen Zyklus erstellen.");
        }

        addElementIfNeeded(x);
        addElementIfNeeded(y);
        addRelationIfNeeded(x, y);
    }

    /**
     * Führt für jedes Element 'e' die Operation setBefore(e.add(x), e) aus.
     * @param x Der Wert für die 'add'-Operation.
     */
    public void plus(X x) {
        // Wir iterieren über eine Momentaufnahme der Elementliste,
        // da setBefore die Liste (elementHead) potenziell ändert.
        ElementNode snapshot = this.elementHead;

        for (ElementNode current = snapshot; current != null; current = current.next) {
            E e = current.element;
            // e.add(x) gibt ein NEUES Objekt zurück
            E e_neu = e.add(x);
            // setBefore fügt e_neu hinzu (falls nötig) und setzt die Relation
            this.setBefore(e_neu, e);
        }
    }

    /**
     * Führt für jedes Element 'e' die Operation setBefore(e.subtract(x), e) aus.
     * @param x Der Wert für die 'subtract'-Operation.
     */
    public void minus(X x) {
        // Wir iterieren über eine Momentaufnahme der Elementliste,
        // da setBefore die Liste (elementHead) potenziell ändert.
        ElementNode snapshot = this.elementHead;

        for (ElementNode current = snapshot; current != null; current = current.next) {
            E e = current.element;
            // e.subtract(x) gibt ein NEUES Objekt zurück
            E e_neu = e.subtract(x);
            // setBefore fügt e_neu hinzu (falls nötig) und setzt die Relation
            this.setBefore(e_neu, e);
        }
    }

    /**
     * Gibt eine sinnvolle textuelle Darstellung des Sets zurück
     * @return Ein String, der Elemente und Relationen auflistet.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MSet:\n");
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