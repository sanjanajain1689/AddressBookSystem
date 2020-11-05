import java.util.*;

public class AddressBook implements java.io.Serializable{
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
    public void sortContactListByCityAsc() {
        Comparator<Contact> contactComparator = (contact1, contact2) -> contact1.getCity().compareTo(contact2.getCity());
        Collections.sort(contactList, contactComparator);
    }
    public void sortContactListByCityDesc() {
        Comparator<Contact> contactComparator = (contact1, contact2) -> contact2.getCity().compareTo(contact1.getCity());
        Collections.sort(contactList, contactComparator);
    }
    public void sortContactListByStateAsc() {
        Comparator<Contact> contactComparator = (contact1, contact2) -> contact1.getState().compareTo(contact2.getState());
        Collections.sort(contactList, contactComparator);
    }
    public void sortContactListByStateDesc() {
        Comparator<Contact> contactComparator = (contact1, contact2) -> contact2.getState().compareTo(contact1.getState());
        Collections.sort(contactList, contactComparator);
    }
    public void sortContactListByZipAsc() {
        Comparator<Contact> contactComparator = (contact1, contact2) -> ((Long)contact1.getZip()).compareTo((Long)contact2.getZip());
        Collections.sort(contactList, contactComparator);
    }
    public void sortContactListByZipDesc() {
        Comparator<Contact> contactComparator = (contact1, contact2) -> ((Long)contact2.getZip()).compareTo((Long)contact1.getZip());
        Collections.sort(contactList, contactComparator);
    }
}