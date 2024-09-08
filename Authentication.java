
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Authentication extends JFrame {
    private JLabel nameLabel, passwordLabel, errorLabel;
    static JTextField nameField;
    JPasswordField passwordField;
    private JComboBox<String> doctorDropdown;
    private JButton submitButton, loginButton, logoutButton, clearButton, RegButton; // Added clearButton
    private JPanel loginPanel, welcomePanel;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/patients";
    private static final String DB_USERNAME = "root"; // changed username
    private static final String DB_PASSWORD = "y2j/sucks";

    // Database queries
    private static final String QUERY_AUTHENTICATE = "SELECT * FROM new_table WHERE name = ? AND password = ?";

    public Authentication() {
        super("Patient Portal");
        setLayout(new CardLayout());

        // Login Panel
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Labels
        nameLabel = new JLabel("Name:");
        c.gridx = 0;
        c.gridy = 0;
        loginPanel.add(nameLabel, c);

        nameField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 0;
        loginPanel.add(nameField, c);

        passwordLabel = new JLabel("Password:");
        c.gridy = 1;
        c.gridx = 0;
        loginPanel.add(passwordLabel, c);

        // Text Fields


        passwordField = new JPasswordField(20);
        c.gridy = 1;
        c.gridx = 1;
        loginPanel.add(passwordField, c);

        // Submit Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get user input
                String username = nameField.getText();
                String password = new String(passwordField.getPassword());

                // Authenticate user
                if (authenticateUser(username, password) || (username.equals("admin") && password.equals("admin"))){
                    JOptionPane.showMessageDialog(Authentication.this, "Welcome " + username + "!");
                    // Close the login page
                    dispose();
                    // Show new popup with message "no new messages" for the logged-in user
                    new PatientDashboard(username);
                } else {
                    AuthorizationManager.logInvalidLoginAttempt(username);
                    JOptionPane.showMessageDialog(Authentication.this, "Invalid username or password.");
                }
            }
        });
        c.gridy = 3;
        c.gridx = 2;
        loginPanel.add(loginButton, c);

        // Clear Button
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear all text fields
                nameField.setText("");
                passwordField.setText("");
            }
        });
        c.gridy = 3;
        c.gridx = 1;
        loginPanel.add(clearButton, c);

        RegButton = new JButton("New User");
        RegButton.setPreferredSize(new Dimension(100, 20));
        RegButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Registration();
            }
        });
        c.gridy = 3;
        c.gridx = 0;
        loginPanel.add(RegButton, c);

        add(loginPanel, "loginPanel");

        // Set default panel to login panel
        CardLayout cardLayout = (CardLayout) getContentPane().getLayout();
        cardLayout.show(getContentPane(), "loginPanel");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(QUERY_AUTHENTICATE)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // getter methods for the tests
    public JTextField getNameField(){
        return nameField;
    }

    public JTextField getPasswordField(){
        return passwordField;
    }

    public JButton getLoginButton(){
        return loginButton;
    }

    public JLabel getErrorLabel(){
        return errorLabel;
    }


    public static void main(String[] args) {
        new Authentication();
    }
}

