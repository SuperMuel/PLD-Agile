package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class DeliveryRequest {
    // Doesn't need to carry an ID since it's never stored

    private final LocalDate date;

    private final Intersection destination;

    private final TimeWindow timeWindow;

    private final long clientId;

    private final long courierId;

    public Intersection getDestination() {
        return destination;
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    public DeliveryRequest(LocalDate date, long clientId, Intersection destination, TimeWindow timeWindow, long courierId) {
        if (date == null || destination == null || timeWindow == null) {
            throw new IllegalArgumentException("DeliveryRequest constructor: null argument");
        }

        this.date = date;
        this.clientId = clientId;
        this.destination = destination;
        this.courierId = courierId;
        this.timeWindow = timeWindow;
    }

    // create a constructor that takes a Delivery as argument
    public DeliveryRequest(Delivery delivery) {
        if (delivery == null) {
            throw new IllegalArgumentException("DeliveryRequest constructor: null argument");
        }
        this.date = delivery.scheduledDateTime().toLocalDate();
        this.clientId = delivery.clientId();
        this.destination = delivery.destination();
        this.timeWindow = delivery.timeWindow();
        this.courierId = delivery.courierId();
    }


    public LocalDate getDate() {
        return date;
    }


    public long getClientId() {
        return clientId;
    }

    public long getCourierId() {
        return courierId;
    }


    public Delivery toDelivery(LocalTime scheduledTime) {
        return new Delivery(date.atTime(scheduledTime), destination, courierId, clientId, timeWindow);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryRequest that = (DeliveryRequest) o;
        return clientId == that.clientId && courierId == that.courierId && Objects.equals(date, that.date) && Objects.equals(destination, that.destination) && Objects.equals(timeWindow, that.timeWindow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, destination, timeWindow, clientId, courierId);
    }

    @Override
    public String toString() {
        return "DeliveryRequest{" +
                "date=" + date +
                ", destination=" + destination +
                ", timeWindow=" + timeWindow +
                ", clientId=" + clientId +
                ", courierId=" + courierId +
                '}';
    }

}
