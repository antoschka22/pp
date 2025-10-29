import java.util.Random;

/**
 * Implementiert eine angenäherte Normalverteilung,
 * deren Werte auf den Bereich [min, max] beschränkt werden.
 * Dies dient der Verfeinerung des Modells, um realistischere
 * Wahrscheinlichkeiten zu simulieren, bei denen Extremwerte seltener
 * auftreten als Werte um den Mittelwert.
 */
public class GaussianDistribution implements IDistribution {

    private final Random random;

    /**
     * Konstruktor, der den zu verwendenden Zufallszahlengenerator erhält.
     * @param random der Random-Generator
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