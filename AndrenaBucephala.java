import java.time.LocalDateTime;
import java.util.Collections;

public class AndrenaBucephala implements CommunalBee, SolitaryBee {

    private final java.time.LocalDateTime timestamp;
    private final String comment;
    private boolean isValid;

    private final Object individualIdentifier;
    private final Long markerId;



    public AndrenaBucephala(LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = new Object();
        this.markerId = null;
    }

    public AndrenaBucephala(LocalDateTime timestamp, String comment, Bee previousObservation) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = previousObservation.individualIdentifier();
        this.markerId = previousObservation.markerID();
    }

    public AndrenaBucephala(LocalDateTime timestamp, String comment, long markerId) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = new Object();
        this.markerId = markerId;
    }

    public AndrenaBucephala(LocalDateTime timestamp, String comment, Bee previousObservation, long markerId) {
        this.timestamp = timestamp;
        this.comment = comment;
        this.isValid = true;
        this.individualIdentifier = previousObservation.individualIdentifier();
        this.markerId = markerId;
    }

    @Override
    public final LocalDateTime getTimestamp() {
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

    @Override
    public Object individualIdentifier() {
        return this.individualIdentifier;
    }

    @Override
    public Long markerID() {
        return this.markerId;
    }

    @Override
    public SameBeeIter sameBee() {
        return new SameBeeIter(Collections.emptyList());
    }

    @Override
    public SameBeeIterReverse sameBee(boolean reverseOrder) {
        return new SameBeeIterReverse(Collections.emptyList());
    }

    @Override
    public SameBeeIterTimeRange sameBee(LocalDateTime from, LocalDateTime to) {
        return new SameBeeIterTimeRange(Collections.emptyList(), from, to);
    }

    @Override
    public BehaviorIter<WildBee> wild(boolean fromZucht) {
        return new BehaviorIter<>(Collections.emptyList());
    }

    @Override
    public BehaviorIter<SolitaryBee> solitary() {
        return new BehaviorIter<>(Collections.emptyList());
    }

    @Override
    public BehaviorIter<CommunalBee> communal() {
        return new BehaviorIter<>(Collections.emptyList());
    }
}