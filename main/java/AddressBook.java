import java.util.*;

public class AddressBook {
    private String addressBookName;
    private ArrayList<Contact> contactList;
    public AddressBook() {
        addressBookName = "";
        contactList = new ArrayList<Contact>();
    }
    public String getAddressBookName() {
        return addressBookName;
    }
    public void setAddressBookName(String addressBookName) {
        this.addressBookName = addressBookName;
    }
    public ArrayList<Contact> getContactList() {
        return contactList;
    }
    public void setContactList(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }
    public void sortContactListByFirstNameAsc() {
        Comparator<Contact> contactComparator = (contact1, contact2) -> contact1.getFirstName().compareTo(contact2.getFirstName());
        Collections.sort(contactList, contactComparator);
    }
    public void sortContactListByFirstNameDesc() {
        Comparator<Contact> contactComparator = (contact1, contact2) -> contact2.getFirstName().compareTo(contact1.getFirstName());
        Collections.sort(contactList, contactComparator);
    }
}