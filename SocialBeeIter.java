import java.util.ArrayList;
import java.util.List;

/**
 * Dies ist die Iterator-Klasse für soziale Bienen (SocialBee). Der Iterator durchläuft alle
 * Beobachtungen des gleichen Individuums, die soziales Verhalten zeigen.
 * @invariant observationsSocialBee != null
 * @invariant index >= 0 && index <= observationsSocialBee.size()
 */
public class SocialBeeIter {

    private final List<SocialBee> observationsSocialBee;
    private int idx;

    /**
     * Konstruktor für den SocialBeeIter.
     *
     * @param allObservations Liste aller Beobachtungen des gleichen Individuums, die nur soziales Verhalten aufweisen.
     * @pre  allObservations != null
     * @post Es werden die Beobachtungen intern kopiert und der Iterator startet bei dem Index 0.
     */
    public SocialBeeIter(List<SocialBee> allObservations) {
        this.observationsSocialBee = new ArrayList<>(allObservations);
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
        return idx < observationsSocialBee.size();
    }

    /**
     * Gibt die nächste SocialBee-Beobachtung zurück.
     * @return Die nächste SocialBee-Beobachtung oder null, wenn keine mehr vorhanden.
     *
     * @pre hasNext() == true
     * @post Gibt die nächste SocialBee-Beobachtung zurück, wenn vorhanden, ansonsten null.
     *       Zudem wird der Index inkrementiert.
     */
    public SocialBee next(){
        if(!hasNext()) return null;
        return observationsSocialBee.get(idx++);
    }
}
