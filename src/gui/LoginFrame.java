package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame - Admin Login Screen
 * First screen shown to the user.
 * Simple hardcoded admin credentials for demonstration.
 * Username: admin | Password: admin123
 */
public class LoginFrame extends JFrame {

    // --- Hardcoded credentials (extend with DB auth for production) ---
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    // UI Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Donation Management System — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 380);
        setLocationRelativeTo(null);   // Center on screen
        setResizable(false);

        // ---------- Main Panel ----------
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(18, 18, 35));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;

        // ---------- Title ----------
        JLabel lblTitle = new JLabel("🏥 Donation Manager", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(99, 179, 237));
        gbc.gridy = 0;
        mainPanel.add(lblTitle, gbc);

        JLabel lblSub = new JLabel("Admin Portal", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(150, 150, 180));
        gbc.gridy = 1;
        mainPanel.add(lblSub, gbc);

        // Spacer
        gbc.gridy = 2;
        mainPanel.add(Box.createVerticalStrut(10), gbc);

        // ---------- Username ----------
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(200, 200, 220));
        gbc.gridy = 3;
        mainPanel.add(lblUser, gbc);

        txtUsername = new JTextField();
        styleTextField(txtUsername);
        gbc.gridy = 4;
        mainPanel.add(txtUsername, gbc);

        // ---------- Password ----------
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPass.setForeground(new Color(200, 200, 220));
        gbc.gridy = 5;
        mainPanel.add(lblPass, gbc);

        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        gbc.gridy = 6;
        mainPanel.add(txtPassword, gbc);

        // ---------- Status Label ----------
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(252, 129, 129));
        gbc.gridy = 7;
        mainPanel.add(lblStatus, gbc);

        // ---------- Login Button ----------
        btnLogin = new JButton("Login");
        styleButton(btnLogin);
        gbc.gridy = 8;
        mainPanel.add(btnLogin, gbc);

        // ---------- Hint ----------
        JLabel lblHint = new JLabel("Hint: admin / admin123", SwingConstants.CENTER);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(100, 100, 130));
        gbc.gridy = 9;
        mainPanel.add(lblHint, gbc);

        // ---------- Action Listeners ----------
        btnLogin.addActionListener(e -> attemptLogin());

        // Allow Enter key to trigger login
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) attemptLogin();
            }
        });

        setVisible(true);
    }

    /**
     * Validates credentials and opens MainFrame on success.
     */
    private void attemptLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            lblStatus.setText("Please enter username and password.");
            return;
        }

        if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            dispose(); // Close login window
            new MainFrame(); // Open main application
        } else {
            lblStatus.setText("Invalid credentials. Try again.");
            txtPassword.setText("");
        }
    }

    // ---------- Style Helpers ----------

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(30, 30, 55));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 130), 1),
                new EmptyBorder(6, 10, 6, 10)));
        field.setPreferredSize(new Dimension(300, 38));
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(99, 102, 241));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(300, 40));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(79, 82, 220));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(99, 102, 241));
            }
        });
    }
}
