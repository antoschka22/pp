import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Dies ist die generische Iterator-Klasse für Bienen-Beobachtungen (Typ <T>). Der Iterator durchläuft alle
 * Beobachtungen des gleichen Individuums, die dem Typ <T> entsprechen.
 * @invariant observations != null
 * @invariant index >= 0 && index <= observations.size()
 */
public class BehaviorIter<T extends Bee> implements Iterator<T> {

    private final List<T> observations;
    private int idx;

    /**
     * Konstruktor für den BehaviorIter.
     *
     * @param allObservations Liste aller Beobachtungen des Typs T, die dasselbe Verhalten zeigen.
     * @pre  allObservations != null
     * @post Es werden die Beobachtungen intern kopiert und der Iterator startet bei dem Index 0.
     */
    public BehaviorIter(List<T> allObservations) {
        this.observations = new ArrayList<>(allObservations);
        this.idx = 0;
    }

    /**
     * Prüft, ob es noch eine weitere Beobachtung gibt.
     * @return Gibt true zurück, wenn es eine weitere Beobachtung gibt, ansonsten false.
     *
     * @pre N/A
     * @post Gibt true zurück, wenn eine weitere Beobachtung vorhanden ist, also idx < observationsSocialBee.size(),
     *       ansonsten false.
     */
    public boolean hasNext(){
        return idx < observations.size();
    }

    /**
     * Gibt die nächste Beobachtung vom Typ T zurück.
     * @return Die nächste Beobachtung oder null, wenn keine mehr vorhanden.
     *
     * @pre hasNext() == true
     * @post Gibt die nächste Beobachtung zurück, wenn vorhanden, ansonsten null.
     *       Zudem wird der Index inkrementiert.
     */
    public T next(){
        if(!hasNext()) return null;
        return observations.get(idx++);
    }
}
