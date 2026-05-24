package unihousie.boundary;

import javax.swing.*;

public class ReportUserForm extends JFrame {

    public ReportUserForm() {
        super("UC09 — Report a User");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 460);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void openReportForm(String reportedEntityId) {

    }

    public void fillReasonAndComment(String reasonCode, String text) {

    }

    public void showConfirmationDialog() {
        JOptionPane.showMessageDialog(this, "Η καταγγελία υποβλήθηκε. Θα εξεταστεί από διαχειριστή.",
                "UC09", JOptionPane.INFORMATION_MESSAGE);
    }
}
