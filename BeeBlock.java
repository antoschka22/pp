/**
 * Repräsentiert eine "Arbeitseinheit" (Task) für die Worker-Threads.
 * <p>
 * Paradigma-Hintergrund:
 * Um den Verwaltungsaufwand (Overhead) zu minimieren, wird nicht für jede einzelne Biene
 * ein eigener Thread gestartet. Stattdessen werden `b` Bienen zu einem Block zusammengefasst.
 * <p>
 * Diese Klasse dient als:
 * 1. Input-Container: Definiert den Suchbereich und die Anzahl der Bienen für einen Thread.
 * 2. Output-Container: Speichert das lokal beste Ergebnis (Fitness & Position) dieses Blocks.
 * <p>
 * Dies ermöglicht eine Entkopplung der logischen Bienen-Anzahl von der physischen Thread-Anzahl.
 */
public class BeeBlock {
    // --- Eingabedaten für den Thread ---
    // Der Bereich, den dieser Block abdeckt (Intervall [start, end])
    double start;
    double end;
    // Anzahl der Bienen, die in diesem Block simuliert werden sollen (Granularität b)
    int numBees;

    // Status-Flag zur einfachen Verwaltung
    boolean processed = false;

    // --- Ergebnisse der Berechnung (Rückgabewerte) ---
    // Speichert die beste gefundene Fitness innerhalb dieses Blocks.
    // Initialisierung mit -Double.MAX_VALUE, damit jeder gefundene Wert besser ist.
    double bestFitness = -Double.MAX_VALUE;

    // WICHTIG: Wir müssen uns merken, WO das beste Ergebnis war,
    // damit wir diesen Ort in der Rekrutierungsphase als Zentrum für neue lokale Suchen nutzen können.
    double bestPosition = 0.0;

    public BeeBlock(double start, double end, int numBees) {
        this.start = start;
        this.end = end;
        this.numBees = numBees;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}