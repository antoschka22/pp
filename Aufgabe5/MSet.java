/**
 * MSet ist ein Container, der OSet erweitert und Elemente verwaltet, die selbst modifizierbar sind
 * Es realisiert Generizität über zwei Ebenen (E und X)
 *
 * @param <E> Der Typ der Einträge. Muss Modifiable<X, E> implementieren
 * @param <X> Der Typ des Parameters für die Modifikationen (add/subtract) der Elemente
 *
 * @invariant Alle Ordnungsbeziehungen sind zyklenfrei
 * @invariant Alle hinzugefügten Elemente entsprechen den Typschranken
 */
public class MSet<E extends Modifiable<X, E>, X> extends OSet<E> {

    /**
     * Erstellt ein neues MSet und setzt das Prüfobjekt für erlaubte Ordnungsbeziehungen
     *
     * @param c Das Objekt zur Prüfung erlaubter Ordnungsbeziehungen (kann null sein)
     * @post Der Container ist leer, size() == 0.
     */
    public MSet(Ordered<? super E, ?> c) {
        super(c);
    }

    /**
     * Gibt einen Container (MSetResult) zurück, der alle Elemente enthält, die in der Ordnung
     * strikt nach x und vor y stehen
     *
     * @param x Das erste Element (Vorgänger)
     * @param y Das zweite Element (Nachfolger)
     * @return Ein MSetResult-Objekt mit den Zwischenelementen, oder null, wenn x nicht vor y steht
     * @post x und y bleiben unverändert
     * @post Das zurückgegebene Resultat ist eine unabhängige Sicht oder Kopie der relevanten Teilmenge
     */
    @Override
    public MSetResult<E> before(E x, E y) {
        // Prüfung, ob x überhaupt vor y steht (geerbt von AbstractOrdSet/OSet)
        if (!isBefore(x, y)) {
            return null;
        }

        // Erstellt den Ergebnis-Container als Instanz der inneren Klasse.
        ResultImpl resultSet = new ResultImpl(this.c);

        // 1. Elemente filtern: Alle 'z' finden, für die gilt: x < z < y
        for (ElementNode current = this.elementHead; current != null; current = current.next) {
            E z = current.element;
            // Identitätsvergleich, da equals nicht überschrieben werden soll
            if (z != x && z != y && isBefore(x, z) && isBefore(z, y)) {
                resultSet.addElementIfNeeded(z);
            }
        }

        // 2. Relationen kopieren: Nur Relationen übernehmen, bei denen beide Partner im Resultat sind
        for (RelationNode r = this.relationHead; r != null; r = r.next) {
            if (resultSet.containsElement(r.from) && resultSet.containsElement(r.to)) {
                resultSet.addRelationIfNeeded(r.from, r.to);
            }
        }

        return resultSet;
    }

    /**
     * Wendet die 'add'-Operation auf alle Elemente des Containers an und ordnet die Ergebnisse neu ein
     * Führt für jedes Element 'e' die Operation this.setBefore(e.add(x), e) aus
     *
     * @param x Der Wert, der den Elementen hinzugefügt wird
     * @post Für jedes Element e im Container wurde e.add(x) berechnet
     * @post Das Ergebnis von e.add(x) wurde mittels setBefore vor e in den Container eingefügt (sofern nicht identisch
     */
    public void plus(X x) {
        // Wir müssen aufpassen, dass wir die neu eingefügten Elemente nicht sofort wieder bearbeiten,
        // falls sie hinten angehängt würden. Da addElementIfNeeded (in AbstractOrdSet) vorne anfügt,
        // ist die Iteration über die bestehende Verkettung sicher, solange wir 'next' vor der Änderung speichern
        ElementNode snapshot = this.elementHead;
        while (snapshot != null) {
            E e = snapshot.element;
            ElementNode nextNode = snapshot.next; // Pointer retten, bevor Struktur sich ändert

            E e_neu = e.add(x);
            // Nur einfügen, wenn e.add(x) ein neues/anderes Objekt zurückgibt
            if (e_neu != e) {
                this.setBefore(e_neu, e);
            }

            snapshot = nextNode;
        }
    }

    /**
     * Wendet die 'subtract'-Operation auf alle Elemente des Containers an und ordnet die Ergebnisse neu ein
     * Führt für jedes Element 'e' die Operation this.setBefore(e.subtract(x), e) aus
     *
     * @param x Der Wert, der von den Elementen subtrahiert wird
     * @post Für jedes Element e im Container wurde e.subtract(x) berechnet
     * @post Das Ergebnis von e.subtract(x) wurde mittels setBefore vor e in den Container eingefügt
     */
    public void minus(X x) {
        ElementNode snapshot = this.elementHead;
        while (snapshot != null) {
            E e = snapshot.element;
            ElementNode nextNode = snapshot.next;

            E e_neu = e.subtract(x);
            if (e_neu != e) {
                this.setBefore(e_neu, e);
            }

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

    /**
     * Private Implementierung von MSetResult
     * Diese Klasse ist private, um die Implementierungsdetails zu verbergen (Sichtbrkeit!)
     * Sie erbt von AbstractOrdSet, um die Container-Funktionalität wiederzuverwenden
     */
    private class ResultImpl extends AbstractOrdSet<E, Boolean> implements MSetResult<E> {

        /**
         * Erstellt ein neues ResultImpl mit dem gegebenen Checker
         * @param c Der Checker für Ordnungsbeziehungen
         */
        public ResultImpl(Ordered<? super E, ?> c) {
            super(c);
        }

        /**
         * Prüft, ob x vor y steht
         *
         * @return Boolean.TRUE, wenn x vor y steht, sonst null (entsprechend der Angabe für OSetResult)
         */
        @Override
        public Boolean before(E x, E y) {
            if (this.isBefore(x, y)) {
                return Boolean.TRUE;
            }
            return null;
        }

        /**
         * Fügt eine Ordnungsbeziehung im Resultat-Set UND im umschließenden MSet hinzu
         *
         * @pre x und y müssen bereits im Container sein
         * @throws IllegalArgumentException bei Zyklen oder Identität
         */
        @Override
        public void setBefore(E x, E y) {
            if (x == y) {
                throw new IllegalArgumentException("Elemente x und y dürfen nicht identisch sein.");
            }

            // Prüfung auf lokalen Zyklus im Resultat
            if (this.isBefore(y, x)) {
                throw new IllegalArgumentException("Ordnungsbeziehung würde einen Zyklus erstellen.");
            }

            // Delegation: Die Änderung muss auch im Haupt-Container (MSet.this) wirksam werden.
            // OSet-Logik verlangt Konsistenz.
            MSet.this.setBefore(x, y);

            // Lokal speichern
            addRelationIfNeeded(x, y);
        }

        /**
         * Gibt eine Kopie dieses Resultats zurück, erweitert um e
         *
         * @param e Das hinzuzufügende Elemen.
         * @return Ein neues ResultImpl-Objekt
         * @post this bleibt unverändert (Funktionale Art)
         */
        @Override
        public MSetResult<E> add(E e) {
            ResultImpl newSet = new ResultImpl(this.c);
            copyContentTo(newSet);
            newSet.addElementIfNeeded(e);
            return newSet;
        }

        /**
         * Gibt eine Kopie dieses Resultats zurück, reduziert um e
         *
         * @param e Das zu entfernende Element
         * @return Ein neues ResultImpl-Objekt ohne e und dessen Relationen
         * @post this bleibt unverändert
         */
        @Override
        public MSetResult<E> subtract(E e) {
            ResultImpl newSet = new ResultImpl(this.c);
            // Elemente kopieren außer e
            for (ElementNode n = this.elementHead; n != null; n = n.next) {
                if (n.element != e) {
                    newSet.addElementIfNeeded(n.element);
                }
            }
            // Relationen kopieren, sofern sie e nicht betreffen
            for (RelationNode r = this.relationHead; r != null; r = r.next) {
                if (r.from != e && r.to != e) {
                    newSet.addRelationIfNeeded(r.from, r.to);
                }
            }
            return newSet;
        }

        /**
         * Hilfsmethode zum Kopieren des Inhalts in ein neues ResultImpl
         */
        private void copyContentTo(ResultImpl target) {
            for (ElementNode n = this.elementHead; n != null; n = n.next) {
                target.addElementIfNeeded(n.element);
            }
            for (RelationNode r = this.relationHead; r != null; r = r.next) {
                target.addRelationIfNeeded(r.from, r.to);
            }
        }

        /**
         * Hilfsmethode: Prüft, ob ein Element in diesem Resultat enthalten ist
         */
        protected boolean containsElement(E e) {
            for (ElementNode current = elementHead; current != null; current = current.next) {
                if (current.element == e) return true;
            }
            return false;
        }
    }
}