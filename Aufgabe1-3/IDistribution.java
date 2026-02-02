/**
 * Interface für eine Wahrscheinlichkeitsverteilung, die einen Wert innerhalb
 * eines bestimmten Bereichs [min, max] generiert
 * Dies dient der Verfeinerung des Modells und ermöglicht es,
 * verschiedene Verteilungsstrategien (z.B. Gleichverteilung, Normalverteilung)
 * per Dependency Injection in die Populationsmodelle einzuspeisen
 * STYLE: Objektorientiert
 * GOOD: Dieses Interface ist ein gutes Beispiel für schwache Kopplung.
 * Klassen wie WildbeePopulation oder Plantpopulation
 * hängen nur von dieser Abstraktion ab, nicht von einer konkreten
 * Implementierung (z.B. GaussianDistribution).
 * Man könnte leicht eine 'UniformDistribution'-Klasse hinzufügen und
 * sie in der Simulation austauschen, ohne eine einzige Zeile
 * in den Populationsklassen ändern zu müssen.
 */
public interface IDistribution {

    /**
     * Generiert eine Double Zufallszahl innerhalb des angegebenen Bereichs
     *
     * @param min die untere Grenze (inklusiv)
     * @param max die obere Grenze (inklusiv)
     * @return eine Zufallszahl zwischen min und max
     * @pre min <= max
     * @post Ein double-Wert 'result' wird zurückgegeben, sodass min <= result <= max.
     */
    double nextDouble(double min, double max);

    /**
     * Generiert eine Integer Zufallszahl innerhalb des angegebenen Bereichs
     *
     * @param min die untere Grenze (inklusiv)
     * @param max die obere Grenze (inklusiv)
     * @return eine Zufallszahl zwischen min und max
     * @pre min <= max
     * @post Ein int-Wert 'result' wird zurückgegeben, sodass min <= result <= max.
     */
    int nextInt(int min, int max);
}