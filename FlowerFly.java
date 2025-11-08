
public class FlowerFly implements Pollinator {

    private final java.time.LocalDateTime timestamp;
    private final String comment;
    private boolean isValid;

    public FlowerFly(java.time.LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
    }


    @Override
    public final java.time.LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public final String getComment() {
        return comment;
    }

    @Override
    public final void remove() {
        this.isValid = false;
    }

    @Override
    public final boolean valid() {
        return isValid;
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