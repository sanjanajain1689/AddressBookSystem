import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookServiceTest {

    @Test
    public void givenDatabase_WhenRetrieved_ShouldreturnCorrectCount() {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "select count(contact_id) from contact";
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            int expectedOutput = resultSet.getInt("count(contact_id)");
            CrudOperations crudOperations = new CrudOperations();
            crudOperations.readAll();
            crudOperations.displayAddressBooks();
            int output = AddressBookMain.addressBookMap.get("AB1").contactList.size();
            Assert.assertEquals(expectedOutput, output);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenContactName_WhenPhoneUpdated_ShouldSync() {
        CrudOperations crudOperations = new CrudOperations();
        crudOperations.updateContact("Kumar", "1111111111");
        Contact dbContact = crudOperations.readByName("Kumar");
        Contact mapContact = null;
        for(Contact contact : AddressBookMain.addressBookMap.get("AB1").contactList) {
            if(contact.getFirstName().equals("Kumar"))
                mapContact = contact;
        }
        Assert.assertTrue(dbContact.equalsObject(mapContact));
        crudOperations.updateContact("Kumar", "2222222222");
        dbContact = crudOperations.readByName("Kumar");
        mapContact = null;
        for(Contact contact : AddressBookMain.addressBookMap.get("AB1").contactList) {
            if(contact.getFirstName().equals("Kumar"))
                mapContact = contact;
        }
        Assert.assertTrue(dbContact.equalsObject(mapContact));
    }

    @Test
    public void givenTwoDates_WhenRetrievedContactsAddedBetweenDatesFromDB_ShouldReturnCorrectCount() {
        try {
            CrudOperations crudOperations = new CrudOperations();
            int initialCount = crudOperations.readByDateAdded("2018-01-01", "2020-01-01");
            System.out.println(initialCount);
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "insert into contact (ab_id, first_name, last_name, " +
                    "phone_no, email, date_added) values " +
                    "(1, 'Dummy_fname', 'Dummy_lname', 'Dummy_phno', " +
                    "'Dummy_email', '2018-09-09')";
            stmt.executeUpdate(query);
            int count = crudOperations.readByDateAdded("2018-01-01", "2020-01-01");
            Assert.assertEquals(initialCount+1, count);

            query = "delete from contact where first_name = 'Dummy_fname'";
            stmt.executeUpdate(query);
            count = crudOperations.readByDateAdded("2018-01-01", "2020-01-01");
            Assert.assertEquals(initialCount, count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenCity_WhenRetrievedContactByCityFromDB_ShouldReturnCorrectCount() {
        CrudOperations crudOperations = new CrudOperations();
        int countForCity = crudOperations.readCountByCity("Salem");
        Assert.assertEquals(2, countForCity);
    }

    @Test
    public void givenState_WhenRetrievedContactByStateFromDB_ShouldReturnCorrectCount() {
        CrudOperations crudOperations = new CrudOperations();
        int countForState = crudOperations.readCountByState("Maharashtra");
        Assert.assertEquals(2, countForState);
    }

    @Test
    public void givenARecord_WhenSuccessfullyInsertedToDB_ShouldReturnTrue() {
        CrudOperations crudOperations = new CrudOperations();
        boolean output = crudOperations.createRecord(1, "Raghav", "Joshi", "4234234111", "raghav@gmail.com", "W Street", "Surat", "Gujrat", 234123, new ArrayList<String>(Arrays.asList("Friends", "Family")));
        Assert.assertTrue(output);
    }
}