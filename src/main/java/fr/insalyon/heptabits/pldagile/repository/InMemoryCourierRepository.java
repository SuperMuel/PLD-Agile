package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Courier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Repository for couriers to create, find, update and delete couriers.
 *
 * This implementation stores couriers in memory, so this it not persistent.
 */
public class InMemoryCourierRepository implements CourierRepository {

    private final HashMap<Long, Courier> couriers;
    private final IdGenerator idGenerator;

    /**
     * Creates a new in memory courier repository.
     *
     * @param idGenerator the id generator to use
     */
    public InMemoryCourierRepository(IdGenerator idGenerator) {
        this.couriers = new HashMap<>();
        this.idGenerator = idGenerator;
    }

    /**
     * Creates a courier.
     *
     * @param firstName the first name of the courier
     * @param lastName the last name of the courier
     * @param email the email of the courier
     * @param phoneNumber the phone number of the courier
     * @return the created courier
     */
    @Override
    public Courier create(String firstName, String lastName, String email, String phoneNumber) {
        Courier courier = new Courier(idGenerator.getNextId(), firstName, lastName, email, phoneNumber);
        couriers.put(courier.getId(), courier);
        return courier;
    }

    /**
     * Finds a courier by its id.
     *
     * @param id the id of the courier
     * @return the courier if found, null otherwise
     */
    @Override
    public Courier findById(Long id) {
        return  couriers.get(id);
    }

    /**
     * Finds all couriers.
     *
     * @return a list of all couriers
     */
    @Override
    public List<Courier> findAll() {
        return new ArrayList<>(this.couriers.values());
    }

    /**
     * Updates a courier.
     *
     * @param courier the courier to update
     * @return the updated courier
     */
    @Override
    public Courier update(Courier courier) {
        couriers.put(courier.getId(), courier);
        return courier;
    }

    /**
     * Deletes a courier.
     *
     * @param id the id of the courier to delete
     */
    @Override
    public void deleteById(long id) {
        couriers.remove(id);
    }
}
