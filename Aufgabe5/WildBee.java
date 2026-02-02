/**
 * Diese Klasse repräsentiert eine Beobachtung einer Wildbiene. Sie implementiert das Interface
 * Modifiable, um die geschätzte Länge der Biene zu verändern und dementsprechend ein neues
 * WildBee-Objekt zurückzugeben.
 */
public class WildBee extends Bee implements Modifiable<Integer, WildBee>{

    private final int length;

    /**
     * Konstruktor, der ein neues WildBee-Objekt mit Beschreibung und gegebener Länge erstellt.
     * @param description Die Beschreibung der Beobachtung.
     * @param length Die geschätzte Länge in Millimetern.
     */
    public WildBee(String description, int length) {
        super(description);
        this.length = length;
    }

    /**
     * Gibt die geschätzte Länge der Wildbiene in Millimetern zurück.
     * @return Der Rückgabewert ist die geschätzte Länge der Wildbiene in Millimetern.
     */
    public Integer length(){
        return length;
    }

    /**
     * Gibt ein neues WildBee-Objekt mit einer um i größeren Länge zurück, nur wenn i > 0 gilt.
     * @param i Die hinzuzufügende Länge.
     * @return Der Rückgabewert ist ein neues WildBee-Objekt, deren Länge sich um i vergrößert hat, falls i > 0 gilt.
     *         Andernfalls wird this mit der aktuellen Länge zurückgegeben.
     */
    @Override
    public WildBee add(Integer i){
        if(i != null && i > 0){
            return new WildBee(super.toString(), this.length + i);
        }
        return this;
    }

    /**
     * Gibt ein neues WildBee-Objekt mit einer um i kürzeren Länge zurück, nur wenn i > 0 und i < aktuelle Länge gilt.
     * @param i Die abzuziehende Länge.
     * @return Der Rückgabewert ist ein neues WildBee-Objekt, deren Länge sich um i verringert hat, falls i > 0 und i < length gilt.
     *         Andernfalls wird this mit der aktuellen Länge zurückgegeben.
     */
    @Override
    public WildBee subtract(Integer i){
        if(i != null && i > 0 && i < length){
            return new WildBee(super.toString(), this.length - i);
        }
        return this;
    }
}
