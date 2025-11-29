@ProjectClass
@Author(name = "Miriam Reumann")
@Invariant(condition = "collectedFromZ() == 0")
public class BieneU extends Biene {

    public BieneU(){ super(9); }

    @Override
    @Pre(condition = "p != null")
    public boolean prefersX(PflanzeX p) {
        return true;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean prefersY(PflanzeY p) {
        return false;
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
        return true;
    }

    @Override
    @Pre(condition = "p != null")
    public boolean canUseAlternativeZ(PflanzeZ p) {
        return false;
    }

    @Override
    protected void updatePlantCounter(Pflanze p) {
        p.incrementU();
    }
}
