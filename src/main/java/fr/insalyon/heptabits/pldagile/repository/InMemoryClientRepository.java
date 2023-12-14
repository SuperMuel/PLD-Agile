package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Client;

import java.util.*;


/**
 * Repository to create, find, update and delete clients.
 * <p>
 * The entities are stored in memory and are not persisted.
 */
public class InMemoryClientRepository implements ClientRepository {
    final private HashMap<Long,Client> hashMapClients;

    final private IdGenerator idGenerator;


    /** Creates a new in memory client repository.
     *
     * @param idGenerator the id generator to use.
     */
    public InMemoryClientRepository(IdGenerator idGenerator){
        hashMapClients = new HashMap<>();
        this.idGenerator = idGenerator;
    }

    /**
     * Creates a client.
     *
     * @param name the name of the client
     * @param lastName the last name of the client
     * @param phoneNumber the phone number of the client
     * @return the created client
     * <p>
     * No validation is done on the parameters. It is the responsibility of the
     * caller to ensure that the parameters are valid.
     */
    @Override
    public Client create(String name, String lastName, String phoneNumber) {
        Client client = new Client(idGenerator.getNextId(), name, lastName, phoneNumber);
        hashMapClients.put(client.getId(), client);

        return client;
    }

    /**
     * Finds a client by its id.
     *
     * @param id the id of the client
     * @return the client if found, null otherwise
     */
    @Override
    public Client findById(long id) {
        return hashMapClients.get(id);
    }


    /**
     * Finds a client by its firstName and/or surname.
     * <p>
     * Case-insensitive. Matches if the name or the surname contains the given
     * name or surname.
     *
     * @param firstName the name of the client
     * @param lastName the surname of the client
     *
     * @return a list of clients with the given name and surname
     */
    @Override
    public List<Client> findByName(String firstName, String lastName) {
        if(firstName==null && lastName==null){
            throw new NullPointerException("findByName: both name and lastName are null");
        }

        Set<Client> res = new HashSet<>();
        if(firstName!=null){
            for (java.util.Map.Entry<Long, Client> mapEntry : hashMapClients.entrySet()) {
                Client c = mapEntry.getValue();
                if (c.getFirstName().toLowerCase().contains(firstName.toLowerCase())) {
                    res.add(c);
                }
            }
        }
        if(lastName!=null){
            for (java.util.Map.Entry<Long, Client> mapEntry : hashMapClients.entrySet()) {
                Client c = mapEntry.getValue();
                if (c.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
                    res.add(c);
                }
            }
        }
        return new ArrayList<>(res);
    }

    /**
     * Finds all clients.
     *
     * @return a list of all clients
     */
    @Override
    public List<Client> findAll() {
        List<Client> res = new ArrayList<>();
        for (java.util.Map.Entry<Long, Client> mapEntry : hashMapClients.entrySet()) {
            res.add(mapEntry.getValue());
        }

        Comparator<Client> byLastName = Comparator.comparing(Client::getLastName);
        res.sort(byLastName);

        return res;
    }

    /**
     * Updates a client.
     *
     * @param client the client to update
     * @return the updated client
     * <p>
     * No validation is done on the parameters. It is the responsibility of the
     * caller to ensure that the parameters are valid.
     */
    @Override
    public Client update(Client client) {
        hashMapClients.put(client.getId(), client);
        return hashMapClients.get(client.getId());
    }

    /**
     * Deletes a client.
     *
     * @param client the client to delete
     */
    @Override
    public void delete(Client client) {
        hashMapClients.remove(client.getId());
    }
}
