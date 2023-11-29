package fr.insalyon.heptabits.pldagile.model;

public class Client extends BaseEntity {
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLastName() { return lastName; }

    private final String name;
    private final String lastName;
    private final String phoneNumber;

    public Client(long id, String name, String lastName, String phoneNumber) {
        super(id);
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

