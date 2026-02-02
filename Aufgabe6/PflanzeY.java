@ProjectClass
@Author(name = "Antonio Molina Gradischnig")
@Invariant(condition = "visitedByW() == 0") // W kann nicht auf Y zurückgreifen
public class PflanzeY extends Pflanze {

    @Pre(condition = "lebensdauer > 0")
    @Post(condition = "visitedByU() == 0 && visitedByV() == 0 && visitedByW() == 0")
    public PflanzeY() {
        super(8);
    }

    /**
     * Prüft Präferenz
     * Ruft 'b.prefersY(this)' auf
     * - Wenn b eine BieneV ist: return true (V bevorzugt Y)
     * - Andere: false
     */
    @Override
    @Pre(condition = "b != null")
    public boolean isPreferredBy(Biene b) {
        return b.prefersY(this);
    }

    /**
     * Prüft Alternative.
     * Ruft 'b.canUseAlternativeY(this)' auf
     * - Wenn b eine BieneU ist: return true (U kann auf Y zurückgreifen)
     * - Wenn b eine BieneW ist: return false (W kann nicht auf Y zurückgreifen)
     */
    @Override
    @Pre(condition = "b != null")
    public boolean isAlternativeFor(Biene b) {
        return b.canUseAlternativeY(this);
    }

    /**
     * Führt den Besuch durch
     * Ruft 'b.visitY(this)' auf
     * BieneU und BieneV können diese Methode nutzen. BieneW nicht
     */
    @Override
    @Pre(condition = "b != null")
    public void acceptVisit(Biene b) {
        b.visitY(this);
    }
}