
public interface Observation {

    java.time.LocalDateTime getTimestamp();

    String getComment();

    void remove();

    boolean valid();

    BehaviorIter<Observation> later();

    BehaviorIter<Observation> earlier();
}