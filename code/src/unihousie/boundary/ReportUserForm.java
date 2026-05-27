package unihousie.boundary;

import unihousie.Session;
import unihousie.controller.ModerationController;
import unihousie.entity.Report;
import unihousie.mock.DataStore;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReportUserForm extends JFrame {

    private final ModerationController controller = new ModerationController();
    private final String reporterId;
    private final String reportedEntityId;
    private final String targetType;

    private JComboBox<String> reasonCombo;
    private JTextArea descriptionArea;
    private JLabel headerLabel;

    public ReportUserForm(String reporterId, String reportedEntityId, String targetType) {
        super("UC09 — Καταγγελία");
        if (reportedEntityId == null || targetType == null) {
            throw new IllegalArgumentException("Απαιτείται target για την καταγγελία.");
        }
        this.reporterId = reporterId != null ? reporterId : Session.getCurrentUserId();
        this.reportedEntityId = reportedEntityId;
        this.targetType = targetType;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 460);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(new EmptyBorder(16, 20, 16, 20));

        headerLabel = new JLabel();
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 16f));
        renderTargetInfo();
        root.add(headerLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 6, 8, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        int row = 0;

        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(new JLabel("Αιτία καταγγελίας:"), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        reasonCombo = new JComboBox<>(new String[]{
                "-- Επιλέξτε αιτία --",
                Report.REASON_INAPPROPRIATE + " (Ακατάλληλο περιεχόμενο)",
                Report.REASON_SPAM + " (Spam / Διαφήμιση)",
                Report.REASON_FRAUD + " (Απάτη)",
                Report.REASON_OTHER + " (Άλλο)"
        });
        form.add(reasonCombo, gc);

        row++;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0; gc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Περιγραφή (προαιρετική):"), gc);
        gc.gridx = 1; gc.weightx = 1.0; gc.fill = GridBagConstraints.BOTH; gc.weighty = 1.0;
        descriptionArea = new JTextArea(6, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        form.add(new JScrollPane(descriptionArea), gc);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("🚩 Υποβολή Καταγγελίας");
        submitBtn.setFont(submitBtn.getFont().deriveFont(Font.BOLD));
        submitBtn.addActionListener(e -> onSubmitClicked());
        JButton cancelBtn = new JButton("Ακύρωση");
        cancelBtn.addActionListener(e -> dispose());
        actions.add(cancelBtn);
        actions.add(submitBtn);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void renderTargetInfo() {
        String name;
        if (Report.TARGET_STUDENT.equals(targetType)) {
            unihousie.entity.User u = DataStore.findUser(reportedEntityId);
            name = (u != null ? u.getFullName() : reportedEntityId);
            headerLabel.setText("<html>Καταγγελία χρήστη: <b>" + escape(name) + "</b></html>");
        } else if (Report.TARGET_LISTING.equals(targetType)) {
            unihousie.entity.HousingListing l = DataStore.findListing(reportedEntityId);
            name = (l != null ? l.getTitle() : reportedEntityId);
            headerLabel.setText("<html>Καταγγελία αγγελίας: <b>" + escape(name) + "</b></html>");
        } else {
            headerLabel.setText("Καταγγελία: " + reportedEntityId);
        }
    }

    private void onSubmitClicked() {
        int idx = reasonCombo.getSelectedIndex();
        if (idx <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Παρακαλώ επιλέξτε αιτία καταγγελίας.",
                    "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String reasonCode;
        switch (idx) {
            case 1: reasonCode = Report.REASON_INAPPROPRIATE; break;
            case 2: reasonCode = Report.REASON_SPAM; break;
            case 3: reasonCode = Report.REASON_FRAUD; break;
            case 4: reasonCode = Report.REASON_OTHER; break;
            default: return;
        }

        String text = descriptionArea.getText().trim();

        try {
            controller.submitReport(reporterId, reportedEntityId, targetType, reasonCode, text);
            showConfirmationDialog();
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Σφάλμα Υποβολής", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void display() {
        setVisible(true);
    }

    public void showConfirmationDialog() {
        JOptionPane.showMessageDialog(this,
                "Η καταγγελία υποβλήθηκε.\nΘα εξεταστεί από διαχειριστή.",
                "UC09 — Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}