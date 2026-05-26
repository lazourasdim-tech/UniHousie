package unihousie.boundary;

import unihousie.controller.AdminReportController;
import unihousie.entity.HousingListing;
import unihousie.entity.Report;
import unihousie.entity.User;
import unihousie.mock.DataStore;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AdminReportPage extends JFrame {

    private final AdminReportController controller = new AdminReportController();

    private JList<Report> reportsList;
    private DefaultListModel<Report> reportsModel;
    private JTextArea detailsArea;
    private JButton dismissBtn, warningBtn, suspendBtn, deleteBtn;

    public AdminReportPage() {
        super("UC09 — Ουρά Καταγγελιών");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        buildUI();
        loadPendingReports();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel title = new JLabel("UC09 — Επεξεργασία Καταγγελιών");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        root.add(title, BorderLayout.NORTH);

        reportsModel = new DefaultListModel<>();
        reportsList = new JList<>(reportsModel);
        reportsList.setCellRenderer((lst, value, idx, sel, foc) -> {
            JLabel l = new JLabel(formatReportSummary(value));
            l.setOpaque(true);
            if (sel) {
                l.setBackground(lst.getSelectionBackground());
                l.setForeground(lst.getSelectionForeground());
            }
            l.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
            return l;
        });
        reportsList.addListSelectionListener(e -> renderReportDetails());
        JScrollPane listScroll = new JScrollPane(reportsList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Καταγγελίες σε αναμονή"));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Λεπτομέρειες"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detailsScroll);
        split.setResizeWeight(0.4);
        root.add(split, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dismissBtn = new JButton("Απόρριψη (Dismiss)");
        warningBtn = new JButton("⚠ Warning");
        suspendBtn = new JButton("🚫 Suspend User");
        deleteBtn  = new JButton("🗑 Delete Listing");

        dismissBtn.addActionListener(e -> resolve(AdminReportController.ACTION_DISMISS));
        warningBtn.addActionListener(e -> resolve(AdminReportController.ACTION_WARNING));
        suspendBtn.addActionListener(e -> resolve(AdminReportController.ACTION_SUSPEND));
        deleteBtn.addActionListener(e -> resolve(AdminReportController.ACTION_DELETE));

        actions.add(dismissBtn);
        actions.add(warningBtn);
        actions.add(suspendBtn);
        actions.add(deleteBtn);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void loadPendingReports() {
        reportsModel.clear();
        List<Report> pending = controller.getPendingReports();
        for (Report r : pending) reportsModel.addElement(r);

        if (pending.isEmpty()) {
            detailsArea.setText("Δεν υπάρχουν εκκρεμείς καταγγελίες.");
        } else {
            reportsList.setSelectedIndex(0);
        }
    }

    private void renderReportDetails() {
        Report r = reportsList.getSelectedValue();
        if (r == null) {
            detailsArea.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Report ID: ").append(r.getReportId()).append("\n\n");
        sb.append("Καταγγέλλων: ").append(describeUser(r.getReporterId())).append("\n");
        sb.append("Καταγγελλόμενος: ").append(describeTarget(r.getReportedEntityId(), r.getTargetType())).append("\n");
        sb.append("Τύπος target: ").append(r.getTargetType()).append("\n");
        sb.append("Αιτία: ").append(r.getReasonCode()).append("\n\n");
        sb.append("Περιγραφή:\n").append(r.getText() == null || r.getText().isEmpty() ? "—" : r.getText());
        sb.append("\n\nΗμερομηνία: ").append(r.getCreatedAt());
        sb.append("\nΚατάσταση: ").append(r.getStatus());
        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);
    }

    private void resolve(String action) {
        Report r = reportsList.getSelectedValue();
        if (r == null) {
            JOptionPane.showMessageDialog(this, "Επιλέξτε καταγγελία πρώτα.",
                    "Προσοχή", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (AdminReportController.ACTION_DELETE.equals(action) ||
                AdminReportController.ACTION_SUSPEND.equals(action)) {
            int conf = JOptionPane.showConfirmDialog(this,
                    "Επιβεβαιώνετε την ενέργεια: " + action + ";",
                    "Επιβεβαίωση", JOptionPane.YES_NO_OPTION);
            if (conf != JOptionPane.YES_OPTION) return;
        }

        try {
            controller.processResolution(r.getReportId(), action);
            showSuccessMessage();
            loadPendingReports();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatReportSummary(Report r) {
        String targetName = describeTarget(r.getReportedEntityId(), r.getTargetType());
        return "[" + r.getReasonCode() + "] " + targetName + " (από " + r.getReporterId() + ")";
    }

    private String describeUser(String userId) {
        User u = DataStore.findUser(userId);
        return u != null ? u.getFullName() + " (" + userId + ")" : userId;
    }

    private String describeTarget(String entityId, String targetType) {
        if (Report.TARGET_STUDENT.equals(targetType)) {
            User u = DataStore.findUser(entityId);
            return u != null ? u.getFullName() + " [STUDENT]" : entityId;
        } else if (Report.TARGET_LISTING.equals(targetType)) {
            HousingListing l = DataStore.findListing(entityId);
            return l != null ? l.getTitle() + " [LISTING]" : entityId;
        }
        return entityId;
    }

    public void display() {
        setVisible(true);
    }

    public void showSuccessMessage() {
        JOptionPane.showMessageDialog(this, "Η καταγγελία επιλύθηκε.",
                "UC09 — Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
    }
}