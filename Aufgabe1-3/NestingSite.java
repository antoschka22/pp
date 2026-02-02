/**
 * Diese Klasse stellt einen Nistplatz für eine Bienenpopulation mti einer maximalen Kapazität
 * und einem bestimmten Standort dar.
 * * STYLE: Objektorientiert
 * * @invariant this.beePopulation != null
 * @invariant this.capacity >= 0
 * @invariant this.coordinates != null
 * GOOD: Diese Klasse ist ein gutes Beispiel für hohen Klassenzusammenhalt
 * (High Cohesion) in Form einer reinen Daten-Aggregationsklasse.
 * Sie bündelt drei Konzepte (eine Bienenpopulation, eine Kapazität,
 * einen Standort), die logisch untrennbar zu einem "Nistplatz" gehören.
 * Eine Variante mit niedrigerem Zusammenhalt wäre, wenn die Simulation-Klasse
 * drei separate Listen (List<IBeePopulation>, List<Double> capacities,
 * List<Coordinates> locations) verwalten würde. Dies würde die Komplexität
 * in der Simulation-Klasse erhöhen (Synchronhalten der Indizes) und die
 * konzeptionelle Idee eines "Nistplatzes" verschleiern.
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
     * @pre beePopulation != null
     * @pre capacity >= 0
     * @pre coordinates != null
     * @post Ein neues NestingSite-Objekt ist erstellt und alle Felder sind initialisiert.
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
     * @pre N/A
     * @post Gibt das 'beePopulation'-Objekt zurück.
     */
    public IBeePopulation getBeePopulation() {
        return beePopulation;
    }

    /**
     * Gibt die maximale Kapazität des Nistplatzes aus.
     * @return maximale Kapazität
     * @pre N/A
     * @post Gibt den Wert von 'capacity' zurück.
     */
    public double getCapacity() {
        return capacity;
    }

    /**
     * Gibt den Standort des Nistplatzes aus.
     * @return Standort des Nistplatzes.
     * @pre N/A
     * @post Gibt das 'coordinates'-Objekt zurück.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

}
