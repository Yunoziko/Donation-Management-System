package gui;

import service.DonorService;
import service.DonationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * MainFrame - Root Application Window
 * Houses a sidebar navigation and a content area (CardLayout).
 * Switches between Donor, Donation, and Report panels.
 */
public class MainFrame extends JFrame {

    // Shared service instances passed to child panels
    private DonorService donorService = new DonorService();
    private DonationService donationService = new DonationService();

    // Content panel with CardLayout for switching views
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Named cards for each section
    private static final String CARD_DONORS    = "DONORS";
    private static final String CARD_DONATIONS = "DONATIONS";
    private static final String CARD_REPORTS   = "REPORTS";

    // Keep panel references so they can be refreshed
    private DonorPanel donorPanel;
    private DonationPanel donationPanel;
    private ReportPanel reportPanel;

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Donation Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));

        // Root layout: sidebar + content
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(12, 12, 28));

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContentArea(), BorderLayout.CENTER);

        setVisible(true);
        showCard(CARD_DONORS); // Default view
    }

    // ===================== SIDEBAR =====================

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(18, 18, 40));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // App logo/title
        JLabel logo = new JLabel("💚 DonationMS");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        logo.setForeground(new Color(72, 199, 142));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        JLabel sub = new JLabel("Admin Dashboard");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(new Color(100, 100, 140));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(sub);

        sidebar.add(Box.createVerticalStrut(30));

        // Navigation buttons
        sidebar.add(buildNavButton("👥  Donors",    CARD_DONORS));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(buildNavButton("💰  Donations", CARD_DONATIONS));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(buildNavButton("📊  Reports",   CARD_REPORTS));

        sidebar.add(Box.createVerticalGlue());

        // Logout button at the bottom
        JButton btnLogout = buildNavButton("🔒  Logout", null);
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        sidebar.add(btnLogout);

        return sidebar;
    }

    /**
     * Creates a styled sidebar navigation button.
     * @param text  Label text
     * @param card  Card name to navigate to (null = logout)
     */
    private JButton buildNavButton(String text, String card) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(200, 200, 230));
        btn.setBackground(new Color(25, 25, 55));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(190, 40));
        btn.setPreferredSize(new Dimension(190, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(5, 14, 5, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(99, 102, 241));
                btn.setForeground(Color.WHITE);
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(25, 25, 55));
                btn.setForeground(new Color(200, 200, 230));
            }
        });

        if (card != null) {
            btn.addActionListener(e -> showCard(card));
        }

        return btn;
    }

    // ===================== CONTENT AREA =====================

    private JPanel buildContentArea() {
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(12, 12, 28));

        // Instantiate panels (share the same service objects)
        donorPanel    = new DonorPanel(donorService, donationService);
        donationPanel = new DonationPanel(donationService, donorService);
        reportPanel   = new ReportPanel(donorService, donationService);

        contentPanel.add(donorPanel,    CARD_DONORS);
        contentPanel.add(donationPanel, CARD_DONATIONS);
        contentPanel.add(reportPanel,   CARD_REPORTS);

        return contentPanel;
    }

    // ===================== NAVIGATION =====================

    /**
     * Switches the visible card and refreshes its table data.
     */
    private void showCard(String card) {
        cardLayout.show(contentPanel, card);
        // Refresh data whenever a panel becomes visible
        switch (card) {
            case CARD_DONORS:    donorPanel.refreshTable();    break;
            case CARD_DONATIONS: donationPanel.refreshTable(); break;
            case CARD_REPORTS:   reportPanel.refreshReport();  break;
        }
    }
}
