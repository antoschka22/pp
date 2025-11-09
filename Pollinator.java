/**
 * Interface Pollinator: Repräsentiert die Beobachtung irgendeines Insekts,
 * das als üblicher Bestäuber von Blütenpflanzen bekannt ist
 * Dazu zählen alle Bienen und auch FlowerFly
 *
 * @invariant Erbt Invarianten von Observation
 */
public interface Pollinator extends Observation {
    // Dieses Interface fügt keine neuen Methoden hinzu, sondern dient
    // der Typisierung im Vererbungssystem.
}