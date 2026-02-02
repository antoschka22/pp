import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse modelliert die täglichen Wetterbedingungen (Sonnenscheindauer und Bodenfeuchte) während der Vegetationsperiode
 * anhand von realen Daten für die Temperatur und die Sonnenscheindauer in Wien (Daten von März - Oktober 2024 und März - Oktober 2025, https://meteostat.net)
 * sowie Zufallswerten für die Bodenfeuchte.
 *
 * GOOD: die Klasse weist einen hohen Klassenzusammenhalt auf. Sie ist ausschließlich für die Modellierung und Bereitstellung der Wetterdaten zuständig und
 * verwaltet die gesamten internen Zustände des Wettersystems. Außerdem ist die Objektkoppelung schwach: die Zufallszahlen werden durch das Interface IDistribution bereitgestellt,
 * somit können die konkreten Implementierungen der Zufallsverteilung leicht ausgetauscht werden.
 *
 * @invariant accumulatedSunHours >= 0.0
 * @invariant soilMoisture im Bereich [0.0, 1.0]
 * @invariant sunHoursToday >= 0.0
 * @invariant restPhaseDistribution != null
 */
public class CsvWeather implements IWeather{
    //aktueller Wetterzustand
    private double sunHoursToday; // Sonnenscheindauer d
    private double accumulatedSunHours; // aufsummierte Sonnenstunden h
    private double soilMoisture; // Bodenfeuchte f (0 ≤ f ≤ 1)
    private double temperature;

    private final IDistribution restPhaseDistribution;// Zufallszahl generieren
    private double soilMoistureOverride = -1.0;

    private record WeatherRecord(double temperature, double sunMinutes){};

    //speichert alle verarbeiteten Wetterdaten aus der CSV-Datei
    private final List<WeatherRecord> weatherRecords = new ArrayList<>();

    private int totalDaysElapsed = 0;

    /**
     * Konstruktor initialisiert ein neues Weather-Objekt.
     *
     * @param restPhaseDistribution der Zufallszahlengenerator
     * @param csvPath Pfad zur CSV-Datei
     * @throws IllegalArgumentException Wenn die Zufallszahl null ist.
     * @throws RuntimeException Wenn CSV-Datei nicht korrekt ist.
     *
     * @pre restPhaseDistribution, csvPath != null
     * @post die Liste weatherRecords wird gefüllt
     * @post die Wetterbedingungen werden initialisiert
     */
    public CsvWeather(String csvPath, IDistribution restPhaseDistribution) {
        if(restPhaseDistribution == null){
            throw new IllegalArgumentException("restPhaseDistribution must not be null");
        }
        if(csvPath == null){
            throw new IllegalArgumentException("csvPath must not be null");
        }
        this.restPhaseDistribution = restPhaseDistribution;

        //CSV-Datei einlesen
        readCSV(csvPath);

        initializeForVegetationPeriod();
    }

    /**
     * Liest die CSV-Datei in die Liste weatherRecords ein mit dem Format: Tag, Temperatur, Sonnenminuten.
     * @param csvPath Pfad zur CSV-Datei
     *
     * @pre csvPath ist ein gülter Pfad zu einer nicht-leeren CSV-Datei.
     * @post weatherRecords enthält alle gültigen WeatherRecords-Objekte aus der CSV-Datei.
     * @post Falls die Datei leer ist oder die Datei nicht gelesen werden kann, wird eine RuntimeException geworfen.
     */
    private void readCSV(String csvPath){
        try(BufferedReader br = new BufferedReader(new FileReader(csvPath))){
            String line;
            br.readLine(); //Kopfzeile überspringen

            while ((line = br.readLine()) != null){
                String[] values = line.split(";");
                if(values.length >= 3){
                    String tempString = values[1].trim().replace(',', '.');
                    double temperature = Double.parseDouble(tempString);
                    double sunMinutes = Double.parseDouble(values[2]);
                    weatherRecords.add(new WeatherRecord(temperature, sunMinutes));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Einlesen der CSV-Datei: " + e.getMessage());
        }
        if(weatherRecords.isEmpty()){
            throw new RuntimeException("CSV-Datei ist leer: " + csvPath);
        }
    }

    /**
     * Initialisierung der Wetterwerte am Beginn der Vegetationsperiode.
     * Die Summe der Sonnenstunden startet bei 0.0 und die Bodenfeuchte f wird zufällig gewählt.
     *
     * @post sunHoursToday, accumulatedSunHours und temperature werden auf 0.0 gesetzt.
     * @post soilMoisture ist im Bereich [0.0, 1.0)
     */
    @Override
    public void initializeForVegetationPeriod() {
        this.sunHoursToday = 0.0;
        this.accumulatedSunHours = 0.0;
        this.soilMoisture = restPhaseDistribution.nextDouble(0,1); // 0 (inklusive) ≤ f < 1 (exklusive)
        this.temperature = 0.0;

    }

    /**
     * Berechnet den Index in der CSV-Datei und realisiert,
     * dass die Wetterdaten wieder von vorne beginnen, sobald alle verwendet wurden.
     * @return nächster Datensatz aus weatherRecord
     *
     * @post der zurückgegebene WeatherRecord != null
     */
    private WeatherRecord getRecordForNextDay() {
        int index = totalDaysElapsed % weatherRecords.size();

        return weatherRecords.get(index);
    }

    /**
     * Simulation der täglichen Wetterveränderungen.
     * - Die Sonnenscheindauer d in Wien von der CSV-Datei.
     * - Die aufsummierten Werte der Sonnenscheindauer ab Beginn der Vegetationsperiode ergeben die Sonnenstunden h.
     * - Die Bodenfeuchte f mit 0 ≤ f ≤ 1 wird am Beginn der Vegetationsperiode zufällig gewählt und kann sich täglich zufällig um 10% verändern.
     * - Die Temperatur in Wien von der CSV-Datei.
     *
     * @param day aktueller Tag der Vegetationsperiode
     *
     * @pre day >= 1
     * @post totalDaysElapsed wird um 1 erhöht.
     * @post accumulatedSunHours ist um sunHoursToday gestiegen
     * @post soilMoisture ist im Bereich [0.0, 1.0]
     *
     * STYLE: prozedural. Klare Abfolge von Schritten mit direkter Manipulation der Variablen.
     */
    @Override
    public void simulateDailyChange(int day) {

        //CSV-Datei lesen
        WeatherRecord record = getRecordForNextDay();
        this.sunHoursToday = record.sunMinutes / 60.0;
        this.temperature = record.temperature();

        //Zähler aktualisieren
        this.accumulatedSunHours += this.sunHoursToday;
        this.totalDaysElapsed++;

        // zufällige Veränderung der Bodenfeuchte f um bis zu 10%
        double changeSoilMoisture = restPhaseDistribution.nextDouble(0, 0.1);
        soilMoisture += changeSoilMoisture;

        // Überprüfung der Intervallgröße der Bodenfeuchte f (0 ≤ f ≤ 1)
        if(this.soilMoisture < 0.0) this.soilMoisture = 0.0;
        if(this.soilMoisture > 1.0) this.soilMoisture = 1.0;

    }
    /**
     * Getter-Methode der Sonnenscheindauer d
     * @return Sonnenscheindauer des jeweiligen Tages
     *
     * @post Der Rückgabewert ist die Sonnenscheindauer sunHoursToday >= 0.
     */
    @Override
    public double getSunHoursToday() {
        return this.sunHoursToday;
    }
    /**
     * Getter-Methode der Sonnenstunden h
     * @return aufsummierten Sonnenstunden ab Beginn der Vegetationsperiode
     *
     * @post Der Rückgabewert ist die akkumulierte Sonnenscheindauer >= 0.
     */
    @Override
    public double getAccumulatedSunHours() {
        return this.accumulatedSunHours;
    }

    /**
     * Getter-Methode der Bodenfeuchte f
     * @return aktuelle Bodenfeuchte, deren Wert zwischen 0 und 1 liegt
     *
     * post: Der Rückgabewert ist die Bodenfeuchte soilMoisture im Bereich [0.0, 1.0].
     */
    @Override
    public double getSoilMoisture() {
        return this.soilMoisture;
    }

    /**
     * Getter-Methode der Temperatur
     * @return aktuelle Temperatur
     *
     * @post Der Rückgabewert ist die aktuelle Temperatur.
     */
    @Override
    public double getTemperature() {
        return this.temperature;
    }

    /**
     * Erzwingt einen bestimmten Bodenfeuchtewert für den *aktuellen* Tag.
     * Wird von Events wie DroughtEvent aufgerufen.
     * @param newMoisture Der zu setzende Feuchtewert (sollte zwischen 0 und 1 liegen).
     *
     * @pre newMoisture im Bereich [0.0, 1.0].
     * @post: soilMoistureOverride ist auf newMoisture gesetzt.
     */
    @Override
    public void forceSoilMoisture(double newMoisture) {
        this.soilMoistureOverride = newMoisture;
    }

    /**
     * Gibt eine lesbare Darstellung der aktuellen Wetterbedingungen des jeweiligen Tages in Wien.
     * @return Ein String mit den täglichen Wetterbedingungen.
     *
     * @post: Der Rückgabewert ist eine nicht-leere Zeichenkette.
     */
    public String toString(){
        return String.format("Heutige Sonnenscheindauer in Wien: %.2f Sonnenstunden, Summe: %.2f Sonnenstunden, Bodenfeuchte: %.2f, Temperatur in Wien: %.2f °C",
                this.sunHoursToday,
                this.accumulatedSunHours,
                this.soilMoisture,
                this.temperature);
    }
}
