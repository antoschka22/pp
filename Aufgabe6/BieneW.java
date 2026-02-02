@ProjectClass
@Author(name = "Miriam Reumann")
@Invariant(condition = "collectedFromY() == 0")
public class BieneW extends Biene{

    public BieneW(){ super(10); }

    @Override
    @Pre(condition = "p != null")
    public boolean prefersX(PflanzeX p) {
        return false;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean prefersY(PflanzeY p) {
        return false;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean prefersZ(PflanzeZ p) {
        return true;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean canUseAlternativeX(PflanzeX p) {
        return true;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean canUseAlternativeY(PflanzeY p) {
        return false;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean canUseAlternativeZ(PflanzeZ p) {
        return false;
    }

    @Override
    protected void updatePlantCounter(Pflanze p) {
        p.incrementW();
    }
}
