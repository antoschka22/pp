import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@ProjectClass
@Author(name = "Miriam Reumann")
public class Test {

    @Author(name = "Antonio Molina Gradischnig")
    @Pre(condition = "args != null")
    @Post(condition = "Simulationen und Tests wurden ausgeführt")
    public static void main(String[] args) {
        System.out.println("--- Starte Tests für Programmieraufgabe 6 ---");
        System.out.println("Testet sowohl das Verhalten der Bienen- und Pflanzenklassen als auch die Einhaltung der Annotations- und Zusicherungsregeln...");
        System.out.println("\n// Kontext A: Bienen-Blütenpflanzen-Simulation //");

        // Durchführung von mehreren Simulationsläufen
        System.out.println("Starte 1. Simulationslauf:");
        Simulation s1 = new Simulation();
        s1.run();

        System.out.println("\nStarte 2. Simulationslauf:");
        Simulation s2 = new Simulation();
        s2.run();

        System.out.println("\nStarte 3. Simulationslauf:");
        Simulation s3 = new Simulation();
        s3.run();

        System.out.println("\nStarte 4. Simulationslauf:");
        Simulation s4 = new Simulation();
        s4.run();

        System.out.println("\n// Kontext B: Datenextraktion //");
        extractData();
    }


    @Author(name = "Miriam Reumann")
    @Pre(condition = "true")
    @Post(condition = "Statistiken wurden auf der Konsole ausgegeben")
    private static void extractData() {
        // Alle selbstgeschriebenen Klassen, Interfaces und Annotationen
        Class<?>[] mainClasses = new Class<?>[] {
                Author.class, Pre.class, Post.class, Invariant.class, HistoryConstraint.class, ProjectClass.class,
                Pflanze.class, PflanzeX.class, PflanzeY.class, PflanzeZ.class,
                Biene.class, BieneU.class, BieneV.class, BieneW.class,
                Set.class, Simulation.class
        };

        // vollständige Liste inklusive aller inneren Klassen finden
        List<Class<?>> classList = new ArrayList<>();

        for (Class<?> c : mainClasses) {
            classList.add(c); // Hauptklasse hinzufügen

            // getDeclaredClasses() findet innere Klassen
            for (Class<?> inner : c.getDeclaredClasses()) {
                classList.add(inner);
            }
        }
        Class<?>[] allClasses = classList.toArray(new Class<?>[0]);

        // Datenstrukturen für die Statistik (Punkte 4, 5, 6)
        Map<String, Integer> classCountPerAuthor = new HashMap<>();
        Map<String, Integer> methodCountPerAuthor = new HashMap<>(); // Nur für Klassen
        Map<String, Integer> assertionCountPerAuthor = new HashMap<>(); // Klassen & Interfaces

        System.out.println("1. Namen aller Klassen, Interfaces & Annotationen:");
        for(Class<?> c : allClasses){
            String typeOfClass = "Klasse";
            if(c.isInterface()){
                typeOfClass = c.isAnnotation() ? "Annotation" : "Interface";
            }
            System.out.println(typeOfClass + ": " + c.getSimpleName());
        }

        System.out.println("\n2. Hauptverantwortliches Gruppenmitglied:");
        for(Class<?> c : allClasses){
            Author a = c.getAnnotation(Author.class);
            if(a != null){
                System.out.println("Author of " + c.getSimpleName() + " is " + a.name());
                // Für Punkt 4 sammeln: Anzahl der Klassen/Interfaces pro Autor
                classCountPerAuthor.put(a.name(), classCountPerAuthor.getOrDefault(a.name(), 0) + 1);
            }
        }

        System.out.println("\n3. Signaturen & Zusicherungen:");
        for(Class<?> c : allClasses){
            // Nur Klassen und Interfaces sind hier relevant für die Detailausgabe
            if(c.isInterface() || !c.isAnnotation()) {
                Author classAuthorAnno = c.getAnnotation(Author.class);
                String classAuthorName = classAuthorAnno != null ? classAuthorAnno.name() : null;

                if (!c.isAnnotation()) {
                    System.out.println((c.isInterface() ? "Interface: " : "Klasse: ") + c.getSimpleName());
                }

                if (!c.isAnnotation()) {
                    // --- KLASSEN-EBENE ZUSICHERUNGEN (Invarianten / History Constraints) ---
                    // Diese gehören immer dem Klassen-Autor
                    int assertionsOnClassLevel = 0;
                    Invariant[] invar = c.getAnnotationsByType(Invariant.class);
                    for (Invariant i : invar) {
                        System.out.println("  Invariante: " + i.condition());
                        assertionsOnClassLevel++;
                    }

                    HistoryConstraint[] histC = c.getAnnotationsByType(HistoryConstraint.class);
                    for (HistoryConstraint h : histC) {
                        System.out.println("  History-Constraint: " + h.condition());
                        assertionsOnClassLevel++;
                    }

                    // Statistik Punkt 6 (Zusicherungen) für den Klassenautor eintragen
                    if (classAuthorName != null) {
                        assertionCountPerAuthor.put(classAuthorName, assertionCountPerAuthor.getOrDefault(classAuthorName, 0) + assertionsOnClassLevel);
                    }

                    printInheritedAssertions(c);

                    // --- KONSTRUKTOREN ---
                    if (!c.isInterface()) {
                        Constructor<?>[] constructorOfClass = c.getDeclaredConstructors();

                        for (Constructor<?> con : constructorOfClass) {
                            System.out.println("  Konstruktor: " + con.getName() + paramsAsString(con.getParameterTypes()));

                            // Autor bestimmen: Hat der Konstruktor eine eigene Annotation?
                            Author conAuthorAnno = con.getAnnotation(Author.class);
                            String effectiveAuthor = null;

                            if (conAuthorAnno != null) {
                                effectiveAuthor = conAuthorAnno.name();
                            } else {
                                effectiveAuthor = classAuthorName; // Fallback auf Klassenautor
                            }

                            // Statistik Punkt 5 (Methoden/Konstruktoren)
                            if (effectiveAuthor != null) {
                                methodCountPerAuthor.put(effectiveAuthor, methodCountPerAuthor.getOrDefault(effectiveAuthor, 0) + 1);
                            }

                            // Vor/Nachbedingungen zählen (gehören dem effektiven Autor des Konstruktors)
                            int assertionsInCon = 0;
                            Pre[] pres = con.getAnnotationsByType(Pre.class);
                            for (Pre p : pres) {
                                System.out.println("  -> Vorbedingung: " + p.condition());
                                assertionsInCon++;
                            }
                            Post[] posts = con.getAnnotationsByType(Post.class);
                            for (Post p : posts) {
                                System.out.println("  -> Nachbedingung: " + p.condition());
                                assertionsInCon++;
                            }

                            // Statistik Punkt 6 (Zusicherungen) für Konstruktoren
                            if (effectiveAuthor != null) {
                                assertionCountPerAuthor.put(effectiveAuthor, assertionCountPerAuthor.getOrDefault(effectiveAuthor, 0) + assertionsInCon);
                            }
                        }
                    }

                    // --- METHODEN ---
                    Method[] methods = c.getDeclaredMethods();

                    for (Method m : methods) {
                        System.out.println("  Methode: " + m.getReturnType().getSimpleName() + " " + m.getName() + paramsAsString(m.getParameterTypes()));

                        // Autor bestimmen: Hat die Methode eine eigene Annotation?
                        Author methodAuthorAnno = m.getAnnotation(Author.class);
                        String effectiveAuthor = null;

                        if (methodAuthorAnno != null) {
                            effectiveAuthor = methodAuthorAnno.name();
                        } else {
                            effectiveAuthor = classAuthorName; // Fallback auf Klassenautor
                        }

                        // Statistik Punkt 5 (Methoden/Konstruktoren)
                        if (effectiveAuthor != null && !c.isInterface()) {
                            methodCountPerAuthor.put(effectiveAuthor, methodCountPerAuthor.getOrDefault(effectiveAuthor, 0) + 1);
                        }

                        // Vor/Nachbedingungen zählen (gehören dem effektiven Autor der Methode)
                        int assertionsInMethod = 0;
                        Pre[] pres = m.getAnnotationsByType(Pre.class);
                        for (Pre p : pres) {
                            System.out.println("  -> Vorbedingung: " + p.condition());
                            assertionsInMethod++;
                        }
                        Post[] posts = m.getAnnotationsByType(Post.class);
                        for (Post p : posts) {
                            System.out.println("  -> Nachbedingung: " + p.condition());
                            assertionsInMethod++;
                        }

                        // Statistik Punkt 6 (Zusicherungen) für Methoden
                        if (effectiveAuthor != null) {
                            assertionCountPerAuthor.put(effectiveAuthor, assertionCountPerAuthor.getOrDefault(effectiveAuthor, 0) + assertionsInMethod);
                        }
                    }
                    System.out.println();
                }
            }
        }

        // --------------- Grenzfälle testen --------------------
        System.out.println("\n// Kontext C: Prüfung der Grenzfälle und Logik //");
        testGrenzfaelle();


        // --- AUSGABE DER STATISTIKEN (Punkte 4, 5, 6) ---

        System.out.println("\n 4. Anzahl der Klassen, Interfaces & Annotationen pro Autor:");
        for (Map.Entry<String, Integer> entry : classCountPerAuthor.entrySet()) {
            System.out.println("   " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n5. Anzahl der Methoden und Konstruktoren pro Autor (nur Klassen):");
        for (Map.Entry<String, Integer> entry : methodCountPerAuthor.entrySet()) {
            System.out.println("   " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n6. Anzahl der Zusicherungen pro Autor (Klassen & Interfaces):");
        for (Map.Entry<String, Integer> entry : assertionCountPerAuthor.entrySet()) {
            System.out.println("   " + entry.getKey() + ": " + entry.getValue());
        }
    }
    // -------------HILFSMETHODEN FÜR Grenzfälle-------------
    @Author(name = "Antonio Molina Gradischnig")
    @Pre(condition = "true")
    @Post(condition = "Grenzfälle für Set und Lebensdauer wurden geprüft")
    private static void testGrenzfaelle() {
        // TESTBEREICH 1: Set - Reihenfolge und Iterator
        // Die Implementierung von Set.add nutzt new Node(obj, head)
        // Das bedeutet, neue Elemente kommen an den Anfang
        // Der Iterator sollte diese Reihenfolge widerspiegeln

        System.out.println("1. Teste Set-Reihenfolge (LIFO) & Iterator:");
        Set orderSet = new Set();
        String s1 = "Erster";
        String s2 = "Zweiter";
        String s3 = "Dritter";

        orderSet.add(s1); // Liste: Erster -> null
        orderSet.add(s2); // Liste: Zweiter -> Erster -> null
        orderSet.add(s3); // Liste: Dritter -> Zweiter -> Erster -> null

        java.util.Iterator it = orderSet.iterator();

        check("Iterator hat Elemente", it.hasNext(), true);
        Object o1 = it.next();
        check("1. Element ist 'Dritter' (zuletzt eingefügt)", o1.equals(s3), true);

        Object o2 = it.next();
        check("2. Element ist 'Zweiter'", o2.equals(s2), true);

        Object o3 = it.next();
        check("3. Element ist 'Erster'", o3.equals(s1), true);

        check("Iterator ist am Ende", it.hasNext(), false);


        // TESTBEREICH 2: Set - Löschen (Head, Tail, Middle)
        // remove() hat oft Fehler beim ersten oder letzten Element.
        System.out.println("\n2. Teste Set-Remove an Randpositionen:");

        // Wir bauen das Set neu: Dritter -> Zweiter -> Erster
        // Fall A: Löschen am Ende (Tail)
        orderSet = new Set();
        orderSet.add(s1); orderSet.add(s2); orderSet.add(s3);

        orderSet.remove(s1); // Lösche "Erster" (ganz hinten)
        check("Größe nach Löschen am Ende ist 2", orderSet.size() == 2, true);
        check("End-Element 'Erster' ist weg", orderSet.contains(s1), false);
        check("Start-Element 'Dritter' noch da", orderSet.contains(s3), true);

        // Fall B: Löschen am Anfang (Head)
        // Zustand aktuell: Dritter -> Zweiter
        orderSet.remove(s3); // Lösche "Dritter" (ganz vorne)
        check("Größe nach Löschen am Anfang ist 1", orderSet.size() == 1, true);
        check("Head-Element 'Dritter' ist weg", orderSet.contains(s3), false);
        check("Mittel-Element 'Zweiter' ist noch da", orderSet.contains(s2), true);

        // Fall C: Löschen des letzten verbleibenden Elements
        orderSet.remove(s2);
        check("Set ist leer nach vollständigem Leeren", orderSet.isEmpty(), true);

        // Fall D: Löschen auf leerem Set (Darf nicht crashen)
        try {
            orderSet.remove("GibtsNicht");
            check("Remove auf leerem Set wirft keine Exception", true, true);
        } catch (Exception e) {
            check("Remove auf leerem Set hat Exception geworfen: " + e, false, true);
        }


        // TESTBEREICH 3: Lebensdauer aller Arten
        System.out.println("\n3. Teste Lebensdauern aller Arten exakt:");

        // Teste Biene V (Soll: 8 Tage)
        BieneV bv = new BieneV();
        checkLebensdauerBiene("Biene V", bv, 8);

        // Teste Biene W (Soll: 10 Tage)
        BieneW bw = new BieneW();
        checkLebensdauerBiene("Biene W", bw, 10);

        // Teste Pflanze Y (Soll: 8 Tage)
        PflanzeY py = new PflanzeY();
        checkLebensdauerPflanze("Pflanze Y", py, 8);

        // Teste Pflanze Z (Soll: 10 Tage)
        PflanzeZ pz = new PflanzeZ();
        checkLebensdauerPflanze("Pflanze Z", pz, 10);
    }

   // --- HILFSMETHODEN FÜR LEBENSDAUER ---
    @Author(name = "Antonio Molina Gradischnig")
    @Pre(condition = "b != null && maxTage > 0")
    @Post(condition = "b.isAlive() == false")
    private static void checkLebensdauerBiene(String name, Biene b, int maxTage) {
        // 1. Altern bis kurz vor Tod
        for(int i = 0; i < maxTage; i++) {
            if (!b.isAlive()) {
                System.out.println("  [FEHLER] " + name + " ist zu früh gestorben an Tag " + i);
                return;
            }
            b.nextDay();
        }
        // 2. Jetzt muss sie tot sein (aktiveTage == lebensdauer)
        // isAlive ist definiert als (aktiveTage < lebensdauer).
        boolean istTot = !b.isAlive();
        check(name + " stirbt exakt nach " + maxTage + " Tagen", istTot, true);
    }

    @Author(name = "Antonio Molina Gradischnig")
    @Pre(condition = "p != null && maxTage > 0")
    @Post(condition = "p.isAlive() == false")
    private static void checkLebensdauerPflanze(String name, Pflanze p, int maxTage) {
        // 1. Altern bis kurz vor Tod
        for(int i = 0; i < maxTage; i++) {
            if (!p.isAlive()) {
                System.out.println("  [FEHLER] " + name + " ist zu früh verwelkt an Tag " + i);
                return;
            }
            p.nextDay();
        }
        // 2. Jetzt muss sie tot sein
        boolean istTot = !p.isAlive();
        check(name + " verwelkt exakt nach " + maxTage + " Tagen", istTot, true);
    }

    @Author(name = "Antonio Molina Gradischnig")
    @Pre(condition = "beschreibung != null")
    @Post(condition = "Ergebnis wurde auf Konsole ausgegeben")
    private static void check(String beschreibung, boolean ist, boolean soll) {
        if (ist == soll) {
            System.out.println("  [OK] " + beschreibung);
        } else {
            System.out.println("  [FEHLER] " + beschreibung + " | Ist: " + ist + ", Soll: " + soll);
        }
    }


    // -------------HILFSMETHODEN FÜR STATISTIK-------------
    @Author(name = "Antonio Molina Gradischnig")
    @Pre(condition = "c != null")
    @Post(condition = "Geerbte Zusicherungen wurden ausgegeben")
    private static void printInheritedAssertions(Class<?> c){
        Class<?> superClass = c.getSuperclass();
        if(superClass != null && !superClass.equals(Object.class)){
            Invariant[] superInvar = superClass.getAnnotationsByType(Invariant.class);
            for(Invariant invar : superInvar){
                System.out.println("      Invariante geerbt aus " + superClass.getSimpleName() + ": " + invar.condition());
            }

            HistoryConstraint[] superHC = superClass.getAnnotationsByType(HistoryConstraint.class);
            for(HistoryConstraint hC : superHC){
                System.out.println("      History-Constraint geerbt aus " + superClass.getSimpleName() + ": " + hC.condition());
            }
        }
    }

    @Author(name = "Antonio Molina Gradischnig")
    @Pre(condition = "params != null")
    @Post(condition = "result.startsWith(\"(\") && result.endsWith(\")\")")
    private static String paramsAsString(Class<?>[] params){
        if(params.length == 0) return "()";

        StringBuilder sb = new StringBuilder("(");
        for(int i = 0; i < params.length; i++){
            sb.append(params[i].getSimpleName());
            if(i < params.length - 1) sb.append(", ");
        }
        sb.append(")");

        return sb.toString();
    }
}
