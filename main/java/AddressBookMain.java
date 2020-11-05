import java.util.*;
import java.util.stream.*;


public class AddressBookMain {
    public static Scanner in = new Scanner(System.in);
    public static HashMap<String, AddressBook> addressBookMap = new HashMap<String, AddressBook>();
    public static HashMap<String, ArrayList<String>> cityPersonMap = new HashMap<String, ArrayList<String>>();
    public static HashMap<String, ArrayList<String>> statePersonMap = new HashMap<String, ArrayList<String>>();

    public static void main(String args[]){
        menuDrivenEntry();
        System.out.println("-----Program Terminated-----");
        in.close();
    }

    private static void menuDrivenEntry() {
        int choice = 0;
        do
        {
            System.out.println("1. Add Address Book");
            System.out.println("2. Add Contact");
            System.out.println("3. Edit Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Search Persons By City or State");
            System.out.println("6. Get Total Persons Count By City or State");
            System.out.println("7. Sort Contacts in Address Book");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = in.nextInt();
            in.nextLine();
            switch(choice)
            {
                case 1:
                {
                    addAddressBook();
                    break;
                }
                case 2:
                {
                    addContactToAddressBook();
                    break;
                }
                case 3:
                {
                    editAddressBook();
                    break;
                }
                case 4:
                {
                    deleteAddressBook();
                    break;
                }
                case 5:
                {
                    viewPersonsByCityOrState();
                    break;
                }
                case 6:
                {
                    getCountOfPersonsByCityOrState();
                    break;
                }
                case 7:
                {
                    sortContacts();
                }
            }
        }while(choice!=8);
    }

    private static void addAddressBook() {
        System.out.print("Enter name for new address book: ");
        String addressBookName = in.nextLine();
        AddressBook addressBookObject = new AddressBook();
        addressBookMap.put(addressBookName, addressBookObject);
    }

    private static String getAddressBookNameForEntry() {
        System.out.print("Enter Address Book Name: ");
        return in.nextLine();
    }

    private static boolean isAddressBookExist(String addressBookName) {
        if(addressBookMap.containsKey(addressBookName))
            return true;
        System.out.println("Address book does not exist");
        return false;
    }

    private static Contact getContactDetails() {
        Contact contact = new Contact();
        System.out.print("Enter first name: ");
        contact.setFirstName(in.nextLine());
        System.out.print("Enter last name: ");
        contact.setLastName(in.nextLine());
        System.out.print("Enter address: ");
        contact.setAddress(in.nextLine());
        System.out.print("Enter city: ");
        contact.setCity(in.nextLine());
        System.out.print("Enter state: ");
        contact.setState(in.nextLine());
        System.out.print("Enter phoneNumber: ");
        contact.setPhoneNumber(in.nextLine());
        System.out.print("Enter email: ");
        contact.setEmail(in.nextLine());
        System.out.print("Enter zip: ");
        contact.setZip(in.nextLong());
        in.nextLine();
        return contact;
    }

    private static void addContactToAddressBook() {
        String addressBookName = getAddressBookNameForEntry();
        if(isAddressBookExist(addressBookName) == false)
            return;
        AddressBook addressBookObject = addressBookMap.get(addressBookName);
        ArrayList<Contact> contactList = addressBookObject.getContactList();
        Contact contact = getContactDetails();
        if(addressBookHasContact(contactList, contact) == true)
            return;
        contactList.add(contact);
        addressBookObject.setContactList(contactList);
        addressBookMap.put(addressBookName, addressBookObject);
        insertTimeUpdateMaps(contact.getCity(), contact.getState(), contact.getFirstName());
    }

    private static void editAddressBook() {
        String addressBookName = getAddressBookNameForEntry();
        if(isAddressBookExist(addressBookName) == false)
            return;
        AddressBook addressBookObject = addressBookMap.get(addressBookName);
        ArrayList<Contact> contactList = addressBookObject.getContactList();
        System.out.print("Enter the contact first name to edit: ");
        String contactFirstName = in.nextLine();
        for(Contact contactInList : contactList)
        {
            if(contactInList.getFirstName().equals(contactFirstName))
            {
                Contact contact = getContactDetails();
                contactList.set(contactList.indexOf(contactInList), contact);
                addressBookObject.setContactList(contactList);
                addressBookMap.put(addressBookName, addressBookObject);
                editTimeUpdateMaps(contact.getCity(), contact.getState(), contactFirstName, contact.getFirstName());
                System.out.println("Contact modified");
                return;
            }
        }
        System.out.println("Contact not found");
    }

    private static void deleteAddressBook() {
        String addressBookName = getAddressBookNameForEntry();
        if(isAddressBookExist(addressBookName) == false)
            return;
        AddressBook addressBookObject = addressBookMap.get(addressBookName);
        ArrayList<Contact> contactList = addressBookObject.getContactList();
        System.out.print("Enter the contact first name to delete: ");
        String contactFirstName = in.nextLine();
        try{
            Contact contact = contactList.stream().filter(e -> e.getFirstName().equals(contactFirstName)).findFirst().get();
            contactList.remove(contact);
            addressBookObject.setContactList(contactList);
            addressBookMap.put(addressBookName, addressBookObject);
            deleteTimeUpdateMaps(contact.getCity(), contact.getState(), contactFirstName);
            System.out.println("Contact deleted successfully");
        } catch(NoSuchElementException e) {
            System.out.println("Contact not found");
        }
    }
    private static boolean addressBookHasContact(ArrayList<Contact> contactList, Contact contact) {
        try {
            if(contact.equals(contactList.stream().filter(e -> e.equals(contact)).findFirst().get()))
            {
                System.out.println("Contact matching " + contact.getFirstName() + " already exists");
                return true;
            }
        } catch(NoSuchElementException e) {
            return false;
        }
        return false;
    }

    private static void viewPersonsByCityOrState() {
        System.out.println("1. City \n"
                + "2. State");
        System.out.print("Enter your choice: ");
        int choice = in.nextInt();
        in.nextLine();
        switch(choice)
        {
            case 1:
            {
                System.out.print("Enter city: ");
                String city = in.nextLine();
                cityPersonMap.entrySet().stream().filter(e -> e.getKey().equals(city)).forEach(e -> System.out.println(e.getValue()));
                break;
            }
            case 2:
            {
                System.out.print("Enter state: ");
                String state = in.nextLine();
                statePersonMap.entrySet().stream().filter(e -> e.getKey().equals(state)).forEach(e -> System.out.println(e.getValue()));
            }
        }
    }

    private static void updateMaps(String locality, String contactFirstName, HashMap<String, ArrayList<String>> map, int actionChoice) {

        switch(actionChoice)
        {
            //Add
            case 1:
            {
                ArrayList<String> firstNameList;
                if(map.containsKey(locality))
                    firstNameList = map.get(locality);
                else
                    firstNameList = new ArrayList<String>();
                firstNameList.add(contactFirstName);
                map.put(locality, firstNameList);
                break;
            }
            //Remove
            case 2:
            {
                ArrayList<String> firstNameList = map.get(locality);
                firstNameList.remove(contactFirstName);
                if(firstNameList.isEmpty())
                    map.remove(locality);
                else
                    map.put(locality, firstNameList);
            }
        }
    }

    private static void insertTimeUpdateMaps(String city, String state, String firstName) {
        updateMaps(city, firstName, cityPersonMap, 1); //add new firstName to cityPersonMap
        updateMaps(state, firstName, statePersonMap, 1); //add new firstName to statePersonMap
    }

    private static void editTimeUpdateMaps(String city, String state, String olderFirstName, String currentFirstName) {
        updateMaps(city, olderFirstName, cityPersonMap, 2); //remove old firstName from cityPersonMap
        updateMaps(city, currentFirstName, cityPersonMap, 1); //add new firstName to cityPersonMap
        updateMaps(state, olderFirstName, statePersonMap, 2); //remove old firstName from statePersonMap
        updateMaps(state, currentFirstName, statePersonMap, 1); //add new firstName to statePersonMap
    }

    private static void deleteTimeUpdateMaps(String city, String state, String firstName) {
        updateMaps(city, firstName, cityPersonMap, 2); //delete firstName from cityPersonMap
        updateMaps(state, firstName, statePersonMap, 2); //delete firstName from statePersonMap
    }

    
    private static void getCountOfPersonsByCityOrState() {
        System.out.println("1. City \n"
                + "2. State");
        System.out.print("Enter your choice (1/2): ");
        int choice = in.nextInt();
        in.nextLine();
        switch(choice)
        {
            case 1:
            {
                System.out.print("Enter city name: ");
                String cityName = in.nextLine();
                try {
                    System.out.println("Total count: " +
                            cityPersonMap.entrySet().stream().filter(e -> e.getKey().equals(cityName)).findFirst().get().getValue().size());
                } catch (NoSuchElementException e) {
                    System.out.println("Total count: 0");
                }
                break;
            }
            case 2:
            {
                System.out.print("Enter state name: ");
                String stateName = in.nextLine();
                try {
                    System.out.println("Total count: " +
                            statePersonMap.entrySet().stream().filter(e -> e.getKey().equals(stateName)).findFirst().get().getValue().size());
                } catch (NoSuchElementException e) {
                    System.out.println("Total count: 0");
                }
                break;
            }
        }
    }

    private static void sortContacts() {
        String addressBookName = getAddressBookNameForEntry();
        if(isAddressBookExist(addressBookName) == false)
            return;
        AddressBook addressBookObject = addressBookMap.get(addressBookName);
        System.out.println("Sort contacts using first name by \n"
                + "1.Ascending \n"
                + "2.Descending");
        System.out.print("Enter your choice (1/2): ");
        int choice = in.nextInt();
        in.nextLine();
        switch(choice)
        {
            case 1:
                addressBookObject.sortContactListByFirstNameAsc();
                break;
            case 2:
                addressBookObject.sortContactListByFirstNameDesc();
                break;
            default:
                System.out.println("Invalid Input");
        }
        System.out.println("Contact list after sorting: ");
        addressBookObject.getContactList().stream().forEach(e -> System.out.println(e.toString()));
    }
}
