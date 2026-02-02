/**
 * Ein Record, der die Ergebnisse eines einzelnen Simulationslaufs
 * strukturiert speichert.
 */
public record SimulationResult(
        int runIndex,         // Der Index des Laufs (z.B. 0-9)
        int groupIndex,       // Die Gruppe (z.B. 0-2)
        int seed,             // Der verwendete Seed
        String resultSummary, // Der formatierte Ergebnis-String
        boolean success,      // Ob der Lauf erfolgreich war
        String errorMessage   // Fehlermeldung, falls !success
) {
    // Statische Factory-Methoden für einfache Erstellung

    /** Erstellt ein erfolgreiches Ergebnis. */
    public static SimulationResult success(int run, int group, int seed, String summary) {
        return new SimulationResult(run, group, seed, summary, true, null);
    }

    /** Erstellt ein fehlerhaftes Ergebnis. */
    public static SimulationResult failure(int run, int group, int seed, String error) {
        return new SimulationResult(run, group, seed, null, false, error);
    }
}