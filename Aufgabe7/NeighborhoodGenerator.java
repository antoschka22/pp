/**
 * Funktionales Interface zur Generierung von Nachbarschaftslösungen.
 * Definiert die Abbildung eines Punktes auf einen neuen Punkt in seiner unmittelbaren Umgebung.
 *
 */
@FunctionalInterface
public interface NeighborhoodGenerator {

    /**
     * Erzeugt eine Position in der Umgebung eines Zentrums.
     * Transformiert den Eingabevektor mithilfe der Reichweite
     * in einen neuen Vektor.
     *
     * @param center Mittelpunkt des Suchfelds
     * @param ranges die Größe des Suchfelds für jede Dimension
     */
    double[] generate(double[] center, double [] ranges);
}
