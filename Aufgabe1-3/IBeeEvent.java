/**
 * Interface für alle simulierbaren Ereignisse.
 * Jedes Event kann auf den Simulationskontext und Bienenpopulationen zugreifen,
 * um Störungen zu modellieren.
 * STYLE: Objektorientiert (Abstraktion / Polymorphie). Dieses Interface
 * definiert einen abstrakten "Vertrag" (eine Schnittstelle) für alle
 * Arten von Events. Es ermöglicht der Simulation, eine Liste von
 * 'IBeeEvent'-Objekten zu verwalten und 'apply()' aufzurufen, ohne die
 * konkrete Implementierung (z.B. 'PesticideEvent')
 * zu kennen. Dies ist ein klares Beispiel für lose Kopplung.
 * GOOD: Dieses Interface ist ein gutes Beispiel für die Nutzung von
 * dynamischer Bindung (Polymorphie) zur Vereinfachung des Programmcodes.
 * Die Simulation-Klasse muss nicht eine Kette von
 * if (event instanceof PesticideEvent) ... else if (event instanceof ...)
 * verwenden. Sie ruft einfach triggeredEvent.apply() auf.
 * Das Hinzufügen neuer Bienen-Events (zB DiseaseEvent) erfordert
 * keine Änderung am Code der Simulation-Klasse, was dem
 * Open/Closed-Prinzip entspricht und die Wartbarkeit stark verbessert.
 */
public interface IBeeEvent {
    /**
     * Wendet den Effekt des Ereignisses auf die Simulation an, bezogen auf Population von Bienen
     * Diese Methode wird von der Simulations-Hauptschleife aufgerufen
     *
     * @param currentPopulation Anzahl von Bienen in der Simulation
     * @param weather Das aktuelle Wetterobjekt in der Simulation
     * @return Gibt die neue Bienenpopulation zurück
     * @pre currentPopulation >= 0 && weather != null
     * @post Gibt die modifizierte Bienenpopulation als double zurück.
     * Der Rückgabewert muss >= 0 sein.
     * Der Zustand von 'weather' kann modifiziert werden (z.B. wenn ein Event
     * auch das Wetter beeinflusst, obwohl es ein 'BeeEvent' ist).
     */
    double apply(double currentPopulation, IWeather weather);

    /**
     * Gibt den Namen des Events für Logging-Zwecke zurück.
     * @return Name des Events
     * @pre N/A
     * @post Gibt einen nicht-leeren String zurück, der das Event beschreibt.
     */
    String getName();
}
