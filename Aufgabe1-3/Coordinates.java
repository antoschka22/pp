/**
 * Dieser Record speichert die Koordinaten eines Punktes
 * und ermittelt den Abstand zu einem anderen Punkt.
 * @param x die x-Koordinate des Punktes
 * @param y die y-Koordinate des Punktes
 */
public record Coordinates(double x, double y) {

    /**
     * Berechnet die euklidische Distanz zwischen this und other.
     * @param other ein anderer Punkt.
     * @return Abstand zwischen this und other.
     *
     * @pre other ist nicht null.
     * @post der Rückgabewert ist >= 0.0.
     */
    public double distanceTo(Coordinates other){
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
