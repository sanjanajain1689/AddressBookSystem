import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CrudOperations {

    public synchronized boolean createRecord(int ab_id, String first_name, String last_name, String phone_no, String email, String address, String city, String state, int zip, ArrayList<String> type) throws InvalidSqlOperationException {
        Connection con = JDBCConnection.getInstance().getConnection();
        try {
            con.setAutoCommit(false);

            System.out.println("Inserting contact " + first_name + " into DB...");

            String query = "insert into contact (ab_id, first_name, last_name, phone_no, email, date_added) value " +
                    "(?,?,?,?,?, date(now()) )";
            PreparedStatement preparedStatementContact = con.prepareStatement(query);
            preparedStatementContact.setInt(1, ab_id);
            preparedStatementContact.setString(2, first_name);
            preparedStatementContact.setString(3, last_name);
            preparedStatementContact.setString(4, phone_no);
            preparedStatementContact.setString(5, email);
            int countContactRecordsChanged = preparedStatementContact.executeUpdate();

            query = "insert into address (contact_id, address, city, state, zip) values" +
                    "(?,?,?,?,?)";
            PreparedStatement preparedStatementAddress = con.prepareStatement(query);
            preparedStatementAddress.setInt(1, getContactIdByName(first_name));
            preparedStatementAddress.setString(2, address);
            preparedStatementAddress.setString(3, city);
            preparedStatementAddress.setString(4, state);
            preparedStatementAddress.setInt(5, zip);
            int countAddressRecordsChanged = preparedStatementAddress.executeUpdate();

            if(countAddressRecordsChanged>0 && countContactRecordsChanged>0) {
                con.commit();
                System.out.println(first_name + " inserted");
                return true;
            }
            else
                throw new InvalidSqlOperationException("Contact cannot be inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createMultipleRecords(ArrayList<Contact> contactList) {
        HashMap<Integer, Boolean> contactInsertionStatus = new HashMap<>();
        for(Contact contact : contactList) {
            Runnable task = () -> {
                contactInsertionStatus.put(contact.hashCode(), false);
                CrudOperations crudOperations = new CrudOperations();
                try {
                    crudOperations.createRecord(contact.getAb_id(), contact.getFirstName(),
                            contact.getLastName(), contact.getPhoneNumber(), contact.getEmail(),
                            contact.getAddress(), contact.getCity(), contact.getState(),
                            contact.getZip(), contact.type);
                } catch (InvalidSqlOperationException e) {
                    e.printStackTrace();
                }
                contactInsertionStatus.put(contact.hashCode(), true);
            };
            Thread thread = new Thread(task);
            thread.start();
        }
        while(contactInsertionStatus.containsValue(false)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

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

    public Contact readByName(String firstName) throws RecordNotFoundInDB {
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
            if(!resultSet.isBeforeFirst())
                throw new RecordNotFoundInDB("Record for First Name " + firstName + " not found in DB");
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

    public int readCountByCity(String city) {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "select count(contact_id) from address where city = '"+city+"' ";
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            return resultSet.getInt("count(contact_id)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int readCountByState(String state) {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "select count(contact_id) from address where state = '"+state+"' ";
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            return resultSet.getInt("count(contact_id)");
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

    public int getContactIdByName(String first_name) {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "select contact_id from contact where first_name = '" + first_name + "'";
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            return resultSet.getInt("contact_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNoOfContactsInDB() {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "select count(contact_id) from contact";
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            return resultSet.getInt("count(contact_id)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}