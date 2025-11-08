import java.util.ArrayList;
import java.util.List;

/**
 * Dies ist die Iterator-Klasse für Bienen. Der Iterator durchläuft alle Beobachtungen des gleichen Individuums
 * in aufsteigender Reihenfolge.
 * @invariant observationsBee != null
 * @invariant index >= 0 && index <= observationsBee.size()
 */
public class SameBeeIter {

    private final List<Bee> observationsBee;
    private int idx;

    /**
     * Konstruktor für den SameBeeIter.
     *
     * @param allObservations Liste aller Beobachtungen des gleichen Individuums.
     * @pre  allObservations != null
     * @post Es werden die Beobachtungen intern kopiert und der Iterator startet bei dem Index 0.
     */
    public SameBeeIter(List<Bee> allObservations) {
        this.observationsBee = new ArrayList<>(allObservations);
        this.idx = 0;
    }

    /**
     * Prüft, ob es noch eine weitere Beobachtung gibt.
     * @return Gibt true zurück, wenn es eine weitere Beobachtung gibt, ansonsten false.
     *
     * @pre N/A
     * @post Gibt true zurück, wenn eine weitere Beobachtung vorhanden ist, also idx < observationsBee.size(),
     *       ansonsten false.
     */
    public boolean hasNext(){
        return idx < observationsBee.size();
    }

    /**
     * Gibt die nächste Bee-Beobachtung zurück.
     * @return Die nächste Bee-Beobachtung oder null, wenn keine mehr vorhanden.
     *
     * @pre hasNext() == true
     * @post Gibt die nächste Bee-Beobachtung zurück, wenn vorhanden, ansonsten null.
     *       Zudem wird der Index inkrementiert.
     */
    public Bee next(){
        if(!hasNext()) return null;
        return observationsBee.get(idx++);
    }
}
