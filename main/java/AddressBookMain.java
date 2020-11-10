import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


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
            AddressBookMain addressBookMain = new AddressBookMain();
            System.out.println("1. Add Address Book");
            System.out.println("2. Add Contact");
            System.out.println("3. Edit Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Search Persons By City or State");
            System.out.println("6. Get Total Persons Count By City or State");
            System.out.println("7. Sort Contacts in Address Book");
            System.out.println("8. Write Address Book To File");
            System.out.println("9. Read Address Book From File");
            System.out.println("10. Write Address Book To CSV");
            System.out.println("11. Read Address Book From CSV");
            System.out.println("12. Write Address Book To JSON");
            System.out.println("13. Read Address Book From JSON");
            System.out.println("14. Exit");
            System.out.print("Enter your choice: ");
            choice = in.nextInt();
            in.nextLine();
            switch(choice)
            {
                case 1:
                {
                    addressBookMain.addAddressBook();
                    break;
                }
                case 2:
                {
                    addressBookMain.addContactToAddressBook();
                    break;
                }
                case 3:
                {
                    addressBookMain.editAddressBook();
                    break;
                }
                case 4:
                {
                    addressBookMain.deleteAddressBook();
                    break;
                }
                case 5:
                {
                    addressBookMain.viewPersonsByCityOrState();
                    break;
                }
                case 6:
                {
                    addressBookMain.getCountOfPersonsByCityOrState();
                    break;
                }
                case 7:
                {
                    addressBookMain.sortContacts();
                    break;
                }
                case 8:
                {
                    addressBookMain.writeAddressBookToFile();
                    break;
                }
                case 9:
                {
                    addressBookMain.readAddressBookFromFile();
                    break;
                }
                case 10:
                {
                    addressBookMain.writeAddressBookToCSV();
                    break;
                }
                case 11:
                {
                    addressBookMain.readAddressBookFromCSV();
                    break;
                }
                case 12:
                {
                    addressBookMain.writeAddressBookToJSON();
                    break;
                }
                case 13:
                {
                    addressBookMain.readAddressBookFromJSON();
                }
            }
        }while(choice!=14);
    }

    private void addAddressBook() {
        System.out.print("Enter name for new address book: ");
        String addressBookName = in.nextLine();
        AddressBook addressBookObject = new AddressBook();
        addressBookObject.setAddressBookName(addressBookName);
        addressBookMap.put(addressBookName, addressBookObject);
    }

    private String getAddressBookNameForEntry() {
        System.out.print("Enter Address Book Name: ");
        return in.nextLine();
    }

    private boolean isAddressBookExist(String addressBookName) {
        if(addressBookMap.containsKey(addressBookName))
            return true;
        System.out.println("Address book does not exist");
        return false;
    }

    private Contact getContactDetails() {
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
        contact.setZip(in.nextInt());
        in.nextLine();
        return contact;
    }


    private void addContactToAddressBook() {
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

    private void editAddressBook() {
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


    private void deleteAddressBook() {
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


    private boolean addressBookHasContact(ArrayList<Contact> contactList, Contact contact) {
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


    private void viewPersonsByCityOrState() {
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


    private void updateMaps(String locality, String contactFirstName, HashMap<String, ArrayList<String>> map, int actionChoice) {

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

    private void insertTimeUpdateMaps(String city, String state, String firstName) {
        updateMaps(city, firstName, cityPersonMap, 1); //add new firstName to cityPersonMap
        updateMaps(state, firstName, statePersonMap, 1); //add new firstName to statePersonMap
    }

    private void editTimeUpdateMaps(String city, String state, String olderFirstName, String currentFirstName) {
        updateMaps(city, olderFirstName, cityPersonMap, 2); //remove old firstName from cityPersonMap
        updateMaps(city, currentFirstName, cityPersonMap, 1); //add new firstName to cityPersonMap
        updateMaps(state, olderFirstName, statePersonMap, 2); //remove old firstName from statePersonMap
        updateMaps(state, currentFirstName, statePersonMap, 1); //add new firstName to statePersonMap
    }

    private void deleteTimeUpdateMaps(String city, String state, String firstName) {
        updateMaps(city, firstName, cityPersonMap, 2); //delete firstName from cityPersonMap
        updateMaps(state, firstName, statePersonMap, 2); //delete firstName from statePersonMap
    }


    private void getCountOfPersonsByCityOrState() {
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

    private void sortContacts() {
        String addressBookName = getAddressBookNameForEntry();
        if(isAddressBookExist(addressBookName) == false)
            return;
        AddressBook addressBookObject = addressBookMap.get(addressBookName);
        System.out.println("Sort contacts in \n"
                + "1. Ascending \n"
                + "2. Descending");
        System.out.print("Enter your choice (1/2): ");
        int choice = in.nextInt();
        in.nextLine();
        System.out.println("Sort by \n"
                + "1. First name \n"
                + "2. City \n"
                + "3. State \n"
                + "4. Zip");
        System.out.print("Enter your choice (1/2/3/4): ");
        int orderByChoice = in.nextInt();
        in.nextLine();
        switch(choice)
        {
            case 1:
            {
                switch(orderByChoice)
                {
                    case 1:
                        addressBookObject.sortContactListByFirstNameAsc();
                        break;
                    case 2:
                        addressBookObject.sortContactListByCityAsc();
                        break;
                    case 3:
                        addressBookObject.sortContactListByStateAsc();
                        break;
                    case 4:
                        addressBookObject.sortContactListByZipAsc();
                }
            }
            break;
            case 2:
            {
                switch(orderByChoice)
                {
                    case 1:
                        addressBookObject.sortContactListByFirstNameDesc();
                        break;
                    case 2:
                        addressBookObject.sortContactListByCityDesc();
                        break;
                    case 3:
                        addressBookObject.sortContactListByStateDesc();
                        break;
                    case 4:
                        addressBookObject.sortContactListByZipDesc();
                }
            }
            break;
            default:
                System.out.println("Invalid Input");
        }
        System.out.println("Contact list after sorting: ");
        addressBookObject.getContactList().stream().forEach(e -> System.out.println(e.toString()));
    }

    /**
     * Adds an existing address book to the file
     * File holds one address book at a time
     */
    private void writeAddressBookToFile() {
        String addressBookName = getAddressBookNameForEntry();
        if(isAddressBookExist(addressBookName) == false)
            return;
        AddressBook addressBook = addressBookMap.get(addressBookName);
        String filename = "C:\\Users\\hp\\IdeaProjects\\AddressBookProblem\\src\\AddressBookFile.ser";
        try
        {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(addressBook);
            out.close();
            file.close();
            System.out.println("The address book has been written to file");
        } catch(IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    private void readAddressBookFromFile() {
        String addressBookName = getAddressBookNameForEntry();
        String filename ="C:\\Users\\hp\\IdeaProjects\\AddressBookProblem\\src\\AddressBookFile.ser";
        try
        {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            AddressBook addressBook = (AddressBook)in.readObject();
            if(!addressBookName.equals(addressBook.getAddressBookName()))
                System.out.println("Address book '" + addressBookName + "' not found in file");
            else
                System.out.println("The address book has been read from file");
            in.close();
            file.close();
        } catch(IOException ex) {
            System.out.println("IOException is caught");
        } catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
    }

    private void writeAddressBookToCSV() {
        String addressBookName = getAddressBookNameForEntry();
        if(isAddressBookExist(addressBookName) == false)
            return;
        AddressBook addressBook = addressBookMap.get(addressBookName);
        String filename = "C:\\Users\\hp\\IdeaProjects\\AddressBookProblem\\src\\AddressBookCSV.csv";
        File outputFile = new File(filename);
        try (FileWriter outputFileWriter = new FileWriter(outputFile);
             CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter)){
            String[] header = { "Address Book Name", "First Name", "Last Name", "Address", "City", "State", "PhoneNumber", "Email", "Zip" };
            outputCSVWriter.writeNext(header);
            for(Contact contact : addressBook.getContactList()) {
                String[] rowData = { addressBookName, contact.getFirstName(), contact.getLastName(),
                        contact.getAddress(), contact.getCity(), contact.getState(),
                        contact.getPhoneNumber(), contact.getEmail(), Long.toString(contact.getZip()) };
                outputCSVWriter.writeNext(rowData);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Address Book added to CSV");
    }

    private void readAddressBookFromCSV() {
        String filename = "C:\\Users\\hp\\IdeaProjects\\AddressBookProblem\\src\\AddressBookCSV.csv";
        File inputFile = new File(filename);
        try (FileReader inputFileReader = new FileReader(inputFile);
             CSVReader inputCSVReader = new CSVReader(inputFileReader)){
            String[] rowData = null;
            String[] header = inputCSVReader.readNext();
            while((rowData = inputCSVReader.readNext()) != null) {
                for(int i=0; i<rowData.length; i++) {
                    System.out.print(header[i] + ": " + rowData[i] + " | ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Address Book read from CSV");
    }

    private void writeAddressBookToJSON() {
        String addressBookName = getAddressBookNameForEntry();
        if(isAddressBookExist(addressBookName) == false)
            return;
        AddressBook addressBook = addressBookMap.get(addressBookName);
        String filename = "C:\\Users\\hp\\IdeaProjects\\AddressBookProblem\\src\\AddressBookJSON.json";
        File outputFile = new File(filename);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter outputFileWriter = new FileWriter(outputFile)) {
            gson.toJson(addressBook, outputFileWriter);
        } catch (Exception e) {
            e.getMessage();
        }
        System.out.println("Address Book added to JSON");
    }

    private void readAddressBookFromJSON() {
        String filename = "C:\\Users\\hp\\IdeaProjects\\AddressBookProblem\\src\\AddressBookJSON.json";
        File inputFile = new File(filename);
        Gson gson = new Gson();
        try (FileReader inputFileReader = new FileReader(inputFile)) {
            AddressBook addressBook = gson.fromJson(inputFileReader, AddressBook.class);
            System.out.println("Address Book '" + addressBook.getAddressBookName() +
                    "' read from JSON");
        } catch (Exception e) {
            e.getMessage();
        }
    }
}