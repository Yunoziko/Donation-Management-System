package gui;

import model.Donation;
import model.Donor;
import service.DonationService;
import service.DonorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Optional;

/**
 * DonationPanel - Donation Management UI
 * Features: Add a donation (linked to an existing donor), view all donations in a table.
 * Date format enforced: YYYY-MM-DD.
 */
public class DonationPanel extends JPanel {

    private DonationService donationService;
    private DonorService donorService;

    // Form fields
    private JTextField txtDonorId, txtAmount, txtDate, txtPurpose;

    // Table
    private JTable donationTable;
    private DefaultTableModel tableModel;

    // Status bar
    private JLabel lblStatus;

    public DonationPanel(DonationService donationService, DonorService donorService) {
        this.donationService = donationService;
        this.donorService    = donorService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(12, 12, 28));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildCenter(),    BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ===================== HEADER =====================

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(12, 12, 28));

        JLabel title = new JLabel("💰 Donation Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(251, 189, 35));
        header.add(title, BorderLayout.WEST);

        JLabel hint = new JLabel("Add a donation linked to an existing donor ID");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(new Color(120, 120, 160));
        header.add(hint, BorderLayout.EAST);

        return header;
    }

    // ===================== CENTER =====================

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(new Color(12, 12, 28));

        center.add(buildForm(),  BorderLayout.NORTH);
        center.add(buildTable(), BorderLayout.CENTER);

        return center;
    }

    // ---------- Input Form ----------

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(20, 20, 45));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 90), 1),
                new EmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels row
        addLabel(form, "Donor ID *",         0, 0, gbc);
        addLabel(form, "Amount (₹) *",        1, 0, gbc);
        addLabel(form, "Date (YYYY-MM-DD) *", 2, 0, gbc);
        addLabel(form, "Purpose *",           3, 0, gbc);

        // Fields row
        txtDonorId = new JTextField(8);  styleField(txtDonorId);
        txtAmount  = new JTextField(10); styleField(txtAmount);
        txtDate    = new JTextField(12); styleField(txtDate); txtDate.setText("2026-04-09");
        txtPurpose = new JTextField(16); styleField(txtPurpose);

        gbc.gridy = 1; gbc.gridx = 0; form.add(txtDonorId, gbc);
        gbc.gridx = 1; form.add(txtAmount,  gbc);
        gbc.gridx = 2; form.add(txtDate,    gbc);
        gbc.gridx = 3; form.add(txtPurpose, gbc);

        // Buttons row
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(new Color(20, 20, 45));

        JButton btnAdd    = actionButton("➕ Add Donation",  new Color(251, 189, 35));
        JButton btnRefresh = actionButton("🔄 Refresh Table", new Color(100, 100, 140));

        btnAdd.addActionListener(e    -> addDonation());
        btnRefresh.addActionListener(e -> refreshTable());

        btnPanel.add(btnAdd);
        btnPanel.add(btnRefresh);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4;
        form.add(btnPanel, gbc);

        return form;
    }

    // ---------- Results Table ----------

    private JScrollPane buildTable() {
        String[] cols = {"Don. ID", "Donor ID", "Donor Name", "Amount (₹)", "Date", "Purpose"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        donationTable = new JTable(tableModel);
        donationTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        donationTable.setForeground(new Color(220, 220, 240));
        donationTable.setBackground(new Color(20, 20, 45));
        donationTable.setSelectionBackground(new Color(251, 189, 35));
        donationTable.setSelectionForeground(Color.BLACK);
        donationTable.setRowHeight(28);
        donationTable.setGridColor(new Color(40, 40, 70));

        JTableHeader header = donationTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(30, 30, 65));
        header.setForeground(new Color(220, 190, 80));

        // Column widths
        donationTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        donationTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        donationTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        donationTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        donationTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        donationTable.getColumnModel().getColumn(5).setPreferredWidth(200);

        JScrollPane scroll = new JScrollPane(donationTable);
        scroll.getViewport().setBackground(new Color(20, 20, 45));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 90)));
        return scroll;
    }

    // ===================== STATUS BAR =====================

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(new Color(18, 18, 40));
        lblStatus = new JLabel("Ready.");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(120, 120, 160));
        bar.add(lblStatus);
        return bar;
    }

    // ===================== ACTIONS =====================

    private void addDonation() {
        // --- Validate Donor ID ---
        int donorId;
        try {
            donorId = Integer.parseInt(txtDonorId.getText().trim());
        } catch (NumberFormatException ex) {
            setStatus("⚠️ Donor ID must be a valid number.", new Color(251, 189, 35)); return;
        }

        Optional<Donor> donor = donorService.getDonorById(donorId);
        if (!donor.isPresent()) {
            setStatus("❌ No donor found with ID " + donorId + ". Add the donor first.", new Color(248, 87, 87)); return;
        }

        // --- Validate Amount ---
        double amount;
        try {
            amount = Double.parseDouble(txtAmount.getText().trim());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            setStatus("⚠️ Amount must be a positive number.", new Color(251, 189, 35)); return;
        }

        // --- Validate Date & Purpose ---
        String date    = txtDate.getText().trim();
        String purpose = txtPurpose.getText().trim();

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            setStatus("⚠️ Date must be in YYYY-MM-DD format.", new Color(251, 189, 35)); return;
        }
        if (purpose.isEmpty()) {
            setStatus("⚠️ Purpose is required.", new Color(251, 189, 35)); return;
        }

        boolean ok = donationService.addDonation(donorId, amount, date, purpose);
        if (ok) {
            setStatus("✅ Donation of ₹" + String.format("%.2f", amount) + " added for " + donor.get().getName(), new Color(72, 199, 142));
            refreshTable();
            resetForm();
        } else {
            setStatus("❌ Failed to add donation.", new Color(248, 87, 87));
        }
    }

    /** Reloads the table with all donations, resolving donor names from DonorService. */
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Donation> list = donationService.getAllDonations();
        for (Donation don : list) {
            String donorName = donorService.getDonorById(don.getDonorId())
                    .map(Donor::getName).orElse("Unknown");
            tableModel.addRow(new Object[]{
                don.getDonationId(),
                don.getDonorId(),
                donorName,
                String.format("₹%.2f", don.getAmount()),
                don.getDate(),
                don.getPurpose()
            });
        }
        setStatus("Showing " + list.size() + " donation(s) | Total: ₹" +
                  String.format("%.2f", donationService.getTotalDonationAmount()),
                  new Color(120, 120, 160));
    }

    private void resetForm() {
        txtDonorId.setText("");
        txtAmount.setText("");
        txtPurpose.setText("");
        txtDate.setText("2026-04-09");
    }

    // ===================== HELPERS =====================

    private void addLabel(JPanel panel, String text, int x, int y, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(160, 160, 200));
        gbc.gridx = x; gbc.gridy = y;
        panel.add(lbl, gbc);
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(new Color(30, 30, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 120), 1),
                new EmptyBorder(5, 8, 5, 8)));
    }

    private JButton actionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        Color dimmed = bg.darker();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(dimmed); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private void setStatus(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }
}
