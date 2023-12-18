package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.IdGenerator;

/**
 * A mock for the courier repository.
 */
public class MockCourierRepository extends InMemoryCourierRepository {

    /**
     * Creates a new mock courier repository.
     *
     * @param idGenerator the id generator to use
     */
    public MockCourierRepository(IdGenerator idGenerator) {
        super(idGenerator);
                create("John", "Doe", "johndoe.email.com", "0123456789");
                create("Jane", "Doe", "janedoe.email.com", "0123456789");
                create("John", "Smith", "johnsmith.email.com", "0123456789");
                create("Jane", "Smith", "janesmith.email.com", "0123456789");
                create("John", "Johnson", "johnjohnson.email.com", "0123456789");
                create("Jane", "Johnson", "janejohnson.email.com", "0123456789");
                create("John", "Williams", "johnwilliams.email.com", "0123456789");
                create("Jane", "Williams", "janewilliams.email.com", "0123456789");

    }

}
