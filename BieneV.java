@ProjectClass
@Author(name = "Miriam Reumann")
@Invariant(condition = "collectedFromX() == 0")
public class BieneV extends Biene{

    public BieneV(){ super(8); }

    @Override
    @Pre(condition = "p != null")
    public boolean prefersX(PflanzeX p) {
        return false;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean prefersY(PflanzeY p) {
        return true;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean prefersZ(PflanzeZ p) {
        return false;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean canUseAlternativeX(PflanzeX p) {
        return false;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean canUseAlternativeY(PflanzeY p) {
        return false;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean canUseAlternativeZ(PflanzeZ p) {
        return true;
    }

    @Override
    protected void updatePlantCounter(Pflanze p) {
        p.incrementV();
    }
}
