import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BookingViewerTest {

    @Test
    public void testViewBookings() {
        // Test case to check if bookings are displayed correctly
        BookingViewer bookingViewer = new BookingViewer();

        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Provide input for the month and year
        ByteArrayInputStream in = new ByteArrayInputStream("4\n2024\n".getBytes());
        System.setIn(in);

        // Call the viewBookings method
        bookingViewer.viewBookings("4", "2024");

        // Check if the correct output is displayed
        String expectedOutput = "Date: 2024-04-01\nTime: 12:00:00\n\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testNoBookingsFound() {
        // Test case to check if the correct message is displayed when no bookings are found
        BookingViewer bookingViewer = new BookingViewer();

        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Provide input for the month and year
        ByteArrayInputStream in = new ByteArrayInputStream("5\n2024\n".getBytes());
        System.setIn(in);

        // Call the viewBookings method
        bookingViewer.viewBookings("5", "2024");

        // Check if the correct output is displayed
        String expectedOutput = "No bookings found for 5 2024";
        assertEquals(expectedOutput, outContent.toString().trim());
    }
}
