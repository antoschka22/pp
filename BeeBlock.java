/**
 * (Eigentlich Person B) - Datenstruktur für einen Block
 */
public class BeeBlock {
    // Bereich, den dieser Block abdeckt
    double start;
    double end;
    int numBees;
    boolean processed = false;

    // Platzhalter für das beste Ergebnis in diesem Block
    double bestFitness = -Double.MAX_VALUE;

    public BeeBlock(double start, double end, int numBees) {
        this.start = start;
        this.end = end;
        this.numBees = numBees;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}