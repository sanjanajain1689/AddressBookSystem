import java.util.*;
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
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = in.nextInt();
            in.nextLine();
            switch(choice)
            {
                case 1:
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
                    addressBookMap.put(contact.firstName,contact);
                    break;
                }
                case 2:
                {
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
                    break;
                }
                case 3:
                {
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
            }
        }while(choice!=4);
        System.out.println("Program Terminated");
        in.close();
    }
}