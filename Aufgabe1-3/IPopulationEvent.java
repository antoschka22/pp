import java.util.List;

/**
 * Interface für alle simulierbaren Ereignisse.
 * Jedes Event kann auf den Simulationskontext und Pflanzenpopulationen zugreifen,
 * um Störungen zu modellieren.
 * STYLE: Objektorientiert (Abstraktion / Polymorphie). Dieses Interface
 * definiert einen abstrakten "Vertrag" (eine Schnittstelle) für alle
 * Arten von Events. Es ermöglicht der Simulation, eine Liste von
 * 'IPopulationEvent'-Objekten zu verwalten und 'apply()' aufzurufen, ohne die
 * konkrete Implementierung (z.B. 'DroughtEvent' oder 'MowingEvent')
 * zu kennen. Dies ist ein klares Beispiel für lose Kopplung.
 */
public interface IPopulationEvent {

    /**
     * Wendet den Effekt des Ereignisses auf die Simulation an, bezogen auf Population von Pflanzen
     * Diese Methode wird von der Simulations-Hauptschleife aufgerufen
     *
     * @param populations Die Liste aller Populationen in der Simulation
     * @param weather Das aktuelle Wetterobjekt
     *
     * @pre populations und weather != null
     * @post interner Zustand von populations wird aktualisiert.
     */
    void apply(List<IPlantPopulation> populations, IWeather weather);

    /**
     * Gibt den Namen des Events für Logging-Zwecke zurück.
     * @return Name des Events
     *
     * @post der zurückgegebene String ist der nicht-leere Name des Events.
     */
    String getName();
}