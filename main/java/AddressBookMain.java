import java.util.*;
public class AddressBookMain {
    public static ArrayList<AddressBook> addressBookList = new ArrayList<AddressBook>();
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
                    addressBookList.add(contact);
                    break;
                }
                case 2:
                {
                    System.out.print("Enter the contact first name to edit: ");
                    String searchName = in.nextLine();
                    for(int i=0;i<addressBookList.size();i++)
                    {
                        if(searchName.equals(addressBookList.get(i).firstName))
                        {
                            System.out.print("Enter first name: ");
                            addressBookList.get(i).firstName = in.nextLine();
                            System.out.print("Enter last name: ");
                            addressBookList.get(i).lastName = in.nextLine();
                            System.out.print("Enter address: ");
                            addressBookList.get(i).address = in.nextLine();
                            System.out.print("Enter city: ");
                            addressBookList.get(i).city = in.nextLine();
                            System.out.print("Enter state: ");
                            addressBookList.get(i).state = in.nextLine();
                            System.out.print("Enter phoneNumber: ");
                            addressBookList.get(i).phoneNumber = in.nextLine();
                            System.out.print("Enter email: ");
                            addressBookList.get(i).email = in.nextLine();
                            System.out.print("Enter zip: ");
                            addressBookList.get(i).zip = in.nextLong();
                            in.nextLine();
                            break;
                        }
                    }
                    break;
                }
                case 3:
                {
                    System.out.print("Enter the contact first name to delete: ");
                    String searchName = in.nextLine();
                    for(int i=0;i<addressBookList.size();i++)
                    {
                        if(searchName.equals(addressBookList.get(i).firstName))
                        {
                            addressBookList.remove(i);
                            System.out.println("Contact deleted successfully");
                        }
                    }
                }
            }
        }while(choice!=4);
        System.out.println("Program Terminated");
        in.close();
    }
}