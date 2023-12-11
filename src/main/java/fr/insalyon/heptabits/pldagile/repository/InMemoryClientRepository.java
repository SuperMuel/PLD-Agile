package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;
import fr.insalyon.heptabits.pldagile.model.Client;

import java.util.*;


public class InMemoryClientRepository implements ClientRepository {
    final private HashMap<Long,Client> hashMapClients;

    final private IdGenerator idGenerator;
    public InMemoryClientRepository(IdGenerator idGenerator){
        hashMapClients = new HashMap<>();
        this.idGenerator = idGenerator;
    }
    @Override
    public Client create(String name, String lastName, String phoneNumber) {

        Client client = new Client(idGenerator.getNextId(), name, lastName, phoneNumber);
        hashMapClients.put(client.getId(), client);

        return client;
    }

    public Client findById(long id) {
        return hashMapClients.get(id);
    }

    @Override
    public List<Client> findByName(String name, String lastName) {
        List<Client> res = new ArrayList<>();
        if (lastName == null) {
            for (java.util.Map.Entry<Long, Client> mapEntry : hashMapClients.entrySet()) {
                Client c = mapEntry.getValue();
                if (c.getFirstName().contains(name)) {
                    res.add(c);
                }
            }
        } else if (name == null) {
            for (java.util.Map.Entry<Long, Client> mapEntry : hashMapClients.entrySet()) {
                Client c = mapEntry.getValue();
                if (c.getLastName().contains(lastName)) {
                    res.add(c);
                }
            }
        } else {
            for (java.util.Map.Entry<Long, Client> mapEntry : hashMapClients.entrySet()) {
                Client c = mapEntry.getValue();
                if (c.getFirstName().contains(name) && c.getLastName().contains(lastName)) {
                    res.add(c);
                }
            }
        }

        return res;
    }

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

    @Override
    public Client update(Client client) {
        hashMapClients.put(client.getId(), client);
        return hashMapClients.get(client.getId());
    }

    @Override
    public void delete(Client client) {
        hashMapClients.remove(client.getId());
    }
}
