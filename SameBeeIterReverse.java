import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dies ist die Iterator-Klasse für Bienen. Der Iterator durchläuft alle Beobachtungen des gleichen Individuums,
 * entweder in aufsteigender oder in absteigender Reihenfolge, je nachdem wie {@link Bee#sameBee(boolean reverseOrder)}
 * gesetzt wurde.
 * Wenn reverseOrder == true, dann läuft der Iterator in absteigender Reihenfolge.
 * Wenn reverseOder == false, dann läuft der Iterator in aufsteigender Reihenfolge.
 *
 * @invariant observationsBee != null
 * @invariant index >= 0 && index <= observationsBee.size()
 */
public class SameBeeIterReverse {

    private final List<Bee> observationsBeeReverse;
    private int idx;

    /**
     * Konstruktor für den SameBeeIterReverse.
     *
     * @param allObservations Liste aller Beobachtungen des gleichen Individuums.
     * @pre  allObservations != null
     * @post Es werden die Beobachtungen intern kopiert und mithilfe des Aufrufs der
     *       Hilfsmethode reverse wird dafür gesorgt, dass die Beobachtungen in
     *       umgekehrter Reihenfolge gespeichert werden. Der Iterator startet bei dem
     *       Index 0.
     */
    public SameBeeIterReverse(List<Bee> allObservations) {
        this.observationsBeeReverse = reverse(allObservations);
        this.idx = 0;
    }

    /**
     * Hilfsmethode, um die gegebene Liste in umgekehrter Reihenfolge zu erhalten.
     * @return Gibt die Liste aller Beobachtungen in umgekehrter Reihenfolge zurück.
     *
     * @param allObservations Liste aller Beobachtungen des gleichen Individuums.
     * @pre allObservations != null
     * @post Gibt die Liste alle Beobachtungen in umgekehrter Reihenfolge zurück,
     *       mit reversed.size() == allObservations.size().
     */
    private static List<Bee> reverse(List<Bee> allObservations) {
        List<Bee> reversed = new ArrayList<>(allObservations);
        Collections.reverse(reversed);
        return reversed;
    }

    /**
     * Prüft, ob es noch eine weitere Beobachtung gibt.
     * @return Gibt true zurück, wenn es eine weitere Beobachtung gibt, ansonsten false.
     *
     * @pre N/A
     * @post Gibt true zurück, wenn eine weitere Beobachtung vorhanden ist, also idx < observationsBeeReverse.size(),
     *       ansonsten false.
     */
    public boolean hasNext(){
        return idx < observationsBeeReverse.size();
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
        return observationsBeeReverse.get(idx++);
    }
}
