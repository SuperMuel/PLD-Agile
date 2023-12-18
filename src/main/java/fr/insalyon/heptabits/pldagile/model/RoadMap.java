package fr.insalyon.heptabits.pldagile.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The RoadMap class represents a road map for a courier on a given date.
 * It contains a list of deliveries and a list of legs.
 * The first leg's departure point must match the last leg's destination. (Typically the warehouse)
 * Each leg's departure point must match the previous leg's arrival point.
 * The number of legs must be one more than the number of deliveries.
 * The deliveries must be made by the same courier.
 * There should be at least one delivery. (If there are no deliveries, the road map is useless)
 */
public class RoadMap extends BaseEntity {
    private final List<Delivery> deliveries;
    private final List<Leg> legs;

    /**
     * Creates a new road map.
     *
     * @param id the id of the road map
     * @param deliveries the list of deliveries
     * @param legs the list of legs
     *
     * The first leg's departure point must match the last leg's destination. (Typically the warehouse)
     * Each leg's departure point must match the previous leg's arrival point.
     * The number of legs must be one more than the number of deliveries.
     * The deliveries must be made by the same courier.
     * There should be at least one delivery. (If there are no deliveries, the road map is useless)
     *
     * @throws IllegalArgumentException if the arguments are invalid
     */
    public RoadMap(long id, List<Delivery> deliveries, List<Leg> legs) {
        super(id);

        if (deliveries == null || legs == null) {
            throw new IllegalArgumentException("RoadMap constructor: null argument");
        }

        if (deliveries.isEmpty()) {
            throw new IllegalArgumentException("RoadMap constructor: empty deliveries list");
        }

        // Check that all deliveries are made by the same courier
        final long numberOfCouriers = deliveries.stream().map(Delivery::courierId).distinct().count();
        if (numberOfCouriers != 1) {
            throw new IllegalArgumentException("RoadMap constructor: deliveries must be made by the same courier");
        }

        if (legs.isEmpty()) {
            throw new IllegalArgumentException("RoadMap constructor: empty legs list");
        }

        if (deliveries.size() != legs.size() - 1) {
            throw new IllegalArgumentException("RoadMap constructor: number of legs must be one more than number of deliveries");
        }

        // Check that departure and arrival points match
        if (!(legs.getFirst().getOrigin().equals(legs.getLast().getDestination()))) {
            throw new IllegalArgumentException("RoadMap constructor: first leg departure point must match first delivery destination");
        }

        // Assert that each leg's departure point matches the previous leg's arrival point
        for (int i = 1; i < legs.size(); i++) {
            if (!(legs.get(i).intersections().getFirst() == (legs.get(i - 1).getDestination()))) {
                throw new IllegalArgumentException("RoadMap constructor: leg departure point must match previous leg destination");
            }
        }

        // Assert that each leg's departure time is before the next leg's departure time
        for (int i = 1; i < legs.size(); i++) {
            if (!(legs.get(i).departureTime().isAfter(legs.get(i - 1).departureTime()))) {
                throw new IllegalArgumentException("RoadMap constructor: leg departure time must be after previous leg departure time");
            }
        }

        this.deliveries = deliveries;
        this.legs = legs;
    }


    /**
     * Returns an unmodifiable list of all deliveries.
     * This ensures that the list cannot be modified outside the RoadMap class.
     *
     * @return An unmodifiable list of deliveries.
     */
    public List<Delivery> getDeliveries() {
        return Collections.unmodifiableList(deliveries);
    }

    /**
     * Returns an unmodifiable list of all legs.
     * This ensures that the list cannot be modified outside the RoadMap class.
     *
     * @return An unmodifiable list of legs.
     */
    public List<Leg> getLegs() {
        return Collections.unmodifiableList(legs);
    }


    /**
     * @return The total length of the road map.
     */
    public double getTotalLength(){
        return legs.stream().mapToDouble(Leg::getTotalLength).sum();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoadMap roadMap = (RoadMap) o;
        return (getId() == roadMap.getId()) && Objects.equals(deliveries, roadMap.deliveries) && Objects.equals(legs, roadMap.legs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deliveries, legs);
    }


    /**
     * @return The id of the courier who will make the deliveries in the road map.
     */
    public long getCourierId() {
        return deliveries.getFirst().courierId();
    }



    /**
     * @return The date of the first delivery in the road map.
     */
    public LocalDate getDate() {
        return deliveries.getFirst().scheduledDateTime().toLocalDate();
    }

    @Override
    public String toString() {
        return "RoadMap{" +
                "id=" + getId() +
                ", deliveries=" + deliveries +
                ", legs=" + legs +
                '}';
    }

}