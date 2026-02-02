/**
 * Das Interface für das von MSet.before() zurückgegebene Objekt
 * Es dient als spezialisierter Rückgabetyp für MSet, um Typsicherheit zu garantieren
 *
 * Durch die Vererbung von OSetResult ist es automatisch auch 'Ordered' und 'Modifiable'
 * Es verfeinert jedoch die Rückgabetypen der Modifiable-Methoden
 *
 * @param <E> Der Typ der Einträge.
 */
public interface MSetResult<E> extends OSetResult<E> {

    /**
     * Gibt ein neues MSetResult zurück, das um x erweitert wurde.
     *
     * @param x Das hinzuzufügende Element
     * @return Ein neues MSetResult-Objekt, das x enthält, oder this, falls x schon enthalten war
     * @post Das ursprüngliche Objekt (this) bleibt unverändert
     * @post Der Rückgabetyp ist spezifisch MSetResult<E>
     */
    @Override
    MSetResult<E> add(E x);

    /**
     * Gibt ein neues MSetResult zurück, aus dem x entfernt wurde
     *
     * @param x Das zu entfernende Element
     * @return Ein neues MSetResult-Objekt ohne x, oder this, falls x nicht enthalten war
     * @post Das ursprüngliche Objekt (this) bleibt unverändert
     * @post Der Rückgabetyp ist spezifisch MSetResult<E>
     */
    @Override
    MSetResult<E> subtract(E x);
}