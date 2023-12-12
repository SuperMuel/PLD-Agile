package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;

public class MockClientRepository extends InMemoryClientRepository {

    public MockClientRepository(IdGenerator idGenerator) {
        super(idGenerator);

        this.create("John", "Doe", "0123456789");
        this.create("Jane", "Doe", "0123456789");
        this.create("John", "Smith", "0123456789");
        this.create("Jane", "Smith", "0123456789");
        this.create("John", "Johnson", "0123456789");
        this.create("Jane", "Johnson", "0123456789");
        this.create("John", "Williams", "0123456789");
        this.create("Jane", "Williams", "0123456789");
        this.create("John", "Jones", "0123456789");
        this.create("Jane", "Jones", "0123456789");
        this.create("John", "Brown", "0123456789");
        this.create("Jane", "Brown", "0123456789");
        this.create("John", "Davis", "0123456789");
        this.create("Jane", "Davis", "0123456789");
        this.create("John", "Miller", "0123456789");
        this.create("Jane", "Miller", "0123456789");
        this.create("John", "Wilson", "0123456789");
        this.create("Jane", "Wilson", "0123456789");

    }


}
