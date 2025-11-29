import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@ProjectClass
@Author(name = "Miriam Reumann")
public class Test {
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

    private static void extractData() {
        // alle selbstgeschriebenen Klassen, Interfaces und Annotationen
        Class<?>[] allClasses = new Class<?>[] {
                Author.class, Pre.class, Post.class, Invariant.class, HistoryConstraint.class, ProjectClass.class,
                Pflanze.class, PflanzeX.class, PflanzeY.class, PflanzeZ.class,
                Biene.class, BieneU.class, BieneV.class, BieneW.class,
                Set.class, Simulation.class
        };

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

                // Für Punkt 4 sammeln
                classCountPerAuthor.put(a.name(), classCountPerAuthor.getOrDefault(a.name(), 0) + 1);
            }
        }

        System.out.println("\n3. Signaturen & Zusicherungen:");
        for(Class<?> c : allClasses){
            // Nur Klassen und Interfaces sind hier relevant für die Detailausgabe
            if(c.isInterface() || !c.isAnnotation()) {
                Author classAuthor = c.getAnnotation(Author.class);
                String authorName = classAuthor != null ? classAuthor.name() : "Unbekannt";

                // Vorbereitung für Punkt 6
                int assertionsInClass = 0;

                if (!c.isAnnotation()) {
                    System.out.println((c.isInterface() ? "Interface: " : "Klasse: ") + c.getSimpleName());
                }

                if (!c.isAnnotation()) {
                    // Klassenzusicherungen ausgeben
                    Invariant[] invar = c.getAnnotationsByType(Invariant.class);
                    for (Invariant i : invar) {
                        System.out.println("  Invariante: " + i.condition());
                        assertionsInClass++;
                    }

                    // Klassenzusicherungen (HistoryConstraints)
                    HistoryConstraint[] histC = c.getAnnotationsByType(HistoryConstraint.class);
                    for (HistoryConstraint h : histC) {
                        System.out.println("  History-Constraint: " + h.condition());
                        assertionsInClass++;
                    }

                    // Geerbte Zusicherungen ausgeben (Zählen nicht für den Autor der Unterklasse!)
                    printInheritedAssertions(c);

                    // --- Konstruktoren ---
                    if (!c.isInterface()) {
                        // getDeclaredConstructors() findet auch nicht-public Konstruktoren
                        Constructor<?>[] constructorOfClass = c.getDeclaredConstructors();

                        // Statistik Punkt 5 (Methoden/Konstruktoren in Klassen)
                        if (classAuthor != null) {
                            methodCountPerAuthor.put(authorName, methodCountPerAuthor.getOrDefault(authorName, 0) + constructorOfClass.length);
                        }

                        for (Constructor<?> con : constructorOfClass) {
                            System.out.println("  Konstruktor: " + con.getName() + paramsAsString(con.getParameterTypes()));

                            // Vorbedingungen
                            Pre[] pres = con.getAnnotationsByType(Pre.class);
                            for (Pre p : pres) {
                                System.out.println("  -> Vorbedingung: " + p.condition());
                                assertionsInClass++;
                            }

                            // Nachbedingungen
                            Post[] posts = con.getAnnotationsByType(Post.class);
                            for (Post p : posts) {
                                System.out.println("  -> Nachbedingung: " + p.condition());
                                assertionsInClass++;
                            }
                        }
                    }

                    // --- Methoden ---
                    // getDeclaredMethods() findet alle selbst deklarierten Methoden (ohne geerbte)
                    Method[] methods = c.getDeclaredMethods();

                    // Statistik Punkt 5 (Methoden in Klassen)
                    if (classAuthor != null && !c.isInterface()) {
                        methodCountPerAuthor.put(authorName, methodCountPerAuthor.getOrDefault(authorName, 0) + methods.length);
                    }

                    for (Method m : methods) {
                        System.out.println("  Methode: " + m.getReturnType().getSimpleName() + " " + m.getName() + paramsAsString(m.getParameterTypes()));

                        Pre[] pres = m.getAnnotationsByType(Pre.class);
                        for (Pre p : pres) {
                            System.out.println("  -> Vorbedingung: " + p.condition());
                            assertionsInClass++;
                        }

                        Post[] posts = m.getAnnotationsByType(Post.class);
                        for (Post p : posts) {
                            System.out.println("  -> Nachbedingung: " + p.condition());
                            assertionsInClass++;
                        }
                    }
                    System.out.println();
                }

                // Statistik Punkt 6 speichern (nur wenn Autor bekannt)
                if (classAuthor != null) {
                    assertionCountPerAuthor.put(authorName, assertionCountPerAuthor.getOrDefault(authorName, 0) + assertionsInClass);
                }
            }
        }

        // --- AUSGABE DER STATISTIKEN (Punkte 4, 5, 6) ---

        System.out.println("4. Anzahl der Klassen, Interfaces & Annotationen pro Autor:");
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
    // -------------HILFSMETHODEN-------------

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
