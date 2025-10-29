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
 */
public interface IBeeEvent {
    /**
     * Wendet den Effekt des Ereignisses auf die Simulation an, bezogen auf Population von Bienen
     * Diese Methode wird von der Simulations-Hauptschleife aufgerufen
     *
     * @param currentPopulation Anzahl von Bienen in der Simulation
     * @param weather Das aktuelle Wetterobjekt in der Simulation
     * @return Gibt die neue Bienenpopulation zurück
     */
    double apply(double currentPopulation, IWeather weather);

    /**
     * Gibt den Namen des Events für Logging-Zwecke zurück.
     * @return Name des Events
     */
    String getName();
}
