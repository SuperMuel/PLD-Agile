package fr.insalyon.heptabits.pldagile.model;

/**
 * Generates unique ids.
 *
 * The ids are generated sequentially, starting from 1.
 *
 * This is used when we are not using a database, to generate ids for entities.
 */
public class IdGenerator {
    private long id = 1;

    public long getNextId() {
        return id++;
    }

}
