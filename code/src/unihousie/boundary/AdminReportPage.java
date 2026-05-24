package unihousie.boundary;

import javax.swing.*;

public class AdminReportPage extends JFrame {

    public AdminReportPage() {
        super("UC09 — Reports Queue");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(840, 600);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void openReportQueue() {

    }

    public void selectReport(String reportId) {

    }

    public void resolveReport(String reportId, String decisionAction) {

    }

    public void showSuccessMessage() {
        JOptionPane.showMessageDialog(this, "Η καταγγελία επιλύθηκε.",
                "UC09", JOptionPane.INFORMATION_MESSAGE);
    }
}
