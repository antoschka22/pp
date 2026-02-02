/*
Miriam Reumann hat das OrdSet Interface und die folgenden Klassen: Bee, WildBee und HoneyBee implementiert.
Zudem hat sie die ersten drei Punkte für die Testfälle gemäß der Aufgabenstellung gemacht, d.h. sie hat alle
vorgegebenen Objekte erstellt, diese mit Einträgen und Ordnungsbeziehungen befüllt, die Iterator- und Methoden-
aufrufe typgerecht getestet und die Begründungen zu den Untertypbeziehungen dokumentiert.

Antonio Molina Gradischnig hat die Klassen Num, MSet mit der privaten Klasse ResultImpl, die Interfaces MSetResult und Ordered implementiert.
Außerdem habe ich in der Testklasse die Punkte 4-6 von den Testfällen gemäß der Aufgabenstellung gemacht. Außerdem
habe ich bei der Begründung der Untertypbeziehungen mitgeholfen.

Simon Oberdörfer hat die Interfaces Modifiable und OSetResults geschrieben. Außerdem hat er die abstrakte Klasse AbstractOrdSet
mit RelationNode und ElementNode geschrieben sowie die Klassen ISet und OSet vollständig implementiert
(inklusive der privaten Klasse Subset).
*/

/*
Begründung der Untertypbeziehungen zwischen ISet, OSet & MSet:
- Die Klassen ISet und OSet erben direkt von AbstractOrdSet<E,R>.
  MSet hingegen erbt von OSet<E>. Dadurch besteht eine direkte Untertypbeziehung zwischen MSet und OSet.
- Zwischen ISet und den anderen beiden Klassen (OSet & MSet) besteht keine direkte Untertypbeziehung.
  Der Grund liegt hauptsächlich in den unterschiedlichen Rückgabetypen der Methoden, wie folgende Punkte zeigen:

  1) Unterschiede in der before()-Methode:
     * ISet: Gibt einen Iterator<E> zurück.
     * OSet: Gibt ein 'OSetResult'-Objekt (SubSet) zurück.
     * MSet: Erbt von OSet und überschreibt die Methode, um ein 'MSetResult' zurückzugeben.
       Dies ist erlaubt, da 'MSetResult' ein Untertyp von 'OSetResult' ist (kovarianter Rückgabetyp).
     Die unterschiedlichen Rückgabetypen (Iterator bei ISet vs. Container bei OSet) verhindern jedoch eine direkte Vererbung zwischen ISet und OSet.

  2) Unterschiede in Typparametern:
     * ISet & OSet: Haben einen Typparameter E.
     * MSet: Der Typparameter E wird stärker eingeschränkt (muss Modifiable<X, E> sein) und der Parameter X kommt hinzu.
       Da dies eine Spezialisierung darstellt, ist die Ableitung MSet extends OSet zulässig. Eine Ableitung von ISet ist aufgrund von Punkt 1 nicht möglich.

  3) Unterschiede bezüglich des Verhaltens & der Einschränkungen:
     * ISet: Iteriert lediglich über Elemente zwischen x & y.
     * OSet: Liefert geordnete Teilmengen.
     * MSet: Übernimmt die Funktionalität von OSet und erweitert sie um die Methoden plus & minus, die auf den modifizierbaren Elementen operieren.
 */

import java.util.Iterator;

public class Test {

    public static void main(String[] args) {

        System.out.println("--- Starte Tests für Programmieraufgabe 5 ---");
        System.out.println("Testet die Verwendbarkeit und Funktionalität der generischen Container...");
        

        try {
            // Punkt 1: Container erstellen & mit Einträgen & Ordnungsbeziehungen befüllen //

            // Container erstellen
            // Container für Num
            ISet<Num> isetNum = new ISet<>(null);
            OSet<Num> osetNum = new OSet<>(null);
            MSet<Num, Num> msetNumNum = new MSet<>(null);

            // Container für Bienen
            ISet<Bee> isetBee = new ISet<>(null);
            OSet<Bee> osetBee = new OSet<>(null);

            // Container für Wildbienen
            ISet<WildBee> isetWildBee = new ISet<>(null);
            OSet<WildBee> osetWildBee = new OSet<>(null);
            MSet<WildBee, Integer> msetWildBeeInt = new MSet<>(null);

            // Container für Honigbienen
            ISet<HoneyBee> isetHoneyBee = new ISet<>(null);
            OSet<HoneyBee> osetHoneyBee = new OSet<>(null);
            MSet<HoneyBee, String> msetHoneyBeeString = new MSet<>(null);

            // Objekte erzeugen
            // Num-Objekte
            Num n1 = new Num(4);
            Num n2 = new Num(9);

            // Bee-Objekte
            Bee bee1 = new Bee("Biene im Garten");
            Bee bee2 = new Bee("Biene beim Nektar sammeln");

            // WildBee-Objekte
            WildBee wb1 = new WildBee("Wildbiene auf Sonnenblume", 13);
            WildBee wb2 = new WildBee("Wildbiene beim Pollensammeln", 8);

            // HoneyBee-Objekte
            HoneyBee hb1 = new HoneyBee("Honigbiene auf Stockwand", "Melipona");
            HoneyBee hb2 = new HoneyBee("Honigbiene gesehen auf Wiese ", "Apis");

            // Container mit Einträgen & Ordnungsbeziehungen befüllen
            isetNum.setBefore(n1, n2);
            osetNum.setBefore(n1, n2);
            msetNumNum.setBefore(n1, n2);

            isetBee.setBefore(bee1, bee2);
            osetBee.setBefore(bee1, bee2);

            isetWildBee.setBefore(wb1, wb2);
            osetWildBee.setBefore(wb1, wb2);
            msetWildBeeInt.setBefore(wb1, wb2);

            isetHoneyBee.setBefore(hb1, hb2);
            osetHoneyBee.setBefore(hb1, hb2);
            msetHoneyBeeString.setBefore(hb1, hb2);

            // Punkt 2: Ordnungsbeziehungen aus c1; c2 übertragen //
            ISet<Bee> a1 = isetBee;
            OSet<Bee> a2 = osetBee;
            MSet<WildBee, Integer> b1 = msetWildBeeInt;
            MSet<HoneyBee, String> b2 = msetHoneyBeeString;
            OSet<WildBee> c1 = osetWildBee;
            ISet<HoneyBee> c2 = isetHoneyBee;

            // 1) aus c1 alle Einträge auslesen, length() aufrufen und mittels der before-Methode alle Ordnungen ermitteln & diese auf a1 & b1 übertragen
           Iterator<WildBee> iter1 = c1.iterator();
           while(iter1.hasNext()){
                WildBee w1 = iter1.next();
                w1.length();
                Iterator<WildBee> iter2 = c1.iterator();
                while(iter2.hasNext()){
                    WildBee w2 = iter2.next();
                    // Sicherstellen, dass nicht dasselbe Objekt verglichen wird
                    if(!w1.equals(w2)){
                        // prüfen, ob es eine Ordnungsbeziehung zwischen w1 & w2 gibt
                        if(c1.before(w1, w2) != null){
                            // Ordnungsbeziehung auf Container a1 & b1 übertragen
                            a1.setBefore(w1, w2);
                            b1.setBefore(w1, w2);
                        }
                    }
                }
            }

            // 2) aus c2 alle Einträge auslesen, sort() aufrufen und die Ordnungen auf a2 & b2 übertragen
            Iterator<HoneyBee> it1 = c2.iterator();
            while(it1.hasNext()){
                HoneyBee h1 = it1.next();
                h1.sort();
                Iterator<HoneyBee> it2 = c2.iterator();
                while(it2.hasNext()){
                    HoneyBee h2 = it2.next();
                    // Sicherstellen, dass nicht dasselbe Objekt verglichen wird
                    if(!h1.equals(h2)){
                        // prüfen, ob es eine Ordnungsbeziehung zwischen h1 & h2 gibt
                        if(c2.before(h1, h2) != null){
                            // Ordnungsbeziehung auf Container a2 & b2 übertragen
                            a2.setBefore(h1, h2);
                            b2.setBefore(h1, h2);
                        }
                    }
                }
            }

            // Punkt 3: check & checkForced mit erlaubten Typen testen //
            // OSet<Num> kann check/checkForced mit ISet<Num> und MSet<Num,Num> aufrufen
            // OSet<Num>.check(OSet<Bee>) ist NICHT ERLAUBT, aufgrund verschiedener Typparameter!
            osetNum.check(isetNum);
            osetNum.check(msetNumNum);
            osetNum.checkForced(isetNum);
            osetNum.checkForced(msetNumNum);

            // ISet<Num> kann check/checkForced mit OSet<Num> und MSet<Num,Num> aufrufen
            isetNum.check(osetNum);
            isetNum.check(msetNumNum);
            isetNum.checkForced(osetNum);
            isetNum.checkForced(msetNumNum);

            // ISet<WildBee> kann check/checkForced mit MSet<WildBee, Integer> aufrufen
            isetWildBee.check(b1);
            isetWildBee.checkForced(b1);

            // OSet<HoneyBee> kann check/checkForced mit MSet<HoneyBee, String> aufrufen
            osetHoneyBee.check(b2);
            osetHoneyBee.checkForced(b2);

            // ------------------------------------------------------------
            // PUNKT 4
            // ------------------------------------------------------------
            System.out.println("\n--- Punkt 4: Untertypbeziehungen testen ---");

            // Test: MSet ist Untertyp von OSet
            System.out.println("Test 4.1: Zuweisung MSet -> OSet Variable (Liskov Substitution)");
            OSet<WildBee> osetRef = msetWildBeeInt;
            // Wir fügen über die OSet Referenz etwas hinzu, das im MSet landen muss
            WildBee wbExtra = new WildBee("Extra Biene", 10);
            // OSet hat setBefore
            osetRef.setBefore(wbExtra, wb1);
            System.out.println("  Erfolg: Element via OSet-Referenz in MSet eingefügt.");
            System.out.println("  MSet size ist nun: " + msetWildBeeInt.size());

            // Begründung für fehlende Beziehungen (ISet <-> OSet) wurde im Dateikopf gegeben.
            // Code-technisch würde ISet<WildBee> x = osetWildBee; nicht kompilieren.

            // Zusicherung testen: OSetResult (Rückgabe von OSet.before) muss Modifiable sein
            System.out.println("Test 4.2: Rückgabetyp von OSet.before ist Modifiable");
            OSetResult<WildBee> subset = osetRef.before(wbExtra, wb2);
            // wb1 liegt zwischen wbExtra und wb2 (da wbExtra->wb1 und wb1->wb2)
            if (subset != null) {
                // Modifiable Methoden testen
                OSetResult<WildBee> subModified = subset.add(new WildBee("Neu", 5));
                System.out.println("  Erfolg: OSetResult verhält sich wie Modifiable.");
            } else {
                System.out.println("  Info: Kein Subset gefunden (Ordnung evtl. anders als erwartet).");
            }


            // ------------------------------------------------------------
            // PUNKT 5
            // ------------------------------------------------------------
            System.out.println("\n--- Punkt 5: Gesamte Funktionalität & Ausgabe ---");

            // 5.1 MSet spezifische Methoden (plus / minus)
            System.out.println("5.1 Teste MSet.plus (alle Längen um 2 erhöhen) auf WildBienen:");
            System.out.println("  Vorher: " + msetWildBeeInt);
            // Wir erhöhen alle Bienen-Längen um 2.
            // Achtung: plus() ruft setBefore auf -> erstellt neue Objekte und ordnet sie vor den alten ein.
            msetWildBeeInt.plus(2);
            System.out.println("  Nachher: " + msetWildBeeInt);

            System.out.println("5.2 Teste MSet.minus (String 'o' entfernen) auf Honigbienen:");
            System.out.println("  Vorher: " + msetHoneyBeeString);
            msetHoneyBeeString.minus("o");
            System.out.println("  Nachher: " + msetHoneyBeeString);

            // 5.3 ISet before (Iterator)
            System.out.println("5.3 Teste ISet.before (Iterator Rückgabe)");
            Iterator<HoneyBee> iterHB = isetHoneyBee.before(hb1, hb2);
            if (iterHB != null) {
                System.out.print("  Elemente zwischen hb1 und hb2: ");
                boolean found = false;
                while(iterHB.hasNext()) {
                    System.out.print(iterHB.next() + ", ");
                    found = true;
                }
                if (!found) System.out.print("Keine (direkte Kante oder leer).");
                System.out.println();
            }

            // 5.4 Methoden der Elemente (Modifiable) explizit aufrufen und ausgeben
            System.out.println("5.4 Element-Methoden Tests:");
            // Num
            Num nSum = n1.add(n2);
            System.out.println("  Num add: " + n1 + " + " + n2 + " = " + nSum);
            // WildBee
            System.out.println("  WildBee Length: " + wb1.length());
            WildBee wbGrown = wb1.add(5);
            System.out.println("  WildBee Add(5): " + wb1 + " -> " + wbGrown + " (Len: " + wbGrown.length() + ")");
            // HoneyBee
            System.out.println("  HoneyBee Sort: " + hb1.sort());
            HoneyBee hbMod = hb1.add("XL");
            System.out.println("  HoneyBee Add('XL'): " + hbMod.sort());

            // 5.5 Ausgabe aller Container (toString)
            System.out.println("\n--- Status aller Container ---");
            System.out.println(isetNum);
            System.out.println(osetNum);
            System.out.println(msetNumNum);
            System.out.println(isetBee);
            System.out.println(osetBee); // Enthält nun auch hb1->hb2 aus Punkt 2
            System.out.println(isetWildBee);
            System.out.println(osetWildBee);
            System.out.println(msetWildBeeInt);
            System.out.println(isetHoneyBee);
            System.out.println(osetHoneyBee);
            System.out.println(msetHoneyBeeString);


            // ------------------------------------------------------------
            // PUNKT 6 (Optional)
            // ------------------------------------------------------------
            System.out.println("\n--- Punkt 6: Weitere Überprüfungen (Fehlerfälle) ---");

            // Test: Zyklus verhindern
            System.out.println("Test 6.1: Zyklische Ordnung verhindern");
            try {
                // n1 ist vor n2. Versuche n2 vor n1 zu setzen.
                osetNum.setBefore(n2, n1);
                System.out.println("  FEHLER: Zyklus wurde nicht erkannt!");
            } catch (IllegalArgumentException e) {
                System.out.println("  Erfolg: Zyklus erkannt und verhindert (" + e.getMessage() + ")");
            }

            // Test: Selbst-Referenz
            System.out.println("Test 6.2: Identische Elemente vergleichen");
            try {
                isetNum.setBefore(n1, n1);
                System.out.println("  FEHLER: Identität nicht erkannt!");
            } catch (IllegalArgumentException e) {
                System.out.println("  Erfolg: Identische Elemente abgefangen (" + e.getMessage() + ")");
            }

        } catch (Exception e) {
            System.out.println("...FEHLER!");
            System.out.println("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("\n--- Tests beendet ---");
    }
}
