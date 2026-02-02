import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Diese abstrakte Klasse dient als Hilfsmittel, um alle sameBee Methoden in einer Klasse
 * zu haben und sie in Untertypen von Biene (zB AndrenaBucephala) zu verwenden. Somit erspart man sich
 * denselben Code für sameBee Methoden, die immer gleich aussehen
 * ObservationIterHelper wird erweitert, weil jede Beeuntertypeklasse (zB AndrenaBucephala) ein Untertyp
 * von Observation ist und diese IterHelper benötigt
 */
public abstract class BeeIterHelper extends ObservationIterHelper implements Bee {

    /**
     * Iterator über alle Beobachtungen des gleichen Individuums in aufsteigender Reihenfolge.
     *
     * @pre N/A
     * @post Gibt alle Beobachtungen in zeitlich sortiert nach dem Beobachtungszeitraum des gleichen Individuums zurück.
     */
    @Override
    public BehaviorIter<Bee> sameBee() {
        List<Bee> sameBees = getSameIndividualObservations();

        sameBees.sort(Comparator.comparing(Observation::getTimestamp));

        return new BehaviorIter<>(sameBees);
    }

    /**
     * Iterator über alle Beobachtungen des gleichen Individuums.
     *
     * @param reverseOrder true: Rückgabe erfolgt in absteigender Reihenfolge
     *                     false: Rückgabe erfolgt in aufsteigender Reihenfolge
     * @pre N/A
     * @post Gibt alle Beobachtungen entsprechend reverseOrder des gleichen Individuums zurück.
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
     * Iterator über alle Beobachtungen des gleichen Individuums innerhalb eines Zeitraums, der durch LocalDateTime from
     * und LocalDateTime to bestimmt wird.
     *
     * @param from der Startzeitpunkt des Beobachtungszeitraums (inklusive)
     * @param to der Endzeitpunkt des Beobachtungszeitraums (inklusive)
     * @pre from und to != null
     * @post Gibt alle Beobachtungen des gleichen Individuums innerhalb eines Zeitraums zurück, der durch LocalDateTime
     *       from und LocalDateTime to bestimmt wurde.
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
     * protected Hilfsmethode
     * Sammelt alle gültigen Beobachtungen (valid() == true) des gleichen
     * Individuums wie this aus der globalen Liste.
     *
     * @return Eine unsortierte Liste von Bee-Beobachtungen.
     */
    protected List<Bee> getSameIndividualObservations() {
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
                if (!obs.valid() || !(obs instanceof Bee BeeObs)) {
                    continue;
                }
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
