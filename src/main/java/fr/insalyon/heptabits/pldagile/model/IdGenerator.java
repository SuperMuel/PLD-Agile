package fr.insalyon.heptabits.pldagile.model;

/**
 * Generates unique ids.
 * <p>
 * The ids are generated sequentially, starting from 1.
 * <p>
 * This is used when we are not using a database, to generate ids for entities.
 *
 * This class is not a singleton. Each instance has its own sequence of ids.
 */
public class IdGenerator {
    private long id = 1;

    /**
     * Creates a new id generator.
     */
    public IdGenerator() {
    }

    /**
     * Returns the next id.
     *
     * @return the next id. The first call to this method returns 1. The ids are guaranteed to be unique
     * across each call on this specific instance, but not across instances.
     */
    public long getNextId() {
        return id++;
    }

}
