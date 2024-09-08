import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthorizationManager {

    private static final String LOG_FILE = "authorization_log.txt";

    // Log user sign-in
    public static void logSignIn(String username) {
        String logEntry = getTimestamp() + ": User '" + username + "' signed in.";
        writeLog(logEntry);
    }

    // Log user sign-out
    public static void logSignOut(String username) {
        String logEntry = getTimestamp() + ": User '" + username + "' signed out.";
        writeLog(logEntry);
    }

    // Log functionality usage
    public static void logFunctionalityUsage(String username, String functionality) {
        String logEntry = getTimestamp() + ": User '" + username + "' accessed functionality: " + functionality;
        writeLog(logEntry);
    }

    // Get current timestamp
    private static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // Write log entry to file
    private static void writeLog(String logEntry) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE, true)))) {
            writer.println(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logInvalidLoginAttempt(String username) {
        // Log invalid login attempt
        String logEntry = getTimestamp() + ": Invalid login attempt for user '" + username + "'";
        writeLog(logEntry);
    }

    public static void logRegistration(String name) {
        // Log registration
        String logEntry = getTimestamp() + ": New user registered with name '" + name + "'";
        writeLog(logEntry);
    }
}
