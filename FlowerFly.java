import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Klasse FlowerFly: Repräsentiert eine Beobachtung einer Schwebfliege
 * Schwebfliegen sind Pollinator, aber explizit
 * *nicht* mit Bienen oder Wespen verwandt
 *
 * @invariant timestamp != null
 * @invariant comment != null
 * @invariant (isValid == true) || (isValid == false)
 */
public class FlowerFly implements Pollinator {

    private final java.time.LocalDateTime timestamp;
    private final String comment;
    private boolean isValid; // Status für valid() und remove()

    /**
     * Konstruktor für eine FlowerFly-Beobachtung.
     *
     * @param timestamp Zeitstempel der Beobachtung.
     * @param comment   Textueller Kommentar zur Beobachtung.
     * @pre timestamp != null && comment != null
     * @post Ein neues FlowerFly-Objekt ist erstellt.
     */
    public FlowerFly(java.time.LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * @pre true
     * @post Liefert den Zeitstempel (siehe Konstruktor)
     */
    @Override
    public final java.time.LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @pre true
     * @post Liefert den Kommentar (siehe Konstruktor)
     */
    @Override
    public final String getComment() {
        return comment;
    }

    /**
     * @pre true
     * @post this.valid() == false
     */
    @Override
    public final void remove() {
        this.isValid = false;
    }

    /**
     * @pre true
     * @post Liefert true, wenn remove() nicht gerufen wurde, sonst false
     */
    @Override
    public final boolean valid() {
        return isValid;
    }

    @Override
    public BehaviorIter<Observation> later() {
        LocalDateTime thisTime = this.getTimestamp();

        List<Observation> laterObservations = new ArrayList<>();

        // Synchronisiere den Zugriff, da die Liste threadsicher ist
        synchronized (ObservationData.ALL_OBSERVATIONS) {
            for (Observation obs : ObservationData.ALL_OBSERVATIONS) {

                // Muss gültig sein (valid() == true)
                // Muss nach dieser Beobachtung sein
                // Darf nicht diese Beobachtung selbst sein
                if (obs.valid() && obs != this && obs.getTimestamp().isAfter(thisTime)) {
                    laterObservations.add(obs);
                }
            }
        }

        // Sortiere: näher liegend zuerst
        // aufsteigend nach Zeit
        laterObservations.sort(Comparator.comparing(Observation::getTimestamp));

        return new BehaviorIter<>(laterObservations);
    }

    @Override
    public BehaviorIter<Observation> earlier() {
        LocalDateTime thisTime = this.getTimestamp();

        List<Observation> earlierObservations = new ArrayList<>();

        synchronized (ObservationData.ALL_OBSERVATIONS) {
            for (Observation obs : ObservationData.ALL_OBSERVATIONS) {

                // Muss gültig sein (valid() == true)
                // Muss vor dieser Beobachtung sein
                // Darf nicht diese Beobachtung selbst sein
                if (obs.valid() && obs != this && obs.getTimestamp().isBefore(thisTime)) {
                    earlierObservations.add(obs);
                }
            }
        }

        // Sortiere: näher liegend zuerst
        // absteigend nach Zeit (Comparator.reverseOrder())
        earlierObservations.sort(Comparator.comparing(Observation::getTimestamp).reversed());

        // 6. Gib den Iterator zurück
        return new BehaviorIter<>(earlierObservations);
    }
}