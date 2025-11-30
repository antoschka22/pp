@ProjectClass
@Author(name = "Antonio Molina Gradischnig")
@Invariant(condition = "visitedByU() == 0") // U kann nicht auf Z zurückgreifen
public class PflanzeZ extends Pflanze {

    /**
     * Erzeugt eine Pflanze der Art Z
     */
    public PflanzeZ() {
        super(10);
    }

    /**
     * Prüft Präferenz
     * Ruft 'b.prefersZ(this)' auf
     * - Wenn b eine BieneW ist: return true (W bevorzugt Z)
     * - Andere: false
     */
    @Override
    @Pre(condition = "b != null")
    public boolean isPreferredBy(Biene b) {
        return b.prefersZ(this);
    }

    /**
     * Prüft Alternative.
     * Ruft 'b.canUseAlternativeZ(this)' auf.
     * - Wenn b eine BieneV ist: return true (V kann auf Z zurückgreifen).
     * - Wenn b eine BieneU ist: return false (U kann nicht auf Z zurückgreifen).
     */
    @Override
    @Pre(condition = "b != null")
    public boolean isAlternativeFor(Biene b) {
        return b.canUseAlternativeZ(this);
    }

    /**
     * Führt den Besuch durch.
     * Ruft 'b.visitZ(this)' auf.
     * BieneW und BieneV können diese Methode nutzen. BieneU nicht.
     */
    @Override
    @Pre(condition = "b != null")
    public void acceptVisit(Biene b) {
        b.visitZ(this);
    }
}