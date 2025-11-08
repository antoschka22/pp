import java.time.LocalDateTime;

/**
 * Dies ist das zentrale Interface für alle Bienenarten. Es ist zu beachten, dass jede Biene
 * sowohl eine Wasp, als auch ein Pollinator ist.
 */
public interface Bee extends Wasp, Pollinator{

    /**
     * Ein internes Objekt, das alle Beobachtungen eines Individuums erkennt und gruppiert.
     *
     * @pre N/A
     * @post Alle Beobachtungen derselben Biene liefern das gleiche Objekt zurück.
     */
    Object individualIdentifier();

    /**
     * Gibt die Marker-ID zurück.
     * Wenn das Individuum nicht markiert ist, dann ist Marker-ID null.
     *
     * @pre N/A
     * @post Gibt entweder null oder eine Zahl zurück, die eindeutig das markierte Individuum repräsentiert.
     */
    Long markerID();

    /**
     * Iterator über alle Beobachtungen des gleichen Individuums in aufsteigender Reihenfolge.
     *
     * @pre N/A
     * @post Gibt alle Beobachtungen in zeitlich sortiert nach dem Beobachtungszeitraum des gleichen Individuums zurück.
     */
    SameBeeIter sameBee();

    /**
     * Iterator über alle Beobachtungen des gleichen Individuums.
     *
     * @param reverseOrder true: Rückgabe erfolgt in absteigender Reihenfolge
     *                     false: Rückgabe erfolgt in aufsteigender Reihenfolge
     * @pre N/A
     * @post Gibt alle Beobachtungen entsprechend reverseOrder des gleichen Individuums zurück.
     */
    SameBeeIterReverse sameBee(boolean reverseOrder);

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
    SameBeeIterTimeRange sameBee(LocalDateTime from, LocalDateTime to);
}
