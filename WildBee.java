/**
 * Dieses Interface stellt die Beobachtung einer Wildbiene dar.
 * Zu den Wildbienen zählen alle heimischen, wild lebenden Bienen,
 * insbesondere solitär lebende Bienen.
 */
public interface WildBee extends Bee {
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
    BehaviorIter<WildBee> wild (boolean fromBreeding);
}
