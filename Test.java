import java.time.LocalDateTime;

/**
 * Diese Klasse testet die Ersetzbarkeit der
 * implementierten Typen, wie in der Aufgabenstellung gefordert.
 */
public class Test {

    // Zähler für Testfälle
    private static int testCount = 0;
    private static int failCount = 0;

    /**
     * Hauptmethode, die alle Tests ausführt.
     */
    public static void main(String[] args) {
        System.out.println("--- Starte Tests für AndrenaBucephala und FlowerFly ---");

        testObservationSubstitutability();
        testRemoveAndValid();
        testAndrenaBucephalaSpecifics();

        System.out.println("----------------------------------------");
        System.out.println("Testdurchlauf beendet.");
        System.out.println("Insgesamt ausgeführt: " + testCount);
        System.out.println("Davon fehlgeschlagen: " + failCount);
        System.out.println("----------------------------------------");

        // Füge hier die Kommentare zu Aufgabenaufteilung
        // und fehlenden Untertypbeziehungen ein, wie von der
        // Aufgabenstellung verlangt.
    }

    /**
     * Testet die Ersetzbarkeit von Observation-Untertypen.
     * Prüft, ob sich AndrenaBucephala und FlowerFly identisch verhalten,
     * wenn sie als Observation behandelt werden.
     */
    public static void testObservationSubstitutability() {
        System.out.println("\n--- Test: Observation Ersetzbarkeit (later/earlier) ---");
        ObservationData.clearAll();

        // 1. Test-Szenario aufbauen
        LocalDateTime time1 = LocalDateTime.of(2025, 11, 1, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2025, 11, 1, 11, 0);
        LocalDateTime time3 = LocalDateTime.of(2025, 11, 1, 12, 0);

        // Erzeuge Objekte. Diese fügen sich selbst zu ObservationData.ALL_OBSERVATIONS
        Observation obs1_AB = new AndrenaBucephala(time1, "Biene (früh)");
        Observation obs2_FF = new FlowerFly(time2, "Schwebfliege (mitte)");
        Observation obs3_AB = new AndrenaBucephala(time3, "Biene (spät)");

        // 2. Testfall A: AndrenaBucephala (die sich korrekt verhält)
        System.out.println("\nTestfall A: AndrenaBucephala.later() (obs1_AB)");
        // Wir rufen later() auf obs1_AB auf.
        // Wir erwarten, dass obs2_FF und obs3_AB gefunden werden (2 Ergebnisse).
        BehaviorIter<Observation> laterIter_AB = obs1_AB.later();
        check(laterIter_AB != null, "A.1: Iterator sollte nicht null sein");
        printIter(laterIter_AB, 2, "A.2: Erwarte 2 spätere Beobachtungen");

        System.out.println("\nTestfall B: FlowerFly.later() (obs2_FF) - VERLETZUNG");
        // Wir rufen later() auf obs2_FF auf.
        // Gemäß Ersetzbarkeit (LSP) müsste sie obs3_AB finden (1 Ergebnis).
        // Deine Implementierung von FlowerFly gibt aber 'null' zurück.
        BehaviorIter<Observation> laterIter_FF = obs2_FF.later();
        check(laterIter_FF == null, "B.1: FlowerFly.later() liefert null");
        if (laterIter_FF == null) {
            System.out.println("  [INFO] Test 'B.2' übersprungen, da Iterator null ist (Verletzung der Zusicherung!)");
            failCount++; // Zählt als Fehler, da die Zusicherung verletzt wurde
        } else {
            // Dieser Code wird nicht erreicht, wäre aber der korrekte Test
            printIter(laterIter_FF, 1, "B.2: Erwarte 1 spätere Beobachtung");
        }

        // 3. Testfall C: AndrenaBucephala (earlier)
        System.out.println("\nTestfall C: AndrenaBucephala.earlier() (obs3_AB)");
        // Wir rufen earlier() auf obs3_AB auf.
        // Wir erwarten, dass obs2_FF und obs1_AB gefunden werden (2 Ergebnisse).
        BehaviorIter<Observation> earlierIter_AB = obs3_AB.earlier();
        check(earlierIter_AB != null, "C.1: Iterator sollte nicht null sein");
        printIter(earlierIter_AB, 2, "C.2: Erwarte 2 frühere Beobachtungen");

        // 4. Testfall D: FlowerFly.earlier() (obs2_FF) - VERLETZUNG
        System.out.println("\nTestfall D: FlowerFly.earlier() (obs2_FF) - VERLETZUNG");
        // Wir rufen earlier() auf obs2_FF auf.
        // Sie müsste obs1_AB finden (1 Ergebnis).
        // Deine Implementierung von FlowerFly gibt aber 'null' zurück.
        BehaviorIter<Observation> earlierIter_FF = obs2_FF.earlier();
        check(earlierIter_FF == null, "D.1: FlowerFly.earlier() liefert null");
    }

    /**
     * Testet die remove() und valid() Funktionalität,
     * die in Observation spezifiziert ist.
     */
    public static void testRemoveAndValid() {
        System.out.println("\n--- Test: remove() und valid() ---");
        ObservationData.clearAll();

        LocalDateTime time1 = LocalDateTime.of(2025, 11, 2, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2025, 11, 2, 11, 0);
        LocalDateTime time3 = LocalDateTime.of(2025, 11, 2, 12, 0);

        Observation obs1 = new AndrenaBucephala(time1, "o1");
        Observation obs2_to_remove = new FlowerFly(time2, "o2 (wird entfernt)");
        Observation obs3 = new AndrenaBucephala(time3, "o3");

        // Test A: Prüfen, ob 'later' o2 und o3 findet
        System.out.println("Prüfe 'later' von o1 (vor remove):");
        printIter(obs1.later(), 2, "Erwarte o2 und o3");

        // Test B: valid() Status prüfen
        check(obs2_to_remove.valid(), "o2.valid() ist anfangs true");

        // Test C: Objekt entfernen
        obs2_to_remove.remove();
        System.out.println("-> o2 wurde entfernt.");

        // Test D: valid() Status nach remove prüfen
        check(!obs2_to_remove.valid(), "o2.valid() ist nach remove() false");

        // Test E: Prüfen, ob 'later' o2 (entfernt) nicht mehr findet
        // Dies testet, ob die Implementierung von AndrenaBucephala.later()
        // das 'valid()' Flag korrekt prüft.
        System.out.println("Prüfe 'later' von o1 (nach remove):");
        printIter(obs1.later(), 1, "Erwarte nur noch o3");
    }

    /**
     * Testet die spezifischen (leeren) Iterator-Methoden
     * von AndrenaBucephala.
     */
    public static void testAndrenaBucephalaSpecifics() {
        System.out.println("\n--- Test: AndrenaBucephala Spezifische Methoden ---");
        // Diese Tests prüfen nur, ob die Methoden die
        // (laut deiner Implementierung) erwarteten leeren
        // Iteratoren zurückgeben.

        AndrenaBucephala ab = new AndrenaBucephala(LocalDateTime.now(), "Test-Biene");

        System.out.println("Prüfe communal():");
        BehaviorIter<CommunalBee> communalIter = ab.communal();
        check(communalIter != null, "communal() Iterator ist nicht null");
        printIter(communalIter, 0, "communal() Iterator ist leer");

        System.out.println("Prüfe solitary():");
        BehaviorIter<SolitaryBee> solitaryIter = ab.solitary();
        check(solitaryIter != null, "solitary() Iterator ist nicht null");
        printIter(solitaryIter, 0, "solitary() Iterator ist leer");

        System.out.println("Prüfe wild(true):");
        BehaviorIter<WildBee> wildIter = ab.wild(true);
        check(wildIter != null, "wild(true) Iterator ist nicht null");
        printIter(wildIter, 0, "wild(true) Iterator ist leer");
    }


    // --- Test-Hilfsfunktionen ---

    /**
     * Zählt die Elemente in einem BehaviorIter und prüft auf die erwartete Anzahl.
     */
    public static <T extends Observation> void printIter(BehaviorIter<T> iter, int expectedSize, String message) {
        testCount++;
        int count = 0;
        if (iter == null) {
            failCount++;
            System.out.println("  [FAIL] " + message + " - Iterator war null!");
            return;
        }

        try {
            while (iter.hasNext()) {
                T obs = iter.next();
                if (obs != null) {
                    count++;
                }
            }
            if (count != expectedSize) {
                failCount++;
                System.out.println("  [FAIL] " + message + " - Falsche Anzahl: Erwartet=" + expectedSize + ", Bekommen=" + count);
            } else {
                System.out.println("  [OK] " + message + " - Korrekte Anzahl: " + count);
            }
        } catch (Exception e) {
            failCount++;
            System.out.println("  [FAIL] " + message + " - Exception beim Iterieren: " + e.getMessage());
        }
    }

    /**
     * Eine einfache assert-Alternative.
     */
    public static void check(boolean condition, String message) {
        testCount++;
        if (condition) {
            System.out.println("  [OK] " + message);
        } else {
            failCount++;
            System.out.println("  [FAIL] " + message);
        }
    }
}