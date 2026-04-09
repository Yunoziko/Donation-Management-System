package gui;

import model.Donation;
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

/**
 * ReportPanel - Reports & Analytics UI
 * Features:
 *  - Overall summary cards (total donors, donations, amount)
 *  - Full donation list with totals
 *  - Donations-by-donor lookup report
 */
public class ReportPanel extends JPanel {

    private DonorService donorService;
    private DonationService donationService;

    // Summary labels
    private JLabel lblTotalDonors, lblTotalDonations, lblTotalAmount;

    // Per-donor report
    private JTextField txtDonorIdReport;
    private JLabel lblDonorName, lblDonorTotal;
    private DefaultTableModel donorReportModel;

    // All-donations table
    private DefaultTableModel allDonationsModel;

    public ReportPanel(DonorService donorService, DonationService donationService) {
        this.donorService    = donorService;
        this.donationService = donationService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(12, 12, 28));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(),      BorderLayout.NORTH);
        add(buildContent(),     BorderLayout.CENTER);
    }

    // ===================== HEADER =====================

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(12, 12, 28));

        JLabel title = new JLabel("📊 Reports & Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(167, 105, 255));
        header.add(title, BorderLayout.WEST);

        JButton btnRefresh = actionButton("🔄 Refresh All", new Color(99, 102, 241));
        btnRefresh.addActionListener(e -> refreshReport());
        header.add(btnRefresh, BorderLayout.EAST);

        return header;
    }

    // ===================== MAIN CONTENT =====================

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(new Color(12, 12, 28));

        // Top: Summary cards + Per-donor report (side by side)
        JPanel topRow = new JPanel(new GridLayout(1, 2, 15, 0));
        topRow.setBackground(new Color(12, 12, 28));
        topRow.add(buildSummaryCards());
        topRow.add(buildDonorReport());

        content.add(topRow,                      BorderLayout.NORTH);
        content.add(buildAllDonationsTable(),    BorderLayout.CENTER);

        return content;
    }

    // ---------- Summary Cards ----------

    private JPanel buildSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 10));
        panel.setBackground(new Color(12, 12, 28));

        lblTotalDonors    = new JLabel("—");
        lblTotalDonations = new JLabel("—");
        lblTotalAmount    = new JLabel("—");

        panel.add(buildCard("👥 Total Donors",    lblTotalDonors,    new Color(72, 199, 142)));
        panel.add(buildCard("💰 Total Donations", lblTotalDonations, new Color(251, 189, 35)));
        panel.add(buildCard("💵 Total Amount",    lblTotalAmount,    new Color(167, 105, 255)));

        return panel;
    }

    private JPanel buildCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(new Color(20, 20, 45));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent.darker().darker(), 1),
                new EmptyBorder(14, 18, 14, 18)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(150, 150, 200));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(accent);

        card.add(lblTitle,  BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ---------- Per-Donor Report ----------

    private JPanel buildDonorReport() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(new Color(20, 20, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 90), 1),
                new EmptyBorder(14, 18, 14, 18)));

        JLabel title = new JLabel("🔍 Donations by Donor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(new Color(167, 105, 255));
        panel.add(title, BorderLayout.NORTH);

        // Search row
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchRow.setBackground(new Color(20, 20, 45));

        JLabel lbl = new JLabel("Donor ID:");
        lbl.setForeground(new Color(160, 160, 200));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtDonorIdReport = new JTextField(8);
        styleField(txtDonorIdReport);

        JButton btnSearch = actionButton("Search", new Color(167, 105, 255));
        btnSearch.addActionListener(e -> runDonorReport());

        searchRow.add(lbl);
        searchRow.add(txtDonorIdReport);
        searchRow.add(btnSearch);
        panel.add(searchRow, BorderLayout.CENTER);

        // Result area
        JPanel resultArea = new JPanel(new GridLayout(3, 1, 0, 4));
        resultArea.setBackground(new Color(20, 20, 45));

        lblDonorName  = resultLabel("Donor: —");
        lblDonorTotal = resultLabel("Total donated: —");

        String[] cols = {"Don. ID", "Amount (₹)", "Date", "Purpose"};
        donorReportModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable donorTable = styledTable(donorReportModel);
        JScrollPane scroll = new JScrollPane(donorTable);
        scroll.getViewport().setBackground(new Color(20, 20, 45));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 90)));
        scroll.setPreferredSize(new Dimension(0, 100));

        resultArea.add(lblDonorName);
        resultArea.add(lblDonorTotal);
        resultArea.add(scroll);

        panel.add(resultArea, BorderLayout.SOUTH);

        return panel;
    }

    // ---------- All Donations Table ----------

    private JPanel buildAllDonationsTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(new Color(12, 12, 28));

        JLabel title = new JLabel("📋 All Donations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(new Color(251, 189, 35));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"Don. ID", "Donor ID", "Donor Name", "Amount (₹)", "Date", "Purpose"};
        allDonationsModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable allTable = styledTable(allDonationsModel);
        JScrollPane scroll = new JScrollPane(allTable);
        scroll.getViewport().setBackground(new Color(20, 20, 45));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 90)));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ===================== ACTIONS =====================

    private void runDonorReport() {
        String raw = txtDonorIdReport.getText().trim();
        if (raw.isEmpty()) return;
        try {
            int id = Integer.parseInt(raw);
            Donor donor = donorService.getDonorById(id).orElse(null);

            donorReportModel.setRowCount(0);

            if (donor == null) {
                lblDonorName.setText("Donor: Not found");
                lblDonorTotal.setText("Total donated: —");
                return;
            }

            lblDonorName.setText("Donor: " + donor.getName() + " (ID " + id + ")");
            List<Donation> list = donationService.getDonationsByDonor(id);
            for (Donation d : list) {
                donorReportModel.addRow(new Object[]{
                    d.getDonationId(),
                    String.format("₹%.2f", d.getAmount()),
                    d.getDate(),
                    d.getPurpose()
                });
            }

            double total = donationService.getTotalByDonor(id);
            lblDonorTotal.setText("Total donated: ₹" + String.format("%.2f", total) +
                                  "  |  " + list.size() + " donation(s)");

        } catch (NumberFormatException ex) {
            lblDonorName.setText("⚠️ Enter a numeric ID.");
        }
    }

    /** Refreshes all data on this panel — called when the panel becomes visible. */
    public void refreshReport() {
        lblTotalDonors.setText(String.valueOf(donorService.getDonorCount()));
        lblTotalDonations.setText(String.valueOf(donationService.getDonationCount()));
        lblTotalAmount.setText("₹" + String.format("%.2f", donationService.getTotalDonationAmount()));

        // Reload all-donations table
        allDonationsModel.setRowCount(0);
        for (Donation d : donationService.getAllDonations()) {
            String name = donorService.getDonorById(d.getDonorId())
                    .map(Donor::getName).orElse("Unknown");
            allDonationsModel.addRow(new Object[]{
                d.getDonationId(), d.getDonorId(), name,
                String.format("₹%.2f", d.getAmount()), d.getDate(), d.getPurpose()
            });
        }
    }

    // ===================== HELPERS =====================

    private JLabel resultLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(200, 200, 230));
        return lbl;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setForeground(new Color(220, 220, 240));
        t.setBackground(new Color(20, 20, 45));
        t.setSelectionBackground(new Color(99, 102, 241));
        t.setSelectionForeground(Color.WHITE);
        t.setRowHeight(26);
        t.setGridColor(new Color(40, 40, 70));

        JTableHeader hdr = t.getTableHeader();
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hdr.setBackground(new Color(30, 30, 65));
        hdr.setForeground(new Color(150, 150, 220));
        return t;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(new Color(30, 30, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 120), 1),
                new EmptyBorder(4, 8, 4, 8)));
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
}
