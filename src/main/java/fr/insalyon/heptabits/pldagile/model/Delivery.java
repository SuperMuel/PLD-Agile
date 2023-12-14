package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDateTime;


/**
 * Represents a delivery.
 * <p>
 * Stores the scheduled date and time, the destination, the courier and the client.
 * <p>
 * The id is inherited from BaseEntity.
 * <p>
 * All fields are final.
 */
public record Delivery(LocalDateTime scheduledDateTime, Intersection destination, long courierId, long clientId,
                       TimeWindow timeWindow) {

    @Override
    public String toString() {
        return "Delivery{" +
                "scheduledDateTime=" + scheduledDateTime +
                ", destination=" + destination +
                ", courierId=" + courierId +
                ", clientId=" + clientId +
                ", timeWindow=" + timeWindow +
                '}';
    }


    /**
     * Transforms the delivery into a delivery request.
     * <p>
     * The request will have the same destination, courier, client and TimeWindow.
     *
     * @return the delivery request
     */
    public DeliveryRequest toDeliveryRequest() {
        return new DeliveryRequest(this);
    }

}
