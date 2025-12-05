import java.util.Arrays;

/**
 * Unveränderlicher Container mit Parametern des Bienenalgorithmus (BA).
 *
 */
public record AlgorithmConfig(
        int a, //Anzahl der Parameter der zu untersuchenden Funktion
        int t, //Anzahl der Suchschritte nach denen abgebrochen wird
        int n, //Anzahl der Kundschafterinnen

        int m, //Anzahl der Felder, die weiter untersucht werden
        int e, //Anzahl der exzellenten Felder, die sehr genau untersucht werden
        int p, //Anzahl der für ein exzellentes Feld rekrutierten Bienen
        int q, //Anzahl der für ein anderes Feld rekrutierten Bienen
        double s, //Feldgröße (relativ, z.b. 0.1)
        int r, //Anzahl der Rückgabewerte
        double [][] w //Wertebereich aller Argumente: Index 0 = Min, Index 1 = Max
) {

    /**
     * Konstruktor zur Validierung der Konfiguration.
     */
    public AlgorithmConfig {
        if(a <= 0) throw new IllegalArgumentException("a muss größer als 0 sein.");
        if(n <= 0) throw new IllegalArgumentException("n muss größer als 0 sein.");
        if (m >= n) throw new IllegalArgumentException("m muss kleiner als n sein.");
        if (e >= m) throw new IllegalArgumentException("e muss kleiner als m sein.");
        if (q >= p) throw new IllegalArgumentException("q muss kleiner als p sein (q < p).");
        if (t <= 0) throw new IllegalArgumentException("Anzahl der Schritte t muss positiv sein.");
        if (s <= 0 || s > 1.0) throw new IllegalArgumentException("Feldgröße s muss zwischen 0.0 und 1.0 liegen.");
        if (r <= 0) throw new IllegalArgumentException("Anzahl Rückgabewerte r muss > 0 sein.");

        if(w == null){
            throw new IllegalArgumentException("Wertebereich darf nicht null sein");
        }
        //Prüfung der Spaltenanzahl
        if(w.length != a){
            throw new IllegalArgumentException("w muss genau a Einträge enthalten (einen pro Parameter)");
        }
        //Prüfung der Grenzen pro Zeile
        boolean isValid = Arrays.stream(w).allMatch(range ->
                range != null && range.length == 2 && range[0] < range[1]
        );

        if (!isValid) {
            throw new IllegalArgumentException(
                    "Jeder Eintrag in w muss [min, max] enthalten mit min < max."
            );
        }
    }
}