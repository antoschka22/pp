/**
 * Diese Klasse stellt einen Nistplatz für eine Bienenpopulation mti einer maximalen Kapazität
 * und einem bestimmten Standort dar.
 */
public class NestingSite {
    private final IBeePopulation beePopulation;
    private final double capacity;
    private final Coordinates coordinates;

    /**
     * Konstruktor initialisiert ein neues NestingSite-Objekt
     * @param beePopulation die Bienenpopulation des des Nistplatzes
     * @param capacity die maximale Anzahl an Bienen in einem Nistplatz
     * @param coordinates der genaue Standort des Nistplatzes
     */
    public NestingSite(IBeePopulation beePopulation, double capacity, Coordinates coordinates) {

        if(beePopulation == null){
            throw new IllegalArgumentException("BeePopulation must not be null");
        }
        if(capacity < 0){
            throw new IllegalArgumentException("Capacity must be non-negative");
        }
        if(coordinates == null){
            throw new IllegalArgumentException("Coordinates must not be null");
        }
        this.capacity = capacity; //maximale Anzahl an Bienen in einem Nistplatz.
        this.coordinates = coordinates;
        this.beePopulation = beePopulation;
    }

    /**
     * Gibt die Anzahl der Bienen im Niestplatz aus
     * @return Anzahl der Bienen.
     */
    public IBeePopulation getBeePopulation() {
        return beePopulation;
    }

    /**
     * Gibt die maximale Kapazität des Nistplatzes aus.
     * @return maximale Kapazität
     */
    public double getCapacity() {
        return capacity;
    }

    /**
     * Gibt den Standort des Nistplatzes aus.
     * @return Standort des Nistplatzes.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

}
