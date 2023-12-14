package fr.insalyon.heptabits.pldagile.model;

/**
 * Represents a courier.
 * <p>
 * Stores the first name, last name, email and phone number of the courier.
 * <p>
 * The id is inherited from BaseEntity.
 * <p>
 * All fields are final.
 */
public class Courier extends BaseEntity {

    private final String firstName;

    private final String lastName;

    private final String email;

    private final String phoneNumber;

    /**
     * Creates a new courier.
     *
     * @param id the id of the courier
     * @param firstName the first name of the courier
     * @param lastName the last name of the courier
     * @param email the email of the courier
     * @param phoneNumber the phone number of the courier
     *
     * Fields are not validated. You should use a validator before creating a courier.
     */
    public Courier(long id,String firstName, String lastName, String email, String phoneNumber) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    /**
     * @return the first name of the courier
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the last name of the courier
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the email of the courier
     */

    public String getEmail() {
        return email;
    }

    /**
     * @return the phone number of the courier
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the full name of the courier
     *
     * Example: "John Doe"
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
