import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Diese Klasse hält eine statische Liste aller erstellten Observation-Objekte
 * Sie ist notwendig, damit die Methoden later() und earlier()
 * alle anderen Beobachtungen durchsuchen können
 */
public class ObservationData {

    /**
     * Eine threadsichere Liste, die alle Beobachtungs-Objekte enthält
     * Jedes Objekt muss sich bei seiner Erstellung selbst hier hinzufügen
     */
    public static final List<Observation> ALL_OBSERVATIONS = Collections.synchronizedList(new ArrayList<>());

    /**
     * Hilfsmethode, um die Liste für jeden Testlauf zurückzusetzen.
     */
    public static void clearAll() {
        // Synchronisiere den Zugriff, um Thread-Interferenzen zu vermeiden
        synchronized (ALL_OBSERVATIONS) {
            ALL_OBSERVATIONS.clear();
        }
    }
}