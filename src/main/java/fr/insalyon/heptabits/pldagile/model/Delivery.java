package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDateTime;


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


    public DeliveryRequest toDeliveryRequest() {
        return new DeliveryRequest(this);
    }

}
