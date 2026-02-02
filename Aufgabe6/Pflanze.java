@ProjectClass
@Author(name = "Antonio Molina Gradischnig")
@Invariant(condition = "visitedByU() >= 0 && visitedByV() >= 0 && visitedByW() >= 0")
@HistoryConstraint(condition = "Die Anzahl der Besuche darf niemals abnehmen.")
public abstract class Pflanze {

    private int countU;
    private int countV;
    private int countW;

    private int tageAktiv;
    private final int maximaleLebensdauer;

    /**
     * Initialisiert eine Pflanze
     * Pflanzen blühen für eine feste Anzahl von Tagen (X=9, Y=8, Z=10)
     * Anfangs sind alle Besuchszähler auf 0
     * @param lebensdauer Die Anzahl der Tage, die diese Pflanze blüht
     */
    @Pre(condition = "lebensdauer > 0")
    @Post(condition = "visitedByU() == 0 && visitedByV() == 0 && visitedByW() == 0")
    protected Pflanze(int lebensdauer) {
        this.maximaleLebensdauer = lebensdauer;
        this.tageAktiv = 0;
        this.countU = 0;
        this.countV = 0;
        this.countW = 0;
    }

    /**
     * Simuliert das Altern der Pflanze um einen Tag
     * Die Pflanze ist nur aktiv, solange 'tageAktiv' kleiner als die 'maximaleLebensdauer' ist
     * Dies wird für die Simulation über den Zeitraum benötigt
     */
    @Pre(condition = "isAlive() == true")
    @Post(condition = "tageAktiv == old(tageAktiv) + 1")
    public void nextDay() {
        this.tageAktiv++;
    }

    /**
     * Prüft, ob die Pflanze noch blüht (Nektar anbietet)
     * Laut Angabe sammeln Bienen Nektar bzw. Pflanzen blühen für eine bestimmte Zeit
     * @return true, wenn die Pflanze noch nicht verblüht ist
     */
    @Pre(condition = "true")
    public boolean isAlive() {
        return this.tageAktiv < this.maximaleLebensdauer;
    }


    /**
     * Gibt zurück, wie oft Blüten dieser Pflanze von Bienen der Art U (U) besucht wurden
     */
    @Pre(condition = "true")
    public int visitedByU() {
        return countU;
    }

    /**
     * Gibt zurück, wie oft Blüten dieser Pflanze von Bienen der Art V besucht wurden
     */
    @Pre(condition = "true")
    public int visitedByV() {
        return countV;
    }

    /**
     * Gibt zurück, wie oft Blüten dieser Pflanze von Bienen der Art W besucht wurden
     */
    @Pre(condition = "true")
    public int visitedByW() {
        return countW;
    }

    // --- Interne Methoden zum Hochzählen (Technik: Double Dispatch) ---

    /**
     * Erhöht den Zähler für Besuche durch Biene U
     * Wird von der BieneU über 'acceptVisit' indirekt aufgerufen
     */
    @Pre(condition = "true")
    @Post(condition = "countU == old(countU) + 1")
    protected void incrementU() {
        this.countU++;
    }

    /**
     * Erhöht den Zähler für Besuche durch Biene V
     * Wird von der BieneV über 'acceptVisit' indirekt aufgerufen
     */
    @Pre(condition = "true")
    @Post(condition = "countV == old(countV) + 1")
    protected void incrementV() {
        this.countV++;
    }

    /**
     * Erhöht den Zähler für Besuche durch Biene W
     * Wird von der BieneW über 'acceptVisit' indirekt aufgerufen
     */
    @Pre(condition = "true")
    @Post(condition = "countW == old(countW) + 1")
    protected void incrementW() {
        this.countW++;
    }

    // --- Abstrakte Methoden für Typauflösung ohne "instanceof" ---

    /**
     * Ermittelt per Double Dispatch, ob diese Pflanze von der übergebenen Biene bevorzugt wird
     * Dient dazu, die Regel "bevorzugt X, kann auf Y zurückgreifen..." umzusetzen,
     * ohne den Typ der Pflanze explizit abzufragen
     */
    @Pre(condition = "b != null")
    public abstract boolean isPreferredBy(Biene b);

    /**
     * Ermittelt per Double Dispatch, ob diese Pflanze für die übergebene Biene eine Alternative darstellt
     */
    @Pre(condition = "b != null")
    public abstract boolean isAlternativeFor(Biene b);

    /**
     * Nimmt einen Besuch entgegen.
     * Hier wird das Visitor-Pattern angewandt: Die Pflanze ruft eine Methode auf der Biene auf (z.B. b.visitX(this)),
     * damit die Biene weiß, welche Pflanze sie gerade besucht, und den korrekten Zähler (increment...) erhöhen kann
     */
    @Pre(condition = "b != null")
    public abstract void acceptVisit(Biene b);
}