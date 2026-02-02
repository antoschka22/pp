import java.time.LocalDateTime;

/**
 * Klasse FlowerFly: Repräsentiert eine Beobachtung einer Schwebfliege
 * Schwebfliegen sind Pollinator, aber explizit
 * *nicht* mit Bienen oder Wespen verwandt
 *
 * @invariant timestamp != null
 * @invariant comment != null
 * @invariant (isValid == true) || (isValid == false)
 */
public class FlowerFly extends ObservationIterHelper implements Pollinator {

    private final java.time.LocalDateTime timestamp;
    private final String comment;
    private boolean isValid; // Status für valid() und remove()

    /**
     * Konstruktor für eine FlowerFly-Beobachtung.
     *
     * @param timestamp Zeitstempel der Beobachtung.
     * @param comment   Textueller Kommentar zur Beobachtung.
     * @pre timestamp != null && comment != null
     * @post Ein neues FlowerFly-Objekt ist erstellt.
     */
    public FlowerFly(LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;

        ObservationData.ALL_OBSERVATIONS.add(this);
    }

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
}