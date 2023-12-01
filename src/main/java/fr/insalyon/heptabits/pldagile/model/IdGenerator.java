package fr.insalyon.heptabits.pldagile.model;

public class IdGenerator {
    private long id = 1;

    public long getNextId() {
        return id++;
    }

}
