/**
 * Interface CommunalBee: Repräsentiert die Beobachtung einer Biene einer kommunalen Art
 * Kommunale Bienen teilen Nester, aber nicht die Brutpflege
 * Alle kommunalen Bienen können laut Angabe auch solitär leben
 *
 * @invariant Erbt Invarianten von Bee.
 */
public interface CommunalBee extends SolitaryBee {

    /**
     * Gibt einen Iterator über jede Beobachtung des gleichen Individuums zurück,
     * aus der eine kommunale Lebensweise dieses Individuums hervorgeht
     *
     * @return Ein BehaviorIter<CommunalBee> über kommunale Beobachtungen
     * desselben Individuums.
     * @pre true
     * @post Liefert einen Iterator, der alle gültigen Beobachtungen desselben
     * Individuums (this.individualIdentifier()) zurückgibt, die als
     * CommunalBee mit kommunalem Verhalten markiert sind.
     */
    BehaviorIter<CommunalBee> communal();
}