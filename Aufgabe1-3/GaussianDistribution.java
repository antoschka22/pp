import java.util.Random;

/**
 * Implementiert eine angenäherte Normalverteilung,
 * deren Werte auf den Bereich [min, max] beschränkt werden.
 * Dies dient der Verfeinerung des Modells, um realistischere
 * Wahrscheinlichkeiten zu simulieren, bei denen Extremwerte seltener
 * auftreten als Werte um den Mittelwert.
 * STYLE: Objektorientiert
 * @invariant random != null
 */
public class GaussianDistribution implements IDistribution {

    private final Random random;

    /**
     * Konstruktor, der den zu verwendenden Zufallszahlengenerator erhält.
     * @param random der Random-Generator
     * * @pre random != null
     * @post Ein neues GaussianDistribution-Objekt ist erstellt und this.random ist initialisiert.
     */
    public GaussianDistribution(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random must not be null");
        }
        this.random = random;
    }

    /**
     * Generiert eine normalverteilte Zufallszahl, die auf den Bereich [min, max]
     * zugeschnitten wird.
     * Der Mittelwert wird als Zentrum des Bereichs angenommen und die
     * Standardabweichung als 1/4 der Bereichsbreite.
     *
     * @param min die untere Grenze (inklusiv)
     * @param max die obere Grenze (inklusiv)
     * @return eine Zufallszahl zwischen min und max
     * @pre min <= max && min >= 0
     * @post Ein double-Wert 'result' wird zurückgegeben, sodass min <= result <= max.
     * * ERROR: Verletzung der Ersetzbarkeit.
     * Das Interface IDistribution.nextDouble(min, max) hat keine Vorbedingung bezüglich min >= 0.
     * Diese Implementierung fügt eine stärkere Vorbedingung (if(min < 0) throw...) hinzu.
     * Ein Client, der gegen IDistribution programmiert und negative 'min'-Werte verwendet,
     * würde bei einer Ersetzung durch GaussianDistribution abstürzen.
     * Zur Korrektur müsste entweder das Interface IDistribution die Vorbedingung min >= 0
     * ebenfalls fordern oder diese Implementierung müsste negative min-Werte zulassen.
     */
    @Override
    public double nextDouble(double min, double max) {
        if(min < 0)
            throw new IllegalArgumentException("Min must not be below 0");

        // Mittelwert als Zentrum des Bereichs
        double mean = (min + max) / 2.0;

        // Standardabweichung so gewählt, dass ca. 95% der Werte
        // vor dem Clamping innerhalb von 2*stddev (also [min, max]) liegen.
        double stddev = (max - min) / 4.0;

        // Generiere normalverteilten Wert
        double value = random.nextGaussian() * stddev + mean;

        // Stelle sicher, dass der Wert im Bereich [min, max] liegt
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Generiert eine normalverteilte Zufalls-Ganzzahl (int), die auf den Bereich [min, max]
     * (beide inklusive) zugeschnitten wird.
     *
     * @param min die untere Grenze (inklusiv)
     * @param max die obere Grenze (inklusiv)
     * @return eine ganzzahlige Zufallszahl zwischen min und max
     * @pre min >= 0 && min <= max
     * @post Ein int-Wert 'result' wird zurückgegeben, sodass min <= result <= max.
     * * ERROR: Verletzung der Ersetzbarkeit (Liskovsches Substitutionsprinzip).
     * Selbes Problem wie bei nextDouble(): Die Vorbedingung min >= 0 ist stärker
     * als im Interface IDistribution.nextInt(min, max) definiert.
     */
    @Override
    public int nextInt(int min, int max) {
        if(min < 0)
            throw new IllegalArgumentException("Min must not be below 0");

        // Berechne Mittelwert und Standardabweichung (exakt wie bei nextDouble).
        double mean = (min + max) / 2.0;
        double stddev = (max - min) / 4.0;

        // Generiere den normalverteilten double-Wert.
        double value = random.nextGaussian() * stddev + mean;

        // "Clampe" den Wert, d.h. beschränke ihn auf den Bereich [min, max].
        double clampedValue = Math.max(min, Math.min(value, max));

        // Runde das Ergebnis auf die nächste Ganzzahl (long) und konvertiere es
        // sicher in einen int. Math.round() sorgt für kaufmännisches Runden.
        return (int) Math.round(clampedValue);
    }
}