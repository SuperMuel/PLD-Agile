package fr.insalyon.heptabits.pldagile.model;

/**
 * Base class for all entities.
 * <p>
 * Stores the id of the entity.
 */
public abstract class BaseEntity {
    protected final long id;

    public BaseEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
