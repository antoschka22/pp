/**
 * Interface für eine Wahrscheinlichkeitsverteilung, die einen Wert innerhalb
 * eines bestimmten Bereichs [min, max] generiert
 * Dies dient der Verfeinerung des Modells und ermöglicht es,
 * verschiedene Verteilungsstrategien (z.B. Gleichverteilung, Normalverteilung)
 * per Dependency Injection in die Populationsmodelle einzuspeisen
 */
public interface IDistribution {

    /**
     * Generiert eine Double Zufallszahl innerhalb des angegebenen Bereichs
     *
     * @param min die untere Grenze (inklusiv)
     * @param max die obere Grenze (inklusiv)
     * @return eine Zufallszahl zwischen min und max
     */
    double nextDouble(double min, double max);

    /**
     * Generiert eine Integer Zufallszahl innerhalb des angegebenen Bereichs
     *
     * @param min die untere Grenze (inklusiv)
     * @param max die obere Grenze (inklusiv)
     * @return eine Zufallszahl zwischen min und max
     */
    int nextInt(int min, int max);
}