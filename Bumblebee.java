import java.time.LocalDateTime;

/**
 * Diese Klasse repräsentiert eine Hummel. Hummeln sind staatenbildend, das bedeutet also
 * sie zählen lediglich zu den sozialen Bienen (SocialBee).
 */
public class Bumblebee implements SocialBee, WildBee{


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
    public BehaviorIter<WildBee> wild(boolean fromBreeding) {
        return null;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return null;
    }

    @Override
    public String getComment() {
        return "";
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
