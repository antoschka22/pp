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
    private final double hMinus, hPlus; // Blühgrenzen
    private final double q; // Blühintensität
    private final double p; // Bestäubungswahrscheinlichkeit

    /**
    Konstruktor für alle Pflanzspezies
    Zuerst werden die Grenzen der Werte laut Angabe überprüft,
    dann werden die Werte zugewiessen
     */
    public Plantspecies(String name,
                        double cMinus, double CPlus,
                        double fMinus, double fPlus,
                        double hMinus, double hPlus,
                        double q, double p) {

        // ----------------- Grenzen überprüfen ------------------
        // 0 < fMinus < fPlus < 1
        if (fMinus <= 0 || fPlus <= fMinus || fPlus >= 1) {
            throw new IllegalArgumentException(
                    "Feuchtegrenze für " + name + " ist ungültig. Sollte: 0 < fMinus < fPlus < 1. Eingegebener Wert: fMinus=" + fMinus + ", fPlus=" + fPlus);
        }

        // 0 < hMinus < hPlus
        if (hMinus <= 0 || hPlus <= hMinus) {
            throw new IllegalArgumentException(
                    "Blühgrenze für " + name + " ist ungültig. Sollte: 0 < hMinus < hPlus. Eingegebener Wert: hMinus=" + hMinus + ", hPlus=" + hPlus);
        }

        // 0 < p < 1/(hPlus - hMinus)
        double pUpperbound = 1.0 / (hPlus - hMinus);
        if (p <= 0 || p >= pUpperbound) {
            throw new IllegalArgumentException(
                    "Bestäubungswahrscheinlichkeit für " + name + " ist ungültig. Sollte: 0 < p < " + pUpperbound + ". Eingegebener Wert: p=" + p);
        }

        // 0 < q < 1/15
        if(q <= 0 || q >= (double) 1 /15){
            throw new IllegalArgumentException(
                    "Blühintensität für " + name + " ist ungültig. Sollte: 0 < q < 1/15. Eingegebener Wert: q=" + q);
        }

        //Wenn die Grenzen stimmen, werden die Werte eingetragen
        this.name = name;
        this.cMinus = cMinus;
        this.CPlus = CPlus;
        this.fMinus = fMinus;
        this.fPlus = fPlus;
        this.hMinus = hMinus;
        this.hPlus = hPlus;
        this.q = q;
        this.p = p;
    }

    // ---------------------------------- GETTER ----------------------------------------------
    public String getName() { return name; }

    public double getcMinus() { return cMinus; }

    public double getCPlus() { return CPlus; }

    public double getfMinus() { return fMinus; }

    public double getfPlus() { return fPlus; }

    public double gethMinus() { return hMinus; }

    public double gethPlus() { return hPlus; }

    public double getQ() { return q; }

    public double getP() { return p; }
}
