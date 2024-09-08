import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewVisits extends JFrame {

    private JLabel titleLabel, monthLabel, yearLabel;
    private JTextField monthField, yearField;
    private JButton viewButton;

    public ViewVisits() {
        super("Visit Viewer");
        setLayout(new GridLayout(4, 1)); // Adjust grid layout size as needed

        // Title Label
        titleLabel = new JLabel("View Visits");
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
                String month = monthField.getText();
                String year = yearField.getText();
                viewVisits(month, year); // Call the method to retrieve and display visit details
            }
        });
        add(viewButton);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window, not the entire application
        setVisible(true);
    }

    // Method to retrieve and display visit details for the specified month and year
    private void viewVisits(String month, String year) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/patients", "root", "y2j/sucks")) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM visits WHERE MONTH(visit_date) = ? AND YEAR(visit_date) = ?");
            statement.setString(1, month);
            statement.setString(2, year);
            ResultSet resultSet = statement.executeQuery();

            StringBuilder visitDetails = new StringBuilder();
            // Iterate through the results and append them to the display string
            while (resultSet.next()) {
                visitDetails.append("Visit Date: ").append(resultSet.getDate("visit_date")).append("\n");
                visitDetails.append("Visit Time: ").append(resultSet.getTime("visit_time")).append("\n");
                visitDetails.append("Doctor: ").append(resultSet.getString("doctor")).append("\n");
                visitDetails.append("Visit Details: ").append(resultSet.getString("visit-details")).append("\n\n");
            }

            // Display visit details to the user
            if (visitDetails.length() > 0) {
                JOptionPane.showMessageDialog(this, "Visit Details for " + month + " " + year + ":\n\n" + visitDetails.toString());
            } else {
                JOptionPane.showMessageDialog(this, "No visit details found for " + month + " " + year);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving visit details: " + ex.getMessage());
        }
    }

    // Method to be called to show the visit viewer
    public static void showVisitViewer() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ViewVisits();
            }
        });
    }
}
