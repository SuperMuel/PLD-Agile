package fr.insalyon.heptabits.pldagile.model;

public class Courier extends BaseEntity {

    private final String firstName;

    private final String lastName;

    private final String email;

    private final String phoneNumber;

    public Courier(long id,String firstName, String lastName, String email, String phoneNumber) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
