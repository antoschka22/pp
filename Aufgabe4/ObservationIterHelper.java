import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Diese abstrakte Klasse dient als Hilfsmittel, um die Methoden earlier und later in einer Klasse
 * zu haben und Untertypen von Observation (zB FlowerFly) zu verwenden. Somit erspart man sich
 * denselben Code für earlier und later Methoden, die immer gleich aussehen
 */
public abstract class ObservationIterHelper implements Observation{
    /**
     * Retourniert einen Iterator über alle Beobachtungen, die zeitlich später
     * als diese (this) stattgefunden haben.
     * Die Iteration erfolgt mit zeitlich näher liegenden Beobachtungen zuerst
     *
     * @return Ein BehaviorIter<Observation> über spätere Beobachtungen.
     * @pre true
     * @post Liefert einen Iterator, der alle gültigen Beobachtungen > this.getTimestamp()
     * in aufsteigender Reihenfolge (relativ zu this) zurückgibt.
     */
    public BehaviorIter<Observation> later(){
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

        laterObservations.sort(Comparator.comparing(Observation::getTimestamp));

        return new BehaviorIter<>(laterObservations);
    }

    /**
     * Retourniert einen Iterator über alle Beobachtungen, die zeitlich früher
     * als diese (this) stattgefunden haben.
     * Die Iteration erfolgt mit zeitlich näher liegenden Beobachtungen zuerst
     *
     * @return Ein BehaviorIter<Observation> über frühere Beobachtungen.
     * @pre true
     * @post Liefert einen Iterator, der alle gültigen Beobachtungen < this.getTimestamp()
     * in absteigender Reihenfolge (relativ zu this) zurückgibt.
     */
    public BehaviorIter<Observation> earlier(){
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
