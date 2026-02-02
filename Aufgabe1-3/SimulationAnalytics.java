import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In dieser Klasse enthält neue Funktionalitäten, um das Programm zu erweitern.
 * Dafür wurden neue Methoden zur Auswertung der Simulationsergebnisse implementiert,
 * die lediglich funktional arbeiten.
 *
 * STYLE: Die Klasse folgt dem funktionalen Paradigma. D.h. also alle Methoden
 * sind statisch und verändern keine Zustände der übergebenen Listen, wodurch
 * referentielle Transparenz sichergestellt wird. Zudem werden ohne jegliche
 * Änderungen durchzuführen, die Daten nur gelesen, um die benötigten Ergebnisse
 * zu berechnen. Es werden Streams, Lambdas und Funktionen höherer Ordnung verwendet.
 */
public class SimulationAnalytics {

    /**
     * Berechnet die durchschnittliche Wuchskraft aller Pflanzen.
     *
     * @param plants Liste von Pflanzenpopulation
     * @return durchschnittliche Wuchskraft
     *
     * @pre plants != null
     * @post Der Rückgabewert ist die durchschnittliche Wuchskraft aller Pflanzen im Bereich [0.0, 1.0].
     *
     * STYLE: Diese Methode wendet das funktionale Paradigma an, weil sie eine reine Berechnung enthält,
     *        keinen veränderlichen Zustand verwendet und immer nur von ihren Eingaben abhängt. Deswegen
     *        ist sie vollständig referentiell transparent.
     */
    public static double calculateAverageVigor(List<IPlantPopulation> plants){
        return plants.stream()
                .mapToDouble(IPlantPopulation :: getVigor)
                .average()
                .orElse(0.0);
    }

    /**
     *
     * Gibt die durchschnittliche Samenqualität pro Pflanzenspezies zurück
     * @param plants Liste von Pflanzenpopulation
     * @return durchschnittliche Samenqualität pro Pflanzenspezies
     *
     * @pre plants != null
     * @post Liefert eine Map mit dem Namen der Pflanzenspezies und deren durchschnittliche Samenqualität.
     *
     * STYLE: Diese Methode gehört zum funktionalen Paradigma, da sie Daten nur ausliest und in eine neue
     *        Struktur abbildet, ohne die ursprüngliche Eingabeliste zu verändern. Zudem beschreibt diese
     *        Methode deklarativ, was berechnet werden soll und somit wird die referentielle Transparenz
     *        eingehalten.
     */
    public static Map<String, Double> getAverageSeedQualityPerSpecies(List<IPlantPopulation> plants){
        return plants.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getSpeciesName().toString(),
                        Collectors.averagingDouble(IPlantPopulation :: getSeedQuality)
                ));
    }

    /**
     *
     * Gibt die durchschnittliche Samenqualität aller Pflanzenspezies zurück
     * @param plants Liste von Pflanzenpopulation
     * @return durchschnittliche Samenqualität aller Pflanzenspezies
     *
     * @pre plants != null
     * @post Liefert den Durchschnitt der Samenqualität.
     *
     * STYLE: Diese Methode ist funktional, weil sie ausschließlich eine mathematische Auswertung durchführt,
     *        ohne dabei irgendwelche Seiteneffekte zu erzeugen und außerdem werden weder globale Daten verändert
     *        noch die übergebene Liste. Dementsprechend bleibt dadurch eine reine Funktion bestehen.
     */
    public static double avgSeedQuality(List<IPlantPopulation> plants){
        return plants.stream()
                .mapToDouble(IPlantPopulation :: getSeedQuality)
                .average()
                .orElse(0.0);
    }

    /**
     *
     * Finden alle Pflanzen, die unter einem bestimmten Wuchskraft-Schwellenwert liegen.
     * @param plants Liste von Pflanzenpopulation
     * @param vigorThreshold Wuchskraft-Schwellenwert
     * @return Liste aller Pflanzen mit vigor < vigorThreshold
     *
     * @pre plants != null
     * @pre vigorThreshold >= 0.0
     * @post Liefert eine Liste mit allen Pflanzen mit einer Wuchskraft kleiner als dem Wuchskraft-Schwellenwert.
     *
     * STYLE: Die Methode gehört zum funktionalen Paradigma, da sie eine neue Menge von Ergebnissen aus den Eingaben
     *        erzeugt, ohne dabei die Eingaben selbst zu modifizieren. Zudem arbeitet die Methode ausschließlich auf den
     *        übergebenen Daten und verändert keine Zustände außerhalb der Methode. Dementsprechend bleibt sie dadurch
     *        funktional und referentiell transparent, weil sie immer das gleiche Ergebnis für die gleiche Eingabe liefert.
     */
    public static List<IPlantPopulation> findPlantsUnderStress(List<IPlantPopulation> plants, double vigorThreshold){
        return plants.stream()
                .filter(p -> p.getVigor() < vigorThreshold)
                .collect(Collectors.toList());
    }

    /**
     *
     * Gibt die x Pflanzenpopulation mit der höchsten Samenqualität zurück.
     * @param plants Liste von Pflanzenpopulation
     * @param x Anzahl der Pflanzenpopulationen
     * @return Liste aller x Pflanzenpopulationen mit der höchsten Samenqualität
     *
     * @pre plants != null
     * @pre x >= 0
     * @post Liefert eine Liste der maximal x Pflanzenpopulationen mit der höchsten Samenqualität.
     *
     * STYLE: Diese Methode wendet das funktionale Paradigma an, weil sie einen neuen Ergebniswert erzeugt
     *        und nichts am bestehenden Zustand des Programms ändert. Sie sortiert und wählt die Listenobjekte
     *        nur aus, ohne außerhalb etwas zu verändern, wodurch referentielle Transparenz sichergestellt wird.
     */
    public static List<IPlantPopulation> getTopSeedQualitySpecies(List<IPlantPopulation> plants, int x){
        return plants.stream()
                .sorted(Comparator.comparing(IPlantPopulation::getSeedQuality).reversed())
                .limit(x)
                .collect(Collectors.toList());
    }

    /**
     *
     * Gibt alle blühenden Pflanzen zurück.
     * @param plants Liste von Pflanzenpopulation
     * @return Liste aller blühenden Pflanzen
     *
     * @pre plants != null
     * @post Liefert eine List mit allen blühenden Pflanzen
     *
     * STYLE: Diese Methode wendet das funktionale Paradigma an, da sie keine Werte überschreibt oder verändert. Der Grund
     *        dafür ist, dass sie lediglich anhand der Eingaben ein neues Ergebnis bestimmt. Außerdem liefert sie bei gleichen
     *        Eingaben immer dasselbe Ergebnis und somit werden außerhalb der Methode keine Zustände verändert (= keine Seiteneffekte).
     */
    public static List<IPlantPopulation> getBloomingSpecies(List<IPlantPopulation> plants){
        return plants.stream()
                .filter(p -> p.getBloomProportion() > 0)
                .collect(Collectors.toList());
    }

    /**
     *
     * Berechnet die Summe der Nahrung der Pflanzen.
     * @param plants Liste von Pflanzenpopulation
     * @return Summe der Nahrung der Pflanzen
     *
     * @pre plants != null
     * @post Liefert die Summe der Nahrung der Pflanzen.
     *
     * STYLE: Diese Methode ist funktional, weil sie lediglich mit unveränderten Daten arbeitet und das
     *        Ergebnis berechnet, ohne etwas zu verändern. Somit hat die Methode keine Nebeneffekte, weil
     *        sie nur die übergebenen Daten auswertet und außerhalb von der Methode nichts verändert. Zudem
     *        liefert sie bei gleichen Eingaben immer denselben Rückgabewert, wodurch sie referentiell
     *        transparent ist.
     */
    public static double calculateTotalFoodSupply(List<IPlantPopulation> plants){
        return plants.stream()
                .mapToDouble(p -> p.getVigor() * p.getBloomProportion())
                .sum();
    }

    /**
     *
     * Findet Pflanzenspezies, die genügend Nahrung für eine bestimmte Bienenpopulation bieten kann.
     * @param plants Liste von Pflanzenpopulation
     * @param beePopulation Bienenpopulation
     * @return Liste von Pflanzen mit genügend Nahrung
     *
     * @pre plants != null
     * @pre beePopulation >= 0.0
     * @post Liefert Liste aller Pflanzen, die ausreichend Nahrung für die Bienenpopulation haben.
     *
     * STYLE: Diese Methode ist funktional, weil sie nur die übergebenen Daten verarbeitet und dann eine neue
     *        unveränderliche Liste erstellt. Außerdem beeinflusst die Methode keine externen Zustände und
     *        dadurch bleibt sie vollständig referentiell transparent.
     */
    public static List<IPlantPopulation> findSufficientSpeciesForBees(List<IPlantPopulation> plants, double beePopulation){
        return plants.stream()
                .filter(p -> (p.getVigor() * p.getBloomProportion()) >= beePopulation)
                .collect(Collectors.toList());
    }
}
