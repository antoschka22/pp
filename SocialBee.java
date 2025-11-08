/**
 * Dieses Interface dient dazu, Bienen sozialer Arten zu definieren. Soziale Bienen gehen bei der
 * Brutpflege arbeitsteilig vor, d.h. also sie bilden einen Staat. Jede SocialBee ist auch
 * gleichzeitig eine Bee.
 */
public interface SocialBee extends Bee {

    /**
     * Iterator über alle Beobachtungen des gleichen Individuums, die soziales Verhalten zeigen.
     *
     * @pre N/A
     * @post Gibt alle Beobachtungen des gleichen Individuums zurück, die soziales Verhalten zeigen.
     */
    BehaviorIter<SocialBee> social();
}
