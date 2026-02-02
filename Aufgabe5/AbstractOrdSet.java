import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Die abstrakte Basisklasse, die die gesamte gemeinsame Container-Logik implementiert.
 * Sie verwaltet die interne Datenstruktur (Elemente und Relationen) und
 * implementiert alle Methoden von OrdSet, außer der 'before'- und der 'setBefore'-Methode.
 *
 * @param <E> Der Element-Typ
 * @param <R> Der  Rückgabetyp der 'before'-Methode
 */
public abstract class AbstractOrdSet<E, R> implements OrdSet<E, R> {


    /**
     * Interner Knoten zur Speicherung eines Elements in einer verketteten Liste.
     * Dient dem iterator() und der Speicherung aller Elemente.
     */
    protected class ElementNode {
        E element;
        ElementNode next;

        ElementNode(E element, ElementNode next) {
            this.element = element;
            this.next = next;
        }
    }

    /**
     * Interner Knoten zur Speicherung einer Ordnungsbeziehung (x vor y).
     * Dient als Adjazenzliste für den Graphen.
     */
    protected class RelationNode {
        E from;
        E to;
        RelationNode next;

        RelationNode(E from, E to, RelationNode next) {
            this.from = from;
            this.to = to;
            this.next = next;
        }
    }

    protected ElementNode elementHead; // Kopf der Element-Liste
    protected RelationNode relationHead; // Kopf der Relations-Liste
    protected int elementCount; // Für size()
    protected Ordered<? super E, ?> c; // Das Prüfobjekt

    /**
     * Erstellt einen leeren Container und setzt das Prüfobjekt.
     * @param c Das Objekt zur Prüfung erlaubter Ordnungsbeziehungen (kann null sein).
     * @post this.elementCount == 0 und alle Listenköpfe sind null.
     */
    public AbstractOrdSet(Ordered<? super E, ?> c) {
        this.c = c;
        this.elementHead = null;
        this.relationHead = null;
        this.elementCount = 0;
    }

    @Override
    public int size() {
        return this.elementCount;
    }

    /**
     * Gibt den einen Iterator zurück, der über alle im Container enthaltenen Einträge läuft.
     * Die Reihenfolge ist dabei nicht festgelegt.
     * @return Ein Iterator über alle Elemente.
     * @post Der zurückgegebene Iterator erlaubt keine remove-Operation.
     */
    @Override
    public Iterator<E> iterator() {
        return new OrdSetIterator(this.elementHead);
    }


    /**
     * Setzt das neue Prüfobjekt `newC`, wenn alle bestehenden Ordnungsbeziehungen
     * mit diesem Objekt kompatibel sind.
     * @param newC Das neue Prüfobjekt (kann null sein).
     * @pre Alle bestehenden Ordnungsbeziehungen (RelationNode) müssen durch newC erlaubt sein.
     * @post Wenn keine Ausnahme ausgelöst wird, ist this.c == newC.
     * @throws IllegalArgumentException Wenn eine bestehende Relation durch newC nicht erlaubt ist.
     */
    @Override
    public void check(Ordered<? super E, ?> newC) {
        // Alle Relationen in 'relationHead' durchlaufen
        for (RelationNode current = relationHead; current != null; current = current.next) {
            if (newC != null && newC.before(current.from, current.to) == null) {
                // Prüfung schlägt fehl
                throw new IllegalArgumentException("Bestehende Relation (" + current.from + " -> " + current.to + ") verletzt das neue 'c'.");
            }
        }
        // Wenn alle Prüfungen erfolgreich:
        this.c = newC;
    }

    /**
     * Legt das neue Prüfobjekt zwingend fest und entfernt alle Ordnungsbeziehungen,
     * die für das neue `newC` nicht mehr erlaubt sind.
     * @param newC Das neue Prüfobjekt (kann null sein).
     * @post this.c == newC. Alle Relationen, für die newC.before() null zurückgibt, wurden entfernt.
     */
    @Override
    public void checkForced(Ordered<? super E, ?> newC) {
        this.c = newC;
        RelationNode prev = null;
        RelationNode current = this.relationHead;

        // Alle Relationen in 'relationHead' durchlaufen und ggf. entfernen
        while (current != null) {
            if (newC != null && newC.before(current.from, current.to) == null) {
                // Relation ist ungültig und muss entfernt werden
                if (prev == null) {
                    // Es ist der erste Knoten
                    this.relationHead = current.next;
                } else {
                    // Es ist ein Knoten mitten/am Ende
                    prev.next = current.next;
                }
                current = current.next; // Weiterrücken
            } else {
                // Relation ist gültig, normal weitermachen
                prev = current;
                current = current.next;
            }
        }
    }


    /**
     * Berechnet den Rückgabewert, der die Ordnungsbeziehung zwischen x und y beschreibt.
     *
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @return Ein Ergebnis vom Typ R, ungleich null wenn x vor y kommt, sonst null.
     * @post this, x und y bleiben unverändert.
     */
    @Override
    public abstract R before(E x, E y);

    /**
     * Stellt eine Ordnungsbeziehung zwischen x und y her, falls dies möglich ist.
     * @param x Das erste Element.
     * @param y Das zweite Element.
     * @pre Es darf kein Zyklus entstehen (y vor x ist nicht erlaubt). x und y dürfen nicht identisch sein.
     * Die Beziehung muss durch this.c erlaubt sein.
     * @post Wenn keine Ausnahme ausgelöst wird, sind x und y im Container enthalten und x steht in der Ordnung vor y.
     * @throws IllegalArgumentException Wenn die Vorbedingungen nicht erfüllt sind.
     */
    @Override
    public abstract void setBefore(E x, E y);

    /**
     * Fügt ein Element zur Liste hinzu, falls es noch nicht existiert (basierend auf Objektidentität).
     * @param e Das hinzuzufügende Element.
     * @post Wenn e nicht enthalten war, wird this.elementCount erhöht und e ist in der Liste enthalten.
     */
    protected void addElementIfNeeded(E e) {
        for (ElementNode current = elementHead; current != null; current = current.next) {
            if (current.element == e) {
                return; // Element bereits vorhanden
            }
        }
        // Element nicht gefunden, neu hinzufügen
        this.elementHead = new ElementNode(e, this.elementHead);
        this.elementCount++;
    }

    /**
     * Fügt eine gerichtete Relation (x vor y) hinzu, falls sie noch nicht existiert.
     * @param x Das Startelement (from).
     * @param y Das Zielelement (to).
     * @pre x und y sind bereits im Container enthalten.
     * @post Die Relation (x -> y) ist in relationHead enthalten.
     */
    protected void addRelationIfNeeded(E x, E y) {
        for (RelationNode current = relationHead; current != null; current = current.next) {
            if (current.from == x && current.to == y) {
                return; // Relation bereits vorhanden
            }
        }
        // Relation nicht gefunden, neu hinzufügen
        this.relationHead = new RelationNode(x, y, this.relationHead);
    }

    /**
     * Prüft, ob x in der Ordnung vor y steht (Pfad existiert im Graphen).
     *
     * @param x Der Startknoten (von)
     * @param y Der Zielknoten (zu)
     * @return true, wenn ein Pfad von x nach y existiert, sonst false.
     * @pre Die Überprüfung basiert auf der Objektidentität von x und y.
     */
    protected boolean isBefore(E x, E y) {
        // Jede neue Suche startet mit einem neuen Set von besuchten Knoten.
        VisitedSet visited = new VisitedSet();

        // Starte die rekursive Suche
        return dfsRecursive(x, y, visited);
    }

    /**
     * Ein minimales Set (nur 'add' und 'contains'), um besuchte
     * Knoten während der Tiefensuche (DFS) zu speichern.
     */
    private class VisitedSet {
        private ElementNode visitedHead = null;

        /** Prüft, ob ein Element im Set ist. */
        public boolean contains(E element) {
            for (ElementNode current = visitedHead; current != null; current = current.next) {
                if (current.element == element) return true;
            }
            return false;
        }

        /**
         * Fügt ein Element hinzu, wenn es noch nicht enthalten ist.
         * @return true, wenn das Element neu hinzugefügt wurde,
         * false, wenn es bereits enthalten war.
         */
        public boolean add(E element) {
            if (contains(element)) return false;
            visitedHead = new ElementNode(element, visitedHead);
            return true;
        }
    }

    /**
     * Private, rekursive Tiefensuche (DFS).
     *
     * @param current Der aktuell besuchte Knoten
     * @param target  Der Knoten, den wir suchen (y)
     * @param visited Das Set der Knoten, die wir in DIESEM Suchlauf schon besucht haben
     */
    private boolean dfsRecursive(E current, E target, VisitedSet visited) {
        // Ein Element kann nicht vor sich selbst stehen
        if (current == target) {
            return false;
        }

        // Finde alle direkten Nachbarn von 'current'
        for (RelationNode rel = this.relationHead; rel != null; rel = rel.next) {

            // prüft, ob diese Relation bei 'current' startet
            if (rel.from == current) {
                E neighborNode = rel.to;

                // target erreicht
                if (neighborNode == target) {
                    return true;
                }

                // gibt true, wenn der Knoten neu ist
                if (visited.add(neighborNode)) {

                    // Rekursiver Schritt: Suche vom Nachbarn aus weiter
                    if (dfsRecursive(neighborNode, target, visited)) {
                        return true; // Pfad wurde irgendwo in der Tiefe gefunden
                    }
                }
            }
        }
        return false;
    }


    /**
     * Der gemeinsame Iterator für alle OrdSet-Implementierungen.
     */
    protected class OrdSetIterator implements Iterator<E> {
        private ElementNode currentNode;

        public OrdSetIterator(ElementNode startNode) {
            this.currentNode = startNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = currentNode.element;
            currentNode = currentNode.next;
            return element;
        }
    }

}