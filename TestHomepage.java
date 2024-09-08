import static org.junit.Assert.*;
import org.junit.*;
import java.awt.*;
import java.util.concurrent.*;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.*;

public class TestHomepage {

    private Homepage homepage;

    @Before
    public void setUp() {
        // Initialize the homepage before each test
        homepage = new Homepage();
    }

    @Test
    public void testWelcomeLabel() {
        Component[] components = homepage.getContentPane().getComponents();
        JLabel welcomeLabel = null;
        for (Component component : components) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (label.getText().equals("Welcome!")) {
                    welcomeLabel = label;
                    break;
                }
            }
        }
        assertNotNull("Welcome label should not be null", welcomeLabel);
        assertEquals("Welcome label text should be 'Welcome!'", "Welcome!", welcomeLabel.getText());
        assertEquals("Welcome label font should be bold", Font.BOLD, welcomeLabel.getFont().getStyle());
        assertEquals("Welcome label font size should be 20", 20, welcomeLabel.getFont().getSize());
    }

    @Test
    public void testRegisterButton() throws InterruptedException, ExecutionException {
        JButton registerButton = findButtonByName("Register");
        assertNotNull("Register button should not be null", registerButton);

        // Simulate button click and wait for the registration window to open
        CompletableFuture<Void> registrationOpened = new CompletableFuture<>();
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationOpened.complete(null);
            }
        });
        registerButton.doClick();
        registrationOpened.get();

        assertNotNull("Registration window should be opened", findFrameByName("Register Patient"));
    }

    @Test
    public void testLoginButton() throws InterruptedException, ExecutionException {
        JButton loginButton = findButtonByName("Login");
        assertNotNull("Login button should not be null", loginButton);

        // Simulate button click and wait for the authentication window to open
        CompletableFuture<Void> authenticationOpened = new CompletableFuture<>();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticationOpened.complete(null);
            }
        });
        loginButton.doClick();
        authenticationOpened.get();

        assertNotNull("Authentication window should be opened", findFrameByName("Patient Portal"));
    }

    // Helper method to find a button by name
    private JButton findButtonByName(String name) {
        Component[] components = homepage.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals(name)) {
                    return button;
                }
            }
        }
        return null;
    }

    // Helper method to find a frame by name
    private Frame findFrameByName(String name) {
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            if (frame.getTitle().equals(name)) {
                return frame;
            }
        }
        return null;
    }
}