import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
