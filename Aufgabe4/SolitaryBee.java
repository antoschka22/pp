/**
 * Dieses Interface stellt eine Beobachtung einer Biene, die solitär leben kann, dar.
 */

public interface SolitaryBee extends WildBee {
    /**
     * Iterator über alle Beobachtungen des gleichen Individuums, die solitäres Verhalten zeigen.
     *
     * @post Gibt alle Beobachtungen des gleichen Individuums zurück, aus der eine solitäre
     * (nicht kommunale oder soziale) Lebensweise hervorgeht.
     */
    BehaviorIter<SolitaryBee> solitary();

}
