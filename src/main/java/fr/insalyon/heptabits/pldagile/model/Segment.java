package fr.insalyon.heptabits.pldagile.model;

public class Segment extends BaseEntity {

    // has a origin and destination intersection ids, and a name
    private final long originId;
    private final long destinationId;
    private final String name;


    public Segment(long id, long originId, long destinationId, String name) {
        super(id);
        this.originId = originId;
        this.destinationId = destinationId;
        this.name = name;
    }

    public long getOriginId() {
        return originId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "id=" + getId() +
                ", originId=" + originId +
                ", destinationId=" + destinationId +
                ", name='" + name + '\'' +
                '}';
    }


}
