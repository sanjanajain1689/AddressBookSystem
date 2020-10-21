import java.util.*;
public class AddressBookMain {
    public static void main(String args[]){
        System.out.println("Welcome to Address Book Program");
        Scanner in = new Scanner(System.in);
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
        System.out.print("Enter the contact first name to edit: ");
        String searchName = in.nextLine();
        if(searchName.equals(contact.firstName))
        {
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
        }
        in.nextLine();
        System.out.print("Enter the contact first name to edit: ");
        if(searchName.equals(contact.firstName))
        {
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
        }
        in.close();
    }
}