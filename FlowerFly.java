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

    /**
     * @pre true
     * @post Liefert null (laut Implementierung)
     */
    @Override
    public BehaviorIter<Observation> later() {
        return null;
    }

    /**
     * @pre true
     * @post Liefert null (laut Implementierung)
     */
    @Override
    public BehaviorIter<Observation> earlier() {
        return null;
    }
}