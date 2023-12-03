package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Client;

import java.util.List;

public interface ClientRepository {
    Client create(String firstName, String lastName, String phoneNumber);
    Client findById(long id);
    List<Client> findByName(String firstName, String lastName);
    List<Client> findAll();
    Client update(Client client);
    void delete(Client client);
}
