/**
 * MSet erweitert OSet und verwaltet Elemente, die das Interface Modifiable implementieren.
 * Es erbt die grundlegende Ordnungslogik und setBefore von OSet.
 * Die 'before'-Methode wird überschrieben, um ein MSetResult zurückzugeben.
 *
 * @param <E> Der Typ der Einträge, muss Modifiable<X, E> sein.
 * @param <X> Der Typ des Parameters für die Modifikationen (add/subtract).
 */
public class MSet<E extends Modifiable<X, E>, X> extends OSet<E> {

    /**
     * Konstruktor, der das Prüfobjekt c setzt.
     * Ruft den Konstruktor der Oberklasse OSet auf.
     *
     * @param c Das Objekt zur Prüfung (kann null sein).
     */
    public MSet(Ordered<? super E, ?> c) {
        super(c);
    }

    /**
     * Gibt einen Container (MSetResult) zurück, der alle Elemente 'z' enthält,
     * für die (x -> z) und (z -> y) gilt.
     * Überschreibt die Methode aus OSet, um den spezifischeren Rückgabetyp zu liefern.
     *
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @return Ein MSetResult-Container oder null, wenn x nicht vor y steht.
     */
    @Override
    public MSetResult<E> before(E x, E y) {
        // Zugriff auf protected isBefore aus AbstractOrdSet
        if (!isBefore(x, y)) {
            return null;
        }

        // Erstellt den Ergebnis-Container als Instanz der inneren Klasse.
        ResultImpl resultSet = new ResultImpl(this.c);

        // 1. Elemente filtern: Alle 'z' finden, die strikt zwischen x und y liegen
        // Zugriff auf protected elementHead aus AbstractOrdSet
        for (ElementNode current = this.elementHead; current != null; current = current.next) {
            E z = current.element;
            if (z != x && z != y && isBefore(x, z) && isBefore(z, y)) {
                resultSet.addElementIfNeeded(z);
            }
        }

        // 2. Relationen kopieren: Nur wenn BEIDE Partner im Resultat sind
        // Zugriff auf protected relationHead aus AbstractOrdSet
        for (RelationNode r = this.relationHead; r != null; r = r.next) {
            if (resultSet.containsElement(r.from) && resultSet.containsElement(r.to)) {
                resultSet.addRelationIfNeeded(r.from, r.to);
            }
        }

        return resultSet;
    }

    // setBefore muss nicht mehr implementiert werden, da es von OSet geerbt wird.

    /**
     * Führt für jedes Element 'e' die Operation setBefore(e.add(x), e) aus.
     * @param x Der Wert für die 'add'-Operation.
     */
    public void plus(X x) {
        // Iteriert über eine Momentaufnahme oder direkt, da setBefore die Struktur ändern könnte.
        // Da wir über elementHead iterieren und setBefore Elemente anfügen könnte,
        // ist es sicherer, nur über die bestehenden Elemente zu iterieren.
        // Hier eine einfache Iteration wie im Originalcode:
        ElementNode snapshot = this.elementHead;
        while (snapshot != null) {
            E e = snapshot.element;
            // Wir merken uns next, falls setBefore die Liste verändert (was es tut, wenn e_neu neu ist)
            ElementNode nextNode = snapshot.next;

            E e_neu = e.add(x);
            this.setBefore(e_neu, e);

            snapshot = nextNode;
        }
    }

    /**
     * Führt für jedes Element 'e' die Operation setBefore(e.subtract(x), e) aus.
     * @param x Der Wert für die 'subtract'-Operation.
     */
    public void minus(X x) {
        ElementNode snapshot = this.elementHead;
        while (snapshot != null) {
            E e = snapshot.element;
            ElementNode nextNode = snapshot.next;

            E e_neu = e.subtract(x);
            this.setBefore(e_neu, e);

            snapshot = nextNode;
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

            if (this.isBefore(y, x)) {
                throw new IllegalArgumentException("Ordnungsbeziehung würde einen Zyklus erstellen.");
            }

            // Ruft setBefore auf dem umschließenden MSet auf (delegiert an OSet-Logik)
            MSet.this.setBefore(x, y);

            addRelationIfNeeded(x, y);
        }

        @Override
        public MSetResult<E> add(E e) {
            ResultImpl newSet = new ResultImpl(this.c);
            copyContentTo(newSet);
            newSet.addElementIfNeeded(e);
            return newSet;
        }

        @Override
        public MSetResult<E> subtract(E e) {
            ResultImpl newSet = new ResultImpl(this.c);
            for (ElementNode n = this.elementHead; n != null; n = n.next) {
                if (n.element != e) {
                    newSet.addElementIfNeeded(n.element);
                }
            }
            for (RelationNode r = this.relationHead; r != null; r = r.next) {
                if (r.from != e && r.to != e) {
                    newSet.addRelationIfNeeded(r.from, r.to);
                }
            }
            return newSet;
        }

        private void copyContentTo(ResultImpl target) {
            for (ElementNode n = this.elementHead; n != null; n = n.next) {
                target.addElementIfNeeded(n.element);
            }
            for (RelationNode r = this.relationHead; r != null; r = r.next) {
                target.addRelationIfNeeded(r.from, r.to);
            }
        }

        protected boolean containsElement(E e) {
            for (ElementNode current = elementHead; current != null; current = current.next) {
                if (current.element == e) return true;
            }
            return false;
        }
    }
}