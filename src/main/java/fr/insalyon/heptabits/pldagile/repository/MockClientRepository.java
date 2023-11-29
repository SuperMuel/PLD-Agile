package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Client;

import java.util.List;

public class MockClientRepository implements ClientRepository {
    private final List<Client> clients;

    public MockClientRepository() {
        this.clients = List.of(
            new Client(1, "John", "Doe", "0123456789"),
            new Client(2, "Jane", "Doe", "0123456789"),
            new Client(3, "John", "Smith", "0123456789"),
            new Client(4, "Jane", "Smith", "0123456789"),
            new Client(5, "John", "Wayne", "0123456789"),
            new Client(6, "Jane", "Wayne", "0123456789"),
            new Client(7, "John", "Doe", "0123456789"),
            new Client(8, "Jane", "Doe", "0123456789"),
            new Client(9, "John", "Smith", "0123456789"),
            new Client(10, "Jane", "Smith", "0123456789"),
            new Client(11, "John", "Wayne", "0123456789"),
            new Client(12, "Jane", "Wayne", "0123456789"),
            new Client(13, "John", "Doe", "0123456789"),
            new Client(14, "Jane", "Doe", "0123456789"),
            new Client(15, "John", "Smith", "0123456789"),
            new Client(16, "Jane", "Smith", "0123456789"),
            new Client(17, "John", "Wayne", "0123456789"),
            new Client(18, "Jane", "Wayne", "0123456789"),
            new Client(19, "John", "Doe", "0123456789"),
            new Client(20, "Jane", "Doe", "0123456789"),
            new Client(21, "John", "Smith", "0123456789"),
            new Client(22, "Jane", "Smith", "0123456789"),
            new Client(23, "John", "Wayne", "0123456789"),
            new Client(24, "Jane", "Wayne", "0123456789"),
            new Client(25, "John", "Doe", "0123456789"),
            new Client(26, "Jane", "Doe", "0123456789"),
            new Client(27, "John", "Smith", "0123456789")
        );
    }
    @Override
    public Client create(String name, String surname, String phoneNumber) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");
    }

    @Override
    public Client findById(long id) {
        return clients.stream()
                .filter(client -> client.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Client> findByName(String name, String lastName) {
        return clients.stream()
                .filter(client -> client.getName().equals(name) || client.getLastName().equals(lastName))
                .toList();
    }

    @Override
    public List<Client> findAll() {
        return clients;
    }

    @Override
    public Client update(Client client) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");
    }

    @Override
    public void delete(Client client) {
        // thrown "unimplemented" error
        throw new UnsupportedOperationException("This is a mock repository. Only get and find methods are implemented.");

    }
}
