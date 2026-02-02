/*
Miriam Reumann hat die Interfaces Bee und SocialBee implementiert. Sie hat ebenfalls die Klassen Honeybee, BumbleBee und
BehaviourIter gemacht. In der Testklasse hat sie die folgenden Methoden implementiert: testBeeContracts(), testSocialBeeContracts(),
checkSocialIteration(), checkSameBeeIteration() und checkIteratorOrder(). Außerdem bei den Begründungen für nicht vorhandenen
Untertypbeziehungen als Kommentar mitgeholfen

Antonio Molina Gradischnig: Ich habe die Interfaces Observation, Wasp, Pollinator und CommunalBee und die Klassen Flowerfly und
AndrenaBucephala implementiert. Außerdem habe ich die Hilfsklassen ObservationData, BeeIterHelper und ObservationIterHelper implementiert.
In der Test Klasse war ich für die Methoden testPollinatorAndWaspContracts, testCommunalContracts und testObservationContracts
Außerdem bei den Begründungen für nicht vorhandenen Untertypbeziehungen als Kommentar mitgeholfen

Simon Oberdörfer hat die Interfaces SolitaryBee und WildBee implementiert. Außerdem hat er bei der Ausarbeitung der BeeIterHelper
und ObservationHelper mitgearbeitet. Er hat die Klassen LasioglossumCalceatum und OsmiaCornuta geschrieben. In der Testklasse hat er die Methoden
testSolitaryBeeContracts(),  testWildBeeContracts(), checkSolitaryIteration und checkSolitaryIteration geschrieben.
Außerdem hat er alle Begründungen für nicht vorhandenen Untertypbeziehungen als Kommentar in der Testklasse geschrieben.
 */

/*
Begründung, wenn keine Untertypbeziehung vorliegt:
- Wasp ist kein Untertyp von Pollinator: in der Beschreibung von Wasp wird angegeben, dass "Viele Wespenarten keine Bestäuber" sind.
Da es also viele Wespen gibt, die nicht bestäuben, kann nicht jedes Objekt einer Wasp als Pollinator gelten.

- FlowerFly ist kein Untertyp von Bee, Wasp, CommunalBee, WildBee, SocialBee, SolitaryBee, AndrenaBucephala, OsmiaCornuta, LasioglossumCalceatum, Honeybee, Bumblebee:
In der Beschreibung steht: "Schwebfliegen gehören zu den Fliegen und sind daher nicht mit Bienen und Wespen verwandt". Somit kann FlowerFly kein Untertyp von den angegebenen Typen sein,
da es sich bei allen um Bienen und Wespen handelt. Außerdem wird der Nachwuchs nicht mit Nektar oder Pollen versorgt, im Gegensatz zu allen Bee Typen und deren Untertypen.

- Bee, Wasp, CommunalBee, WildBee, SocialBee, SolitaryBee, AndrenaBucephala, OsmiaCornuta, LasioglossumCalceatum, Honeybee, Bumblebee sind keine Untertypen von FlowerFly:
In der Beschreibung steht: "Schwebfliegen gehören zu den Fliegen und sind daher nicht mit Bienen und Wespen verwandt". Somit können alle angegebenen Typen kein Untertyp von FlowerFly sein,
da es sich bei Flowerfly sicher nicht um Bienen und Wespen handelt. Außerdem wird der Nachwuchs nicht mit Nektar oder Pollen versorgt, im Gegensatz zu allen Bee Typen und deren Untertypen.

- SolitaryBee und SocialBee stehen nicht in einer Untertypbeziehung untereinander: Dies beschreibt drei unterschiedliche Lebensweisen, die sich gegenseitig ausschließen.
Im Gegensatz zu solitär oder kommunal lebenden Bienen gehen soziale Bienen bei der Brutpflege arbeitsteilig vor, bilden also einen Staat.
Es werden durch diese 2 Typen verschiede Lebensweisen beschrieben.

- WildBee ist kein Untertyp von SocialBee und Honeybee:
Honeybees zählen laut der Angebe generell nicht zu den Wildbienen, deshalb kann man keine Wildbee verwenden, wo eine Honeybee erwartet wird.
Eine Wildbee ist auch kein Untertyp von Socialbee, da Wildbee umfasst alle heimischen, wild lebenden Bienen, dazu gehören explizit auch solitär lebende Bienen, was der Anforderung von Socialbee widerspricht.

- SocialBee und Honeybee sind keine Untertypen von Wildbee: z.b. ist Honeybee eine SocialBee, die nicht zu den Wildbienen zählt ("Allerdings ist keine wild lebende Honigbienenart hier heimisch,
sodass Honigbienen generell nicht zu den Wildbienen zählen.")

- CommunalBee ist kein Untertyp von OsmiaCornuta, LasioglossumCalceatum, HoneyBee und Bumblebee:
Da eine CommunalBee-Beobachtung eine Art darstellen kann, die nicht die spezifischen Merkmale von Osmia Cornuta, Lasioglossum Calceatum, Honeybee oder Bumblebee aufweist,
kann nicht jede CommunalBee als einer dieser spezifischen Typen eingesetzt werden.

- SolitaryBee ist kein Untertyp von Honeybee und Bumblebee:
Alle SolitaryBee-Objekte leben solitär, Honeybee und Bumblebee leben hingegen aussschließlich sozial.

- Socialbee ist kein Untertyp von AndrenaBucephala und OsmiaCornuta:
Alle SocialBee-Objekte leben sozial, AndrenaBucaphala und OsmiaCornuta leben hingegen kommunal und solitär bzw. solitär.

- AndrenaBucephala, OsmiaCornuta, LasioglossumCalceatum, Honeybee und Bumblebee stehen nicht in einer Untertypbeziehung untereinander:
Die Typen AndrenaBucephala, OsmiaCornuta und LasioglossumCalceatum repräsentieren Beobachtungen von spezifischen Bienenarten, während Honeybee und Bumblebee Beobachtungen von spezifischen Gruppen repräsentieren.
Da die Lebensweise und Klassifizierung jeder dieser Arten bzw. Gruppen spezifisch und disjunkt sind (z.B. eine Honeybee kann keine Bumblebee sein, da sie eine unterschiedliche Art und Lebensweise hat),
kann ein Objekt von einem dieser Typen nicht durch ein Objekt eines anderen dieser Typen ersetzt werden.

- AndreaBucephala ist kein Untertyp von SocialBee:
AndrenaBucephala kann kommunal und solitär leben, aber niemals sozial, also kann sie nicht verwendet werden, wenn eine SocialBee erwartet wird.

- OsmiaCornuta ist kein Untertyp von CommunalBee und SocialBee:
OsmiaCornuta lebt solitär und somit weder kommunal noch sozial ("Das ist eine solitär lebende Wildbiene"),
also kann sie nicht verwendet werden, wenn eine SocialBee bzw. eine CommunalBee erwartet wird.

- LasioglossumCalceatum ist kein Unterty von CommunalBee:
LasioglossumCalceatum leben sozial oder solitär ("leben meist sozial", "In kälteren Klimaten verhält sich die Art solitär"),
aber niemals kommunal, also kann sie nicht verwendet werden, wenn eine CommunalBee erwartet wird.

- Honeybee ist kein Untertyp von SolitaryBee, WildBee, CommunalBee:
Honeybee lebt ausschließlich sozial ("Solitär oder kommunal lebende Honigbienen gibt es nicht"), des Weiteren gehört sie nicht zu den Wildbienen ("Allerdings ist
keine wild lebende Honigbienenart hier heimisch"), also kann sie nicht verwendet werden, wenn eine SolitaryBee bzw. eine WildBee bzw. eine CommunalBee erwartet wird.

- Bumblebee ist kein Untertyp vo SolitaryBee und CommunalBee:
Alle Hummeln sind staatenbildend, also SocialBee. Somit kann sie nicht verwendet werden, wenn eine nicht-soziale Lebensweise
(CommunalBee oder SolitaryBee) erwartet wird.

- CommunalBee ist kein Untertyp von SocialBee und SocialBee ist kein Untertyp von CommunalBee: kommunale Arten teilen nur das Nest,
alle versorgen ihren eigenen Nachwuchs. Kommunale Arten sind hingegegen staatenbildend und teilen sich die Arbeit auf. Damit erfüllt eine kommunale Art
nicht die Zusicherung einer sozialen Art und eine soziale Art nicht die Zusicherung einer kommunalen Art.


 */

import java.time.LocalDateTime;
import java.util.List;

/**
 * Diese Klasse testet die erstellte Typhierarchie auf konformes Verhalten
 * und Einhaltung des Ersetzbarkeitsprinzips.
 * Sie enthält außerdem die geforderten Kommentare zur Aufgabenaufteilung
 * und zu den Begründungen für nicht bestehende Untertypbeziehungen.
 */
public class Test {

    // Zähler für eine einfache Testzusammenfassung
    private static int testCounter = 0;
    private static int failCounter = 0;

    /**
     * Hauptmethode zum Ausführen aller Tests.
     */
    public static void main(String[] args) {
        System.out.println("--- Starte Tests für Programmieraufgabe 4 ---");
        System.out.println("Testet Ersetzbarkeit durch polymorphe Listen...");

        try {
            testObservationContracts();
            testBeeContracts();
            testSocialBeeContracts();
            testSolitaryBeeContracts();
            testWildBeeContracts();
            testCommunalBeeContracts();
            testPollinatorAndWaspContracts();
        } catch (Exception e) {
            System.out.println("\n!!! SCHWERWIEGENDER FEHLER: Testausführung abgebrochen !!!");
            e.printStackTrace();
        }

        System.out.println("\n--- TESTERGEBNIS ---");
        System.out.println("Tests ausgeführt: " + testCounter);
        System.out.println("Tests fehlgeschlagen: " + failCounter);
        if (failCounter == 0) {
            System.out.println(">>> Alle Tests bestanden! <<<");
        } else {
            System.out.println(">>> ACHTUNG: " + failCounter + " Tests fehlgeschlagen! <<<");
        }
    }

    /**
     * Testet die Basisfunktionalität aller Observation-Typen.
     * Jede Observation MUSS die Verträge von remove(), valid(),
     * later() und earlier() einhalten.
     */
    private static void testObservationContracts() {
        System.out.println("\n[Testgruppe: Observation]");
        ObservationData.clearAll();

        LocalDateTime t1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        Observation o1 = new FlowerFly(t1, "Fly 1");
        Observation o2 = new OsmiaCornuta(t1.plusHours(1), "Osmia 1");
        Observation o3 = new Honeybee(t1.plusHours(2), "Honeybee 1");
        Observation o4 = new Bumblebee(t1.plusHours(3), "Bumblebee 1");
        Observation o5 = new LasioglossumCalceatum(t1.plusHours(4), "Lasio 1", true, false);
        Observation o6 = new AndrenaBucephala(t1.plusHours(5), "Andrena 1", false, true);

        List<Observation> observations = List.of(o1, o2, o3, o4, o5, o6);

        for (Observation obs : observations) {
            check(obs.valid(), obs.getComment() + " muss initial valide sein.");
        }

        o3.remove();
        check(!o3.valid(), o3.getComment() + " muss nach remove() invalide sein.");
        check(o2.valid(), o2.getComment() + " muss noch valide sein.");
        check(o4.valid(), o4.getComment() + " muss noch valide sein.");

        System.out.println("  Teste later() für " + o2.getComment());
        BehaviorIter<Observation> laterIter = o2.later();
        check(laterIter.hasNext(), "later() muss Elemente haben.");
        Observation laterObs = laterIter.next();
        check(laterObs == o4, "later() 1. Element muss " + o4.getComment() + " sein, war " + (laterObs != null ? laterObs.getComment() : "null"));
        laterObs = laterIter.next();
        check(laterObs == o5, "later() 2. Element muss " + o5.getComment() + " sein, war " + (laterObs != null ? laterObs.getComment() : "null"));
        laterObs = laterIter.next();
        check(laterObs == o6, "later() 3. Element muss " + o6.getComment() + " sein, war " + (laterObs != null ? laterObs.getComment() : "null"));
        check(!laterIter.hasNext(), "later() darf keine weiteren Elemente haben.");

        System.out.println("  Teste earlier() für " + o5.getComment());
        BehaviorIter<Observation> earlierIter = o5.earlier();
        check(earlierIter.hasNext(), "earlier() muss Elemente haben.");
        Observation earlierObs = earlierIter.next();
        check(earlierObs == o4, "earlier() 1. Element muss " + o4.getComment() + " sein, war " + (earlierObs != null ? earlierObs.getComment() : "null"));
        earlierObs = earlierIter.next();
        check(earlierObs == o2, "earlier() 2. Element muss " + o2.getComment() + " sein, war " + (earlierObs != null ? earlierObs.getComment() : "null"));
        earlierObs = earlierIter.next();
        check(earlierObs == o1, "earlier() 3. Element muss " + o1.getComment() + " sein, war " + (earlierObs != null ? earlierObs.getComment() : "null"));
        check(!earlierIter.hasNext(), "earlier() darf keine weiteren Elemente haben.");
    }

    /**
     * Testet die Verträge des Bee-Interfaces, insb. sameBee() und individualIdentifier().
     * Jede Bee MUSS diese Verträge einhalten.
     */
    private static void testBeeContracts() {
        System.out.println("\n[Testgruppe: Bee]");
        ObservationData.clearAll();

        LocalDateTime t1 = LocalDateTime.of(2025, 3, 1, 10, 0);

        // AndrenaBucephala-Objekte erzeugen
        AndrenaBucephala a1 = new AndrenaBucephala(t1.plusHours(3), "Andrena - Solitär", true, false);
        AndrenaBucephala a2 = new AndrenaBucephala(t1.plusHours(7), "Andrena - Solitär", true, a1, null, false);

        // LasioglossumCalceatum-Objekte erzeugen
        LasioglossumCalceatum l1 = new LasioglossumCalceatum(t1, "Lasioglossum 1", true, true, false);
        LasioglossumCalceatum l2 = new LasioglossumCalceatum(t1.plusHours(1), "Lasioglossum 2", true, true, false, l1);

        // OsmiaCornuta-Objekte erzeugen
        OsmiaCornuta o1 = new OsmiaCornuta(t1, "Osmia 1");
        OsmiaCornuta o2 = new OsmiaCornuta(t1.plusHours(1), "Osmia 2", o1);

        // Honeybee-Objekte erzeugen
        Honeybee hB1 = new Honeybee(t1, "Honeybee 1", 300L);
        Honeybee hB2 = new Honeybee(t1.plusHours(2), "Honeybee 2", hB1);

        // Bumblebee-Objekte erzeugen
        Bumblebee bB1 = new Bumblebee(t1, "Bumblebee 1", false, null);
        Bumblebee bB2 = new Bumblebee(t1.plusHours(3), "Bumblebee 2", bB1);

        List<Bee> listOfBees = List.of(a1, a2, l1, l2, o1, o2, hB1, hB2, bB1, bB2);

        for (Bee bee : listOfBees) {
            String name = bee.getClass().getName();
            System.out.println(" // " + name + ": //");

            // SameBee (standard) testen
            checkSameBeeIteration(bee,2, name + " SameBee standard");

            // Reverse-Iterator testen
            BehaviorIter<Bee> revIter = bee.sameBee(true);
            checkIteratorOrder(revIter, false, name + " SameBee reverse");

            // TimeRange-Iterator testen
            BehaviorIter<Bee> timeIter = bee.sameBee(t1, t1.plusHours(7));
            checkIteratorOrder(timeIter, true, name + " SameBee mit Zeitbereich");

            // Prüfung von individualIdentifier prüfen
            Object iD = bee.individualIdentifier();
            check(iD != null, name + " individualIdentifier liefert nicht null");
        }

        // Ersetzbarkeit prüfen
        System.out.println("  Ersetzbarkeit");
        Bee testBee = new OsmiaCornuta(t1, "Testbiene"); // Beispielbiene erstellen
        Bee bee = testBee;
        Wasp wasp = testBee;
        Pollinator pollinator = testBee;
        Observation obs = testBee;

        check(bee.getComment().equals("Testbiene"), "Bee muss als Bee zuweisbar sein.");
        check(wasp.getComment().equals("Testbiene"), "Bee muss als Wasp zuweisbar sein.");
        check(pollinator.getComment().equals("Testbiene"), "Bee muss als Pollinator zuweisbar sein.");
        check(obs.getComment().equals("Testbiene"), "Bee muss als Observation zuweisbar sein.");
    }

    /**
     * Testet den Vertrag von SocialBee.
     * Der Knackpunkt: Honeybee/Bumblebee sind *immer* sozial,
     * Lasioglossum *kann* sozial sein.
     * Ein SocialBee-Objekt muss `social()` korrekt implementieren,
     * egal welcher Typ es ist.
     */
    private static void testSocialBeeContracts() {
        System.out.println("\n[Testgruppe: SocialBee]");
        ObservationData.clearAll();

        LocalDateTime t1 = LocalDateTime.of(2025, 2, 1, 10, 0);

        // Honeybee-Objekte erzeugen
        Honeybee hB1 = new Honeybee(t1, "Honeybee 1"); // 1. Beobachtung
        Honeybee hB2 = new Honeybee(t1.plusHours(2), "Honeybee 2", hB1); // 2. Beobachtung

        checkSocialIteration(hB1, 2, "Honeybee (immer sozial) muss alle 2 Beobachtungen liefern.");

        // Bumblebee-Objekte erzeugen
        Bumblebee bB1 = new Bumblebee(t1, "Bumblebee 1", false, 150L);
        Bumblebee bB2 = new Bumblebee(t1.plusHours(3), "Bumblebee 2", bB1);

        checkSocialIteration(bB1, 2, "Bumblebee (immer sozial) muss alle 2 Beobachtungen liefern.");

        // LasioglossumCalceatum-Objekte erzeugen
        LasioglossumCalceatum l1 = new LasioglossumCalceatum(t1, "Lasioglossum - Sozial", true, true, true);
        LasioglossumCalceatum l2 = new LasioglossumCalceatum(t1.plusHours(2), "Lasioglossum  - Nicht Sozial", false, true, true);

        checkSocialIteration(l1, 1, "Lasioglosum (sozial) muss alle 2 Beobachtungen liefern.");
        checkSocialIteration(l2, 0, "Lasioglosum (nicht sozial) darf keine Beobachtung liefern.");

        // Sortierung testen
        BehaviorIter<SocialBee> oIter = hB1.social();
        SocialBee first = oIter.next();
        SocialBee second = oIter.next();
        check(first.getTimestamp().isBefore(second.getTimestamp()), "Social Iteration muss chronologisch aufsteigend sortiert sein (hB1 vor hB2).");

        // Ersetzbarkeit prüfen
        System.out.println("  Ersetzbarkeit");
        SocialBee socialBee = new Honeybee(t1, "Testbiene");
        Bee beeSocialBee = socialBee;
        Wasp waspSolitaryBee = socialBee;
        Pollinator pollinatorSocialBee = socialBee;
        Observation observationSocialBee = socialBee;

        check(beeSocialBee.getComment().equals("Testbiene"), "SocialBee muss als Bee zuweisbar sein.");
        check(waspSolitaryBee.getComment().equals("Testbiene"), "SocialBee muss als Wasp zuweisbar sein.");
        check(pollinatorSocialBee.getComment().equals("Testbiene"), "SocialBee muss als Pollinator zuweisbar sein.");
        check(observationSocialBee.getComment().equals("Testbiene"), "SocialBee muss als Observations zuweisbar sein.");
    }

    /**
     * Testet den Vertrag von SolitaryBee.
     * OsmiaCornuta ist IMMER solitär.
     * Lasioglossum und Andrena KÖNNEN solitär sein.
     */
    private static void testSolitaryBeeContracts() {
        System.out.println("\n[Testgruppe: SolitaryBee]");
        ObservationData.clearAll();

        LocalDateTime t1 = LocalDateTime.of(2025, 2, 1, 10, 0);
        Object sameId = new Object();

        // 1. OsmiaCornuta (Immer Solitär)
        // ein Individuum mit 3 Beobachtungen
        OsmiaCornuta o1 = new OsmiaCornuta(t1, "Osmia 1");
        OsmiaCornuta o2 = new OsmiaCornuta(t1.plusHours(1), "Osmia 1 (2. Beobachtung)", o1, 100L);
        OsmiaCornuta o3 = new OsmiaCornuta(t1.plusHours(2), "Osmia 1 (3. Beobachtung)", o2);
        // ein anderes Individuum mit einer Beobachtung
        OsmiaCornuta o4 = new OsmiaCornuta(t1.plusHours(3), "Osmia 2"); // anderes Individuum

        checkSolitaryIteration(o1, 3, "OsmiaCornuta (immer solitär) muss alle 3 Beobachtungen liefern.");
        checkSolitaryIteration(o4, 1, "OsmiaCornuta (immer solitär) muss 1 Beobachtungen liefern.");

        // 2. LasioglossumCalceatum (KANN Solitär sein)
        // l1 mit einer solitären Lebensweise.
        LasioglossumCalceatum l1_sol_1 = new LasioglossumCalceatum(t1.plusHours(2), "Lasio - Solitär", false, true, 200L);
        LasioglossumCalceatum l1_sol_2 = new LasioglossumCalceatum(t1.plusHours(12), "Lasio - Solitär", false, true, 200L);

        // l1 mit einer sozialen Lebensweise, gleiche ID (markerID=200L), sollte NICHT im Solitary-Iterator erscheinen
        LasioglossumCalceatum l1_soc = new LasioglossumCalceatum(t1.plusHours(3), "Lasio - Sozial", true, false, 200L);

        checkSolitaryIteration(l1_sol_1, 2, "LasioglossumCalceatum muss nur die 1 solitäre Beobachtung filtern.");

        // 3. AndrenaBucephala (Kann Solitär sein)
        // a1: Solitär (isSolitary=true), a2: Kommunal (isSolitary=false)
        AndrenaBucephala a1_sol_1 = new AndrenaBucephala(t1.plusHours(4), "Andrena - Solitär", true, true);
        AndrenaBucephala a1_sol_2 = new AndrenaBucephala(t1.plusHours(7), "Andrena - Solitär", true, a1_sol_1, null, true);
        AndrenaBucephala a1_comm = new AndrenaBucephala(t1.plusHours(5), "Andrena - Kommunal", false, a1_sol_1, null, true);

        checkSolitaryIteration(a1_sol_1, 2, "AndrenaBucephala muss 2 solitäre Beobachtungen filtern.");

        // 4. Test der Sortierung
        BehaviorIter<SolitaryBee> oIter = o1.solitary();
        SolitaryBee first = oIter.next();
        SolitaryBee second = oIter.next();
        check(first.getTimestamp().isBefore(second.getTimestamp()), "Solitary Iteration muss chronologisch aufsteigend sortiert sein (a1_sol_1 vor a1_sol_2).");

        System.out.println("  Ersetzbarkeit");
        SolitaryBee solitaryBee = new OsmiaCornuta(t1, "Testbiene");
        WildBee wildBeeSolitaryBee = solitaryBee;
        Bee beeSolitaryBee = solitaryBee;
        Wasp waspSolitaryBee = solitaryBee;
        Pollinator pollinatorSolitaryBee = solitaryBee;
        Observation observationSolitaryBee = solitaryBee;

        check(wildBeeSolitaryBee.getComment().equals("Testbiene"), "SolitaryBee muss als WildBee zuweisbar sein.");
        check(beeSolitaryBee.getComment().equals("Testbiene"), "SolitaryBee muss als Bee zuweisbar sein.");
        check(waspSolitaryBee.getComment().equals("Testbiene"), "SolitaryBee muss als Wasp zuweisbar sein.");
        check(pollinatorSolitaryBee.getComment().equals("Testbiene"), "SolitaryBee muss als Pollinator zuweisbar sein.");
        check(observationSolitaryBee.getComment().equals("Testbiene"), "SolitaryBee muss als Observations zuweisbar sein.");
    }

    /**
     * Testet den Vertrag von WildBee.
     * Jede WildBee (OsmiaCornuta, Bumblebee, LasioglossumCalceatum, AndrenaBucephala) muss `wild()`
     * korrekt implementieren. Honeybee ist KEINE WildBee.
     */
    private static void testWildBeeContracts() {
        System.out.println("\n[Testgruppe: WildBee]");
        ObservationData.clearAll();

        // Eine Liste von Beobachtungen, die zu einem Individuum gehören
        LocalDateTime dt1 = LocalDateTime.of(2025, 10, 14, 10, 0); // 3. im Zeitablauf, fromBreeding=TRUE
        LocalDateTime dt2 = LocalDateTime.of(2025, 10, 12, 10, 0); // 1. im Zeitablauf, fromBreeding=FALSE
        LocalDateTime dt3 = LocalDateTime.of(2025, 10, 13, 10, 0); // 2. im Zeitablauf, fromBreeding=TRUE
        LocalDateTime dt4 = LocalDateTime.of(2025, 10, 15, 10, 0); // 4. im Zeitablauf, fromBreeding=NULL (sollte ignoriert werden)


        // OsmiaCornuta
        // Bee 1 (alle aus Wildnis)
        OsmiaCornuta bee1_1 = new OsmiaCornuta(dt2, "Osmia 1", true);
        OsmiaCornuta bee1_2 = new OsmiaCornuta(dt3, "Osmia 1 ", true, bee1_1);
        OsmiaCornuta bee1_3 = new OsmiaCornuta(dt1, "Comment 1", true, bee1_2);
        // Bee 2 (einmal keine Angaben und einmal aus Zucht angegben)
        OsmiaCornuta bee2_1 = new OsmiaCornuta(dt4, "Comment 4"); // keine Zuchtangabe bekannt
        OsmiaCornuta bee2_2 = new OsmiaCornuta(dt4, "Comment 4", false, bee2_1); // selbes Individuum wie bee2_1, aber mit Zuchtangabe fromBreeding = false

        // --- TEST 1: Filter fromBreeding = TRUE ---
        System.out.println("  Teste OsmiaCornuta: Filter TRUE");

        checkWildIteration(bee1_3, true, 3, "OsmiaCornuta bee1 (alle aus Zucht) muss alle 3 Beobachtungen liefern.");
        checkWildIteration(bee2_1, true, 0, "OsmiaCornuta bee2 (einmal aus Wildnis, einmal ohne Angabe) muss 0 Beobachtungen liefern.");

        System.out.println("  Teste OsmiaCornuta: Filter FALSE (Wildnis)");
        checkWildIteration(bee1_3, false, 0, "OsmiaCornuta bee1 (alle aus Zucht) muss alle 0 Beobachtungen liefern.");
        checkWildIteration(bee2_1, false, 1, "OsmiaCornuta bee2 (einmal aus Wildnis, einmal ohne Angabe) muss 1 Beobachtungen liefern.");

        // LasioglossumCalceatum
        LocalDateTime t_l = LocalDateTime.of(2025, 10, 20, 10, 0);

        LasioglossumCalceatum l1 = new LasioglossumCalceatum(t_l, "Lasio 1", false, true, false); // Wildnis (FALSE)
        LasioglossumCalceatum l2_1 = new LasioglossumCalceatum(t_l.plusHours(1), "Lasio 2_1", false, true, true); // Zucht (TRUE)
        LasioglossumCalceatum l2_2 = new LasioglossumCalceatum(t_l.plusHours(1), "Lasio 2_2", false, true, true, l2_1); // Zucht (TRUE)

        System.out.println("   Teste LasioglossumCalceatum: Filter TRUE\"");
        checkWildIteration(l1, true, 0, "Lasio Filter TRUE muss 0 Beobachtung liefern.");
        checkWildIteration(l2_2, true, 2, "Lasio Filter TRUE muss 2 Beobachtung liefern.");

        System.out.println("   Teste LasioglossumCalceatum: Filter FALSE\"");
        checkWildIteration(l1, false, 1, "Lasio Filter TRUE muss 0 Beobachtung liefern.");
        checkWildIteration(l2_2, false, 0, "Lasio Filter TRUE muss 2 Beobachtung liefern.");

        //BumbleBee
        LocalDateTime t_bb = LocalDateTime.of(2025, 11, 1, 8, 0);
        Bumblebee bb1_1 = new Bumblebee(t_bb, "Bumblebee 1", 300L);
        Bumblebee bb1_2 = new Bumblebee(t_bb, "Bumblebee 1", false, 300L);

        System.out.println("   Teste BumbleBee: Filter TRUE\"");
        checkWildIteration(bb1_1, true, 0, "Bumblebee Filter TRUE muss 0 Elemente liefern.");

        System.out.println("   Teste BumbleBee: Filter FALSE\"");
        checkWildIteration(bb1_1, false, 1, "Bumblebee Filter FALSE muss 1 Elemente liefern.");

        System.out.println("  Ersetzbarkeit");
        Bee WildBee = new Bumblebee(t_bb.plusHours(5), "Testbiene");
        Bee BeeWildBee = WildBee;
        Pollinator pollinatorWildBee = WildBee;
        Wasp waspWildBee = WildBee;
        Observation observationWildBee = WildBee;


        check(BeeWildBee.getComment().equals("Testbiene"), "Wildbiene muss als Biene zuweisbar sein.");
        check(pollinatorWildBee.getComment().equals("Testbiene"), "Wildbiene muss als Pollinator zuweisbar sein.");
        check(waspWildBee.getComment().equals("Testbiene"), "Wildbiene muss als Wasp zuweisbar sein.");
        check(observationWildBee.getComment().equals("Testbiene"), "Wildbiene muss als Observations zuweisbar sein.");

    }

    /**
     * Testet den Vertrag von CommunalBee.
     * Nur AndrenaBucephala implementiert dies.
     */
    private static void testCommunalBeeContracts() {
        System.out.println("\n[Testgruppe: CommunalBee]");
        ObservationData.clearAll();

        LocalDateTime t1 = LocalDateTime.of(2025, 6, 1, 10, 0);

        CommunalBee b1 = new AndrenaBucephala(t1, "Andrena (communal=true)", false, true);
        CommunalBee b2 = new AndrenaBucephala(t1.plusHours(1), "Andrena (communal=false)", true, false);

        List<CommunalBee> communalBees = List.of(b1, b2);

        for (CommunalBee bee : communalBees) {
            String name = bee.getComment();
            System.out.println("  Teste communal() für " + name);
            BehaviorIter<CommunalBee> iter = bee.communal();

            if (name.contains("communal=true")) {
                check(iter.hasNext(), name + ".communal() muss Element(e) liefern.");
                check(iter.next() == bee, name + ".communal() muss sich selbst liefern.");
            } else {
                check(!iter.hasNext(), name + ".communal() darf keine Elemente liefern.");
            }
        }
    }

    /**
     * Testet die Marker-Interfaces Pollinator und Wasp.
     * Zeigt, dass Bienen und FlowerFly Pollinatoren sind.
     * Zeigt, dass Bienen Wespen sind, FlowerFly aber nicht.
     */
    private static void testPollinatorAndWaspContracts() {
        System.out.println("\n[Testgruppe: Pollinator & Wasp (Marker)]");
        ObservationData.clearAll();

        LocalDateTime t1 = LocalDateTime.of(2025, 7, 1, 10, 0);

        Pollinator p1 = new FlowerFly(t1, "Pollinator Fly");
        Pollinator p2 = new Honeybee(t1.plusHours(1), "Pollinator Honeybee");
        Pollinator p3 = new OsmiaCornuta(t1.plusHours(2), "Pollinator Osmia");

        List<Pollinator> pollinators = List.of(p1, p2, p3);

        System.out.println("  Teste Pollinator-Liste (alle müssen Observation-Vertrag erfüllen)");
        for (Pollinator p : pollinators) {
            check(p.valid(), p.getComment() + " muss valide sein.");
            check(p.getTimestamp() != null, p.getComment() + " muss Timestamp haben.");
        }

        Wasp w1 = new Honeybee(t1.plusHours(3), "Wasp Honeybee");
        Wasp w2 = new Bumblebee(t1.plusHours(4), "Wasp Bumblebee");
        //Wasp w3 = new FlowerFly(t1.plusHours(5), "Wasp Fly"); // <- das kompiliert nicht! Was so sein sollte

        List<Wasp> wasps = List.of(w1, w2);

        System.out.println("  Teste Wasp-Liste (alle müssen Observation-Vertrag erfüllen)");
        for (Wasp w : wasps) {
            check(w.valid(), w.getComment() + " muss valide sein.");
            check(w.getTimestamp() != null, w.getComment() + " muss Timestamp haben.");
        }


        Bee bee = new Bumblebee(t1.plusHours(5), "Testbiene");
        Wasp waspBee = bee;
        Pollinator pollinatorBee = bee;

        check(waspBee.getComment().equals("Testbiene"), "Biene muss als Wespe zuweisbar sein.");
        check(pollinatorBee.getComment().equals("Testbiene"), "Biene muss als Pollinator zuweisbar sein.");
    }


    // --- HILFSMETHODEN ---

    /**
     * Eine einfache Test-Hilfsmethode.
     *
     * @param condition Die Bedingung, die wahr sein muss.
     * @param message   Die Erfolgs-/Fehlermeldung.
     */
    private static void check(boolean condition, String message) {
        testCounter++;
        if (!condition) {
            failCounter++;
            System.out.println("  -> FEHLER: " + message);
        } else {
            System.out.println("  -> OK: " + message);
        }
    }

    /**
     * Hilfsmethode zur Überprüfung der Iteration von SolitaryBee.
     *
     * @param bee           Die SolitaryBee-Instanz, deren Iterator geprüft werden soll.
     * @param expectedCount Die erwartete Anzahl von Elementen.
     * @param message       Die Erfolgs-/Fehlermeldung.
     */
    private static void checkSolitaryIteration(SolitaryBee bee, int expectedCount, String message) {
        int count = 0;
        BehaviorIter<SolitaryBee> iter = bee.solitary();
        while (iter.hasNext()) {
            count++;
            iter.next();
        }
        check(count == expectedCount, message + " (Erwartet: " + expectedCount + ", Gefunden: " + count + ")");
    }

    /**
     * Hilfsmethode zur Überprüfung der Iteration von SolitaryBee.
     *
     * @param bee           Die WildBee-Instanz, deren Iterator geprüft werden soll.
     * @param fromBreeding  Die Zuchtangabe
     * @param expectedCount Die erwartete Anzahl von Elementen.
     * @param message       Die Erfolgs-/Fehlermeldung.
     */
    private static void checkWildIteration(WildBee bee, Boolean fromBreeding, int expectedCount, String message) {
        int count = 0;
        BehaviorIter<WildBee> iter = bee.wild(fromBreeding);
        while (iter.hasNext()) {
            count++;
            iter.next();
        }
        check(count == expectedCount, message + " (Erwartet: " + expectedCount + ", Gefunden: " + count + ")");
    }

    /**
     * Hilfsmethode zur Überprüfung der Iteration von SocialBee.
     *
     * @param bee           Die SocialBee-Instanz, deren Iterator geprüft werden soll.
     * @param expectedCount Die erwartete Anzahl von Elementen.
     * @param message       Die Erfolgs-/Fehlermeldung.
     */
    private static void checkSocialIteration(SocialBee bee, int expectedCount, String message) {
        int count = 0;
        BehaviorIter<SocialBee> iter = bee.social();
        while (iter.hasNext()) {
            count++;
            iter.next();
        }
        check(count == expectedCount, message + " (Erwartet: " + expectedCount + ", Gefunden: " + count + ")");
    }


    /**
     * Hilfsmethode zur Überprüfung der Iteration von Bee
     *
     * @param bee            Die Bee-Instanz, deren Iterator geprüft werden soll.
     * @param expectedCount  Die erwartete Anzahl von Elementen.
     * @param message        Die Erfolgs-/Fehlermeldung.
     */
    private static void checkSameBeeIteration(Bee bee, int expectedCount, String message) {
        BehaviorIter<Bee> iter = bee.sameBee();
        int count = 0;
        LocalDateTime t = null;

        while (iter.hasNext()) {
            Bee curr = iter.next();
            count++;

            if(t != null){
                check(!curr.getTimestamp().isBefore(t), "Beobachtungen sind zeitlich sortiert.");
            }
            t = curr.getTimestamp();
        }
        check(count == expectedCount, message + " (Erwartet: " + expectedCount + ", Gefunden: " + count + ")");
    }

    /**
     * Überprüft die übergebenen Beobachtungen des Iterators von den Bee-Objekten korrekt sortiert sind.
     *
     * @param iterator   Der Iterator über Bee-Objekte (sameBee, reverseOrder, [from, to]).
     * @param ascending  Wenn true == wenn Beobachtungen zeitlich aufsteigend sortiert sind.
     *                   Wenn false == wenn Beobachtungen zeitlich absteigend sortiert sind.
     * @param message    Die Erfolgs-/Fehlermeldung.
     */
    private static void checkIteratorOrder(BehaviorIter<Bee> iterator, boolean ascending, String message) {
        LocalDateTime t = null;

        while (iterator.hasNext()) {
            Bee b = iterator.next();
            if (t != null) {
                if (ascending) {
                    check(!b.getTimestamp().isBefore(t), message);
                } else {
                    check(!b.getTimestamp().isAfter(t), message);
                }
            }
            t = b.getTimestamp();
        }
    }
}