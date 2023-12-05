package fr.insalyon.heptabits.pldagile.model;

import java.util.Collections;
import java.util.List;

/**
 * The RoadMap class represents a collection of deliveries, ordered by their scheduled time.
 * It allows for adding, removing, and retrieving deliveries in a manner that ensures
 * they are always sorted based on the delivery time.
 */
public class RoadMap extends BaseEntity {
    private final List<Delivery> deliveries;
    private final List<Leg> legs;

    public RoadMap(long id, List<Delivery> deliveries, List<Leg> legs) {
        super(id);

        if(deliveries == null || legs == null){
            throw new IllegalArgumentException("RoadMap constructor: null argument");
        }

        if(deliveries.isEmpty()){
            throw new IllegalArgumentException("RoadMap constructor: empty deliveries list");
        }

        if(legs.isEmpty()){
            throw new IllegalArgumentException("RoadMap constructor: empty legs list");
        }

        if(deliveries.size() != legs.size() -1){
            throw new IllegalArgumentException("RoadMap constructor: number of legs must be one more than number of deliveries");
        }

        // Check that departure and arrival points match
        if(!(legs.get(0).getOrigin().equals(legs.getLast().getDestination()))){
            throw new IllegalArgumentException("RoadMap constructor: first leg departure point must match first delivery destination");
        }

        // Assert that each leg's departure point matches the previous leg's arrival point
        for(int i = 1; i < legs.size(); i++){
            if(!(legs.get(i).getIntersections().get(0) == (legs.get(i-1).getDestination()))){
                throw new IllegalArgumentException("RoadMap constructor: leg departure point must match previous leg destination");
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
        return Collections.unmodifiableList(this.deliveries);
    }

    public List<Leg> getLegs() {
        return Collections.unmodifiableList(legs);
    }
}