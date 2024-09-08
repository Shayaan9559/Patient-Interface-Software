import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PatientDashboard extends JFrame {

    private JLabel welcomeLabel;
    private JButton bookAppointmentButton, viewMessagesButton, logoutButton, ChangeDoctor, ViewAppointments, Visits, Reschedule;

    public PatientDashboard(String username) {
        super("Patient Dashboard");
        setLayout(new GridLayout(4, 1)); // Adjust grid layout size as needed

        // Welcome Label
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel);

        // Book Appointment Button
        bookAppointmentButton = new JButton("Book Appointment");
        bookAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open booking scheduler
                dispose();
                new BookingScheduler();
                // Log functionality usage
                AuthorizationManager.logFunctionalityUsage(username, "Book Appointment");
            }
        });
        add(bookAppointmentButton);// Book Appointment Button
        ViewAppointments = new JButton("View Appointments");
        ViewAppointments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open booking scheduler
                dispose();
                new BookingViewer();
                // Log functionality usage
                AuthorizationManager.logFunctionalityUsage(username, "Book Appointment");
            }
        });
        add(ViewAppointments);

        // Change Doctor Button
        ChangeDoctor = new JButton("Change my Doctor");
        ChangeDoctor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open booking scheduler
                DoctorChange.main(new DoctorChange());
                // Log functionality usage
                AuthorizationManager.logFunctionalityUsage(username, "Change Doctor");
            }
        });
        add(ChangeDoctor);

        // View Messages Button
        viewMessagesButton = new JButton("View Messages");
        viewMessagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to view messages
                String messages = retrieveMessagesFromDatabase(username);
                JOptionPane.showMessageDialog(PatientDashboard.this, messages);
                // Log functionality usage
                AuthorizationManager.logFunctionalityUsage(username, "View Messages");
            }
        });
        add(viewMessagesButton);

        Visits = new JButton("View Visits");
        Visits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ViewVisits.showVisitViewer();
                // Log functionality usage
                AuthorizationManager.logFunctionalityUsage(username, "View Visits");
            }
        });
        add(Visits);

        // Logout Button
        Reschedule = new JButton("Reschedule Booking");
        Reschedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BookingRescheduler.showBookingRescheduler();
                // Log functionality usage
                AuthorizationManager.logFunctionalityUsage(username, "Reschedule Booking");
            }
        });
        add(Reschedule);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement logic to logout user
                dispose(); // Close the dashboard window
                // Open authentication window again
                new Authentication();
                // Log functionality usage
                AuthorizationManager.logFunctionalityUsage(username, "Logout");
            }
        });
        add(logoutButton);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the entire application on window close
        setVisible(true);
    }

    // Method to retrieve messages from the database
    private String retrieveMessagesFromDatabase(String username) {
        StringBuilder messages = new StringBuilder();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients", "root", "y2j/sucks")) {
            String query = "SELECT message FROM messages WHERE patient = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                messages.append(resultSet.getString("message")).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error retrieving messages";
        }
        if (messages.length() == 0) {
            return "No messages available";
        }
        return messages.toString();
    }

    // Method to be called to show the patient dashboard
    public static void showDashboard(String username) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PatientDashboard(username);
            }
        });
    }
}