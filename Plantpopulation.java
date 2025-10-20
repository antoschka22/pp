import java.util.Random;

/**
 * Diese Klasse modelliert den dynamischen Zustand und das Verhalten einer bestimmten Pflanzenpopulation.
 * Sie beschreibt, wie Wuchskraft, Blütenanteil und Samenqualität der Pflanzenpopulation auf Wetterereignisse und saisonale Zyklen reagiert.
 */
public class Plantpopulation {
    private final Plantspecies species;
    private final Random random;
    private double vigor; //corresponds to y_i
    private double bloomProportion; //corresponds to b_i
    private double seedQuality; //corresponds to s_i


    /**
     * Konstruktor initialisiert eine neue Pflanzenpopulation.
     *
     * @param species      die Art der Blütenpflanze.
     * @param initialVigor die initiale Wuchskraft der Pflanzenpopulation.
     * @throws IllegalArgumentException Wenn die Pflanzenart null ist oder die initiale Wuchskraft kleiner als 0 ist.
     */
    public Plantpopulation(Plantspecies species, double initialVigor, Random random) {
        if (species == null) {
            throw new IllegalArgumentException("Plantspecies must not be null");
        }
        if (initialVigor < 0.0) {
            throw new IllegalArgumentException("Initial vigor must be non-negative");
        }
        if(random == null){
            throw new IllegalArgumentException("Random must not be null");
        }
        this.species = species;
        this.vigor = initialVigor;
        this.bloomProportion = 0.0;
        this.seedQuality = 0.0;
        this.random = random;
    }

    /**
     * Setzt Blütenanteil und Samenqualität der Pflanzenpopulation für eine neue Wachstumsphase zurück.
     */
    public void resetForNewVegetationPeriod() {
        this.bloomProportion = 0.0;
        this.seedQuality = 0.0;
    }

    /**
     * Führt eine tägliche Aktualisierung der Pflanzenpopulation basierend auf Wetter,
     * Bienenpopulation und Nahrungsangebot aus.
     *
     * @param weather         die aktuellen Wetterbedingungen.
     * @param beePopulation aktuelle Bienenpopulation
     * @param totalFoodSupply Nahrungsangebot durch blühende Pflanzen
     */
    public void updateDaily(Weather weather, double beePopulation, double totalFoodSupply) {
        updateVigor(weather.getSoilMoisture());
        updateBloom(weather.getAccumulatedSunHours(), weather.getSunHoursToday());
        updateSeeds(weather.getSunHoursToday(), beePopulation, totalFoodSupply);
    }

    /**
     * Aktualisiert die Wuchskraft der Pflanzenpopulation.
     * Wenn die Bodenfeuchte niedriger als die untere Feuchtgrenze und höher als die Hälfte der unteren Feuchtgrenze ist,
     * oder höher als die obere Feuchtigkeitsgrenze und niedriger als das Doppelte der oberen Feuchtgrenze ist,
     * dann wird die Wuchskraft um 1 % reduziert.
     * Wenn die Bodenfeuchte niedriger oder gleich der Hälfte der unteren Feuchtgrenze ist,
     * oder höher oder gleich dem Doppelten der oberen Feuchtgrenze ist,
     * dann wird die Wuchskraft um 3 % reduziert.
     *
     * @param f aktuelle Bodenfeuchte
     */
    private void updateVigor(double f) {
        double f_minus = this.species.getfMinus();
        double f_plus = this.species.getfPlus();
        if ((f_minus / 2 < f && f < f_minus) || (f_plus < f && f < 2 * f_plus)) {
            this.vigor *= 0.99;
        } else if (f <= f_minus / 2 || 2 * f_plus <= f) {
            this.vigor *= 0.97;
        }
    }

    /**
     * Aktualisiert den Anteil der in Blüte stehenden Pflanzen.
     * Wenn die aufsummierte Sonnenscheindauer größer gleich der unteren Blühgrenze und kleiner der oberen Blühgrenze ist,
     * dann erhöht sich der Blütenanteil der Pflanzenpopulation um qi * (d + 3) bis zu einem Maximum von 1.
     * Wenn die aufsummierte Sonnenscheindauer größer gleich der oberen Blühgrenze ist,
     * reduziert sich der Blütenanteil der Pflanzenpopulation um qi * (d + 3) bis zu einem Minimum von 0.
     *
     * @param h aufsummierte Werte der Sonnenscheindauer ab Beginn der Vegetationsperiode.
     * @param d Sonnenscheindauer an diesem Tag
     */
    private void updateBloom(double h, double d) {
        double h_minus = this.species.gethMinus();
        double h_plus = this.species.gethPlus();
        if (h_minus <= h && h < h_plus) {
            if (this.bloomProportion < 1.0) {
                this.bloomProportion += this.species.getQ() * (d +  3);
            }
        } else if (h >= h_plus) {
            this.bloomProportion -= this.species.getQ() * (d +  3);
        }
        // Kontrolliert, ob die Blütenrate < 0 oder > 1
        if (this.bloomProportion < 0.0) this.bloomProportion = 0.0;
        if (this.bloomProportion > 1.0) this.bloomProportion = 1.0;
    }

    /**
     * Aktualisiert die Qualität der Samen anhand der Bestäubungswahrscheinlichkeit p.
     * Falls die Bienenpopulation x mindestens so groß wie das Nahrungsangebot n ist,
     * erhöht sich die Qualität der Samen um p * b_i * (d + 1), andernfalls um p * b_i * (d + 1) * (x/n).
     *
     * @param d Sonnenscheindauer an diesem Tag
     * @param x Bienenpopulation
     * @param n Nahrungsangebot
     */
    private void updateSeeds(double d, double x, double n) {
        // Falls keine Nahrung vorhanden ist, wird die Samenqualität nicht verändert (Division durch 0)
        if (n <= 0) {
            return;
        }
        double p = this.species.getP();
        if (x >= n) {
            this.seedQuality += p * this.bloomProportion * (d + 1);
        } else {
            this.seedQuality += p * this.bloomProportion * (d + 1) * (x / n);
        }
        // Samenqualität auf 1 begrenzen
        if(this.seedQuality > 1) this.seedQuality = 1.0;

    }

    /**
     * Aktualisiert Wuchskraft für die Ruhephase der Pflanzenpopulation,
     * indem diese mit der Samenqualität und einer zufälligen Zahl zwischen einer unteren und einer oberen Vermehrungsgrenze multipliziert wird.
     */
    public void updateRestPhase() {
        double c_minus = this.species.getcMinus();
        double c_plus = this.species.getCPlus();
        double randomNumber = this.random.nextDouble() * (Math.nextUp(c_plus) - c_minus) + c_minus;
        this.vigor = this.vigor * this.seedQuality * randomNumber;
    }

    /**
     * Getter-Methode der Wuchskraft.
     * @return Wuchskraft
     */
    public double getVigor() {
        return this.vigor;
    }

    /**
     * Getter-Methode des Blütenanteils.
     * @return aktuellen Blütenanteil
     */
    public double getBloomProportion() {
        return this.bloomProportion;
    }

    /**
     * Getter-Methode
     * @return Name der Pflanzenart
     */
    public String getSpeciesName() {
        return this.species.getName();
    }

    /**
     * Berechnet das Nahrungsangebot der Pflanzenpopulation durch Wuchskraft * Blütenanteil.
     * @return aktuellen Nahrungsangebot
     */
    public double getCurrentFoodSupply() {
        return this.vigor * this.bloomProportion;
    }

    /**
     * Gibt den aktuellen Zustand der Pflanzenpopulation zurück.
     * @return Ein String mit den aktuellen Werten der Pflanzenpopulation.
     */
    @Override
    public String toString() {
        return String.format("Art: %s, Wuchskraft: %.2f, Blütenanteil: %.2f%%, Samenqualität: %.2f%%",
                this.species.getName(),
                this.vigor,
                this.bloomProportion * 100,
                this.seedQuality * 100);
    }
}
