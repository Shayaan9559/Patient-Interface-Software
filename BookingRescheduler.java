import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingRescheduler extends JFrame {

    private JLabel titleLabel, dateLabel, timeLabel;
    private JTextField dateField, timeField;
    private JButton rescheduleButton;
    
    // Define your database credentials and other variables here
    private static final String DB_URL = "your_database_url";
    private static final String DB_USERNAME = "your_username";
    private static final String DB_PASSWORD = "your_password";
    private int selectedDoctorId = 1; // replace with actual doctor id
    private int patientId = 1; // replace with actual patient id
    private String oldDate = "old_date"; // replace with actual old date
    private String oldTime = "old_time"; // replace with actual old time

    public BookingRescheduler() {
        super("Booking Rescheduler");
        setLayout(new GridLayout(4, 1)); // Adjust grid layout size as needed

        // Title Label
        titleLabel = new JLabel("Reschedule Booking");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel);

        // Date and Time Input Fields
        JPanel inputPanel = new JPanel(new FlowLayout());
        dateLabel = new JLabel("Date (YYYY-MM-DD):");
        inputPanel.add(dateLabel);
        dateField = new JTextField(10); // Adjust field size as needed
        inputPanel.add(dateField);
        timeLabel = new JLabel("Time (HH:MM):");
        inputPanel.add(timeLabel);
        timeField = new JTextField(10); // Adjust field size as needed
        inputPanel.add(timeField);
        add(inputPanel);

        // Reschedule Button
        rescheduleButton = new JButton("Reschedule");
        rescheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to reschedule booking with doctor
                String date = dateField.getText();
                String time = timeField.getText();
                // Implement logic to check doctor availability and reschedule appointment
                boolean success = rescheduleAppointment(date, time);
                if (success) {
                    sendConfirmationMessages(date, time);
                    JOptionPane.showMessageDialog(BookingRescheduler.this, "Booking rescheduled successfully!");
                    dispose(); // Close the booking rescheduler window
                } else {
                    JOptionPane.showMessageDialog(BookingRescheduler.this, "Failed to reschedule booking. Doctor not available.");
                }
            }
        });
        add(rescheduleButton);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window, not the entire application
        setVisible(true);
    }

    private boolean rescheduleAppointment(String date, String time) {
        // Connect to the database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Check doctor's availability for the specified date and time
            String checkAvailabilityQuery = "SELECT * FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
            try (PreparedStatement availabilityStatement = connection.prepareStatement(checkAvailabilityQuery)) {
                availabilityStatement.setInt(1, selectedDoctorId); // Assuming you have the doctor's ID
                availabilityStatement.setString(2, date);
                availabilityStatement.setString(3, time);
                // Execute the query
                ResultSet resultSet = availabilityStatement.executeQuery();
                if (!resultSet.next()) {
                    // Doctor is available, update the appointment details
                    String updateAppointmentQuery = "UPDATE appointments SET appointment_date = ?, appointment_time = ? WHERE patient_id = ? AND appointment_date = ? AND appointment_time = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateAppointmentQuery)) {
                        updateStatement.setString(1, date);
                        updateStatement.setString(2, time);
                        updateStatement.setInt(3, patientId); // Assuming you have the patient's ID
                        updateStatement.setString(4, oldDate);
                        updateStatement.setString(5, oldTime);
                        // Execute the update query
                        int rowsUpdated = updateStatement.executeUpdate();
                        return rowsUpdated > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Rescheduling failed
    }

    // Method to send confirmation messages to patient and doctor
    private void sendConfirmationMessages(String date, String time) {
        // Logic to send confirmation messages (e.g., emails, SMS)
        // Here, you can implement code to send confirmation messages to both the patient and the doctor
        // For demonstration purposes, we'll just print confirmation messages to the console
        System.out.println("Booking rescheduled for " + date + " at " + time);
        // You can also display confirmation messages in the application interface
    }

    // Method to be called when a patient successfully logs in to show the booking rescheduler
    public static void showBookingRescheduler() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookingRescheduler();
            }
        });
    }
}
