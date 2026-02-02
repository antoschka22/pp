/**
 * Diese Klasse modelliert den dynamischen Zustand und das Verhalten einer bestimmten Pflanzenpopulation.
 * Sie beschreibt, wie Wuchskraft, Blütenanteil und Samenqualität der Pflanzenpopulation auf Wetterereignisse und saisonale Zyklen reagiert.
 * @invariant vigor >= 0.0 && vigor <= 1.0
 * @invariant bloomProportion >= 0.0 && bloomProportion <= 1.0
 * @invariant seedQuality >= 0.0 && seedQuality <= 1.0
 * @invariant species != null
 * @invariant coordinates != null
 * @invariant restPhaseDistribution != null
 *
 * BAD: Geringer Klassenzusammenhalt. Diese Klasse ist für zwei unterschiedliche Dinge zuständig: den biologischen Zustand einer Pflanze
 * und den Standort einer Pflanze. Besser wäre es, diese beiden Aufgaben zu trennen: in eine KLasse Plantpopulation, die den biologischen Zustand einer Pflanze speichert
 * und einer Grid-Klasse, die den Standort der Pflanzen (und auch Nistplätze) speichert.
 */
public class Plantpopulation implements IPlantPopulation {
    private final Plantspecies species;
    private double vigor; //corresponds to y_i
    private double bloomProportion; //corresponds to b_i
    private double seedQuality; //corresponds to s_i
    private final Coordinates coordinates;
    private final IDistribution restPhaseDistribution;
    private boolean hasBloomed; // hatte Pflanze bereits Blühstart?
    private double bloomCount;


    /**
     * Konstruktor initialisiert eine neue Pflanzenpopulation.
     *
     * @param species      die Art der Blütenpflanze.
     * @param initialVigor die initiale Wuchskraft der Pflanzenpopulation.
     * @throws IllegalArgumentException Wenn die Pflanzenart null ist oder die initiale Wuchskraft kleiner als 0 ist.
     *
     * @pre species != null
     * @pre initialVigor >= 0.0 && initialVigor <= 1.0
     * @pre restPhaseDistribution != null
     * @pre coordinates != null
     * @post Die Pflanzenart, die Restphasen-Verteilung und die Koordinaten werden initialisiert.
     * @post vigor == initialVigor
     * @post bloomProportion == 0.0 && seedQuality == 0.0
     */
    public Plantpopulation(Plantspecies species, double initialVigor, IDistribution restPhaseDistribution, Coordinates coordinates) {
        if (species == null) {
            throw new IllegalArgumentException("Plantspecies must not be null");
        }
        if (initialVigor < 0.0) {
            throw new IllegalArgumentException("Initial vigor must be non-negative");
        }
        if(restPhaseDistribution == null){
            throw new IllegalArgumentException("restPhaseDistribution must not be null");
        }
        if(coordinates == null){
            throw new IllegalArgumentException("Coordinates must not be null");
        }
        this.species = species;
        this.vigor = initialVigor;
        this.bloomProportion = 0.0;
        this.seedQuality = 0.0;
        this.coordinates = coordinates;
        this.restPhaseDistribution = restPhaseDistribution;
        this.hasBloomed = false;
        this.bloomCount = 0.0;
    }

    /**
     * Setzt den Zustand der Population für eine neue Vegetationsperiode zurück
     * (z.B. Zurücksetzen von Blütenanteil, Samenqualität und Blühstatus)
     *
     * @post bloomProportion, seedQuality und bloomCount werden auf 0.0 gesetzt.
     * @post hasBloomed wird auf false gesetzt
     */
    @Override
    public void resetForNewVegetationPeriod() {
        this.bloomProportion = 0.0;
        this.seedQuality = 0.0;
        this.hasBloomed = false;
        this.bloomCount = 0.0;
    }

    /**
     * Aktualisiert den Zustand der Population basierend auf dem täglichen Wetter und Umweltfaktoren
     *
     * @param weather         Die täglichen Wetterdaten
     * @param beePopulation   Die aktuelle Bienenpopulation
     * @param totalFoodSupply Das gesamte Nahrungsangebot aller Populationen
     *
     * STYLE: Objektorientiert (Polymorphie, Delegation)
     * Die Verwendung der Abstraktion IWeather erlaubt Polymorphie (Austauschbarkeit der Wettermodelle).
     * Die Methode delegiert alle Aufgaben: Sie fragt Daten beim IWeather-Objekt ab und delegiert die Zustandsveränderung
     * (Vigor, Bloom, Seeds) an interne private Methoden. Dies sorgt für eine hohe Klassenzusammengehörigkeit.
     *
     * @pre weather != null
     * @pre beePopulation >= 0.0
     * @pre totalFoodSupply >= 0.0
     * @post Die Methoden updateVigor, updateBloom und updateSeeds werden intern aufgerufen,
     *       und dabei werden Wuchskraft, Blütenanteil und Samenqualität gemäß der Tagesbedingungen
     *       angepasst.
     */
    @Override
    public void updateDaily(IWeather weather, double beePopulation, double totalFoodSupply) {
        updateVigor(weather.getSoilMoisture());
        updateBloom(weather);
        updateSeeds(weather.getSunHoursToday(), beePopulation, totalFoodSupply);
    }


    /**
     * Aktualisiert die Wuchskraft der Pflanzenpopulation.
     * Die Wuchskraft einer Pflanze soll sich bei geringfügiger und kurzzeitiger Überschreitung
     * der Feuchtegrenzen nur geringfügig auswirken, bei starker oder lang anhaltender Überschreitung
     * dagegen stark, mit kontinuierlichen Verläufen statt sprunghaften Änderungen.
     *
     * BAD: hier besteht eine hohe Objektkoppelung. Die Plantpopulation muss viel über die internen Zustände von Plantspecies wissen,
     * um die Berechnung der Wuchskraft durchführen zu können (getfMinus(), getfPlus(), getStressFactor()). Sinnvoller wäre diese Berechnungen
     * in der Plantspecies-Klasse durchzuführen und dann auf die Berechnung hier zuzugreifen (double stress = this.species.calculateStress(double f)).
     * Somit wäre die Objektkoppelung zwischen Plantpopulation und Plantspecies schwächer.
     *
     * @param f aktuelle Bodenfeuchte
     *
     * @pre 0 <= f  && f <= 1.0
     * @post vigor wird um einen quadratischen Stressfaktor reduziert
     */
    private void updateVigor(double f) {
        double f_minus = this.species.getfMinus();
        double f_plus = this.species.getfPlus();
        double stress = 0.0;
        double k = species.getStressFactor(); // k = quadratischer Stressfaktor

        if (f < f_minus) {
            stress = k * Math.pow(f_minus - f, 2);
        } else if (f > f_plus) {
            stress = k * Math.pow(f - f_plus, 2);
        }

        this.vigor *= (1.0 - stress);
        if(this.vigor < 0.0) this.vigor = 0.0; // negative Wuchskraft nicht erlaubt
        this.vigor = Math.max(this.vigor, 50); // sicherstellen, dass Pflanze überlebt → 50 % von initialVigor
    }

    /**
     * Aktualisiert den Anteil der in Blüte stehenden Pflanzen.
     * Basierend auf den aktuellen Wetterdaten (Temperatur & Sonnenscheindauer) wird der
     * Blühstart der Pflanzenpopulation festgelegt. Nachdem der Blühstart erfolgt, werden
     * die verbleibenden Blühtage bestimmt und anschließend täglich reduziert, bis das
     * Ende der Blühphase erreicht wurde.
     *
     * @param weather aktuellen Wetterdaten an diesem Tag
     * STYLE: Objektorientiert → Innerhalb dieser Methode wird die Blühlogik gekapselt,
     *        d.h. also, dass keine externe Klasse weiß, wie die Aktualisierung der Blüte
     *        berechnet wird. Da die Methode private ist, kann sie von außen nicht verändert
     *        werden und Variablen wie vigor oder bloomProportion können nur innerhalb der
     *        Klasse verändert werden.
     *
     * @pre weather != null
     * @post 0 <= bloomProportion && bloomProportion <= 1.0
     * @post wenn die Pflanze bereits einmal geblüht hat, dann bleibt hasBloomed auf true gesetzt
     */
    private void updateBloom(IWeather weather) {
        double temperature = weather.getTemperature();
        double sunHoursToday = weather.getSunHoursToday();
        double speciesMinBloomTemp = this.species.getMinBloomTemp();
        double speciesMinBloomSunHours = this.species.getMinSunHoursToday();

        // Erfüllt Pflanze die Blühbedingung für Blühstart
        boolean canBloom = temperature > speciesMinBloomTemp && sunHoursToday > speciesMinBloomSunHours;

        // Pflanze kann starten mit Blühen, wenn Blühstart noch nicht war & Blühbedingung für Blühstart gegeben ist
        if(!hasBloomed && canBloom){
            hasBloomed = true;
            bloomCount = 0.0;
        }

        if(hasBloomed){
            bloomCount++;
            bloomProportion = Math.min(1.0, bloomProportion + species.getQ() * sunHoursToday); // Grenzen einhalten: 0 ≤ bloomProportion ≤ 1

            // Ende der Blühphase
            int randomNum = (int) (Math.random() * 5);
            if(bloomCount > this.species.getAvgBloomDurationDays() + randomNum){
                hasBloomed = false;
                bloomProportion = 0.0;
            }
        }
    }

    /**
     * Aktualisiert die Qualität der Samen anhand der Bestäubungswahrscheinlichkeit p.
     * Falls die Bienenpopulation x mindestens so groß wie das Nahrungsangebot n ist,
     * erhöht sich die Qualität der Samen um p * b_i * (d + 1), andernfalls um p * b_i * (d + 1) * (x/n).
     *
     * @param d Sonnenscheindauer an diesem Tag
     * @param x Bienenpopulation
     * @param n Nahrungsangebot
     *
     * @pre d >= 0.0
     * @pre x >= 0.0
     * @pre n >= 0.0
     * @post seedQuality wird entsprechend der übergebenen Parameter erhöht
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
     *
     * @post vigor ist basierend auf der aktuellen seedQuality und einem Zufallswert aus der Restphasen-Verteilung gesetzt
     */
    public void updateRestPhase() {
        double c_minus = this.species.getcMinus();
        double c_plus = this.species.getCPlus();
        double randomNumber = this.restPhaseDistribution.nextDouble(c_minus, c_plus);
        this.vigor = this.vigor * this.seedQuality * randomNumber;
    }

    /**
     * Setzt die Wuchskraft direkt.
     * Wird von Events (z.B. MowingEvent) genutzt.
     * @param newVigor Der neue Wert für die Wuchskraft.
     *
     * @pre newVigor >= 0.0
     * @post vigor ist auf newVigor gesetzt
     */
    public void setVigor(double newVigor) {
        this.vigor = newVigor < 0 ? 0 : newVigor;
    }

    /**
     * Setzt den Blütenanteil direkt.
     * Wird von Events (z.B. MowingEvent) genutzt.
     * @param newBloomProportion Der neue Wert für den Blütenanteil (sollte 0-1 sein).
     *
     * @pre  newBloomProportion im Bereich [0.0, 1.0]
     * @post bloomProportion ist auf newBloomProportion gesetzt
     */
    public void setBloomProportion(double newBloomProportion) {
        if (newBloomProportion < 0.0) newBloomProportion = 0.0;
        if (newBloomProportion > 1.0) newBloomProportion = 1.0;
        this.bloomProportion = newBloomProportion;
    }

    /**
     * Gibt die Koordinaten der Pflanzenart dieser Population zurück
     * @return Koordinaten der Pflanzenart
     *
     * @post Der Rückgabewert sind die Koordinaten der Pflanzenart dieser Population.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gibt den Namen der Pflanzenspezies dieser Population zurück
     * @return Name der Pflanzenspezies
     *
     * @post Der Rückgabewert ist der Name der Pflanzenspezies dieser Population.
     */
    public Object getSpeciesName() {
        return species.getName();
    }

    /**
     * Berechnet das Nahrungsangebot der Pflanzenpopulation durch Wuchskraft * Blütenanteil.
     * @return aktuellen Nahrungsangebot
     *
     * @post Der Rückgabewert ist >= 0.0, der aus dem Produkt von der Wuchskraft und den Blütenanteil berechnet wird.
     */
    public double getCurrentFoodSupply() {
        return this.vigor * this.bloomProportion;
    }

    /**
     * Gibt die Wuchskraft der Pflanzenart dieser Population zurück
     * @return Wuchskraft der Pflanzenart
     *
     * @post Der Rückgabewert ist die Wuchskraft der Pflanzenart dieser Population im Bereich [0.0, 1.0].
     */
    public double getVigor() {
        return this.vigor;
    }

    /**
     * Gibt den Blütenanteil zurück
     * @return Blütenanteil
     *
     * @post Der Rückgabewert ist der Blütenanteil im Bereich [0.0, 1.0].
     */
    public double getBloomProportion() {
        return this.bloomProportion;
    }

    /**
     * Gibt die Samenqualität zurück
     * @return Samenqualität
     *
     * @post Der Rückgabewert ist die Samenqualität im Bereich [0.0, 1.0].
     */
    public double getSeedQuality() {
        return this.seedQuality;
    }

    /**
     * Gibt den aktuellen Zustand der Pflanzenpopulation zurück.
     * @return Ein String mit den aktuellen Werten der Pflanzenpopulation.
     *
     * @post Der Rückgabewert ist eine nicht-leere Zeichenkette.
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
