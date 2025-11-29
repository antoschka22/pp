import java.util.Random;

@ProjectClass
@Author(name = "Simon Oberdörfer")
@Invariant(condition = "bienenSet and pflanzenSet != null")
@HistoryConstraint(condition = "day can only increase")
public class Simulation {

    private final Set bienenSet;
    private final Set pflanzenSet;
    private final Random random;
    private int day;
    private static final int maxBeeDailyEntities = 5;
    private static final int maxPlantDailyEntities = 5;
    private static final int maxVisits = 5;

    @Author(name = "Simon Oberdörfer")
    @Post(condition = "Sets initialized && day == 0")
    public Simulation() {
        this.bienenSet = new Set();
        this.pflanzenSet = new Set();
        this.random = new Random();
        this.day = 0;
    }

    // Biene in Bienenset hinzufügen
    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "b != null")
    @Post(condition = "bieneSet.contains(b)")
    private void addBiene(Biene b) {
        bienenSet.add(b);
    }

    // Pflanze in Pflanzenset hinzufügen
    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "p != null")
    @Post(condition = "pflanzenSet.contains(p)")
    private void addPflanze(Pflanze p) {
        pflanzenSet.add(p);
    }




    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "day == 0")
    @Post(condition = "day >= 7 && (!areBeesActive() || !arePlantsActive())")
    public void run() {
        boolean running = true;

        // Simulationsschleife
        while (running) {
            day++;
            System.out.println("--- Tag " + day + " ---");

            //in den ersten 7 Tagen werden zu Beginn neue Bienen und Pflanzen erstellt
            if (day <= 7) {
                spawnDailyEntities();
            }

            // Bienen fliegen
            performVisits();

            printDailyStatus();

            // Bienen und Pflanzen altern um einen Tag
            processAging();

            // Abbruchbedingung: Mindestens 7 Tage werden simuliert, danach wird geprüft, ob Pflanzen und Bienen noch aktiv sind
            if (day > 7) {
                if (!areBeesActive() || !arePlantsActive()) {
                    running = false;
                }
            }
        }
        System.out.println("Simulation beendet nach " + day + " Tagen.");
        printStatistics();
    }


    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "day <= 7")
    @Post(condition = "adds random amount of bees and plants to the sets")
    private void spawnDailyEntities() {
        // Zufällige Wahl von 2 Bienenarten: 0 -> U und V, 1 -> V und W, 2 -> U und W
        int beeMode = random.nextInt(3);
        // zufällige Wahl der Bienenanzahl
        int beeCount1 = random.nextInt(maxBeeDailyEntities) + 1;
        int beeCount2 = random.nextInt(maxBeeDailyEntities) + 1;

        if (beeMode == 0) {
            for (int i = 0; i < beeCount1; i++) {
                addBiene(new BieneU());
            }
            for (int i = 0; i < beeCount2; i++) {
                addBiene(new BieneV());
            }
        } else if (beeMode == 1) {
            for (int i = 0; i < beeCount1; i++) {
                addBiene(new BieneV());
            }
            for (int i = 0; i < beeCount2; i++) {
                addBiene(new BieneW());
            }
        } else {
            for (int i = 0; i < beeCount1; i++) {
                addBiene(new BieneU());
            }
            for (int i = 0; i < beeCount2; i++) {
                addBiene(new BieneW());
            }
        }
        // Zufällige Wahl von 2 Pflanzenarten: 0 -> X und Y, 1 -> Y und Z, 2 -> X und Z
        int plantMode = random.nextInt(3);
        // Zufällige Wahl der Pflanzenanzahl
        int plantCount1 = random.nextInt(maxPlantDailyEntities) + 1;
        int plantCount2 = random.nextInt(maxPlantDailyEntities) + 1;

        if (plantMode == 0) {
            for (int i = 0; i < plantCount1; i++) {
                addPflanze(new PflanzeX());
            }
            for (int i = 0; i < plantCount2; i++) {
                addPflanze(new PflanzeY());
            }
        } else if (plantMode == 1) {
            for (int i = 0; i < plantCount1; i++) {
                addPflanze(new PflanzeY());
            }
            for (int i = 0; i < plantCount2; i++) {
                addPflanze(new PflanzeZ());
            }
        } else {
            for (int i = 0; i < plantCount1; i++) {
                addPflanze(new PflanzeX());
            }
            for (int i = 0; i < plantCount2; i++) {
                addPflanze(new PflanzeZ());
            }
        }
    }

    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "pflanzenSet and bienenSet != null")
    private void performVisits() {
        if (pflanzenSet.isEmpty()) return;

        // 1. Über alle Bienen iterieren
        for (Object obj : bienenSet) {
            Biene biene = (Biene) obj;
            if(biene.isAlive()){
                // Zufällige Anzahl an Flügen heute
                int flights = random.nextInt(maxVisits) + 1;
                for (int i = 0; i < flights; i++) {
                    // es wird eine bevorzugte Blume gesucht
                    Pflanze targetplant = findPlant(biene, true);

                    // falls keine bevorzugte Blume gefunden wurde, wird eine alternative Blume gesucht
                    if (targetplant == null) {
                        targetplant = findPlant(biene, false);
                    }

                    // Wenn eine Pflanze gefunden wurde, wird Besuch durchgeführt
                    if (targetplant != null) {
                        // Die Pflanze speichert den Besuch
                        targetplant.acceptVisit(biene);
                    }
                }
            }
        }
    }

    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "biene != null")
    @Post(condition = "returns a random plant match for a bee, either a preferred one or an alternative. If no candidate was fount, it returns null")
    private Pflanze findPlant(Biene biene, boolean preferred) {
        // Anzahl der Kandidaten finden
        int candidates = 0;
        for(Object obj : pflanzenSet){
            Pflanze p = (Pflanze) obj;
            if(p.isAlive()){
                boolean matches = preferred ? p.isPreferredBy(biene) : p.isAlternativeFor(biene);
                if(matches) {
                    candidates ++;
                }
            }
        }
        if(candidates == 0) return null;
         // Einen zufälligen Kandidaten wählen
        int pickIndex = random.nextInt(candidates);
        int currentIndex = 0;

        for (Object obj : pflanzenSet){
            Pflanze p = (Pflanze)  obj;
            if(p.isAlive()){
                boolean match = preferred ? p.isPreferredBy(biene) : p.isAlternativeFor(biene);
                if(match){
                    if(currentIndex == pickIndex){
                        return p;
                    }
                    currentIndex ++;
                }
            }
        }
        return null;
    }


    @Author(name = "Simon Oberdörfer")
    @Post(condition = "all organisms aged one day")
    private void processAging() {
        // Bienen altern um einen Tag
        for (Object obj : bienenSet) {
            Biene b = (Biene) obj;
            b.nextDay();
        }

        // Pflanzen altern um einen Tag
        for (Object obj : pflanzenSet) {
            Pflanze p = (Pflanze) obj;
            p.nextDay();
        }
    }

    @Author(name = "Simon Oberdörfer")
    @Post(condition = "returns true, if at least one bee is alive, otherwise false")
    private boolean areBeesActive() {
        for(Object obj : bienenSet){
            Biene b = (Biene) obj;
            if(b.isAlive()){
                return true;
            }
        }
        return false;
    }

    @Author(name = "Simon Oberdörfer")
    @Post(condition = "returns true, if at least one plant is alive, otherwise false")
    private boolean arePlantsActive() {
        for (Object obj : pflanzenSet){
            Pflanze p = (Pflanze) obj;
            if(p.isAlive()){
                return true;
            }
        }
        return false;
    }

    @Author(name = "Simon Oberdörfer")
    @Post(condition = "daily status printed to standard output")
    private void printDailyStatus() {
        int activeBees = 0;
        int activePlants = 0;

        for (Object obj : bienenSet) {
            if (((Biene) obj).isAlive()) activeBees++;
        }
        for (Object obj : pflanzenSet) {
            if (((Pflanze) obj).isAlive()) activePlants++;
        }
        System.out.printf("Aktive Bienen: %d | Blühende Pflanzen: %d%n", activeBees, activePlants);
    }

    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "day > 0")
    @Post(condition = "statistic printed to standard output")
    private void printStatistics() {
        System.out.println("\n--------- STATISTIK ---------");

        // speichert die Anzahl der Pflanzenbesuche der einzelnen Bienenarten
        int totalU = 0;
        int totalV = 0;
        int totalW = 0;

        for(Object obj : pflanzenSet) {
            Pflanze p = (Pflanze) obj;
            // Alle Bienenbesuche einer Pflanze werden addiert
            totalU += p.visitedByU();
            totalV += p.visitedByV();
            totalW += p.visitedByW();
        }
        int totalPlants = pflanzenSet.size();
        double avgU, avgV, avgW;
        avgU = avgV = avgW =  0.0;
        if (totalPlants > 0){
            avgU = (double) totalU / totalPlants;
            avgV = (double) totalV / totalPlants;
            avgW = (double) totalW / totalPlants;
        }
        System.out.printf("Bienenart U: insgesamt %d Pflanzenbesuche, durchschnittlich %.2f Besuche pro Pflanze%n", totalU, avgU);
        System.out.printf("Bienenart V: insgesamt %d Pflanzenbesuche, durchschnittlich %.2f Besuche pro Pflanze%n", totalV, avgV);
        System.out.printf("Bienenart W: insgesamt %d Pflanzenbesuche, durchschnittlich %.2f Besuche pro Pflanze%n", totalW, avgW);
        System.out.println("-----------------------------------------------");

        int totalX = 0;
        int totalY = 0;
        int totalZ = 0;

        for (Object obj : bienenSet) {
            Biene b = (Biene) obj;
            // alle Pflanzenbesuche einer Biene werden addiert
            totalX += b.collectedFromX();
            totalY += b.collectedFromY();
            totalZ += b.collectedFromZ();
        }
        int totalBees = bienenSet.size();
        double avgX, avgY, avgZ;
        avgX = avgY = avgZ =  0.0;
        if (totalPlants > 0){
            avgX = (double) totalX / totalBees;
            avgY = (double) totalY / totalBees;
            avgZ = (double) totalZ / totalBees;
        }
        System.out.printf("Pflanzenart X: insgesamt %d Bienenbesuche, durchschnittlich %.2f Besuche pro Biene%n", totalX, avgX);
        System.out.printf("Pflanzenart V: insgesamt %d Bienenbesuche, durchschnittlich %.2f Besuche pro Biene%n", totalY, avgY);
        System.out.printf("Pflanzenart W: insgesamt %d Bienenbesuche, durchschnittlich %.2f Besuche pro Biene%n", totalZ, avgZ);
    }
}
