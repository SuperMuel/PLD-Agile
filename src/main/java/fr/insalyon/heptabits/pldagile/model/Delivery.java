package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalTime;

public class Delivery extends BaseEntity {
    private final LocalTime scheduledTime;
    private final Intersection destination;

    public Delivery(long id, LocalTime scheduledTime, Intersection destination) {
        super(id);
        this.scheduledTime = scheduledTime;
        this.destination = destination;
    }


    public Intersection getDestination() {
        return destination;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }
}
