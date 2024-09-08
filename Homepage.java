import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Homepage extends JFrame {

    private JButton registerButton, loginButton;
    private JLabel welcomeLabel; // Added welcomeLabel

    public Homepage() {
        super("Homepage");
        setLayout(new GridLayout(3, 1)); // Increased grid layout size

        // Welcome Label
        welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel);

        // Register Button...
        
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the UI for registration
                new Registration();
            }
        });
        add(registerButton);
       

        // Login Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the authentication window for login
                new Authentication();
            }
        });
        add(loginButton);


        pack();
        setSize(300, 200); // Increased window size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Homepage();
    }
}
