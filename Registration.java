import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Registration extends JFrame {

    private JLabel nameLabel, contactLabel, passwordLabel, confirmpasswordLabel, doctorLabel, errorLabel;

    private JLabel emailLabel;
    private JTextField emailField;
    private JTextField nameField, contactField;
    private JPasswordField passwordField, confirmpasswordField;
    private JComboBox<String> doctorDropdown;
    private JButton submitButton;
    private JButton clearButton;

    public Registration() {
        super("Register Patient");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.WEST;

        // Set Insets for consistent spacing around labels and text fields
        Insets labelInsets = new Insets(5, 10, 5, 5);
        Insets fieldInsets = new Insets(5, 5, 5, 10);
        Insets ComboInsets = new Insets(5, 10, 5, 10);

        // Labels
        nameLabel = new JLabel("Name:");
        c.gridx = 0;
        c.gridy = 0;
        c.insets = labelInsets;
        add(nameLabel, c);

        // Inside the constructor
        emailLabel = new JLabel("Email:");
        c.gridx =0;
        c.gridy = 1; // Place the email field after the doctor dropdown
        c.insets = labelInsets;
        add(emailLabel, c);



        contactLabel = new JLabel("Contact:");
        c.gridy = 2;
        c.insets = labelInsets;
        add(contactLabel, c);

        passwordLabel = new JLabel("Password:");
        c.gridy = 3;
        c.insets = labelInsets;
        add(passwordLabel, c);

        confirmpasswordLabel = new JLabel("Confirm Password:");
        c.gridy = 4;
        c.insets = labelInsets;
        add(confirmpasswordLabel, c);

        doctorLabel = new JLabel("Doctor:");
        c.gridy = 5;
        c.insets = ComboInsets;
        add(doctorLabel, c);

        errorLabel = new JLabel(""); // To display error messages
        errorLabel.setForeground(Color.RED);
        c.gridy = 6;
        c.gridwidth = 2; // Span across two columns
        add(errorLabel, c);

        // Text Fields (set same dimensions)
        int fieldWidth = 20;
        nameField = new JTextField(fieldWidth);
        c.gridx = 1;
        c.gridy = 0;
        c.insets = fieldInsets;
        add(nameField, c);

        emailField = new JTextField(fieldWidth);
        c.gridx = 1;
        c.gridy = 1;
        c.insets = fieldInsets;
        add(emailField, c);

        contactField = new JTextField(fieldWidth);
        // Restrict input to digits only using a DocumentFilter
        c.gridy = 2;
        c.insets = fieldInsets;
        add(contactField, c);

        passwordField = new JPasswordField(fieldWidth);
        c.gridy = 3;
        c.insets = fieldInsets;
        add(passwordField, c);

        confirmpasswordField = new JPasswordField(fieldWidth);
        c.gridy = 4;
        c.insets = fieldInsets;
        add(confirmpasswordField, c);

        // Dropdown Menu (populated with existing doctors from database)
        doctorDropdown = new JComboBox<>();
        doctorDropdown.setPreferredSize(new Dimension(100, fieldWidth));
        // Replace this with logic to populate the dropdown with doctor names from the database
        doctorDropdown.addItem("Select a doctor");
        doctorDropdown.addItem("Dr. Smith");
        doctorDropdown.addItem("Dr. Jones");
        doctorDropdown.addItem("Dr. Patel");
        doctorDropdown.addItem("Dr. Lee");
        doctorDropdown.addItem("Dr. Kim");
        doctorDropdown.addItem("Dr. Singh");
        c.gridy = 5;
        c.insets = fieldInsets;
        add(doctorDropdown, c);

        // Submit Button
        submitButton = new JButton("Register");


        // Submit Button
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setText(null);
                emailField.setText(null);
                contactField.setText(null);
                passwordField.setText(null);
                confirmpasswordField.setText(null);
            }
        });
        c.gridy = 7;
        c.gridx = 0;
        // Increase horizontal spacing for buttons
        c.insets = new Insets(5, 10, 5, 10);
        add(clearButton, c);
        c.gridy = 7;
        c.gridx = 2;
        add(submitButton, c);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String errorMessage = validateFields();
                if (errorMessage.isEmpty()){
                    if(!String.valueOf(passwordField.getPassword()).equals(String.valueOf(confirmpasswordField.getPassword()))) {
                        String name = nameField.getText();
                        String contact = contactField.getText();
                        String password = String.valueOf(passwordField.getPassword());
                        String doctor = (String) doctorDropdown.getSelectedItem();

                        try {
                            registerUser(name, contact, password, doctor);
                            JOptionPane.showMessageDialog(Registration.this, "Registration successful!");

                            // Log registration
                            AuthorizationManager.logRegistration(name);
                                                        
                            dispose();
                            // Clear form fields after successful registration
                            nameField.setText("");
                            contactField.setText("");
                            passwordField.setText("");
                        } catch (SQLException ex) {

                            ex.printStackTrace();
                            errorLabel.setText("An error occurred during registration. Please try again.");
                        }
                    }
                    else {
                        errorLabel.setText("Passwords do not match");
                    }
                } else {
                    errorLabel.setText(errorMessage); // Display error message
                }
            }
        });
    }
    private void registerUser(String name, String contact, String password, String doctor) throws SQLException {
        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/patients?user=root&password=y2j/sucks");

            // Prepare the SQL statement with placeholders for values
            String sql = "INSERT INTO new_table (name, contact, password, doctor) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            // Set values for each placeholder
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, contact);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, doctor);

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            // Close resources
            preparedStatement.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String validateFields() {
        StringBuilder errorMessage = new StringBuilder();
        if (nameField.getText().isEmpty()) {
            errorMessage.append("Please enter your name.\n");
        }
        if (String.valueOf(emailField.getText()).isEmpty() ) {
            errorMessage.append("Please enter an email address.\n");
        }else {
            String email = emailField.getText();
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                errorMessage.append("Please enter a valid email address.\n");
            }
        }
        if (contactField.getText().isEmpty()) {
            errorMessage.append("Please enter your contact information.\n");
        }
        if (String.valueOf(passwordField.getPassword()).isEmpty()) {
            errorMessage.append("Please enter a password.\n");
        }

        return errorMessage.toString();
    }

    public static void main(String[] args) {
        new Registration();
    }
}
