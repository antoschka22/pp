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
public class OsmiaCornuta implements SolitaryBee {


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
     * @pre dateTime != null && comment != null, fromBreeding != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post this.valid == true, this.markerID == null, this.fromBreeding != null.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, Boolean fromBreeding) {
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
     * @pre dateTime != null && comment != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums gesetzt.
     * @post this.valid == true, this.fromBreeding == null.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, Long markerID) {
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
     * @pre dateTime != null && comment != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums gesetzt und fromBreeding speichert die Zuchtangabe.
     * @post this.valid == true.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, Boolean fromBreeding, Long markerID) {
        this.dateTime = dateTime;
        this.comment = comment;
        individualIdentifier = new Object();
        this.fromBreeding = fromBreeding;
        this.isValid = true;
        this.markerID = markerID;
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
     * @pre dateTime != null && comment != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und das individualIdentifier-Objekt der früheren Beobachtung desselben Individuums wird gespeichert.
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums.
     * @post this.valid == true, this.fromBreeding == null.
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, OsmiaCornuta prevObservation, Long markerID) {
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
     * @pre dateTime != null && comment != null
     * @post es wird ein neues OsmiaCornuta-Objekt erstellt und das individualIdentifier-Objekt der früheren Beobachtung desselben Individuums wird gespeichert.
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums und fromBreeding speichert die Zuchtangabe.
     * @post this.valid == true
     */
    public OsmiaCornuta(LocalDateTime dateTime, String comment, boolean fromBreeding, OsmiaCornuta prevObservation, Long markerID) {
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
     * Gibt einen Iterator über alle zeitlich späteren, gültigen Beobachtungen
     * von beliebigen Individuen (Untertyp von Observation) in aufsteigender Reihenfolge zurück.
     *
     * @post Liefert einen Iterator mit allen zeitlich späteren Beobachtungen von Untertypen von Observation in aufsteigender Reihenfolge,
     *       die gültig sind.
     * @post this ist nicht im Iterator enthalten.
     */
    @Override
    public BehaviorIter<Observation> later() {
        LocalDateTime thisTime = this.getTimestamp();
        List<Observation> laterObservations = new ArrayList<>();

        synchronized (ObservationData.ALL_OBSERVATIONS) {
            for (Observation obs : ObservationData.ALL_OBSERVATIONS) {

                if (obs.valid() && obs != this && obs.getTimestamp().isAfter(thisTime)) {
                    laterObservations.add(obs);
                }
            }
        }

        laterObservations.sort(Comparator.comparing(Observation::getTimestamp));

        return new BehaviorIter<>(laterObservations);
    }

    /**
     * Gibt einen Iterator über alle zeitlich früheren, gültigen Beobachtungen
     * von beliebigen Individuen (Untertyp von Observation) in absteigender Reihenfolge zurück.
     *
     * @post Liefert einen Iterator mit allen zeitlich früheren beliebigen Beobachtungen von Untertypen von Observation in aufsteigender Reihenfolge,
     *       die gültig sind.
     * @post this ist nicht im Iterator enthalten.
     */
    @Override
    public BehaviorIter<Observation> earlier() {
        LocalDateTime thisTime = this.getTimestamp();
        List<Observation> earlierObservations = new ArrayList<>();

        // Synchronisiere den Zugriff, da die Liste threadsicher ist
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

        // Sortiere: "näher liegend zuerst"
        // Absteigend nach Zeit (Comparator.reverseOrder())
        earlierObservations.sort(Comparator.comparing(Observation::getTimestamp).reversed());

        return new BehaviorIter<>(earlierObservations);
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
        return new BehaviorIter<SolitaryBee>(solitaryObservations);
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

    /**
     * Gibt einen Iterator über alle gültigen Beobachtungen dieses Individuums zurück, die
     * aufsteigend nach dem Beobachtungszeitpunkt sortiert sind.
     * @post Liefert einen Iterator, der alle gültigen Beobachtungen von diesem Individuum enthält.
     */
    @Override
    public BehaviorIter<Bee> sameBee() {
        List<Bee> sameBees = getSameIndividualObservations();
        sameBees.sort(Comparator.comparing(Observation::getTimestamp));
        return new BehaviorIter<Bee>(sameBees);
    }

    /**
     * Gibt einen Iterator über alle gültigen Beobachtungen dieses Individuums zurück, die
     * nach dem Parameter reverseOrder sortiert sind.
     * @post Liefert einen Iterator, der alle gültigen Beobachtungen von diesem Individuum enthält.
     *      Wenn reverseOrder == true, dann durchläuft der Iterator die Beobachtungen dieses Individuums in absteigender Reihenfolge.
     *      Wenn reverseOrder == false, dann durchläuft der Iterator die Beobachtungen dieses Individuums in aufsteigender Reihenfolge.
     */
    @Override
    public BehaviorIter<Bee> sameBee(boolean reverseOrder) {
        List<Bee> sameBees = getSameIndividualObservations();
        if (reverseOrder) {
            // Absteigend
            sameBees.sort(Comparator.comparing(Observation::getTimestamp).reversed());
        } else {
            // Aufsteigend
            sameBees.sort(Comparator.comparing(Observation::getTimestamp));
        }
        return new BehaviorIter<>(sameBees);
    }

    /**
     * Gibt einen Iterator über alle gültigen Beobachtungen dieses Individuums zurück, im Zeitraum von LocalDateTime "from"
     * bis LocalDateTime "to".
     * @post Liefert einen Iterator, der alle gültigen Beobachtungen von diesem Individuum in aufsteigender Reihenfolge enthält
     * im Zeitraum von LocalDateTime "from" bis LocalDateTime "to".
     */
    @Override
    public BehaviorIter<Bee> sameBee(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From- und To-Datum dürfen nicht null sein.");
        }
        List<Bee> sameBees = getSameIndividualObservations();
        List<Bee> filteredBees = new ArrayList<>();

        for (Bee bee : sameBees) {
            LocalDateTime t = bee.getTimestamp();

            // Prüfung: !(t < from) UND !(t > to)
            // (bedeutet: t >= from AND t <= to)
            if (!t.isBefore(from) && !t.isAfter(to)) {
                filteredBees.add(bee);
            }
        }

        filteredBees.sort(Comparator.comparing(Observation::getTimestamp));

        return new BehaviorIter<>(filteredBees);
    }

    /**
     * Diese Hilfsmethode sammelt alle gültigen Beobachtungen (valid() == true) von diesem Individuum
     * aus der Liste der ObservationData-Klasse.
     * @return Eine unsortierte Liste von Bee-Beobachtungen.
     *
     * @pre this.individualIdentifier() != null
     * @post Rückgabe ist eine Liste mit allen Bee-Objekten, die diesselbe markerID oder denselben individualIdentifier
     * wie dieses Individuum haben.
     * @invariant this.individualIdentifier() != null
     * @invariant Der Zugriff auf ObservationData.ALL_OBSERVATIONS erfolgt immer innerhalb eines synchronized-Blocks
     */
    private List<Bee> getSameIndividualObservations() {
        List<Bee> matchingObservations = new ArrayList<>();

        Object thisId = this.individualIdentifier();
        Long thisMarkerID = this.markerID();

        // thisId sollte nie null sein.
        if (thisId == null) {
            return matchingObservations;
        }

        synchronized (ObservationData.ALL_OBSERVATIONS) {
            for (Observation obs : ObservationData.ALL_OBSERVATIONS) {

                // Filterkriterien: Gültig und muss eine Bee sein.
                if (!obs.valid() || !(obs instanceof Bee)) {
                    continue;
                }
                Bee BeeObs = (Bee) obs;
                boolean sameId = thisId.equals(BeeObs.individualIdentifier());

                boolean sameMarker = false;
                if (thisMarkerID != null) {
                    sameMarker = thisMarkerID.equals(BeeObs.markerID());
                }

                if (sameId || sameMarker) {
                    matchingObservations.add(BeeObs);
                }
            }
        }
        return matchingObservations;
    }
}
