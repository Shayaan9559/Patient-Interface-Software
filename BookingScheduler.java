import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BookingScheduler extends JFrame {

    private JLabel titleLabel, dateLabel, timeLabel;
    private JTextField dateField, timeField;
    private JButton bookButton;

    public BookingScheduler() {
        super("Booking Scheduler");
        setLayout(new GridLayout(4, 1)); // Adjust grid layout size as needed

        // Title Label
        titleLabel = new JLabel("Arrange Booking");
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

        // Book Button
        bookButton = new JButton("Book");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to arrange booking with doctor
                String date = dateField.getText();
                String time = timeField.getText();
                boolean success = bookAppointment(date, time);
                if (success) {
                    JOptionPane.showMessageDialog(BookingScheduler.this, "Booking for " + date + " at " + time + " arranged successfully!");
                    dispose(); // Close the booking scheduler window
                } else {
                    JOptionPane.showMessageDialog(BookingScheduler.this, "Failed to arrange booking. Please try again.");
                }
            }
        });
        add(bookButton);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window, not the entire application
        setVisible(true);
    }

    // Method to book an appointment with the doctor
    private boolean bookAppointment(String date, String time) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctors", "root", "y2j/sucks")) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO appointments (date, time) VALUES (?, ?)");
            statement.setString(1, date);
            statement.setString(2, time);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Method to be called to show the booking scheduler interface
    public static void showBookingScheduler() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookingScheduler();
            }
        });
    }
}
