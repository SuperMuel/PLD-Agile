package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.Courier;
import fr.insalyon.heptabits.pldagile.model.IdGenerator;

import java.util.List;

public class MockCourierRepository extends InMemoryCourierRepository {

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
