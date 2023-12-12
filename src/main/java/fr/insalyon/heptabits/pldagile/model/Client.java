package fr.insalyon.heptabits.pldagile.model;

/**
 * Represents a client.
 *
 * Stores the first name, last name and phone number of the client.
 *
 * The id is inherited from BaseEntity.
 *
 * All fields are final.
 */
public class Client extends BaseEntity {


    /**
     * @return the first name of the client
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the phone number of the client
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the last name of the client
     */
    public String getLastName() {
        return lastName;
    }


    private final String firstName;
    private final String lastName;
    private final String phoneNumber;

    /**
     * Creates a new client.
     *
     * @param id the id of the client
     * @param name the first name of the client
     * @param lastName the last name of the client
     * @param phoneNumber the phone number of the client
     *
     * Fields are not validated. You should use a validator before creating a client.
     */
    public Client(long id, String name, String lastName, String phoneNumber) {
        super(id);
        this.firstName = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the full name of the client
     *
     * Example: "John Doe"
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

