/**
 * Interface Wasp: Repräsentiert die Beobachtung eines Tiers einer Wespenart
 * Laut Spezifikation ist jede Biene (Bee) auch eine Wespe (Stechimme)
 *
 * @invariant Erbt Invarianten von Observation
 */
public interface Wasp extends Observation {
    // Dieses Interface fügt keine neuen Methoden hinzu, sondern dient
    // der Typisierung im Vererbungssystem.
}