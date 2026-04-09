package gui;

import model.Donor;
import service.DonorService;
import service.DonationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Optional;

/**
 * DonorPanel - Donor Management UI
 * Features: Add, Update, Delete, Search, and View all donors.
 * Uses a JTable to display results and form fields for input.
 */
public class DonorPanel extends JPanel {

    private DonorService donorService;
    private DonationService donationService;

    // Form fields
    private JTextField txtName, txtContact, txtEmail, txtSearchId;

    // Table
    private JTable donorTable;
    private DefaultTableModel tableModel;

    // Status bar
    private JLabel lblStatus;

    // Track selected donor for update
    private int selectedDonorId = -1;

    public DonorPanel(DonorService donorService, DonationService donationService) {
        this.donorService = donorService;
        this.donationService = donationService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(12, 12, 28));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ===================== HEADER =====================

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(12, 12, 28));

        JLabel title = new JLabel("👥 Donor Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(72, 199, 142));
        header.add(title, BorderLayout.WEST);

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(new Color(12, 12, 28));

        JLabel lbl = new JLabel("Search by ID:");
        lbl.setForeground(new Color(180, 180, 210));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtSearchId = new JTextField(8);
        styleField(txtSearchId);

        JButton btnSearch = actionButton("Search", new Color(14, 165, 233));
        btnSearch.addActionListener(e -> searchDonor());

        JButton btnClear = actionButton("Show All", new Color(100, 100, 140));
        btnClear.addActionListener(e -> refreshTable());

        searchPanel.add(lbl);
        searchPanel.add(txtSearchId);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClear);

        header.add(searchPanel, BorderLayout.EAST);
        return header;
    }

    // ===================== CENTER (Form + Table) =====================

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

        // Row 0: Labels
        addLabel(form, "Name *", 0, 0, gbc);
        addLabel(form, "Contact (10 digits) *", 1, 0, gbc);
        addLabel(form, "Email *", 2, 0, gbc);

        // Row 1: Fields
        txtName    = new JTextField(14); styleField(txtName);
        txtContact = new JTextField(14); styleField(txtContact);
        txtEmail   = new JTextField(18); styleField(txtEmail);

        gbc.gridy = 1; gbc.gridx = 0; form.add(txtName, gbc);
        gbc.gridx = 1; form.add(txtContact, gbc);
        gbc.gridx = 2; form.add(txtEmail, gbc);

        // Row 2: Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(new Color(20, 20, 45));

        JButton btnAdd    = actionButton("➕ Add",    new Color(72, 199, 142));
        JButton btnUpdate = actionButton("✏️ Update", new Color(251, 189, 35));
        JButton btnDelete = actionButton("🗑 Delete", new Color(248, 87, 87));
        JButton btnReset  = actionButton("↩ Reset",  new Color(100, 100, 140));

        btnAdd.addActionListener(e    -> addDonor());
        btnUpdate.addActionListener(e -> updateDonor());
        btnDelete.addActionListener(e -> deleteDonor());
        btnReset.addActionListener(e  -> resetForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReset);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 3;
        form.add(btnPanel, gbc);

        return form;
    }

    // ---------- Results Table ----------

    private JScrollPane buildTable() {
        String[] cols = {"ID", "Name", "Contact", "Email"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        donorTable = new JTable(tableModel);
        donorTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        donorTable.setForeground(new Color(220, 220, 240));
        donorTable.setBackground(new Color(20, 20, 45));
        donorTable.setSelectionBackground(new Color(99, 102, 241));
        donorTable.setSelectionForeground(Color.WHITE);
        donorTable.setRowHeight(28);
        donorTable.setGridColor(new Color(40, 40, 70));
        donorTable.setShowGrid(true);
        donorTable.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = donorTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(30, 30, 65));
        header.setForeground(new Color(150, 150, 220));

        // Click row to populate form for editing
        donorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = donorTable.getSelectedRow();
                if (row >= 0) {
                    selectedDonorId = (int) tableModel.getValueAt(row, 0);
                    txtName.setText((String) tableModel.getValueAt(row, 1));
                    txtContact.setText((String) tableModel.getValueAt(row, 2));
                    txtEmail.setText((String) tableModel.getValueAt(row, 3));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(donorTable);
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

    private void addDonor() {
        if (!validateForm()) return;
        boolean ok = donorService.addDonor(txtName.getText(), txtContact.getText(), txtEmail.getText());
        if (ok) {
            setStatus("✅ Donor added successfully.", new Color(72, 199, 142));
            refreshTable();
            resetForm();
        } else {
            setStatus("❌ Failed to add donor. Check your inputs.", new Color(248, 87, 87));
        }
    }

    private void updateDonor() {
        if (selectedDonorId < 0) { setStatus("⚠️ Select a donor from the table first.", new Color(251, 189, 35)); return; }
        if (!validateForm()) return;
        boolean ok = donorService.updateDonor(selectedDonorId, txtName.getText(), txtContact.getText(), txtEmail.getText());
        if (ok) {
            setStatus("✅ Donor updated.", new Color(72, 199, 142));
            refreshTable();
            resetForm();
        } else {
            setStatus("❌ Update failed. Check your inputs.", new Color(248, 87, 87));
        }
    }

    private void deleteDonor() {
        if (selectedDonorId < 0) { setStatus("⚠️ Select a donor from the table first.", new Color(251, 189, 35)); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete donor ID " + selectedDonorId + "? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            donorService.deleteDonor(selectedDonorId);
            setStatus("🗑 Donor deleted.", new Color(248, 87, 87));
            refreshTable();
            resetForm();
        }
    }

    private void searchDonor() {
        String raw = txtSearchId.getText().trim();
        if (raw.isEmpty()) { setStatus("Enter a donor ID to search.", new Color(251, 189, 35)); return; }
        try {
            int id = Integer.parseInt(raw);
            Optional<Donor> found = donorService.getDonorById(id);
            tableModel.setRowCount(0);
            if (found.isPresent()) {
                Donor d = found.get();
                tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getContact(), d.getEmail()});
                setStatus("🔍 Donor found.", new Color(72, 199, 142));
            } else {
                setStatus("❌ No donor with ID " + id, new Color(248, 87, 87));
            }
        } catch (NumberFormatException ex) {
            setStatus("⚠️ Donor ID must be a number.", new Color(251, 189, 35));
        }
    }

    /** Reloads the table with all donors from the service. */
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Donor> list = donorService.getAllDonors();
        for (Donor d : list) {
            tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getContact(), d.getEmail()});
        }
        setStatus("Showing " + list.size() + " donor(s).", new Color(120, 120, 160));
    }

    private void resetForm() {
        txtName.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtSearchId.setText("");
        selectedDonorId = -1;
        donorTable.clearSelection();
    }

    // ===================== VALIDATION =====================

    /** Returns true only if all visible form fields pass basic checks. */
    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            setStatus("⚠️ Name is required.", new Color(251, 189, 35)); return false;
        }
        if (!txtContact.getText().trim().matches("\\d{10}")) {
            setStatus("⚠️ Contact must be exactly 10 digits.", new Color(251, 189, 35)); return false;
        }
        if (!txtEmail.getText().contains("@")) {
            setStatus("⚠️ Enter a valid email address.", new Color(251, 189, 35)); return false;
        }
        return true;
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
        btn.setForeground(Color.WHITE);
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
