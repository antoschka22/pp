import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert eine Honigbiene. Honigbienen sind sozial, aber explizit
 * nicht kommunal oder solitär und zählen nicht zu den Wildbienen.
 *
 * @invariant individualIdentifier != null
 * @invariant timestamp != null
 * @invariant comment != null
 * @invariant (isValid == true) || (isValid == false)
 */
public class Honeybee extends BeeIterHelper implements SocialBee{

    private final Object individualIdentifier;
    private final Long markerID;
    private final LocalDateTime timestamp;
    private final String comment;
    private boolean isValid; // Status für remove() und valid()


    // --- Konstruktoren ---

    /**
     * Konstruktor für eine neue Beobachtung von einem bisher unbekannten Individuum, welches keine
     * Marker-ID hat.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @pre timestamp != null && comment != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt und dabei wird durch individualIdentifier
     *       ein neues, einzigartiges Objekt geliefert, markerID wird auf null gesetzt und zudem wird
     *       isValid auf true gesetzt.
     */
    public Honeybee(LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.individualIdentifier = new Object();
        this.markerID = null;
        this.isValid = true;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine Beobachtung, die sich auf das gleiche Individuum bezieht wie von einer
     * früheren Beobachtung. Das Individuum erhält hierbei keine neue Marker-ID.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @param prevObservation Eine frühere Beobachtung desselben Individuums
     * @pre timestamp != null && comment != null && prevObservation != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt, individualIdentifier und markerID werden auf
     *       die frühere Beobachtung des gleichen Individuums gesetzt und zudem wird isValid auf true
     *       gesetzt.
     */
    public Honeybee(LocalDateTime timestamp, String comment, Bee prevObservation) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.individualIdentifier = prevObservation.individualIdentifier();
        this.markerID = prevObservation.markerID();
        this.isValid = true;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung eines Individuums, welches eine
     * Marker-ID hat.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @param markerID numerische Marker-ID des Individuums
     * @pre timestamp != null && comment != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt und dabei wird durch individualIdentifier
     *       ein neues, einzigartiges Objekt geliefert, markerID wird auf die jeweilige numerische
     *       Marker-ID des Individuums gesetzt und zudem wird isValid auf true gesetzt.
     */
    public Honeybee(LocalDateTime timestamp, String comment, Long markerID) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.markerID = markerID;
        this.individualIdentifier = new Object();
        this.isValid = true;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine Beobachtung, die sich auf das gleiche Individuum bezieht wie von einer
     * früheren Beobachtung. Das Individuum erhält hierbei eine neue Marker-ID.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @param prevObservation Eine frühere Beobachtung desselben Individuums
     * @param markerID numerische Marker-ID des Individuums
     * @pre timestamp != null && comment != null && prevObservation != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt, individualIdentifier wird auf die frühere
     *       Beobachtung des gleichen Individuums gesetzt, die markerID wird auf die neue Marker-ID
     *       des Individuums gesetzt und zudem wird isValid auf true gesetzt.
     */
    public Honeybee(LocalDateTime timestamp, String comment, Bee prevObservation, Long markerID) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.individualIdentifier = prevObservation.individualIdentifier();
        this.markerID = markerID;
        this.isValid = true;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    // --- Methoden von den Interfaces ---

    /**
     * Gibt das Identifikationsobjekt dieses Individuums zurück.
     * @pre true
     * @post Liefert das Identifikationsobjekt dieses Individuums zurück.
     */
    @Override
    public Object individualIdentifier() {
        return individualIdentifier;
    }

    /**
     * Gibt die Marker-ID dieses Individuums zurück oder null, falls das Individuum nicht markiert ist.
     * @pre true
     * @post Liefert die Marker-ID dieses Individuums oder null, falls das Individuum nicht markiert ist.
     */
    @Override
    public Long markerID() {
        return markerID;
    }

    /**
     * Gibt einen Iterator zurück, der über alle gültigen sozialen Beobachtungen des Individuums iteriert,
     * die aufsteigend nach dem Zeitstempel sortiert sind.
     * @pre true
     * @post Liefert einen Iterator, der nur gültige soziale Beobachtungen enthält, die aufsteigend nach dem
     *       Zeitstempel sortiert sind.
     */
    @Override
    public BehaviorIter<SocialBee> social() {
        List<SocialBee> socialBeeObs = new ArrayList<>();

        for (Bee b : getSameIndividualObservations()) {
            if (b instanceof SocialBee) {
                socialBeeObs.add((SocialBee) b);
            }
        }
        socialBeeObs.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return new BehaviorIter<>(socialBeeObs);
    }

    /**
     * Gibt den Zeitstempel der Beobachtung des Individuums zurück.
     * @pre true
     * @post Liefert den Zeitstempel der Beobachtung des Individuums zurück.
     */
    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gibt einen textuellen Kommentar zurück.
     * @pre true
     * @post Liefert einen textuellen Kommentar zurück.
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Diese Beobachtung wird als ungültig markiert und wird von den Iteratoren ignoriert.
     * @pre true
     * @post isValid wird auf false gesetzt; this.isValid() = false.
     */
    @Override
    public void remove() {
        this.isValid = false;
    }

    /**
     * Gibt zurück, ob diese Beobachtung gültig ist.
     * @pre true
     * @post Liefert true, wenn die remove-Methode nicht aufgerufen wurde, ansonsten false.
     */
    @Override
    public boolean valid() {
        return isValid;
    }
}
