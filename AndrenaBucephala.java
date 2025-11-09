import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
public class AndrenaBucephala implements CommunalBee, SolitaryBee {

    // --- Attribute ---
    private final java.time.LocalDateTime timestamp;
    private final String comment;
    private boolean isValid; // Status für valid() und remove()

    // Attribute zur Identifizierung des Individuums
    private final Object individualIdentifier;
    private final Long markerId;


    // --- Konstruktoren ---

    /**
     * Konstruktor für eine neue Beobachtung eines bisher unbekannten Individuums
     * ohne Marker-ID
     *
     * @param timestamp Zeitstempel der Beobachtung.
     * @param comment   Textueller Kommentar.
     * @pre timestamp != null && comment != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.markerID() == null.
     * this.individualIdentifier() liefert ein neues, einzigartiges Objekt.
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = new Object(); // Neues Individuum
        this.markerId = null;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine Beobachtung, die sich auf dasselbe Individuum
     * wie eine frühere Beobachtung bezieht
     *
     * @param timestamp          Zeitstempel der Beobachtung.
     * @param comment            Textueller Kommentar.
     * @param previousObservation Eine frühere Beobachtung desselben Individuums.
     * @pre timestamp != null && comment != null && previousObservation != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.individualIdentifier() == previousObservation.individualIdentifier().
     * this.markerID() == previousObservation.markerID().
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, Bee previousObservation) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = previousObservation.individualIdentifier();
        this.markerId = previousObservation.markerID();

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine neue Beobachtung eines Individuums mit Marker-ID
     *
     * @param timestamp Zeitstempel der Beobachtung.
     * @param comment   Textueller Kommentar.
     * @param markerId  Die numerische Marker-ID.
     * @pre timestamp != null && comment != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.markerID() == markerId.
     * this.individualIdentifier() liefert ein neues, einzigartiges Objekt.
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, long markerId) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = new Object(); // Neues Individuum
        this.markerId = markerId;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    /**
     * Konstruktor für eine Beobachtung, die sich auf ein bekanntes Individuum
     * (durch previousObservation) bezieht und gleichzeitig eine Marker-ID
     * (erneut) angibt
     *
     * @param timestamp          Zeitstempel der Beobachtung.
     * @param comment            Textueller Kommentar.
     * @param previousObservation Eine frühere Beobachtung desselben Individuums.
     * @param markerId           Die numerische Marker-ID.
     * @pre timestamp != null && comment != null && previousObservation != null
     * @post Neues Objekt erstellt. this.valid() == true.
     * this.individualIdentifier() == previousObservation.individualIdentifier().
     * this.markerID() == markerId (oder previousObservation.markerID(), je nach Spezifikation).
     */
    public AndrenaBucephala(LocalDateTime timestamp, String comment, Bee previousObservation, long markerId) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = previousObservation.individualIdentifier();
        this.markerId = markerId;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

    // --- Methoden von Observation ---

    /**
     * @pre true
     * @post Liefert den Zeitstempel (siehe Konstruktor)
     */
    @Override
    public final LocalDateTime getTimestamp() {
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
     * @post Liefert true, wenn remove() nicht gerufen wurde, sonst false.
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

    // --- Methoden von Bee ---

    /**
     * @pre true
     * @post Liefert das Identifikationsobjekt dieses Individuums
     */
    @Override
    public Object individualIdentifier() {
        return this.individualIdentifier;
    }

    /**
     * @pre true
     * @post Liefert die Marker-ID (Long) oder null, wenn nicht markiert
     */
    @Override
    public Long markerID() {
        return this.markerId;
    }

    /**
     * @pre true
     * @post Liefert einen (leeren) Iterator
     */
    @Override
    public SameBeeIter sameBee() {
        List<Bee> sameBees = getSameIndividualObservations();

        sameBees.sort(Comparator.comparing(Observation::getTimestamp));

        return new SameBeeIter(sameBees);
    }

    /**
     * @pre true
     * @post Liefert einen (leeren) Iterator
     */
    @Override
    public SameBeeIterReverse sameBee(boolean reverseOrder) {
        List<Bee> sameBees = getSameIndividualObservations();

        if (reverseOrder) {
            // Absteigend
            sameBees.sort(Comparator.comparing(Observation::getTimestamp).reversed());
        } else {
            // Aufsteigend
            sameBees.sort(Comparator.comparing(Observation::getTimestamp));
        }

        return new SameBeeIterReverse(sameBees);
    }

    /**
     * @pre from != null && to != null
     * @post Liefert einen (leeren) Iterator
     */
    @Override
    public SameBeeIterTimeRange sameBee(LocalDateTime from, LocalDateTime to) {
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

        return new SameBeeIterTimeRange(filteredBees);
    }

    // --- Methoden von WildBee (via SolitaryBee) ---

    /**
     * @pre true
     * @post Liefert einen (leeren) Iterator
     */
    @Override
    public BehaviorIter<WildBee> wild(boolean fromZucht) {
        return new BehaviorIter<>(Collections.emptyList());
    }

    // --- Methoden von SolitaryBee ---

    /**
     * @pre true
     * @post Liefert einen (leeren) Iterator
     */
    @Override
    public BehaviorIter<SolitaryBee> solitary() {
        return new BehaviorIter<>(Collections.emptyList());
    }

    // --- Methoden von CommunalBee ---

    /**
     * @pre true
     * @post Liefert einen (leeren) Iterator
     */
    @Override
    public BehaviorIter<CommunalBee> communal() {
        return new BehaviorIter<>(Collections.emptyList());
    }

    /**
     * Private Hilfsmethode
     * Sammelt alle gültigen Beobachtungen (valid() == true) des gleichen
     * Individuums wie this aus der globalen Liste.
     *
     * @return Eine unsortierte Liste von Bee-Beobachtungen.
     */
    private List<Bee> getSameIndividualObservations() {
        List<Bee> sameIndividuals = new ArrayList<>();
        Object thisId = this.individualIdentifier();

        // Es wird angenommen, dass individualIdentifier() nie null ist,
        // da es im Konstruktor immer initialisiert wird.

        synchronized (ObservationData.ALL_OBSERVATIONS) {
            for (Observation obs : ObservationData.ALL_OBSERVATIONS) {

                // Filterkriterien:
                // 1. Muss gültig sein
                // 2. Muss eine 'Bee' sein (um individualIdentifier() aufrufen zu können)
                // 3. Muss dasselbe Individuum sein (Vergleich über .equals())
                if (obs.valid() && obs instanceof Bee) {
                    Bee beeObs = (Bee) obs;
                    // Stelle sicher, dass beide IDs nicht null sind,
                    // bevor .equals() gerufen wird
                    if (thisId.equals(beeObs.individualIdentifier())) {
                        sameIndividuals.add(beeObs);
                    }
                }
            }
        }
        return sameIndividuals;
    }
}