import java.time.LocalDateTime;

/**
 * Diese Klasse repräsentiert eine Honigbiene. Honigbienen sind sozial, aber explizit
 * nicht kommunal oder solitär und zählen nicht zu den Wildbienen.
 */
public class Honeybee implements SocialBee{
    @Override
    public Object individualIdentifier() {
        return null;
    }

    @Override
    public Long markerID() {
        return null;
    }

    @Override
    public SameBeeIter sameBee() {
        return null;
    }

    @Override
    public SameBeeIterReverse sameBee(boolean reverseOrder) {
        return null;
    }

    @Override
    public SameBeeIterTimeRange sameBee(LocalDateTime from, LocalDateTime to) {
        return null;
    }

    @Override
    public BehaviorIter<SocialBee> social() {
        return null;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return null;
    }

    @Override
    public String getComment() {
        return null;
    }

    @Override
    public void remove() {

    }

    @Override
    public boolean valid() {
        return false;
    }

    @Override
    public BehaviorIter<Observation> later() {
        return null;
    }

    @Override
    public BehaviorIter<Observation> earlier() {
        return null;
    }
}
