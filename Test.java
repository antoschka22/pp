/*
Miriam Reumann hat das OrdSet Interface und die folgenden Klassen: Bee, WildBee und HoneyBee implementiert.
Zudem hat sie die ersten drei Punkte für die Testfälle gemäß der Aufgabenstellung gemacht, d.h. sie hat alle
vorgegebenen Objekte erstellt, diese mit Einträgen und Ordnungsbeziehungen befüllt, die Iterator- und Methoden-
aufrufe typgerecht getestet und die Begründungen zu den Untertypbeziehungen dokumentiert.

Antonio Molina Gradischnig

Simon Oberdörfer
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

        // Ein (leeres) Dummy-Objekt 'c' wird für die Konstruktoren benötigt.
        Ordered c_null = null;

        try {
            // Punkt 1: Container erstellen & mit Einträgen & Ordnungsbeziehungen befüllen //

            // Container erstellen
            // Container für Num
            ISet<Num> isetNum = new ISet<>(c_null);
            OSet<Num> osetNum = new OSet<>(c_null);
            MSet<Num, Num> msetNumNum = new MSet<>(c_null);

            // Container für Bienen
            ISet<Bee> isetBee = new ISet<>(c_null);
            OSet<Bee> osetBee = new OSet<>(c_null);

            // Container für Wildbienen
            ISet<WildBee> isetWildBee = new ISet<>(c_null);
            OSet<WildBee> osetWildBee = new OSet<>(c_null);
            MSet<WildBee, Integer> msetWildBeeInt = new MSet<>(c_null);

            // Container für Honigbienen
            ISet<HoneyBee> isetHoneyBee = new ISet<>(c_null);
            OSet<HoneyBee> osetHoneyBee = new OSet<>(c_null);
            MSet<HoneyBee, String> msetHoneyBeeString = new MSet<>(c_null);

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

        } catch (Exception e) {
            System.out.println("...FEHLER!");
            System.out.println("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
