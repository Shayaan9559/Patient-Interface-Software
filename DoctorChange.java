import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class DoctorChange extends JFrame {

    private JComboBox<String> doctorDropdown;
    private JButton submitButton;

    public DoctorChange() {
        super("Change Doctor");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Label
        JLabel doctorLabel = new JLabel("Select New Doctor:");
        c.gridx = 0;
        c.gridy = 0;
        add(doctorLabel, c);

        // Dropdown Menu (populated with all doctors)
        doctorDropdown = new JComboBox<>();
        ArrayList<String> doctors = getAllDoctorsFromDatabase();
        for (String doctor : doctors) {
            doctorDropdown.addItem(doctor);
        }
        c.gridx = 1;
        add(doctorDropdown, c);

        // Submit Button
        submitButton = new JButton("Submit");
        c.gridy = 1;
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDoctor = (String) doctorDropdown.getSelectedItem();
                boolean success = updateDoctorInDatabase(selectedDoctor);
                if (success) {
                    sendConfirmationMessages(selectedDoctor);
                    JOptionPane.showMessageDialog(DoctorChange.this, "Doctor changed successfully!");
                    dispose(); // Close the doctor change window
                } else {
                    JOptionPane.showMessageDialog(DoctorChange.this, "Failed to change doctor. Please try again.");
                }
            }
        });
        add(submitButton, c);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private ArrayList<String> getAllDoctorsFromDatabase() {
        ArrayList<String> doctors = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctors", "root", "y2j/sucks")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM doctors_table");
            while (resultSet.next()) {
                doctors.add(resultSet.getString("doctor_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    private boolean updateDoctorInDatabase(String selectedDoctor) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/doctors", "root", "y2j/sucks")) {
            PreparedStatement statement = connection.prepareStatement("UPDATE patients SET doctor = ? WHERE patient_name = ?");
            statement.setString(1, selectedDoctor);
            statement.setString(2, "patient_username"); // Replace "patient_username" with actual patient's username
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendConfirmationMessages(String selectedDoctor) {
        // Logic to send confirmation messages to patient and doctor
        // Implement the message sending logic here
    }

    public static void main(DoctorChange doctorChange) {
        new DoctorChange();
    }
}
