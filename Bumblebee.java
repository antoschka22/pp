import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert eine Hummel. Hummeln sind staatenbildend, das bedeutet also
 * sie zählen zu den sozialen Bienen (SocialBee) und zu den Wildbienen (WildBee).
 *
 * @invariant individualIdentifier != null
 * @invariant timestamp != null
 * @invariant comment != null
 * @invariant (isValid == true) || (isValid == false)
 */
public class Bumblebee implements SocialBee, WildBee{

    private final Object individualIdentifier;
    private final Long markerID;
    private final LocalDateTime timestamp;
    private final String comment;
    private final Boolean fromBreeding; // falls nicht bekannt, dann null
    private boolean isValid; // Status für remove() und valid()


    // --- Konstruktoren ---

    /**
     * Konstruktor für eine neue Beobachtung von einem bisher unbekannten Individuum, welches keine
     * Marker-ID hat und es gibt keine Zuchtangabe.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @pre timestamp != null && comment != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt und dabei wird durch individualIdentifier
     *       ein neues, einzigartiges Objekt geliefert, markerID wird auf null gesetzt, isValid auf true
     *       this.fromBreeding == null gesetzt.
     */
    public Bumblebee(LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.individualIdentifier = new Object();
        this.markerID = null;
        this.fromBreeding = null;
        this.isValid = true;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung von einem bisher unbekannten Individuum, welches keine
     * Marker-ID hat und es gibt eine Zuchtangabe.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @param fromBreeding Angabe, ob das Individuum aus einer Zucht kommt
     * @pre timestamp != null && comment != null && fromBreeding != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt und dabei wird durch individualIdentifier
     *       ein neues, einzigartiges Objekt geliefert, markerID wird auf null gesetzt, isValid auf true
     *       gesetzt.; this.fromBreeding != null
     */
    public Bumblebee(LocalDateTime timestamp, String comment, Boolean fromBreeding) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.individualIdentifier = new Object();
        this.fromBreeding = fromBreeding;
        this.markerID = null;
        this.isValid = true;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine Beobachtung, die sich auf das gleiche Individuum bezieht wie von einer
     * früheren Beobachtung. Das Individuum erhält hierbei keine neue Marker-ID und es gibt keine
     * Zuchtangabe.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @param prevObservation Eine frühere Beobachtung desselben Individuums
     * @pre timestamp != null && comment != null && prevObservation != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt, individualIdentifier und markerID werden auf
     *       die frühere Beobachtung des gleichen Individuums gesetzt und zudem wird isValid auf true
     *       gesetzt.; this.fromBreeding == null
     */
    public Bumblebee(LocalDateTime timestamp, String comment, Bee prevObservation) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.individualIdentifier = prevObservation.individualIdentifier();
        this.markerID = prevObservation.markerID();
        this.isValid = true;
        this.fromBreeding = null;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung eines Individuums, welches eine
     * Marker-ID hat und es gibt keine Zuchtangabe.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @param markerID numerische Marker-ID des Individuums
     * @pre timestamp != null && comment != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt und dabei wird durch individualIdentifier
     *       ein neues, einzigartiges Objekt geliefert, markerID wird auf die jeweilige numerische
     *       Marker-ID des Individuums gesetzt, uns es wird this.valid == true, this.fromBreeding == null
     *       gesetzt.
     */
    public Bumblebee(LocalDateTime timestamp, String comment, long markerID) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.markerID = markerID;
        this.fromBreeding = null;
        this.individualIdentifier = new Object();
        this.isValid = true;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung eines Individuums, welches eine
     * Marker-ID hat und es gibt eine Zuchtangabe.
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment Textueller Kommentar
     * @param fromBreeding Angabe, ob das Individuum aus einer Zucht kommt
     * @param markerID numerische Marker-ID des Individuums
     * @pre timestamp != null && comment != null && fromBreeding != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt und dabei wird durch individualIdentifier
     *       ein neues, einzigartiges Objekt geliefert, markerID wird auf die jeweilige numerische
     *       Marker-ID des Individuums gesetzt, uns es wird this.valid == true gesetzt. fromBreeding
     *       speichert die Zuchtangabe.
     */
    public Bumblebee(LocalDateTime timestamp, String comment, Boolean fromBreeding, long markerID) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.markerID = markerID;
        this.fromBreeding = fromBreeding;
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
     * @param fromBreeding Angabe, ob das Individuum aus einer Zucht kommt.
     * @param prevObservation Eine frühere Beobachtung desselben Individuums
     * @param markerID numerische Marker-ID des Individuums
     * @pre timestamp != null && comment != null && prevObservation != null
     * @post Es wird ein neues HoneyBee-Objekt erstellt, individualIdentifier wird auf die frühere
     *       Beobachtung des gleichen Individuums gesetzt, die markerID wird auf die neue Marker-ID
     *       des Individuums gesetzt und zudem wird isValid auf true gesetzt. fromBreeding speichert
     *       die Zuchtangabe.
     */
   public Bumblebee(LocalDateTime timestamp, String comment, boolean fromBreeding, Bee prevObservation, long markerID) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.fromBreeding = fromBreeding;
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
     * Gibt einen Iterator zurück, der über alle gültigen Beobachtungen des Individuums iteriert, die
     * aufsteigend nach dem Zeitstempel sortiert sind.
     * @pre true
     * @post Liefert einen Iterator, der nur gültige Beobachtungen enthält, die aufsteigend nach dem
     *       Zeitstempel sortiert sind.
     */
    @Override
    public BehaviorIter sameBee() {
        List<Bee> newListOfSameBees = getSameIndividualObs();
        newListOfSameBees.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return new BehaviorIter(newListOfSameBees);
    }

    /**
     * Gibt einen Iterator zurück, der über alle gültigen Beobachtungen des Individuums iteriert, die
     * gemäß reversOrder sortiert sind.
     * @pre true
     * @post Liefert einen Iterator, der nur gültige Beobachtungen enthält, die gemäß reverseOrder
     *       sortiert sind.
     *       Wenn reverseOrder == true, dann läuft der Iterator in absteigender Reihenfolge.
     *       Wenn reverseOder == false, dann läuft der Iterator in aufsteigender Reihenfolge.
     */
    @Override
    public BehaviorIter sameBee(boolean reverseOrder) {
        List<Bee> newListOfSameBees = getSameIndividualObs();

        if (reverseOrder) {
            newListOfSameBees.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        } else {
            newListOfSameBees.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        }
        return new BehaviorIter(newListOfSameBees);
    }

    /**
     * Gibt einen Iterator zurück, der über alle gültigen Beobachtungen des Individuums iteriert, die
     * innerhalb eines Zeitraums, der durch LocalDateTime from und LocalDateTime to bestimmt wird.
     * @pre true
     * @post Liefert einen Iterator, der nur gültige Beobachtungen in aufsteigender Reihenfolge enthält,
     *       die innerhalb des Zeitraums [from, to] liegen.
     */
    @Override
    public BehaviorIter sameBee(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From- und To-Datum dürfen nicht null sein.");
        }

        List<Bee> filtered = new ArrayList<>();

        for(Bee b : getSameIndividualObs()) {
            LocalDateTime ts = b.getTimestamp();
            if (!ts.isBefore(from) && !ts.isAfter(to)) {
                filtered.add(b);
            }
        }
        filtered.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return new BehaviorIter(filtered);
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

        for (Bee b : getSameIndividualObs()) {
            if(b instanceof SocialBee) {
                socialBeeObs.add((SocialBee) b);
            }
        }
        socialBeeObs.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return new BehaviorIter<>(socialBeeObs);
    }

    /**
     * Gibt einen Iterator zurück, der über alle gültigen Wildbienen-Beobachtungen des Individuums iteriert,
     * die aufsteigend nach dem Zeitstempel sortiert sind.
     * @pre true
     * @post Liefert einen Iterator, der nur gültige Wildbienen-Beobachtungen enthält, die aufsteigend nach dem
     *       Zeitstempel sortiert sind.
     *       Wenn fromBreeding == true, dann liefert der Iterator nur Beobachtungen, bei denen die Abstammung aus
     *       der Zucht angegeben ist.
     *       Wenn fromBreeding == false, dann liefert der Iterator nur Beobachtungen, die nicht aus der Zucht stammen.
     */
    @Override
    public BehaviorIter<WildBee> wild(boolean fromBreeding) {
        List<WildBee> wildBeeObs = new ArrayList<>();

        for (Bee b : getSameIndividualObs()) {
            if(b instanceof Bumblebee otherBee) {
                Boolean otherStatus = otherBee.fromBreeding;
                if(otherStatus != null && otherStatus == fromBreeding) {
                    wildBeeObs.add(otherBee);
                }
            }
        }
        wildBeeObs.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return new BehaviorIter<>(wildBeeObs);
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

    /**
     * Gibt einen Iterator zurück, der über alle zeitlich späteren Beobachtungen aller Observations
     * in aufsteigender Reihenfolge iteriert, die gültig sind.
     * @pre true
     * @post Liefert alle zeitlich späteren Beobachtungen des Individuums in aufsteigender Reihenfolge,
     *       die gültig sind.
     */
    @Override
    public BehaviorIter<Observation> later() {
        LocalDateTime currTimeStamp = this.getTimestamp();

        List<Observation> obsLater = new ArrayList<>();

        // Synchronisiere den Zugriff, da die Liste threadsicher ist
        synchronized (ObservationData.ALL_OBSERVATIONS) {
            for (Observation o : ObservationData.ALL_OBSERVATIONS) {
                if (o != this & o.valid() && o.getTimestamp().isAfter(currTimeStamp)) {
                    obsLater.add(o);
                }
            }
        }
        obsLater.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return new BehaviorIter<>(obsLater);
    }

    /**
     * Gibt einen Iterator zurück, der über alle zeitlich früheren Beobachtungen aller Observations
     * in absteigender Reihenfolge iteriert, die gültig sind.
     * @pre true
     * @post Liefert alle zeitlich früheren Beobachtungen des Individuums in absteigender Reihenfolge,
     *       die gültig sind.
     */
    @Override
    public BehaviorIter<Observation> earlier() {
        LocalDateTime currTimeStamp = this.getTimestamp();

        List<Observation> obsEarlier = new ArrayList<>();

        // Synchronisiere den Zugriff, da die Liste threadsicher ist
        synchronized (ObservationData.ALL_OBSERVATIONS) {
            for (Observation o : ObservationData.ALL_OBSERVATIONS) {
                if (o != this & o.valid() && o.getTimestamp().isBefore(currTimeStamp)) {
                    obsEarlier.add(o);
                }
            }
        }
        obsEarlier.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return new BehaviorIter<>(obsEarlier);
    }

    /**
     * Hilfsmethode, für die folgenden Iteratoren:
     *  - SameBeeIter
     *  - SameBeeIterReverse
     *  - SameBeeIterTimeRange
     *  - BehaviorIter<SocialBee>
     *  Dadurch werden nur die Beobachtungen desselben Individuums gefiltert.
     *
     * @return Gibt eine Liste zurück, die alle Beobachtungen beinhaltet, die zum selben Individuum
     *         wie this.individualIdentifier gehören.
     */
    private List<Bee> getSameIndividualObs() {
        List<Bee> sameIndividual = new ArrayList<>();

        for(Observation o : ObservationData.ALL_OBSERVATIONS) {
            if(o.valid() && o instanceof Bee) {
                Bee obsOfBee = (Bee) o;
                if(obsOfBee.individualIdentifier().equals(individualIdentifier)) {
                    sameIndividual.add(obsOfBee);
                }
            }
        }
        return sameIndividual;
    }
}
