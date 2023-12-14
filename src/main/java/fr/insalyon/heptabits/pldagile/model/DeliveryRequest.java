package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a delivery request.
 * <p>
 * Stores the date, the destination, the client and the time window.
 * <p>
 * This class is used to represent a delivery request before it is scheduled (and becomes a Delivery).
 * <p>
 * The id is inherited from BaseEntity.
 * <p>
 * All fields are final.
 */
public class DeliveryRequest {
    private final LocalDate date;


    // We don't use a Client full object here because
    // we could at some point update the client's information
    // in other parts of the application.
    private final long clientId;

    // We don't use a Courier full object here because
    // we could at some point update the courier's information
    // in other parts of the application.
    private final long courierId;

    // We use an Intersection object here because
    // the intersection objects never change.
    private final Intersection destination;

    private final TimeWindow timeWindow;


    /**
     * @return the destination of the delivery request
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * @return the time window of the delivery request
     */
    public TimeWindow getTimeWindow() {
        return timeWindow;
    }


    /**
     * Creates a new delivery request.
     *
     * @param date the date of the delivery request. Must not be null.
     * @param clientId the id of the client.
     * @param destination the destination of the delivery request. Must not be null.
     * @param timeWindow the time window of the delivery request. Must not be null.
     * @param courierId the id of the courier
     */
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

    /**
     * Creates a new delivery request from a delivery.
     * <p>
     * The delivery request will have the same destination, courier, client, date and TimeWindow.
     * (Only the scheduled time is discarded)
     *
     * @param delivery the delivery to copy. Must not be null.
     */
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


    /**
     * @return the date of the delivery request
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return the id of the client
     */
    public long getClientId() {
        return clientId;
    }

    /**
     * @return the id of the courier
     */
    public long getCourierId() {
        return courierId;
    }

    /**
     * Transforms the delivery request into a delivery.
     * <p>
     * The delivery will have the same destination, courier, client, date and time window.
     *
     * @param scheduledTime the scheduled time of the delivery
     * @return the delivery
     */
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
