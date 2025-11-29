@ProjectClass
@Author(name = "Max Mustermann")
@Invariant(condition = "visitedByV() == 0") // V kann nicht auf X zurückgreifen
public class PflanzeX extends Pflanze {

    /**
     * Erzeugt eine Pflanze der Art X.
     */
    public PflanzeX() {
        super(9);
    }

    /**
     * Prüft Präferenz
     * Ruft 'b.prefersX(this)' auf
     * - Wenn b eine BieneU ist: return true (U bevorzugt X)
     * - Wenn b eine BieneW ist: return false (W bevorzugt Z)
     * - Wenn b eine BieneV ist: return false (V bevorzugt Y)
     */
    @Override
    @Pre(condition = "b != null")
    public boolean isPreferredBy(Biene b) {
        return b.prefersX(this);
    }

    /**
     * Prüft Alternative
     * Ruft 'b.canUseAlternativeX(this)' auf
     * - Wenn b eine BieneW ist: return true (W kann auf X zurückgreifen)
     * - Wenn b eine BieneV ist: return false (V kann nicht auf X zurückgreifen)
     */
    @Override
    @Pre(condition = "b != null")
    public boolean isAlternativeFor(Biene b) {
        return b.canUseAlternativeX(this);
    }

    /**
     * Führt den Besuch durch
     * Ruft 'b.visitX(this)' auf
     * Dadurch weiß die Biene: "Ich besuche gerade ein X"
     * Die Biene wird dann 'this.incrementU()' aufrufen
     * Eine BieneV würde diese Methode niemals erfolgreich aufrufen dürfen (Invarianten)
     */
    @Override
    @Pre(condition = "b != null")
    public void acceptVisit(Biene b) {
        b.visitX(this);
    }
}