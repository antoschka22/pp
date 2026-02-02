/**
 * Funktionale Schnittstelle für eine mathematische Zielfunktion f: R^n -> R.
 * Definiert die Abbildung eines n-dimensionalen Vektors auf einen Wert.
 * Dient als generische Typdefinition für alle Optimierungsaufgaben.
 */
@FunctionalInterface
public interface ObjectiveFunction {

    /**
     * Berechnet den Funktionswert f(x) für einen gegebenen Vektor x.
     * Transformiert die Eingaben in eine Bewertung.
     *
     * @param args Der Vektor der Argumente (x1, x2, ..., xn) als Array.
     * @return Der resultierende Funktionswert.
     */
    double apply(double[] args);
}