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

        System.out.println("1. Namen aller Klassen, Interfaces & Annotationen:");
        for(Class<?> c : allClasses){
            String typeOfClass = "Klasse";
            if(c.isInterface()){
                typeOfClass = c.isAnnotation() ? "Annotation" : "Interface";
            }
            System.out.println(typeOfClass + ": " + c.getSimpleName());
        }

        System.out.println("\n2. Hauptverantwortliches Gruppenmitglied:");
        Map<String, Integer> authorOfClass = new HashMap<>();
        for(Class<?> c : allClasses){
            Author a = c.getAnnotation(Author.class);
            if(a != null){
                System.out.println("Author of " + c.getSimpleName() + " is " + a.name());
                authorOfClass.put(a.name(), authorOfClass.getOrDefault(a.name(), 0) + 1);
            }
        }

        System.out.println("\n3. Signaturen & Zusicherungen:");
        Map<String, Integer> methodPerAuthor = new HashMap<>();
        Map<String, Integer> assertionPerAuthor = new HashMap<>();

        for(Class<?> c : allClasses){
            if(c.isInterface() || !c.isAnnotation()) {
                Author classAuthor = c.getAnnotation(Author.class);
                String nameOfAuthor = classAuthor != null ? classAuthor.name() : "Author is unknown";

                if (!c.isAnnotation()) {
                    if (c.isInterface()) {
                        System.out.println("Interface: " + c.getName());
                    } else {
                        System.out.println("Klasse: " + c.getSimpleName());
                    }
                }

                if (!c.isAnnotation()) {
                    // Klassenzusicherungen ausgeben
                    Invariant[] invar = c.getAnnotationsByType(Invariant.class);
                    for (Invariant i : invar) {
                        System.out.println("  Invariante: " + i.condition());
                        assertionPerAuthor.put(nameOfAuthor, assertionPerAuthor.getOrDefault(nameOfAuthor, 0) + 1);
                    }

                    HistoryConstraint[] histC = c.getAnnotationsByType(HistoryConstraint.class);
                    for (HistoryConstraint h : histC) {
                        System.out.println("  History-Constraint: " + h.condition());
                        assertionPerAuthor.put(nameOfAuthor, assertionPerAuthor.getOrDefault(nameOfAuthor, 0) + 1);
                    }

                    // Geerbte Zusicherungen ausgeben
                    printInheritedAssertions(c, assertionPerAuthor, nameOfAuthor);
                    System.out.println();

                    // Konstruktoren mit den Zusicherungen ausgeben pro Klasse
                    if (!c.isInterface()) {
                        Constructor<?>[] constructorOfClass = c.getConstructors();
                        for (Constructor<?> con : constructorOfClass) {
                            if (classAuthor != null) {
                                methodPerAuthor.put(nameOfAuthor, methodPerAuthor.getOrDefault(nameOfAuthor, 0) + 1);
                            }
                            System.out.println("  Konstruktor: " + con.getName() + paramsAsString(con.getParameterTypes()));

                            // Vorbedingungen von Konstruktor ausgeben
                            Pre[] pres = con.getAnnotationsByType(Pre.class);
                            for (Pre p : pres) {
                                System.out.println("  -> Vorbedingung: " + p.condition());
                                assertionPerAuthor.put(nameOfAuthor, assertionPerAuthor.getOrDefault(nameOfAuthor, 0) + 1);
                            }

                            // Nachbedingung von Konstruktor ausgeben
                            Post[] post = con.getAnnotationsByType(Post.class);
                            for (Post p : post) {
                                System.out.println("  -> Nachbedingung: " + p.condition());
                                assertionPerAuthor.put(nameOfAuthor, assertionPerAuthor.getOrDefault(nameOfAuthor, 0) + 1);
                            }
                        }
                        System.out.println();
                    }

                    // Methoden mit den Zusicherungen ausgeben pro Klasse

                    Method[] methods = c.getDeclaredMethods();
                    for (Method m : methods) {
                        if (classAuthor != null && !c.isInterface()) {
                            methodPerAuthor.put(nameOfAuthor, methodPerAuthor.getOrDefault(nameOfAuthor, 0) + 1);
                        }
                        System.out.println("  Methode: " + m.getReturnType().getSimpleName() + " " + m.getName() + paramsAsString(m.getParameterTypes()));

                        // Vorbedingungen von Konstruktor ausgeben
                        Pre[] pres = m.getAnnotationsByType(Pre.class);
                        for (Pre p : pres) {
                            System.out.println("  -> Vorbedingung: " + p.condition());
                            assertionPerAuthor.put(nameOfAuthor, assertionPerAuthor.getOrDefault(nameOfAuthor, 0) + 1);
                        }

                        // Nachbedingung von Konstruktor ausgeben
                        Post[] post = m.getAnnotationsByType(Post.class);
                        for (Post p : post) {
                            System.out.println("  -> Nachbedingung: " + p.condition());
                            assertionPerAuthor.put(nameOfAuthor, assertionPerAuthor.getOrDefault(nameOfAuthor, 0) + 1);
                        }
                        System.out.println();
                    }

                }
            }
        }
        System.out.println("4. Anzahl der Klassen,Interfaces & Annotationen pro Autor:");
        System.out.println("\n5. Anzahl der Methoden und Konstruktoren pro Autor:");
        System.out.println("\n6. Anzahl der Zusicherungen pro Autor:");
    }

    // -------------HILFSMETHODEN-------------

    private static void printInheritedAssertions(Class<?> c, Map<String, Integer> assertionPerAuthor, String author){
        Class<?> superClass = c.getSuperclass();
        if(superClass != null && !superClass.equals(Object.class)){

            // geerbte Invarianten für die jeweilige Klasse ausgeben
            Invariant[] superInvar = superClass.getAnnotationsByType(Invariant.class);
            for(Invariant invar : superInvar){
                System.out.println("      Invariante geerbt aus " + superClass.getSimpleName() + ": " + invar.condition());
                assertionPerAuthor.put(author, assertionPerAuthor.getOrDefault(author, 0) + 1);
            }

            // geerbte History-Constraints für die jeweilige Klasse ausgeben
            HistoryConstraint[] superHC = superClass.getAnnotationsByType(HistoryConstraint.class);
            for(HistoryConstraint hC : superHC){
                System.out.println("      History-Constraint geerbt aus " + superClass.getSimpleName() + ": " + hC.condition());
                assertionPerAuthor.put(author, assertionPerAuthor.getOrDefault(author, 0) + 1);
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
