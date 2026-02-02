/**
 * Funktionales Interface zur Überprüfung der Randbedingungen.
 * Arbeitet als Prädikat, das entscheidet, ob eine Lösung im zulässigen
 * Bereich (= Wertebereich w) liegt.
 */
@FunctionalInterface
public interface ConstraintValidator {

    /**
     * Prüft, ob der Vektor innerhalb des definierten Bereichs liegt.
     *
     * @param args der zu prüfende Vektor (Argumente der Funktion)
     * @param bounds Wertegrenzen
     * @return true, wenn alle Argumente innerhalb der Grenzen liegen, sonst falsch.
     */
    boolean isValid(double[] args, double [][] bounds);
}
