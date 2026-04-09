import gui.LoginFrame;
import javax.swing.*;

/**
 * MainApp - Application Entry Point
 * Sets global Swing Look & Feel, then launches the Login window.
 * Run this class to start the Donation Management System.
 */
public class MainApp {

    public static void main(String[] args) {
        // Run UI on the Event Dispatch Thread (Swing thread safety requirement)
        SwingUtilities.invokeLater(() -> {

            // Apply a modern system Look & Feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fallback to default if system L&F is unavailable
                e.printStackTrace();
            }

            // Apply global dark font defaults
            UIManager.put("OptionPane.background",       new java.awt.Color(20, 20, 45));
            UIManager.put("Panel.background",            new java.awt.Color(20, 20, 45));
            UIManager.put("OptionPane.messageForeground",new java.awt.Color(220, 220, 240));

            // Launch the Login screen
            new LoginFrame();
        });
    }
}
