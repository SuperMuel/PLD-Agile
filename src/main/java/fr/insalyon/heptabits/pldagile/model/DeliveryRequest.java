package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDate;

public class DeliveryRequest {
    // Doesn't need to carry an ID since it's never stored

    private final LocalDate date;

    private final Intersection destination;

    private final TimeWindow timeWindow;

    private final long clientId;

    public Intersection getDestination() {
        return destination;
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    public DeliveryRequest(LocalDate date, long clientId, Intersection destination, TimeWindow timeWindow) {
        if (date == null || destination == null || timeWindow == null) {
            throw new IllegalArgumentException("DeliveryRequest constructor: null argument");
        }

        this.date = date;
        this.clientId = clientId;
        this.destination = destination;
        this.timeWindow = timeWindow;
    }

    // create a constructor that takes a Delivery as argument
    public DeliveryRequest(Delivery delivery) {
        if (delivery == null) {
            throw new IllegalArgumentException("DeliveryRequest constructor: null argument");
        }
        this.date = delivery.getScheduledDateTime().toLocalDate();
        this.clientId = delivery.getClientId();
        this.destination = delivery.getDestination();
        this.timeWindow = delivery.getTimeWindow();
    }


    public LocalDate getDate() {
        return date;
    }


    public long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "DeliveryRequest{" +
                "date=" + date +
                ", destination=" + destination +
                ", timeWindow=" + timeWindow +
                ", clientId=" + clientId +
                '}';
    }

}
