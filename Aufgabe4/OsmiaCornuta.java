import java.time.LocalDateTime;
import java.util.*;

/**
 * Diese Klasse repräsentiert die Beobachtung einer Osmia Cornunta, das ist eine solitär lebende gehörnte Mauerbiene.
 *
 * @invariant dateTime != null
 * @invariant comment != null
 * @invariant individualIdentifier != null
 * @invariant isValid != null
 */
public class OsmiaCornuta extends BeeIterHelper implements SolitaryBee {

    private final LocalDateTime dateTime;
    private final String comment;
    private final Object individualIdentifier;
    private Boolean isValid;
    private final Long markerID; // falls nicht bekannt, dann null
    private final Boolean fromBreeding; // falls nicht bekannt, dann null

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten Osmia Cornunta ohne Angabe einer Marker-ID
     * und ohne einer Zuchtangabe.
     *
     * @param dateTime Zeitstempel der Beobachtung
     * @param comment  Kommentar zur Beobachtung
     * @pre dateTime != null && comment != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post this.valid == true, this.markerID == null, this.fromBreeding == null.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Der Zeitstempel (dateTime) darf nicht null sein.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Der Kommentar (comment) darf nicht null sein.");
        }
        this.dateTime = dateTime;
        this.comment = comment;
        individualIdentifier = new Object();
        this.fromBreeding = null;
        this.isValid = true;
        this.markerID = null;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten Osmia Cornunta mit einer Zuchtangabe,
     * ohne Angabe einer Marker-ID und eines Individual-Identifier.
     *
     * @param dateTime     Zeitstempel der Beobachtung
     * @param comment      Kommentar zur Beobachtung
     * @param fromBreeding Angabe, ob das Individuum aus einer Zucht kommt.
     * @pre dateTime != null && comment != null && fromBreeding != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post this.valid == true, this.markerID == null, this.fromBreeding != null.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, boolean fromBreeding) {
        this.dateTime = dateTime;
        this.comment = comment;
        individualIdentifier = new Object();
        this.fromBreeding = fromBreeding;
        this.isValid = true;
        this.markerID = null;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer Osmia Cornunta, für die bereits eine Marker-ID bekannt ist,
     * und ohne Angabe eines Individual-Identifier und einer Zuchtangabe.
     *
     * @param dateTime Zeitstempel der Beobachtung
     * @param comment  Kommentar zur Beobachtung
     * @param markerID Marker-ID der Beobachtung.
     * @pre dateTime != null && comment != null && markerID != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums gesetzt.
     * @post this.valid == true, this.fromBreeding == null.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, Long markerID) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Der Zeitstempel (dateTime) darf nicht null sein.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Der Kommentar (comment) darf nicht null sein.");
        }
        if (markerID == null) {
            throw new IllegalArgumentException("Die Marker-ID (markerID) darf nicht null sein.");
        }
        this.dateTime = dateTime;
        this.comment = comment;
        individualIdentifier = new Object();
        this.fromBreeding = null;
        this.isValid = true;
        this.markerID = markerID;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer Osmia Cornunta mit einer Zuchtangabe, für die bereits eine Marker-ID bekannt ist,
     * ohne Angabe eines Individual-Identifier.
     *
     * @param dateTime     Zeitstempel der Beobachtung
     * @param comment      Kommentar zur Beobachtung
     * @param fromBreeding Angabe, ob das Individuum aus einer Zucht kommt.
     * @param markerID     Marker-ID der Beobachtung.
     * @pre dateTime != null && comment != null && markerID != null && fromBreeding != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums gesetzt und fromBreeding speichert die Zuchtangabe.
     * @post this.valid == true.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, boolean fromBreeding, Long markerID) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Der Zeitstempel (dateTime) darf nicht null sein.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Der Kommentar (comment) darf nicht null sein.");
        }
        if (markerID == null) {
            throw new IllegalArgumentException("Die Marker-ID (markerID) darf nicht null sein.");
        }
        this.dateTime = dateTime;
        this.comment = comment;
        individualIdentifier = new Object();
        this.fromBreeding = fromBreeding;
        this.isValid = true;
        this.markerID = markerID;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer Osmia Cornunta mit Individuum-Identifier,
     * ohne Zuchtangabe und Marker-ID.
     *
     * @param dateTime        Zeitstempel der Beobachtung
     * @param comment         Kommentar zur Beobachtung
     * @param prevObservation eine frühere Beobachtung desselben Individuums.
     * @pre dateTime != null && comment != null && prevObservation != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und das individualIdentifier-Objekt der früheren Beobachtung desselben Individuums wird darin gespeichert.
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post this.valid == true, this.fromBreeding == null, markerID == null.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, OsmiaCornuta prevObservation) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Der Zeitstempel (dateTime) darf nicht null sein.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Der Kommentar (comment) darf nicht null sein.");
        }
        if (prevObservation == null) {
            throw new IllegalArgumentException("Die vorherige Beobachtung (prevObservation) darf nicht null sein.");
        }
        this.dateTime = dateTime;
        this.comment = comment;
        this.individualIdentifier = prevObservation.individualIdentifier;
        this.fromBreeding = null;
        this.isValid = true;
        this.markerID = null;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer Osmia Cornunta mit Individuum-Identifier und Zuchtangabe,
     * ohne einer Marker-ID.
     *
     * @param dateTime        Zeitstempel der Beobachtung
     * @param comment         Kommentar zur Beobachtung
     * @param prevObservation eine frühere Beobachtung desselben Individuums.
     * @pre dateTime != null && comment != null && prevObservation != null && fromBreeding != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und das individualIdentifier-Objekt der früheren Beobachtung desselben Individuums wird gespeichert.
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post this.valid == true
     * @post fromBreeding speichert die Zuchtangabe.
     * @post markerID wird auf null gesetzt.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, boolean fromBreeding, OsmiaCornuta prevObservation) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Der Zeitstempel (dateTime) darf nicht null sein.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Der Kommentar (comment) darf nicht null sein.");
        }
        if (prevObservation == null) {
            throw new IllegalArgumentException("Die vorherige Beobachtung (prevObservation) darf nicht null sein.");
        }

        this.dateTime = dateTime;
        this.comment = comment;
        individualIdentifier = prevObservation.individualIdentifier;
        this.fromBreeding = fromBreeding;
        this.isValid = true;
        this.markerID = null;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer Osmia Cornunta mit einer Marker-ID und einem Individuum-Identifier,
     * ohne Zuchtangabe.
     *
     * @param dateTime        Zeitstempel der Beobachtung
     * @param comment         Kommentar zur Beobachtung
     * @param prevObservation eine frühere Beobachtung desselben Individuums.
     * @param markerID        Marker-ID der Beobachtung.
     * @pre dateTime != null && comment != null && prevObservation != null && markerID != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und das individualIdentifier-Objekt der früheren Beobachtung desselben Individuums wird gespeichert.
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums gesetzt .
     * @post this.valid == true, this.fromBreeding == null.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, OsmiaCornuta prevObservation, Long markerID) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Der Zeitstempel (dateTime) darf nicht null sein.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Der Kommentar (comment) darf nicht null sein.");
        }
        if (prevObservation == null) {
            throw new IllegalArgumentException("Die vorherige Beobachtung (prevObservation) darf nicht null sein.");
        }
        if (markerID == null) {
            throw new IllegalArgumentException("Die Marker-ID (markerID) darf nicht null sein.");
        }
        this.dateTime = dateTime;
        this.comment = comment;
        individualIdentifier = prevObservation.individualIdentifier;
        this.fromBreeding = null;
        this.isValid = true;
        this.markerID = markerID;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer Osmia Cornunta mit einer Marker-ID,
     * einem Individuum-Identifier und einer Zuchtangabe.
     *
     * @param dateTime        Zeitstempel der Beobachtung
     * @param comment         Kommentar zur Beobachtung
     * @param prevObservation eine frühere Beobachtung desselben Individuums.
     * @param markerID        Marker-ID der Beobachtung.
     * @pre dateTime != null && comment != null && prevObservation != null && fromBreeding != null && markerID != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und das individualIdentifier-Objekt der früheren Beobachtung desselben Individuums wird gespeichert.
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums gesetzt und fromBreeding speichert die Zuchtangabe.
     * @post this.valid == true
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, boolean fromBreeding, OsmiaCornuta prevObservation, Long markerID) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Der Zeitstempel (dateTime) darf nicht null sein.");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Der Kommentar (comment) darf nicht null sein.");
        }
        if (prevObservation == null) {
            throw new IllegalArgumentException("Die vorherige Beobachtung (prevObservation) darf nicht null sein.");
        }
        if (markerID == null) {
            throw new IllegalArgumentException("Die Marker-ID (markerID) darf nicht null sein.");
        }
        this.dateTime = dateTime;
        this.comment = comment;
        individualIdentifier = prevObservation.individualIdentifier;
        this.fromBreeding = fromBreeding;
        this.isValid = true;
        this.markerID = markerID;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    // --- Methoden von Observation ---

    /**
     * Gibt die Zeitangabe dieser Beobachtung einer OsmiaCornuta zurück.
     *
     * @return LocalDateTime der Beobachtung.
     */
    @Override
    public LocalDateTime getTimestamp() {
        return dateTime;
    }

    /**
     * Gibt einen Kommentar dieser Beobachtugn einer OsmiaCornuta zurück.
     *
     * @return Kommentar der Beobachtung.
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Entfernt diese Beobachtung logisch aus dem Datenbestand.
     *
     * @post this.valid == false
     */
    @Override
    public void remove() {
        this.isValid = false;
    }

    /**
     * Gibt an, ob diese Beobachtung logisch entfernt wurde.
     *
     * @post liefert true, wenn diese Beobachtung logisch im Datenbestand ist, sonst false.
     */
    @Override
    public boolean valid() {
        return isValid;
    }

    /**
     * Gibt einen Iterator über jede Beobachtung des gleichen Individuums zurück,
     * aus der eine soziale Lebensweise hervorgeht.
     *
     * @post liefert einen Iterator über alle Beobachtungen von diesem Individuum, die eine solitäre Lebensweise vorweisen.
     * @post Der Iterator ist chronologisch aufsteigend sortiert.
     */
    @Override
    public BehaviorIter<SolitaryBee> solitary() {
        List<Bee> sameIndividuals = this.getSameIndividualObservations();
        List<SolitaryBee> solitaryObservations = new ArrayList<>();

        for (Bee bee : sameIndividuals) {
            if (bee instanceof OsmiaCornuta) {
                solitaryObservations.add((SolitaryBee) bee);
            }
        }
        solitaryObservations.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return new BehaviorIter<>(solitaryObservations);
    }

    /**
     * Gibt einen Iterator über jede Beobachtung des gleichen Individuums zurück,
     * wenn diese Beobachtung den gefragten Zuchtstatus ({@code fromBreeding}) aufweist.
     *
     * @param fromBreeding true: liefert nur Beobachtungen, bei denen die Abstammung aus der Zucht angegeben ist.
     *                     false: liefert nur Beobachtungen, die nicht aus der Zucht stammen.
     * @post liefert einen Iterator über alle Beobachtungen von diesem Individuum, die mit dem Flag "fromBreeding" übereinstimmen.
     * @post Der Iterator ist chronologisch aufsteigend sortiert.
     */
    @Override
    public BehaviorIter<WildBee> wild(boolean fromBreeding) {

        List<Bee> potentialBees = this.getSameIndividualObservations();
        List<WildBee> matchingObservations = new ArrayList<>();

        for (Bee bee : potentialBees) {
            if (bee instanceof OsmiaCornuta) {
                OsmiaCornuta otherBee = (OsmiaCornuta) bee;
                Boolean otherStatus = otherBee.fromBreeding;
                if (otherStatus != null && otherStatus == fromBreeding) {
                    matchingObservations.add(otherBee);
                }
            }
        }
        matchingObservations.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        return new BehaviorIter<WildBee>(matchingObservations);
    }

    /**
     * Gibt den eindeutigen systeminternen Identifikator für dieses Individuum zurück.
     * @post liefert den Wert von `this.individualIdentifier`.
     */
    @Override
    public Object individualIdentifier() {
        return this.individualIdentifier;
    }

    /**
     * Gibt die numeric Marker-Id dieser Beobachtung
     * @post übergibt den Wert von this.markerID
     */
    @Override
    public Long markerID() {
        return this.markerID;
    }
}
