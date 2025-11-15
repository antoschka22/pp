/**
 * Diese Klasse repräsentiert eine Beobachtung einer Honigbiene. Sie implementiert das
 * Interface Modifiable, um die Bezeichnung der Art der Biene zu erweitern oder andernfalls
 * zu verkürzen, und ein neues HoneyBee-Objekt zurückzugeben.
 */
public class HoneyBee extends Bee implements Modifiable<String, HoneyBee>{

    private final String sort;

    /**
     * Konstruktor, der ein neues HoneyBee-Objekt mit Beschreibung und Bezeichnung der Art erstellt.
     * @param description Die Beschreibung der Beobachtung.
     * @param sort Die Bezeichnung der Art oder der Zucht.
     */
    public HoneyBee(String description, String sort) {
        super(description);
        this.sort = (sort != null) ? sort : "";
    }

    /**
     * Gibt eine Zeichenkette zurück, die die Art oder Züchtung bezeichnet.
     * @return Der Rückgabewert ist eine Zeichenkette, die die Art oder Züchtung bezeichnet.
     */
    public String sort(){
        return sort;
    }

    /**
     * Gibt ein neues HoneyBee-Objekt, dessen sort-Zeichenkette um s erweitert ist, zurück, nur wenn s nicht leer ist und
     * die aktuelle Sortenbezeichnung s noch nicht enthaltet.
     * @param s Die Sortenbezeichnung, die hinzuzufügen ist.
     * @return Der Rückgabewert ist ein neues HoneyBee-Objekt, dessen sort-Zeichenkette um s erweitert ist, zurück, nur wenn
     *         s != null und !this.sort.contains(s). Andernfalls wird this mit der aktuellen Sortenbezeichnung zurückgegeben.
     */
    @Override
    public HoneyBee add(String s){
        if(s != null && !s.isEmpty() && !this.sort.contains(s)){
            return new HoneyBee(super.toString(), this.sort + s);
        }
        return this;
    }

    /**
     * Gibt ein neues HoneyBee-Objekt, bei dem s aus der sort-Zeichenkette entfernt wurde, zurück, nur wenn s in der
     * aktuellen Sortenbezeichnung vorkommt.
     * @param s Die Sortenbezeichnung, die zu entfernen ist.
     * @return Der Rückgabewert ist ein neues HoneyBee-Objekt, bei dem s aus der sort-Zeichenkette entfernt wurde, zurück,
     *         nur wenn s != null und this.sort.contains(s). Andernfalls wird this mit der aktuellen Sortenbezeichnung
     *         zurückgegeben.
     */
    @Override
    public HoneyBee subtract(String s){
        if(s != null && !s.isEmpty() && this.sort.contains(s)){
            String newSort = this.sort.replace(s, "");
            return new HoneyBee(super.toString(), newSort);
        }
        return this;
    }
}
