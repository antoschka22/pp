/**
 * Diese Klasse dient als abstrakte Darstellung einer Pflanzenspezies
 * Wobei die Attribute c, f und h eine obere Grenze c+, f+ oder h+ hat und
 * eine untere Grenze c-, f-, h-. Außerdem besitzt jede Pflanzenspezies einen Namen,
 * eine Blühintensität q und eine Bestäubungswahrscheinlichkeit p.
 * Diese Klasse hilft der Klasse Plantpopulation, eine Population von einer Pflanzenspezies zu erstellen.
 * @invariant name != null && !name.isEmpty()
 * @invariant 0.0 <= cMinus  && cMinus <= CPlus
 * @invariant 0.0 < fMinus && fMinus < fPlus && fPlus < 1.0
 * @invariant 0.0 < q && q < 1.0/15.0
 * @invariant 0.0 < p && p <= 1.0
 * @invariant 0.0 < stressFactor && stressFactor < 1.0
 * @invariant 0.0 < minBloomTemp
 * @invariant 0.0 < avgBloomDurationDays
 * @invariant 0.0 <= minSunHoursToday && minSunHoursToday <= 12
 *
 * STYLE: Objektorientiert → Diese Klasse kapselt alle notwendigen Eigenschaften, die eine Pflanzenspezies benötigt,
 *        in einem einzelnen Objekt. Aufgrund der Verwendung von final fields ist die Klasse unveränderlich und daher
 *        bleibt der Zustand des Objekts konsistent. Somit werden also die Grundprinzipien der Objektorientierung,
 *        Kapselung und konsistente Zustandsverwaltung, sichergestellt.
 *
 * GOOD: Alle Variablen dieser Klasse sind private und final. Dadurch wird eine starke Kapselung und eine hohe Kohäsion
 *       gewährleistet und zudem sind die Werte nach der Objekterzeugung unveränderlich.
 */
public class Plantspecies {
    // Final Attribute da die Spezies konstant ist
    private final String name;
    private final double cMinus, CPlus;
    private final double fMinus, fPlus; // Feuchtegrenzen
    private final double q; // Blühintensität
    private final double p; // Bestäubungswahrscheinlichkeit
    private final double stressFactor; // quadratischer StressFaktor
    private final double minBloomTemp;
    private final double avgBloomDurationDays;
    private final double minSunHoursToday; // minimale benötigte Sonnenscheindauer für Blühstart

    /**
     * Konstruktor für alle Pflanzenspezies
     * Zuerst werden die Grenzen der Werte laut Angabe überprüft, dann werden die Werte zugewiesen.
     *
     * @pre name != null && !name.isEmpty()
     * @pre 0.0 <= cMinus  && cMinus <= CPlus
     * @pre 0.0 < fMinus && fMinus < fPlus && fPlus < 1.0
     * @pre 0.0 < q && q < 1.0/15.0
     * @pre 0.0 < p && p <= 1.0
     * @pre 0.0 < stressFactor && stressFactor < 1.0
     * @pre 0.0 < minBloomTemp
     * @pre 0.0 < avgBloomDurationDays
     * @pre 0.0 <= minSunHoursToday && minSunHoursToday <= 12
     * @post Alle notwendigen Variablen für die jeweilige Pflanzenspezies werden initialisiert.
     */
    public Plantspecies(String name,
                        double cMinus, double CPlus,
                        double fMinus, double fPlus,
                        double q, double p,
                        double stressFactor,
                        double minBloomTemp, double avgBloomDurationDays, double minSunHoursToday
    ) {

        // ----------------- Grenzen überprüfen ------------------
        // 0 < fMinus < fPlus < 1
        if (fMinus <= 0 || fPlus <= fMinus || fPlus >= 1) {
            throw new IllegalArgumentException(
                    "Feuchtegrenze für " + name + " ist ungültig. Sollte: 0 < fMinus < fPlus < 1. Eingegebener Wert: fMinus=" + fMinus + ", fPlus=" + fPlus);
        }

        // 0 < q < 1/15
        if(q <= 0 || q >= (double) 1 /15){
            throw new IllegalArgumentException(
                    "Blühintensität für " + name + " ist ungültig. Sollte: 0 < q < 1/15. Eingegebener Wert: q=" + q);
        }

        // 0 < stressFactor < 1
        if(stressFactor <= 0 || stressFactor >= 1){
            throw new IllegalArgumentException(
                    "Stressfaktor für " + name + "ist ungültig. Sollte: 0 < stressFactor < 1. Eingegebener Wert: stressFactor=" + stressFactor);
        }

        // 0 < minBloomTemp
        if(minBloomTemp <= 0){
            throw new IllegalArgumentException(
                    "minBloomTemp für " + name + "ist ungültig. Sollte > 0. Eingegebener Wert: minBloomTemp=" + minBloomTemp);
        }

        // 0 < avgBloomDurationDays
        if(avgBloomDurationDays <= 0){
            throw new IllegalArgumentException(
                    "avgBloomDurationDays für " + name + "ist ungültig. Sollte > 0. Eingegebener Wert: avgBloomDurationDays=" + avgBloomDurationDays);
        }

        // 0 ≤ minSunHoursToday ≤ 12
        if(minSunHoursToday < 0 || minSunHoursToday > 12){
            throw new IllegalArgumentException(
                    "minSunHoursToday für " + name + "ist ungültig. Sollte 0 ≤ minSunHoursToday ≤ 12. Eingegebener Wert: minSunHoursToday=" + minSunHoursToday);
        }

        //Wenn die Grenzen stimmen, werden die Werte eingetragen
        this.name = name;
        this.cMinus = cMinus;
        this.CPlus = CPlus;
        this.fMinus = fMinus;
        this.fPlus = fPlus;
        this.q = q;
        this.p = p;
        this.stressFactor = stressFactor;
        this.minBloomTemp = minBloomTemp;
        this.avgBloomDurationDays = avgBloomDurationDays;
        this.minSunHoursToday = minSunHoursToday;
    }

    // ---------------------------------- GETTER ----------------------------------------------

    /**
     * Gibt den Namen der Pflanzenspezies zurück
     * @return Name der Pflanzenspezies
     *
     * @post Der Rückgabewert ist der Name der Pflanzenspezies.
     */
    public String getName() { return name; }


    /**
     * Gibt die untere Vermehrungsgrenze zurück
     * @return untere Vermehrungsgrenze
     *
     * @post Der Rückgabewert ist die untere Vermehrungsgrenze >= 0.0.
     */
    public double getcMinus() { return cMinus; }


    /**
     * Gibt die obere Vermehrungsgrenze zurück
     * @return obere Vermehrungsgrenze
     *
     * @post Der Rückgabewert ist die obere Vermehrungsgrenze, cMinus <= CPlus.
     */
    public double getCPlus() { return CPlus; }


    /**
     * Gibt die untere Feuchtegrenze zurück.
     * @return untere Feuchtegrenze
     *
     * @post Der Rückgabewert ist die untere Feuchtegrenze, 0.0 < fMinus < fPlus < 1.0.
     */
    public double getfMinus() { return fMinus; }

    /**
     * Gibt die obere Feuchtegrenze zurück
     * @return obere Feuchtegrenze
     *
     * @post Der Rückgabewert ist die obere Feuchtegrenze, fMinus < fPlus < 1.0.
     */
    public double getfPlus() { return fPlus; }

    /**
     * Gibt die Blühintensität zurück
     * @return Blühintensität
     *
     * @post Der Rückgabewert ist die Blühintensität im Bereich (0.0, 1.0/15.0).
     */
    public double getQ() { return q; }

    /**
     * Gibt die Bestäubungswahrscheinlichkeit zurück
     * @return Bestäubungswahrscheinlichkeit
     *
     * @post Der Rückgabewert ist die Bestäubungswahrscheinlichkeit im Bereich (0.0, 1.0].
     */
    public double getP() { return p; }

    /**
     * Gibt den Stressfaktor der Pflanzenspezies zurück
     * @return Stressfaktor der Pflanzenspezies
     *
     * @post Der Rückgabewert ist der Stressfaktor im Bereich (0.0, 1.0).
     */
    public double getStressFactor() { return stressFactor; }

    /**
     * Gibt die minimale Temperatur für den Blühbeginn der Pflanzenspezies zurück
     * @return minimale Temperatur für den Blühbeginn
     *
     * @post Der Rückgabewert ist die minimale Temperatur für den Blühbeginn > 0.0.
     */
    public double getMinBloomTemp() { return minBloomTemp; }

    /**
     * Gibt die durchschnittliche Blühdauer in Tagen der Pflanzenspezies zurück
     * @return durchschnittliche Blühdauer in Tagen
     *
     * @post Der Rückgabewert ist die durchschnittliche Blühdauer in Tagen > 0.0.
     */
    public double getAvgBloomDurationDays() { return avgBloomDurationDays; }

    /**
     * Gibt die minimale Sonnenscheindauer für den Blühstart zurück
     * @return minimale Sonnenscheindauer für Blühstart, die zwischen 0 und 12 liegt
     *
     * @post Der Rückgabewert ist der minimalen Sonnenscheindauer für den Blühstart im Bereich [0.0, 12.0].
     */
    public double getMinSunHoursToday() { return minSunHoursToday; }
}
