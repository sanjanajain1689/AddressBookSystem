import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


public class CrudOperations {
    public void readAll() {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "select ab.ab_name, c.contact_id, c.first_name, c.last_name, c.phone_no, c.email, a.address, a.city, a.state, a.zip, abt.abt_name " +
                    "from address_book ab " +
                    "right join contact c on ab.ab_id = c.ab_id " +
                    "inner join address a on c.contact_id = a.contact_id " +
                    "left join (select contact_id, abt_name from contact_type, address_book_type " +
                    "where contact_type.abt_id = address_book_type.abt_id) abt " +
                    "on c.contact_id = abt.contact_id";
            ResultSet resultSet = stmt.executeQuery(query);
            while(resultSet.next()) {
                AddressBook addressBook;
                Contact contact;
                if(!AddressBookMain.addressBookMap.containsKey(resultSet.getString("ab_name"))) {
                    addressBook = new AddressBook();
                    addressBook.setAddressBookName(resultSet.getString("ab_name"));
                    AddressBookMain.addressBookMap.put(addressBook.getAddressBookName(), addressBook);
                }
                else {
                    addressBook = AddressBookMain.addressBookMap.get(resultSet.getString("ab_name"));
                }
                int contact_id = resultSet.getInt("contact_id");
                if((contact = addressBookHasContactId(addressBook, contact_id)) != null) {
                    contact.type.add(resultSet.getString("abt_name"));
                }
                else {
                    contact = new Contact();
                    contact.setContact_id(contact_id);
                    contact.setFirstName(resultSet.getString("first_name"));
                    contact.setLastName(resultSet.getString("last_name"));
                    contact.setPhoneNumber(resultSet.getString("phone_no"));
                    contact.setEmail(resultSet.getString("email"));
                    contact.setAddress(resultSet.getString("address"));
                    contact.setCity(resultSet.getString("city"));
                    contact.setState(resultSet.getString("state"));
                    contact.setZip(resultSet.getInt("zip"));
                    contact.type.add(resultSet.getString("abt_name"));
                    addressBook.contactList.add(contact);
                }
            }
            displayAddressBooks();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private Contact addressBookHasContactId(AddressBook addressBook, int contact_id) {
        Iterator<Contact> itr = addressBook.getContactList().iterator();
        while(itr.hasNext()) {
            Contact listContact = itr.next();
            if(listContact.getContact_id() == contact_id) {
                return listContact;
            }
        }
        return null;
    }

    private void displayAddressBooks() {
        for(Map.Entry<String, AddressBook> entry : AddressBookMain.addressBookMap.entrySet()) {
            System.out.println("AddressBook Name: " + entry.getKey());
            AddressBook addressBook = entry.getValue();
            for(Contact contact : addressBook.getContactList()) {
                System.out.println(contact);
            }
        }
    }
}
