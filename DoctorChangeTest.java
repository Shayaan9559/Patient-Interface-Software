import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DoctorChangeTest {

    private DoctorChange doctorChange;

    @Before
    public void setUp() {
        doctorChange = new DoctorChange();
    }

    @Test
    public void testGetAllDoctorsFromDatabase() throws Exception {
        // Test method to get all doctors from the database
        ArrayList<String> expectedDoctors = new ArrayList<>();
        expectedDoctors.add("Dr. Smith");
        expectedDoctors.add("Dr. Jane Doe");

        // Call the method under test
        ArrayList<String> actualDoctors = doctorChange.getAllDoctorsFromDatabase();

        // Assert that the expected and actual doctors match
        assertEquals(expectedDoctors, actualDoctors);
    }

    @Test
    public void testUpdateDoctorInDatabase_Success() throws Exception {
        // Test method to update doctor in the database successfully
        // Setting up the test environment
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients", "root", "y2j/sucks");
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO doctors (Name) VALUES ('Doctor 3')");

        // Call the method under test
        boolean success = doctorChange.updateDoctorInDatabase("Doctor 3");

        // Assert that the update was successful
        assertTrue(success);

        // Clean up: Remove the inserted record
        statement.executeUpdate("DELETE FROM doctors WHERE Name = 'Doctor 3'");
        statement.close();
        connection.close();
    }

    @Test
    public void testUpdateDoctorInDatabase_Failure() throws Exception {
        // Test method to update doctor in the database and fail
        // Call the method under test with a doctor that does not exist
        boolean success = doctorChange.updateDoctorInDatabase("Non-existing Doctor");

        // Assert that the update failed
        assertFalse(success);
    }
}
