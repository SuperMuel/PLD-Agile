package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDateTime;

public class Delivery extends BaseEntity {
    private final LocalDateTime scheduledDateTime;

    private final Intersection destination;

    // Using id here because the courier's properties can change.
    private final long courierId;

    private final long clientId;

    public Delivery(long id, LocalDateTime scheduledDateTime, Intersection destination, long courierId, long clientId) {
        super(id);
        this.scheduledDateTime = scheduledDateTime;
        this.destination = destination;
        this.courierId = courierId;
        this.clientId = clientId;
    }


    public Intersection getDestination() {
        return destination;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + getId() +
                ", scheduledDateTime=" + scheduledDateTime +
                ", destination=" + destination +
                ", courierId=" + courierId +
                '}';
    }


    public long getCourierId() {
        return courierId;
    }

    public long getClientId() {
        return clientId;
    }
}
