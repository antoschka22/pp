/**
 * Person B: Datenstruktur für einen Block
 */
public class BeeBlock {
    // Bereich, den dieser Block abdeckt
    double start;
    double end;
    int numBees;
    boolean processed = false;

    // Platzhalter für das beste Ergebnis in diesem Block
    // Initialisierung mit kleinstmöglichem Wert
    double bestFitness = -Double.MAX_VALUE;

    // WICHTIG (Neu): Wir müssen uns merken, WO das beste Ergebnis war,
    // damit wir dort in der nächsten Runde rekrutieren können.
    double bestPosition = 0.0;

    public BeeBlock(double start, double end, int numBees) {
        this.start = start;
        this.end = end;
        this.numBees = numBees;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}