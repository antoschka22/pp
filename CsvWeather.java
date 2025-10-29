import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse modelliert die täglichen Wetterbedingungen (Sonnenscheindauer und Bodenfeuchte) während der Vegetationsperiode
 * anhand von realen Daten für die Temperatur und die Sonnenscheindauer in Wien (Daten von März - Oktober 2024 und März - Oktober 2025, https://meteostat.net)
 * sowie Zufallswerten für die Bodenfeuchte.
 */
public class CsvWeather implements IWeather{
    //aktueller Wetterzustand
    private double sunHoursToday; // Sonnenscheindauer d
    private double accumulatedSunHours; // aufsummierte Sonnenstunden h
    private double soilMoisture; // Bodenfeuchte f (0 ≤ f ≤ 1)
    private double temperature; //
    private int currentVegetationDay; //1-240

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
     */
    @Override
    public void initializeForVegetationPeriod() {
        this.currentVegetationDay = 0;
        this.sunHoursToday = 0.0;
        this.accumulatedSunHours = 0.0;
        this.soilMoisture = restPhaseDistribution.nextDouble(0,1); // 0 (inklusive) ≤ f < 1 (exklusive)
        this.temperature = 0.0;

    }

    /**
     * Berechnet den Index in der CSV-Datei und realisiert,
     * dass die Wetterdaten wieder von vorne beginnen, sobald alle verwendet wurden.
     * @return nächster Datensatz aus weatherRecord
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
     * STYLE: prozedural. Klare Abfolge von Schritten mit direkter Manipulation der Variablen.
     */
    @Override
    public void simulateDailyChange(int day) {
        this.currentVegetationDay = day;

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
     */
    @Override
    public double getSunHoursToday() {
        return this.sunHoursToday;
    }
    /**
     * Getter-Methode der Sonnenstunden h
     * @return aufsummierten Sonnenstunden ab Beginn der Vegetationsperiode
     */
    @Override
    public double getAccumulatedSunHours() {
        return this.accumulatedSunHours;
    }

    /**
     * Getter-Methode der Bodenfeuchte f
     * @return aktuelle Bodenfeuchte, deren Wert zwischen 0 und 1 liegt
     */
    @Override
    public double getSoilMoisture() {
        return this.soilMoisture;
    }

    /**
     * Getter-Methode der Temperatur
     * @return aktuelle Temperatur
     */
    @Override
    public double getTemperature() {
        return this.temperature;
    }

    /**
     * Erzwingt einen bestimmten Bodenfeuchtewert für den *aktuellen* Tag.
     * Wird von Events wie DroughtEvent aufgerufen.
     * @param newMoisture Der zu setzende Feuchtewert (sollte zwischen 0 und 1 liegen).
     */
    @Override
    public void forceSoilMoisture(double newMoisture) {
        this.soilMoistureOverride = newMoisture;
    }

    /**
     * Gibt eine lesbare Darstellung der aktuellen Wetterbedingungen des jeweiligen Tages in Wien.
     * @return Ein String mit den täglichen Wetterbedingungen.
     */
    public String toString(){
        return String.format("Heutige Sonnenscheindauer in Wien: %.2f Sonnenstunden, Summe: %.2f Sonnenstunden, Bodenfeuchte: %.2f, Temperatur in Wien: %.2f °C",
                this.sunHoursToday,
                this.accumulatedSunHours,
                this.soilMoisture,
                this.temperature);
    }
}
