package fr.insalyon.heptabits.pldagile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The RoadMap class represents a collection of deliveries, ordered by their scheduled time.
 * It allows for adding, removing, and retrieving deliveries in a manner that ensures
 * they are always sorted based on the delivery time.
 */
public class RoadMap extends BaseEntity {
    private final List<Delivery> deliveries;

    /**
     * Constructs an empty RoadMap.
     */
    public RoadMap(long id) {
        super(id);
        this.deliveries = new ArrayList<>();

    }

    /**
     * Adds a delivery to the roadmap in a position that keeps the list sorted by time.
     * <p>
     * WARNING: It should be verified beforehand that this will result in a valid RoadMap.
     *
     * @param delivery The delivery to be added.
     */
    public void addDelivery(Delivery delivery) {
        int insertIndex = 0;
        while (insertIndex < this.deliveries.size() && this.deliveries.get(insertIndex).getScheduledDateTime().isBefore(delivery.getScheduledDateTime())) {
            insertIndex++;
        }
        this.deliveries.add(insertIndex, delivery);
    }

    /**
     * Removes a delivery from the roadmap.
     *
     * @param delivery The delivery to be removed.
     */
    public void removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
    }

    /**
     * Retrieves a delivery by its index in the sorted list.
     *
     * @param index The index of the delivery.
     * @return The delivery at the specified index.
     */
    public Delivery getDelivery(int index) {
        return this.deliveries.get(index);
    }

    /**
     * Returns an unmodifiable list of all deliveries.
     * This ensures that the list cannot be modified outside of the RoadMap class.
     *
     * @return An unmodifiable list of deliveries.
     */
    public List<Delivery> getDeliveries() {
        return java.util.Collections.unmodifiableList(this.deliveries);
    }

    /**
     * Returns the number of deliveries in the roadmap.
     *
     * @return The size of the delivery list.
     */
    public int getDeliveryCount() {
        return this.deliveries.size();
    }

}