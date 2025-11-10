import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Diese Klasse repräsentiert die Beobachtung einer Lasioglossum Calceatum, das ist eine Wildbiene,
 * sie sowohl solitär als auch sozial leben kann.
 *
 * @invariant timestamte != null
 * @invariant comment != null
 * @invariant individualIdentifier != null
 * @invariant isValid != null
 */
public class LasioglossumCalceatum implements SolitaryBee, SocialBee {

    private final LocalDateTime dateTime;
    private final String comment;
    private final Object individualIdentifier;
    private Boolean isValid;
    private final boolean isSocial;
    private boolean isSolitary;
    private final Long markerID;
    private final Boolean fromBreeding; // falls nicht bekannt, dann null

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten Lasioglossum Calceatum ohne Angabe einer Marker-ID
     * und ohne einer Zuchtangabe.
     *
     * @param dateTime   Zeitstempel der Beobachtung
     * @param comment    Kommentar zur Beobachtung
     * @param isSocial   sozial lebendes Individuum?
     * @param isSolitary solitärer Individuum?
     * @pre dateTime != null && comment != null && isSocial != null && isSolitary != null
     * @post es wird ein neues Lasioglossum Calceatum-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post this.valid == true, this.markerID == null, this.fromBreeding == null.
     */
    public LasioglossumCalceatum(LocalDateTime dateTime, String comment, boolean isSocial, boolean isSolitary) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.isSocial = isSocial;
        this.isSolitary = isSolitary;
        individualIdentifier = new Object();
        this.fromBreeding = null;
        this.isValid = true;
        this.markerID = null;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten Lasioglossum Calceatum mit einer Zuchtangabe,
     * ohne Angabe einer Marker-ID und eines Individual-Identifier.
     *
     * @param dateTime     Zeitstempel der Beobachtung
     * @param comment      Kommentar zur Beobachtung
     * @param isSocial     sozial lebendes Individuum?
     * @param isSolitary   solitärer Individuum?
     * @param fromBreeding Angabe, ob das Individuum aus einer Zucht kommt.
     * @pre dateTime != null && comment != null && isSocial != null && isSolitary != null
     * @post es wird ein neues Lasioglossum Calceatum-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post this.valid == true, this.markerID == null, this.fromBreeding != null.
     */
    public LasioglossumCalceatum(LocalDateTime dateTime, String comment, boolean isSocial, boolean isSolitary, Boolean fromBreeding) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.isSocial = isSocial;
        this.isSolitary = isSolitary;
        individualIdentifier = new Object();
        this.fromBreeding = fromBreeding;
        this.isValid = true;
        this.markerID = null;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten Lasioglossum Calceatum mit einer Marker-ID
     * und ohne Angabe eines Individual-Identifier und einer Zuchtangabe.
     *
     * @param dateTime   Zeitstempel der Beobachtung
     * @param comment    Kommentar zur Beobachtung
     * @param isSocial   sozial lebendes Individuum?
     * @param isSolitary solitärer Individuum?
     * @param markerID   Marker-ID der Beobachtung.
     * @pre dateTime != null && comment != null && isSocial != null && isSolitary != null
     * @post es wird ein neues Lasioglossum Calceatum-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums gesetzt.
     * @post this.valid == true, this.fromBreeding == null.
     */
    public LasioglossumCalceatum(LocalDateTime dateTime, String comment, boolean isSocial, boolean isSolitary, Long markerID) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.isSocial = isSocial;
        this.isSolitary = isSolitary;
        individualIdentifier = new Object();
        this.fromBreeding = null;
        this.isValid = true;
        this.markerID = markerID;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten Lasioglossum Calceatum mit einer Marker-ID und einer Zuchtangabe,
     * ohne Angabe eines Individual-Identifier.
     *
     * @param dateTime     Zeitstempel der Beobachtung
     * @param comment      Kommentar zur Beobachtung
     * @param isSocial     sozial lebendes Individuum?
     * @param isSolitary   solitärer Individuum?
     * @param fromBreeding Angabe, ob das Individuum aus einer Zucht kommt.
     * @param markerID     Marker-ID der Beobachtung.
     * @pre dateTime != null && comment != null && isSocial != null && isSolitary != null
     * @post es wird ein neues Lasioglossum Calceatum-Objekt erstellt und ein neues individualIdentifier-Objekt gespeichert
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums gesetzt und fromBreeding speichert die Zuchtangabe.
     * @post this.valid == true.
     */
    public LasioglossumCalceatum(LocalDateTime dateTime, String comment, boolean isSocial, boolean isSolitary, Boolean fromBreeding, Long markerID) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.isSocial = isSocial;
        this.isSolitary = isSolitary;
        individualIdentifier = new Object();
        this.fromBreeding = fromBreeding;
        this.isValid = true;
        this.markerID = markerID;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten Lasioglossum Calceatum mit einer Marker-ID und einen Individuum-Identifier,
     * ohne Zuchtangabe.
     *
     * @param dateTime        Zeitstempel der Beobachtung
     * @param comment         Kommentar zur Beobachtung
     * @param isSocial        sozial lebendes Individuum?
     * @param isSolitary      solitärer Individuum?
     * @param prevObservation eine frühere Beobachtung desselben Individuums.
     * @param markerID        Marker-ID der Beobachtung.
     * @pre dateTime != null && comment != null && isSocial != null && isSolitary != null
     * @post es wird ein neues Lasioglossum Calceatum-Objekt erstellt und das individualIdentifier-Objekt der früheren Beobachtung desselben Individuums wird gespeichert.
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums.
     * @post this.valid == true, this.fromBreeding == null.
     */
    public LasioglossumCalceatum(LocalDateTime dateTime, String comment, boolean isSocial, boolean isSolitary, LasioglossumCalceatum prevObservation, Long markerID) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.isSocial = isSocial;
        this.isSolitary = isSolitary;
        individualIdentifier = prevObservation.individualIdentifier;
        this.fromBreeding = null;
        this.isValid = true;
        this.markerID = markerID;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten Lasioglossum Calceatum mit einer Marker-ID,
     * einem Individuum-Identifier und einer Zuchtangabe.
     *
     * @param dateTime        Zeitstempel der Beobachtung
     * @param comment         Kommentar zur Beobachtung
     * @param isSocial        sozial lebendes Individuum?
     * @param isSolitary      solitärer Individuum?
     * @param prevObservation eine frühere Beobachtung desselben Individuums.
     * @param markerID        Marker-ID der Beobachtung.
     * @pre dateTime != null && comment != null && isSocial != null && isSolitary != null
     * @post es wird ein neues Lasioglossum Calceatum-Objekt erstellt und das individualIdentifier-Objekt der früheren Beobachtung desselben Individuums wird gespeichert.
     * @post this wird zur Liste der ObservationData-Klasse hinzugefügt.
     * @post markerID wird auf die numerische Marker-ID des Individuums und fromBreeding speichert die Zuchtangabe.
     * @post this.valid == true
     */
    public LasioglossumCalceatum(LocalDateTime dateTime, String comment, boolean isSocial, boolean isSolitary, boolean fromBreeding, LasioglossumCalceatum prevObservation, Long markerID) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.isSocial = isSocial;
        this.isSolitary = isSolitary;
        individualIdentifier = prevObservation.individualIdentifier;
        this.fromBreeding = fromBreeding;
        this.isValid = true;
        this.markerID = markerID;
        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Gibt einen Iterator über jede Beobachtung des gleichen Individuums zurück,
     * aus der eine soziale Lebensweise hervorgeht.
     *
     * @post liefert einen Iterator über alle Beobachtungen von diesem Individuum aus, die eine soziale Lebensweise vorweisen.
     * @post Der Iterator ist chronologisch aufsteigend sortiert.
     */

    @Override
    public BehaviorIter<SocialBee> social() {
        List<Bee> potentialBees = this.getSameIndividualObservations(); //
        List<SocialBee> matchingObservations = new ArrayList<>();

        for (Bee bee : potentialBees) {
            if (bee instanceof LasioglossumCalceatum) {
                LasioglossumCalceatum otherBee = (LasioglossumCalceatum) bee;
                if (otherBee.isSocial) { // (== true)
                    matchingObservations.add(otherBee);
                }
            }
        }
        matchingObservations.sort(Comparator.comparing(Observation::getTimestamp));
        return new BehaviorIter<>(matchingObservations);
    }

    /**
     * Gibt einen Iterator über jede Beobachtung des gleichen Individuums zurück,
     * aus der eine soziale Lebensweise hervorgeht.
     *
     * @post liefert einen Iterator über alle Beobachtungen von diesem Individuum aus, die eine solitäre Lebensweise vorweisen.
     * @post Der Iterator ist chronologisch aufsteigend sortiert.
     */
    @Override
    public BehaviorIter<SolitaryBee> solitary() {
        List<Bee> sameIndividuals = this.getSameIndividualObservations();
        List<SolitaryBee> solitaryObservations = new ArrayList<>();

        for (Bee bee : sameIndividuals) {
            if (bee instanceof LasioglossumCalceatum) {
                LasioglossumCalceatum otherBee = (LasioglossumCalceatum) bee;
                if (otherBee.isSolitary) {
                    solitaryObservations.add((SolitaryBee) bee);
                }

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
     * @post liefert einen Iterator über alle Beobachtungen von diesem Individuum aus, die mit dem Flag "fromBreeding" übereinstimmen.
     * @post Der Iterator ist chronologisch aufsteigend sortiert.
     */
    @Override
    public BehaviorIter<WildBee> wild(boolean fromBreeding) {

        List<Bee> potentialBees = this.getSameIndividualObservations();
        List<WildBee> matchingObservations = new ArrayList<>();

        for (Bee bee : potentialBees) {
            if (bee instanceof LasioglossumCalceatum) {
                LasioglossumCalceatum otherBee = (LasioglossumCalceatum) bee;
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
     * @post liefert den Wert von this.markerID
     * @return
     */
    @Override
    public Long markerID() {
        return this.markerID;
    }

    @Override
    public BehaviorIter<Bee> sameBee() {
        List<Bee> sameBees = getSameIndividualObservations();
        sameBees.sort(Comparator.comparing(Observation::getTimestamp));
        return new BehaviorIter<Bee>(sameBees);
    }

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
     * Gibt die Zeitangabe dieser Beobachtung einer Lasioglossum Calceatum zurück.
     *
     * @return LocalDateTime der Beobachtung.
     */
    @Override
    public LocalDateTime getTimestamp() {
        return dateTime;
    }

    /**
     * Gibt einen Kommentar dieser Beobachtugn einer Lasioglossum Calceatum zurück.
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

        // 5. Sortiere: "näher liegend zuerst"
        // Aufsteigend nach Zeit
        laterObservations.sort(Comparator.comparing(Observation::getTimestamp));

        return new BehaviorIter<>(laterObservations);
    }


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
     * Private Hilfsmethode
     * Sammelt alle gültigen Beobachtungen (valid() == true) des gleichen
     * Individuums wie this aus der globalen Liste.
     *
     * @return Eine unsortierte Liste von Bee-Beobachtungen.
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
