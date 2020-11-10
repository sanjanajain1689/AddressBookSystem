import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookServiceTest {

    @Test
    public void test() {
        try {
            Connection con = JDBCConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            String query = "select count(contact_id) from contact";
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            int expectedOutput = resultSet.getInt("count(contact_id)");
            CrudOperations crudOperations = new CrudOperations();
            crudOperations.readAll();
            int output = AddressBookMain.addressBookMap.get("AB1").contactList.size();
            Assert.assertEquals(expectedOutput, output);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
