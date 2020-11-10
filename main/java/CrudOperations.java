import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Contact readByName(String firstName) {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            String query = "select ab.ab_name, c.contact_id, c.first_name, c.last_name, c.phone_no, c.email, a.address, a.city, a.state, a.zip, abt.abt_name " +
                    "from address_book ab " +
                    "right join contact c on ab.ab_id = c.ab_id " +
                    "inner join address a on c.contact_id = a.contact_id " +
                    "left join (select contact_id, abt_name from contact_type, address_book_type " +
                    "where contact_type.abt_id = address_book_type.abt_id) abt " +
                    "on c.contact_id = abt.contact_id " +
                    "where c.first_name = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            ResultSet resultSet = preparedStatement.executeQuery();
            Contact contact = null;
            while(resultSet.next()) {
                if(contact == null) {
                    contact = new Contact();
                    contact.setContact_id(resultSet.getInt("contact_id"));
                    contact.setFirstName(resultSet.getString("first_name"));
                    contact.setLastName(resultSet.getString("last_name"));
                    contact.setPhoneNumber(resultSet.getString("phone_no"));
                    contact.setEmail(resultSet.getString("email"));
                    contact.setAddress(resultSet.getString("address"));
                    contact.setCity(resultSet.getString("city"));
                    contact.setState(resultSet.getString("state"));
                    contact.setZip(resultSet.getInt("zip"));
                    contact.type.add(resultSet.getString("abt_name"));
                }
                else
                    contact.type.add(resultSet.getString("abt_name"));
            }
            return contact;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int readByDateAdded(String date1, String date2) {
        System.out.println("Displaying contact added between " + date1 + " and " + date2);
        Connection con = JDBCConnection.getInstance().getConnection();
        try {
            Statement stmt = con.createStatement();
            String query = "select ab.ab_name, c.contact_id, c.first_name, c.last_name, c.phone_no, c.email "+
                    "from address_book ab " +
                    "right join contact c on ab.ab_id = c.ab_id " +
                    "where c.date_added between cast('" + date1 +"' as date) " +
                    "and cast('" + date2 +"' as date)";
            ResultSet resultSet = stmt.executeQuery(query);
            return displayResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateContact(String firstName, String phone) {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "update contact set phone_no = '" + phone + "' where first_name = '" + firstName + "'";
            System.out.println("Updating phone number for " + firstName +"... ");
            stmt.executeUpdate(query);
            System.out.println("Phone number updated");
            sync();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sync() {
        AddressBookMain.addressBookMap.clear();
        readAll();
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

    public void displayAddressBooks() {
        for(Map.Entry<String, AddressBook> entry : AddressBookMain.addressBookMap.entrySet()) {
            System.out.println("AddressBook Name: " + entry.getKey());
            AddressBook addressBook = entry.getValue();
            for(Contact contact : addressBook.getContactList()) {
                System.out.println(contact);
            }
        }
    }

    private int displayResultSet(ResultSet resultSet) {
        int count = 0;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnsNumber = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(resultSetMetaData.getColumnName(i) + " : " + columnValue);
                }
                System.out.println();
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}