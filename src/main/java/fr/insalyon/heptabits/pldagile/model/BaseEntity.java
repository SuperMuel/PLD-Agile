package fr.insalyon.heptabits.pldagile.model;

public abstract class BaseEntity {
    protected long id;

    public BaseEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
