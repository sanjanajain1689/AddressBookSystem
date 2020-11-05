import java.util.*;
import java.util.stream.*;

public class AddressBookMain {
    public static HashMap<String,AddressBook> addressBookMap = new HashMap<String,AddressBook>();
    public static void main(String args[]){
        Scanner in = new Scanner(System.in);
        int choice = 0;
        do
        {
            System.out.println("1. Add Contact");
            System.out.println("2. Edit Contact");
            System.out.println("3. Delete Contact");
            System.out.println("4. Search Persons By City or State");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = in.nextInt();
            in.nextLine();
            switch(choice)
            {
                case 1:
                {
                    addAddressBook(in);
                    break;
                }
                case 2:
                {
                    editAddressBook(in);
                    break;
                }
                case 3:
                {
                    deleteAddressBook(in);
                    break;
                }
                case 4:
                {
                    viewPersonsByCityOrState(in);
                }
            }
        }while(choice!=5);
        System.out.println("Program Terminated");
        in.close();
    }

    /**
     * @param in
     * Adds a contact to the dictionary
     */
    private static void addAddressBook(Scanner in) {
        AddressBook contact = new AddressBook();
        do {
            System.out.print("Enter first name: ");
            contact.firstName = in.nextLine();
        } while(mapHasDuplicates(contact.firstName) == true);
        System.out.print("Enter last name: ");
        contact.lastName = in.nextLine();
        System.out.print("Enter address: ");
        contact.address = in.nextLine();
        System.out.print("Enter city: ");
        contact.city = in.nextLine();
        System.out.print("Enter state: ");
        contact.state = in.nextLine();
        System.out.print("Enter phoneNumber: ");
        contact.phoneNumber = in.nextLine();
        System.out.print("Enter email: ");
        contact.email = in.nextLine();
        System.out.print("Enter zip: ");
        contact.zip = in.nextLong();
        in.nextLine();
        addressBookMap.put(contact.firstName,contact);
    }

    /**
     * @param in
     * Edit a contact in dictionary
     */
    private static void editAddressBook(Scanner in) {
        System.out.print("Enter the contact first name to edit: ");
        String searchName = in.nextLine();
        if(addressBookMap.containsKey(searchName))
        {
            AddressBook contact = new AddressBook();
            System.out.print("Enter first name: ");
            contact.firstName = in.nextLine();
            System.out.print("Enter last name: ");
            contact.lastName = in.nextLine();
            System.out.print("Enter address: ");
            contact.address = in.nextLine();
            System.out.print("Enter city: ");
            contact.city = in.nextLine();
            System.out.print("Enter state: ");
            contact.state = in.nextLine();
            System.out.print("Enter phoneNumber: ");
            contact.phoneNumber = in.nextLine();
            System.out.print("Enter email: ");
            contact.email = in.nextLine();
            System.out.print("Enter zip: ");
            contact.zip = in.nextLong();
            in.nextLine();
            addressBookMap.remove(searchName);
            addressBookMap.put(contact.firstName,contact);
            System.out.println("Contact modified");
        }
        else
            System.out.println("Contact not found");
    }

    /**
     * @param in
     * Delete a contact in dictionary
     */
    private static void deleteAddressBook(Scanner in) {
        System.out.print("Enter the contact first name to delete: ");
        String searchName = in.nextLine();
        if(addressBookMap.containsKey(searchName))
        {
            addressBookMap.remove(searchName);
            System.out.println("Contact deleted successfully");
        }
        else
            System.out.println("Contact not found");
    }

    private static boolean mapHasDuplicates(String firstName) {
        try {
            if(firstName == addressBookMap.entrySet().stream().filter(e -> e.getKey().equals(firstName)).findFirst().get().getKey());
            {
                System.out.println("Contact matching " + firstName + " already exists");
                return true;
            }
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    private static void viewPersonsByCityOrState(Scanner in) {
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
                addressBookMap.entrySet().stream().filter(e -> e.getValue().getCity().equals(city)).forEach(e -> System.out.println(e.getKey()));
                break;
            }
            case 2:
            {
                System.out.print("Enter state: ");
                String state = in.nextLine();
                addressBookMap.entrySet().stream().filter(e -> e.getValue().getState().equals(state)).forEach(e -> System.out.println(e.getKey()));
            }
        }
    }
}