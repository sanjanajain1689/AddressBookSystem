import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AddressBookServiceTest {
    private CrudOperations crudOperations;
    private AddressBookMain addressBookMain;
    private Connection con;
    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4001;
        crudOperations = new CrudOperations();
        addressBookMain = new AddressBookMain();
        con = JDBCConnection.getInstance().getConnection();
    }

    @Test
    public void givenContactDetails_WhenAddedToAddressBook_ShouldReturnTrue() {
        boolean result = addressBookMain.isContactAdditionSuccess("AB1", 1, 1, "Kanishk", "Mathur", "2222221232");
        Assert.assertTrue(result);
    }

    @Test
    public void givenContactFirstName_WhenUpdatedInAddressBook_ShouldReturnTrue() {
        addressBookMain.isContactAdditionSuccess("AB1", 1, 1, "Kanishk", "Mathur", "2222221232");
        boolean result = addressBookMain.isContactUpdationSuccess("Kanishk", "AB1", 1, 1, "Kanishk", "Mathur", "1111111111");
        Assert.assertTrue(result);
    }

    @Test
    public void givenContactFirstName_WhenDeletedInAddressBook_ShouldReturnTrue() {
        addressBookMain.isContactAdditionSuccess("AB1", 1, 1, "Kanishk", "Mathur", "2222221232");
        boolean result = addressBookMain.isContactDeletionSuccess("AB1", "Kanishk");
        Assert.assertTrue(result);
    }

    @Test
    public void givenContact_WhenExistsInAddressBook_ShouldReturnTrue() {
        addressBookMain.isContactAdditionSuccess("AB1", 1, 1, "Kanishk", "Mathur", "2222221232");
        boolean result = addressBookMain.hasContact("AB1", 1, 1, "Kanishk", "Mathur", "2222221232");
        Assert.assertTrue(result);
    }

    @Test
    public void givenContact_WhenWrittenToFile_ShouldReturnTrue() {
        boolean result = addressBookMain.isContactWritingToFileSuccess("AB1", 1, 1, "Kanishk", "Mathur", "2222221232");
        Assert.assertTrue(result);
    }

    @Test
    public void givenFile_WhenReadSuccessfully_ShouldReturnTrue() {
        boolean result = addressBookMain.isContactReadingFromFileSuccess("AB1");
        Assert.assertTrue(result);
    }

    @Test
    public void givenContact_WhenWrittenToCSVFile_ShouldReturnTrue() {
        boolean result = addressBookMain.isContactWritingToCSVFileSuccess("AB1", 1, 1, "Kanishk", "Mathur", "2222221232");
        Assert.assertTrue(result);
    }

    @Test
    public void givenCSVFile_WhenReadSuccessfully_ShouldReturnTrue() {
        boolean result = addressBookMain.isContactReadingFromCSVFileSuccess("AB1");
        Assert.assertTrue(result);
    }

    @Test
    public void givenContact_WhenWrittenToJSONFile_ShouldReturnTrue() {
        boolean result = addressBookMain.isContactWritingToJSONFileSuccess("AB1", 1, 1, "Kanishk", "Mathur", "2222221232");
        Assert.assertTrue(result);
    }

    @Test
    public void givenJSONFile_WhenReadSuccessfully_ShouldReturnTrue() {
        boolean result = addressBookMain.isContactReadingFromJSONFileSuccess("AB1");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDatabase_WhenRetrieved_ShouldreturnCorrectCount() {
        int expectedOutput = crudOperations.getNoOfContactsInDB();
        crudOperations.readAll();
        int output = addressBookMain.getNoOfContactsInAddressBook("AB1");
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void givenContactName_WhenPhoneUpdated_ShouldSync() {
        // Test 1
        crudOperations.updateContact("Kumar", "1111111111");
        Assert.assertTrue(addressBookMain.isLocalAddressBookContainsFirstNamePhoneNoRecord("Kumar", "1111111111"));
        Assert.assertTrue(addressBookMain.isDBContainsFirstNamePhoneNoRecord("Kumar", "1111111111"));

        //Test 2
        crudOperations.updateContact("Kumar", "2222222222");
        Assert.assertTrue(addressBookMain.isLocalAddressBookContainsFirstNamePhoneNoRecord("Kumar", "2222222222"));
        Assert.assertTrue(addressBookMain.isDBContainsFirstNamePhoneNoRecord("Kumar", "2222222222"));
    }

    @Test
    public void givenTwoDates_WhenRetrievedContactsAddedBetweenDatesFromDB_ShouldReturnCorrectCount() {
        int initialCount = crudOperations.readByDateAdded("2018-01-01", "2020-01-01");
        addressBookMain.createDBRecordByFirstNameDate("DummyFirstName", "2018-09-09");
        int outputCount = crudOperations.readByDateAdded("2018-01-01", "2020-01-01");
        Assert.assertEquals(initialCount+1, outputCount);
        addressBookMain.deleteDBRecordByName("DummyFirstName");
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
        try {
            boolean output = crudOperations.createRecord(1, "Raghav", "Joshi", "4234234111", "raghav@gmail.com", "W Street", "Surat", "Gujrat", 234123, new ArrayList<String>(Arrays.asList("Friends", "Family")));
            Assert.assertTrue(output);
            addressBookMain.deleteDBRecordByName("Raghav");
        } catch (InvalidSqlOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void given3Records_WhenSuccessfullyAddedToDB_ShouldReturnCorrectCount() {
        int initialCount = crudOperations.getNoOfContactsInDB();
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        contactList.add(new Contact(1, "TestName1", "T", "1111111111", "test@gmail.com","T Street", "City", "State", 111111, new ArrayList<String>(Arrays.asList("Friends"))));
        contactList.add(new Contact(1, "TestName2", "T", "1111111111", "test@gmail.com","T Street", "City", "State", 111111, new ArrayList<String>(Arrays.asList("Friends"))));
        contactList.add(new Contact(1, "TestName3", "T", "1111111111", "test@gmail.com","T Street", "City", "State", 111111, new ArrayList<String>(Arrays.asList("Friends"))));
        addressBookMain.createDBRecordsByContacts(contactList);
        int outputCount = crudOperations.getNoOfContactsInDB();
        Assert.assertEquals(initialCount+3, outputCount);
        addressBookMain.deleteDBRecordsByFirstNames(new ArrayList<String>(Arrays.asList("TestName1", "TestName2", "TestName3")));
    }

    @Test
    public void givenContact_WhenAddedToJsonServer_ShouldReturnCorrectStatusCode() {
        try {
            int statusCode = addressBookMain.postContactToJsonServer(12, 1, "Murali", "Sundar", "2222333312");
            Assert.assertEquals(201, statusCode);
        } catch (JsonServerException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void WhenRecordsRetrievedFromJsonServer_ShouldReturnCorrectStatusCode() {
        int statusCode = addressBookMain.getContactFromJsonServer();
        Assert.assertEquals(200, statusCode);
    }
}