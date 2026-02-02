import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Repräsentiert eine gefundene Lösung.
 * @param params Die Argumente der Funktion (x1, x2, ... xn)
 * @param value Der berechnete Funktionswert f(x)
 */
public record Solution(double[] params, double value) {

    @Override
    public String toString() {
        //Double-Stream -> formatierte Strings -> zusammengefügter String
        String paramsFormatted = Arrays.stream(params)
                .mapToObj(d -> String.format("%.4f", d))
                .collect(Collectors.joining(", ", "[", "]"));

        return String.format("Ergebnis: %.6f an Position %s", value, paramsFormatted);
    }

}