@ProjectClass
@Author(name = "Miriam Reumann")
@Invariant(condition = "collectedFromX() >= 0 && collectedFromY() >= 0 && collectedFromZ() >= 0")
@HistoryConstraint(condition = "Die Anzahl gesammelter Einheiten darf niemals abnehmen.")
public abstract class Biene {

    // Zähler der bereits aktiven Tage der Biene
    private int aktiveTage;

    // Lebensdauer der Biene in Tagen (wird von Subklasse gesetzt)
    private final int lebensdauer;

    // Zähler für die stattgefundenen Blütenbesuche der Biene pro Pflanzenart
    private int fromX;
    private int fromY;
    private int fromZ;

    @Pre(condition = "lebensdauer > 0")
    @Post(condition = "collectedFromX() == 0 && collectedFromY() == 0 && collectedFromZ() == 0")
    protected Biene(int lebensdauer) {
        this.lebensdauer = lebensdauer;
        this.aktiveTage = 0;
        this.fromX = 0;
        this.fromY = 0;
        this.fromZ = 0;
    }

    @Pre(condition = "isAlive() == true")
    @Post(condition = "aktiveTage == old(aktiveTage) + 1")
    public void nextDay(){ this.aktiveTage++;}

    @Pre(condition = "true")
    @Post(condition = "result == (aktiveTage < lebensdauer)")
    public boolean isAlive(){ return this.aktiveTage < this.lebensdauer;}

    @Pre(condition = "true")
    @Post(condition = "fromX >= 0")
    public int collectedFromX(){ return this.fromX;}

    @Pre(condition = "true")
    @Post(condition = "fromY >= 0")
    public int collectedFromY(){ return this.fromY;}

    @Pre(condition = "true")
    @Post(condition = "fromZ >= 0")
    public int collectedFromZ(){ return this.fromZ;}

    @Pre(condition = "p != null")
    public abstract boolean prefersX(PflanzeX p);

    @Pre(condition = "p != null")
    public abstract boolean prefersY(PflanzeY p);

    @Pre(condition = "p != null")
    public abstract boolean prefersZ(PflanzeZ p);

    @Pre(condition = "p != null")
    public abstract boolean canUseAlternativeX(PflanzeX p);

    @Pre(condition = "p != null")
    public abstract boolean canUseAlternativeY(PflanzeY p);

    @Pre(condition = "p != null")
    public abstract boolean canUseAlternativeZ(PflanzeZ p);

    @Pre(condition = "p != null")
    protected void visitX(PflanzeX p){
        incrementX();
        updatePlantCounter(p);
    }

    @Pre(condition = "p != null")
    protected void visitY(PflanzeY p){
        incrementY();
        updatePlantCounter(p);
    }

    @Pre(condition = "p != null")
    protected void visitZ(PflanzeZ p){
        incrementZ();
        updatePlantCounter(p);
    }

    @Pre(condition = "true")
    @Post(condition = "fromX == old(fromX) + 1")
    protected void incrementX(){ this.fromX++;}

    @Pre(condition = "true")
    @Post(condition = "fromY == old(fromY) + 1")
    protected void incrementY(){ this.fromY++;}

    @Pre(condition = "true")
    @Post(condition = "fromZ == old(fromZ) + 1")
    protected void incrementZ(){ this.fromZ++;}

    protected abstract void updatePlantCounter(Pflanze p);
}
