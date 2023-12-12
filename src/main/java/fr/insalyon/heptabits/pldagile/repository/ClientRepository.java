package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Client;

import java.util.List;

/**
 * Repository for clients.
 *
 * This interface is used to abstract the storage of clients.
 * It is used to create, find, update and delete clients.
 */
public interface ClientRepository {


    /** Creates a client.
     *
     * @param name the name of the client
     * @param surname the surname of the client
     * @param phoneNumber the phone number of the client
     * @return the created client
     */
    Client create(String name, String surname, String phoneNumber);

    /**
     * Finds a client by its id.
     *
     * @param id the id of the client
     * @return the client if found, null otherwise
     */
    Client findById(long id);

    /**
     * Finds a client by its name and surname.
     *
     * @param name the name of the client
     * @param lastName the surname of the client
     * @return a list of clients with the given name and surname
     */
    List<Client> findByName(String name, String lastName);

    /**
     * Finds all clients.
     *
     * @return a list of all clients
     */
    List<Client> findAll();

    /**
     * Updates a client.
     *
     * @param client the client to update
     * @return the updated client
     */
    Client update(Client client);

    /**
     * Deletes a client.
     *
     * @param client the client to delete
     */
    void delete(Client client);
}
