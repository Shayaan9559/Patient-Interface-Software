import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BookingViewer extends JFrame {

    private JLabel titleLabel, monthLabel, yearLabel;
    private JTextField monthField, yearField;
    private JButton viewButton;

    public BookingViewer() {
        super("Booking Viewer");
        setLayout(new GridLayout(4, 1)); // Adjust grid layout size as needed

        // Title Label
        titleLabel = new JLabel("View Bookings");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel);

        // Month and Year Input Fields
        JPanel inputPanel = new JPanel(new FlowLayout());
        monthLabel = new JLabel("Month:");
        inputPanel.add(monthLabel);
        monthField = new JTextField(10); // Adjust field size as needed
        inputPanel.add(monthField);
        yearLabel = new JLabel("Year:");
        inputPanel.add(yearLabel);
        yearField = new JTextField(10); // Adjust field size as needed
        inputPanel.add(yearField);
        add(inputPanel);

        // View Button
        viewButton = new JButton("View");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to retrieve and display bookings based on month and year
                String month = monthField.getText();
                String year = yearField.getText();
                boolean bookingExists = checkBookings(month, year);
                if (bookingExists) {
                    JOptionPane.showMessageDialog(BookingViewer.this, "Bookings found for " + month + " " + year);
                } else {
                    JOptionPane.showMessageDialog(BookingViewer.this, "No bookings found for " + month + " " + year);
                }
            }
        });
        add(viewButton);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window, not the entire application
        setVisible(true);
    }

    // Method to check if bookings exist for a given month and year
    private boolean checkBookings(String month, String year) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctors", "root", "y2j/sucks")) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointments WHERE MONTH(date) = ? AND YEAR(date) = ?");
            statement.setString(1, month);
            statement.setString(2, year);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If resultSet has next, bookings exist; otherwise, no bookings exist
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Error occurred, assume no bookings exist
        }
    }

    // Method to be called when a patient successfully logs in to show the booking viewer
    public static void showBookingViewer() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookingViewer();
            }
        });
    }
}
