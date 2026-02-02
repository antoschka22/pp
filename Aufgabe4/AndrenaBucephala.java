import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Klasse AndrenaBucephala: Repräsentiert eine Beobachtung der Sandbienenart
 * Andrena bucephala
 * diese Art lebt laut Angabe meist kommunal und ab und zu
 * solitär
 *
 * @invariant timestamp != null
 * @invariant comment != null
 * @invariant individualIdentifier != null
 * @invariant (isValid == true) || (isValid == false)
 */
public class AndrenaBucephala extends BeeIterHelper implements CommunalBee {

    // --- Attribute ---
    private final java.time.LocalDateTime timestamp;
    private final String comment;
    private boolean isValid; // Status für valid() und remove()

    // Attribute zur Identifizierung des Individuums
    private final Object individualIdentifier;
    private final Long markerId;
    private final boolean isSolitary;
    private final boolean isCommunal;
    private final Boolean fromBreeding; // falls nicht bekannt, dann null


    // --- Konstruktoren ---

    /**
     * Konstruktor für eine neue Beobachtung eines bisher unbekannten AndrenaBucephala
     * ohne Marker-ID und ohne Zuchtangabe
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment   Textueller Kommentar
     * @param isSolitary ist ein solitäres Individuum
     * @pre timestamp != null && comment != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.markerID() == null.
     * this.individualIdentifier() liefert ein neues, einzigartiges Objekt.
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, boolean isSolitary, boolean isCommunal) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = new Object(); // Neues Individuum
        this.markerId = null;
        this.isSolitary = isSolitary;
        this.isCommunal = isCommunal;
        this.fromBreeding = null;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung eines bisher unbekannten AndrenaBucephala
     * ohne Marker-ID und mit Zuchtangabe
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment   Textueller Kommentar
     * @param isSolitary ist ein solitäres Individuum
     * @pre timestamp != null && comment != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.markerID() == null.
     * this.individualIdentifier() liefert ein neues, einzigartiges Objekt.
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, boolean isSolitary, Boolean fromBreeding, boolean isCommunal) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = new Object(); // Neues Individuum
        this.markerId = null;
        this.isSolitary = isSolitary;
        this.isCommunal = isCommunal;
        this.fromBreeding = fromBreeding;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung eines bisher unbekannten AndrenaBucephala
     * mit Marker-ID, ohne Angabe eines Individual-Identifier und ohne Zuchtangabe
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment   Textueller Kommentar
     * @param isSolitary ist ein solitäres Individuum
     * @pre timestamp != null && comment != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.markerID() == null.
     * this.individualIdentifier() liefert ein neues, einzigartiges Objekt.
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, boolean isSolitary, Long markerId, boolean isCommunal) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = new Object(); // Neues Individuum
        this.markerId = markerId;
        this.isSolitary = isSolitary;
        this.fromBreeding = null;
        this.isCommunal = isCommunal;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung eines bisher unbekannten AndrenaBucephala
     * mit Marker-ID, ohne Angabe eines Individual-Identifier und einer Zuchtangabe
     *
     * @param timestamp Zeitstempel der Beobachtung
     * @param comment   Textueller Kommentar
     * @param isSolitary ist ein solitäres Individuum
     * @pre timestamp != null && comment != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.markerID() == null.
     * this.individualIdentifier() liefert ein neues, einzigartiges Objekt.
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, boolean isSolitary, Boolean fromBreeding, Long markerId, boolean isCommunal) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = new Object(); // Neues Individuum
        this.markerId = markerId;
        this.isSolitary = isSolitary;
        this.fromBreeding = fromBreeding;
        this.isCommunal = isCommunal;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten AndrenaBucephala mit einer Marker-ID und einen Individuum-Identifier,
     * ohne Zuchtangabe.
     *
     * @param timestamp          Zeitstempel der Beobachtung.
     * @param comment            Textueller Kommentar.
     * @param previousObservation Eine frühere Beobachtung desselben Individuums.
     * @pre timestamp != null && comment != null && previousObservation != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.individualIdentifier() == previousObservation.individualIdentifier().
     * this.markerID() == previousObservation.markerID().
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, boolean isSolitary, AndrenaBucephala previousObservation, Long markerId, boolean isCommunal) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = previousObservation.individualIdentifier();
        this.isSolitary = isSolitary;
        this.fromBreeding = null;
        this.markerId = markerId;
        this.isCommunal = isCommunal;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung einer bisher unbekannten AndrenaBucephala mit einer Marker-ID und einen Individuum-Identifier,
     * mit Zuchtangabe.
     *
     * @param timestamp          Zeitstempel der Beobachtung.
     * @param comment            Textueller Kommentar.
     * @param previousObservation Eine frühere Beobachtung desselben Individuums.
     * @pre timestamp != null && comment != null && previousObservation != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.individualIdentifier() == previousObservation.individualIdentifier().
     * this.markerID() == previousObservation.markerID().
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, boolean isSolitary, AndrenaBucephala previousObservation, Long markerId, boolean fromBreeding, boolean isCommunal) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = previousObservation.individualIdentifier();
        this.isSolitary = isSolitary;
        this.fromBreeding = fromBreeding;
        this.markerId = markerId;
        this.isCommunal = isCommunal;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    // --- Methoden von Observation ---

    /**
     * Gibt den Zeitstempel (Datum und Uhrzeit) der Beobachtung zurück.
     *
     * @return Der Zeitstempel der Beobachtung
     * @pre true
     * @post Liefert den Zeitstempel (LocalDateTime) der Beobachtung
     */
    @Override
    public final LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gibt den beschreibenden Kommentar zur Beobachtung zurück
     *
     * @return Der Kommentartext
     * @pre true
     * @post Liefert den Kommentar (String) der Beobachtung.
     */
    @Override
    public final String getComment() {
        return comment;
    }

    /**
     * Entfernt die Beobachtung logisch aus dem Datenbestand.
     * Nach dem Aufruf gibt valid() false zurück.
     * Das Objekt selbst und seine Methoden funktionieren weiterhin.
     *
     * @pre true
     * @post this.valid() == false
     */
    @Override
    public final void remove() {
        this.isValid = false;
    }

    /**
     * Prüft, ob die Beobachtung gültig (nicht entfernt) ist.
     *
     * @return true, wenn remove() noch nicht aufgerufen wurde, sonst false.
     * @pre true
     * @post Liefert true, wenn die Beobachtung gültig ist, andernfalls false.
     */
    @Override
    public final boolean valid() {
        return isValid;
    }

    // --- Methoden von Bee ---

    /**
     * Ein internes Objekt, das alle Beobachtungen eines Individuums erkennt und gruppiert.
     *
     * @pre N/A
     * @post Alle Beobachtungen derselben Biene liefern das gleiche Objekt zurück.
     */
    @Override
    public Object individualIdentifier() {
        return this.individualIdentifier;
    }

    /**
     * Gibt die Marker-ID zurück.
     * Wenn das Individuum nicht markiert ist, dann ist Marker-ID null.
     *
     * @pre N/A
     * @post Gibt entweder null oder eine Zahl zurück, die eindeutig das markierte Individuum repräsentiert.
     */
    @Override
    public Long markerID() {
        return this.markerId;
    }

    // --- Methoden von WildBee (via SolitaryBee) ---

    /**
     * Gibt einen Iterator über jede Beobachtung desselben Individuums zurück, abhängig davon,
     * ob die Beobachtung keine bzw. eine Abstimmung aus einer Zucht angibt.
     * @param fromBreeding true: liefert nur Beobachtungen, bei denen die Abstammung aus der Zucht angegeben ist.
     *                     false: liefert nur Beobachtungen, die nicht aus der Zucht stammen.
     * @return ein Iterator über alle nach Zucht gefilterten WildBee-Beobachtungen.
     *
     * @post Der Iterator enthält nur gültige Beobachtugen desselben Individuums,
     * die dem Filterkriterium fromBreeding entsprechen.
     *
     */
    @Override
    public BehaviorIter<WildBee> wild(boolean fromBreeding) {
        List<Bee> potentialBees = super.getSameIndividualObservations();
        List<WildBee> matchingObservations = new ArrayList<>();

        for (Bee bee : potentialBees) {
            if (bee instanceof AndrenaBucephala otherBee) {
                Boolean otherStatus = otherBee.fromBreeding;
                if (otherStatus != null && otherStatus == fromBreeding) {
                    matchingObservations.add(otherBee);
                }
            }
        }
        matchingObservations.sort(Comparator.comparing(Observation::getTimestamp));
        return new BehaviorIter<>(matchingObservations);
    }

    // --- Methoden von SolitaryBee ---

    /**
     * Iterator über alle Beobachtungen des gleichen Individuums, die solitäres Verhalten zeigen.
     *
     * @post Gibt alle Beobachtungen des gleichen Individuums zurück, aus der eine solitäre
     * (nicht kommunale oder soziale) Lebensweise hervorgeht.
     */
    @Override
    public BehaviorIter<SolitaryBee> solitary() {
        List<Bee> sameIndividuals = super.getSameIndividualObservations();
        List<SolitaryBee> solitaryObservations = new ArrayList<>();

        for (Bee bee : sameIndividuals) {
            if (bee instanceof AndrenaBucephala otherBee) {
                if (otherBee.isSolitary) {
                    solitaryObservations.add((SolitaryBee) bee);
                }

            }
        }
        solitaryObservations.sort(Comparator.comparing(Observation::getTimestamp));
        return new BehaviorIter<>(solitaryObservations);
    }

    // --- Methoden von CommunalBee ---

    /**
     * Gibt einen Iterator über jede Beobachtung des *gleichen Individuums* zurück,
     * aus der eine kommunale Lebensweise dieses Individuums hervorgeht
     *
     * @return Ein BehaviorIter<CommunalBee> über kommunale Beobachtungen
     * desselben Individuums.
     * @pre true
     * @post Liefert einen Iterator, der alle gültigen Beobachtungen desselben
     * Individuums (this.individualIdentifier()) zurückgibt, die als
     * CommunalBee mit kommunalem Verhalten markiert sind.
     */
    @Override
    public BehaviorIter<CommunalBee> communal() {
        List<Bee> sameIndividuals = super.getSameIndividualObservations();
        List<CommunalBee> communalObservations = new ArrayList<>();

        for (Bee bee : sameIndividuals) {
            if (bee instanceof AndrenaBucephala otherBee) {
                if (otherBee.isCommunal) {
                    communalObservations.add((CommunalBee) bee);
                }

            }
        }
        communalObservations.sort(Comparator.comparing(Observation::getTimestamp));
        return new BehaviorIter<>(communalObservations);
    }


}