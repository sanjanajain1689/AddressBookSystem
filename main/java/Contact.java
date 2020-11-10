import java.util.ArrayList;
import java.util.Objects;

public class Contact implements java.io.Serializable{
    private int contact_id;
    private int ab_id;
    private String firstName, lastName, phoneNumber, email;
    private String address, city, state;
    private int zip;
    public ArrayList<String> type;

    public Contact(int ab_id, String firstName, String lastName, String phoneNumber, String email, String address,
                   String city, String state, int zip, ArrayList<String> type) {
        super();
        this.setAb_id(ab_id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.type = type;
    }

    public Contact(int contact_id, int ab_id, String firstName, String lastName, String phoneNumber) {
        this.contact_id = contact_id;
        this.ab_id = ab_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        type = new ArrayList<String>();
    }

    public Contact() {
        type = new ArrayList<String>();
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getZip() {
        return zip;
    }
    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getContact_id() {
        return contact_id;
    }
    public void setContact_id(int emp_id) {
        this.contact_id = emp_id;
    }

    @Override
    public boolean equals(Object ob) {
        Contact contact = (Contact) ob;
        if(contact.getFirstName().equalsIgnoreCase(this.firstName))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Contact ID: " + contact_id +
                ", First Name: " + firstName +
                ", Last Name: " + lastName +
                ", Phone number: " + phoneNumber +
                ", Email: " + email +
                ", Address: " + address +
                ", City: " + city +
                ", State: " + state +
                ", Zip: " + zip +
                ", Type: " + type;
    }

    public boolean equalsObject(Contact contact) {
        return this.contact_id == contact.contact_id &&
                this.firstName.equals(contact.firstName) &&
                this.lastName.equals(contact.lastName) &&
                this.phoneNumber.equals(contact.phoneNumber) &&
                this.email.equals(contact.email) &&
                this.address.equals(contact.address) &&
                this.city.equals(contact.city) &&
                this.state.equals(contact.state) &&
                this.zip == contact.zip &&
                this.type.equals(contact.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contact_id, firstName, lastName, phoneNumber, email, address,
                city, state, zip, type);
    }

    public int getAb_id() {
        return ab_id;
    }

    public void setAb_id(int ab_id) {
        this.ab_id = ab_id;
    }
}
