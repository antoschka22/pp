import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Dies ist die Iterator-Klasse für Bienen. Der Iterator durchläuft alle Beobachtungen des gleichen Individuums innerhalb
 * eines Zeitraums, der durch LocalDateTime from und LocalDateTime to bestimmt wird.
 *
 * @invariant observationsBee != null
 * @invariant index >= 0 && index <= observationsBee.size()
 */
public class SameBeeIterTimeRange {

    private final List<Bee> observationsBee;
    private int idx;

    /**
     * Konstruktor für den SameBeeIterTimeRange.
     *
     * @param allObservations Liste aller Beobachtungen des gleichen Individuums.
     * @param from der Startzeitpunkt des Beobachtungszeitraums (inklusive)
     * @param to der Endzeitpunkt des Beobachtungszeitraums (inklusive)
     * @pre  allObservations != null
     * @pre  from und to != null
     * @post Es werden die Beobachtungen intern kopiert und mithilfe des Aufrufs der
     *       Hilfsmethode sortByTime wird dafür gesorgt, dass die Beobachtungen innerhalb
     *       des angegebenen Zeitbereichs liegen. Der Iterator startet bei dem Index 0.
     */
    public SameBeeIterTimeRange(List<Bee> allObservations, LocalDateTime from, LocalDateTime to) {
        this.observationsBee = sortByTime(allObservations, from, to);
        this.idx = 0;
    }

    /**
     * Hilfsmethode, um alle Beobachtungen der gegebenen Liste, die innerhalb des angegebenen Zeitbereichs liegen,
     * in einer neuen Liste zu speichern.
     * @return Gibt die Liste aller Beobachtungen, die innerhalb des angegebenen Zeitbereichs liegen.
     *
     * @param observations Liste aller Beobachtungen des gleichen Individuums.
     * @param from der Startzeitpunkt des Beobachtungszeitraums (inklusive)
     *      * @param to der Endzeitpunkt des Beobachtungszeitraums (inklusive)
     * @pre observations != null
     * @pre from und to != null
     * @post Gibt die Liste aller Beobachtungen, die innerhalb des angegebenen Zeitbereichs, von from bis to, liegen.
     */
    private static List<Bee> sortByTime(List<Bee> observations, LocalDateTime from, LocalDateTime to) {
        List<Bee> sorted = new ArrayList<>();
        for(Bee o : observations) {
            LocalDateTime t = null; //o.getObservationTime();
            if(!t.isBefore(from) && !t.isAfter(to)) {
                sorted.add(o);
            }
        }
        return sorted;
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
