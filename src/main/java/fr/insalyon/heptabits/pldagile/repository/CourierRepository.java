package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Courier;

import java.util.List;

/**
 * Repository for couriers.
 * <p>
 * This interface is used to abstract the storage of couriers.
 * It is used to create, find, update and delete couriers.
 */
public interface CourierRepository {

    /**
     * Creates a courier.
     *
     * @param firstName the first name of the courier
     * @param lastName the last name of the courier
     * @param email the email of the courier
     * @param phoneNumber the phone number of the courier
     * @return the created courier
     */
    Courier create(String firstName, String lastName, String email, String phoneNumber);

    /**
     * Finds a courier by its id.
     *
     * @param id the id of the courier
     * @return the courier if found, null otherwise
     */
    Courier findById(Long id);


    /**
     * Finds all couriers.
     *
     * @return a list of all couriers
     */
    List<Courier> findAll();


    /**
     * Updates a courier.
     *
     * @param courier the courier to update
     * @return the updated courier
     */
    Courier update(Courier courier);

    /**
     * Deletes a courier.
     *
     * @param id the id of the courier to delete
     */
    void deleteById(long id);
}
