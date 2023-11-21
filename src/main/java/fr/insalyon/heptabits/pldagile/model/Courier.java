package fr.insalyon.heptabits.pldagile.model;

public class Courier extends BaseEntity {

    public String getName() {
        return name;
    }

    private final String name;

    public Courier(long id, String name) {
        super(id);
        this.name = name;
    }


}
