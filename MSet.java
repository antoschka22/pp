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
     * @return Ein MSetResult-Container oder null, wenn x nicht vor y steht.
     */
    @Override
    public MSetResult<E> before(E x, E y) {
        if (!isBefore(x, y)) {
            return null;
        }

        // Erstellt den Ergebnis-Container als Instanz der inneren Klasse.
        // Wir übergeben 'this.c', damit das Resultat dieselben Prüfungen nutzt.
        ResultImpl resultSet = new ResultImpl(this.c);

        // 1. Elemente filtern: Alle 'z' finden, die strikt zwischen x und y liegen
        for (ElementNode current = this.elementHead; current != null; current = current.next) {
            E z = current.element;
            if (z != x && z != y && isBefore(x, z) && isBefore(z, y)) {
                resultSet.addElementIfNeeded(z);
            }
        }

        // 2. Relationen kopieren: Nur wenn BEIDE Partner im Resultat sind
        for (RelationNode r = this.relationHead; r != null; r = r.next) {
            if (resultSet.containsElement(r.from) && resultSet.containsElement(r.to)) {
                resultSet.addRelationIfNeeded(r.from, r.to);
            }
        }

        return resultSet;
    }

    /**
     * Stellt eine Ordnungsbeziehung her.
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

        // prüft auf Zyklen (existiert bereits y -> x?)
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
        ElementNode snapshot = this.elementHead;
        for (ElementNode current = snapshot; current != null; current = current.next) {
            E e = current.element;
            E e_neu = e.add(x);
            this.setBefore(e_neu, e);
        }
    }

    /**
     * Führt für jedes Element 'e' die Operation setBefore(e.subtract(x), e) aus.
     * @param x Der Wert für die 'subtract'-Operation.
     */
    public void minus(X x) {
        ElementNode snapshot = this.elementHead;
        for (ElementNode current = snapshot; current != null; current = current.next) {
            E e = current.element;
            E e_neu = e.subtract(x);
            this.setBefore(e_neu, e);
        }
    }

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

    // =========================================================================
    // Innere Klasse für das Ergebnis von before()
    // =========================================================================

    private class ResultImpl extends AbstractOrdSet<E, Boolean> implements MSetResult<E> {

        public ResultImpl(Ordered<? super E, ?> c) {
            super(c);
        }

        @Override
        public Boolean before(E x, E y) {
            // Nutzt die geerbte isBefore-Logik von AbstractOrdSet (auf den lokalen Daten)
            if (this.isBefore(x, y)) {
                return Boolean.TRUE;
            }
            return null;
        }

        @Override
        public void setBefore(E x, E y) {
            if (x == y) {
                throw new IllegalArgumentException("Elemente x und y dürfen nicht identisch sein.");
            }

            // Zyklusprüfung im lokalen Subset
            if (this.isBefore(y, x)) {
                throw new IllegalArgumentException("Ordnungsbeziehung würde einen Zyklus erstellen.");
            }

            // Wenn dies fehlschlägt (z.B. Zyklus im Eltern-Set), wird dort eine Exception geworfen
            // und hier abgebrochen, bevor wir lokal etwas ändern.
            MSet.this.setBefore(x, y);

            // Wenn Eltern-Set akzeptiert hat, Relation auch lokal eintragen
            addRelationIfNeeded(x, y);
        }

        @Override
        public MSetResult<E> add(E e) {
            // Erstellt eine Kopie dieses Resultats
            ResultImpl newSet = new ResultImpl(this.c);
            copyContentTo(newSet);

            // Fügt das neue Element hinzu
            newSet.addElementIfNeeded(e);
            return newSet;
        }

        @Override
        public MSetResult<E> subtract(E e) {
            ResultImpl newSet = new ResultImpl(this.c);

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

        // Hilfsmethode zum Kopieren des Inhalts in ein neues ResultImpl
        private void copyContentTo(ResultImpl target) {
            for (ElementNode n = this.elementHead; n != null; n = n.next) {
                target.addElementIfNeeded(n.element);
            }
            for (RelationNode r = this.relationHead; r != null; r = r.next) {
                target.addRelationIfNeeded(r.from, r.to);
            }
        }

        // Hilfsmethode für contains
        protected boolean containsElement(E e) {
            for (ElementNode current = elementHead; current != null; current = current.next) {
                if (current.element == e) return true;
            }
            return false;
        }
    }
}