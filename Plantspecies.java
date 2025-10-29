/**
 * Diese Klasse dient als abstrakte Darstellung einer Pflanzspezies
 * Wobei die Attribute c, f und h eine obere Grenze c+, f+ oder h+ hat und
 * eine untere Grenze c-, f-, h-. Außerdem besitzt jede Pflanzspezies einen Namen,
 * eine Blühintesität q und eine Bestäubungswahrscheinlichkeit p.
 * Diese Klasse hilft der Klasse Plantpopulation, eine Population von einer Pflanzspezies zu erstellen
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
    Konstruktor für alle Pflanzspezies
    Zuerst werden die Grenzen der Werte laut Angabe überprüft,
    dann werden die Werte zugewiessen
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
    public String getName() { return name; }

    public double getcMinus() { return cMinus; }

    public double getCPlus() { return CPlus; }

    public double getfMinus() { return fMinus; }

    public double getfPlus() { return fPlus; }

    public double getQ() { return q; }

    public double getP() { return p; }

    public double getStressFactor() { return stressFactor; }

    public double getMinBloomTemp() { return minBloomTemp; }

    public double getAvgBloomDurationDays() { return avgBloomDurationDays; }

    public double getMinSunHoursToday() { return minSunHoursToday; }
}
