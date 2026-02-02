import java.time.LocalDateTime;

/**
 * Interface Observation: Repräsentiert eine Beobachtung im Feldforschungsprojekt
 * Dies ist der Obertyp für alle spezifischen Beobachtungstypen
 * Jede Beobachtung hat einen Zeitstempel und einen Kommentar und kann
 * logisch entfernt werden
 *
 * @invariant getTimestamp() != null
 * @invariant getComment() != null
 */
public interface Observation {

    /**
     * Gibt den Zeitstempel (Datum und Uhrzeit) der Beobachtung zurück.
     *
     * @return Der Zeitstempel der Beobachtung
     * @pre true
     * @post Liefert den Zeitstempel (LocalDateTime) der Beobachtung
     */
    LocalDateTime getTimestamp();

    /**
     * Gibt den beschreibenden Kommentar zur Beobachtung zurück
     *
     * @return Der Kommentartext
     * @pre true
     * @post Liefert den Kommentar (String) der Beobachtung.
     */
    String getComment();

    /**
     * Entfernt die Beobachtung logisch aus dem Datenbestand.
     * Nach dem Aufruf gibt valid() false zurück.
     * Das Objekt selbst und seine Methoden funktionieren weiterhin.
     *
     * @pre true
     * @post this.valid() == false
     */
    void remove();

    /**
     * Prüft, ob die Beobachtung gültig (nicht entfernt) ist.
     *
     * @return true, wenn remove() noch nicht aufgerufen wurde, sonst false.
     * @pre true
     * @post Liefert true, wenn die Beobachtung gültig ist, andernfalls false.
     */
    boolean valid();

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
    BehaviorIter<Observation> later();

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
    BehaviorIter<Observation> earlier();
}