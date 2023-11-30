package fr.insalyon.heptabits.pldagile.model;

public class Client extends BaseEntity {
    public String getFirstName() {
        return firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLastName() {
        return lastName;
    }

    private final String firstName;
    private final String lastName;
    private final String phoneNumber;

    public Client(long id, String name, String lastName, String phoneNumber) {
        super(id);
        this.firstName = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

