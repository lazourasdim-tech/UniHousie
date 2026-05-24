package unihousie.boundary;

import javax.swing.*;

public class AdminApprovalPage extends JFrame {

    public AdminApprovalPage() {
        super("UC06 — Listing Approval Queue");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(840, 600);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void openApprovalQueue() {

    }

    public void selectListing(String listingId) {

    }

    public void approveListing(String listingId) {

    }

    public void rejectListing(String listingId, String rejectionReason) {

    }

    public void showSuccessMessage() {
        JOptionPane.showMessageDialog(this, "Η αγγελία εγκρίθηκε.",
                "UC06", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showRejectionMessage() {
        JOptionPane.showMessageDialog(this, "Η αγγελία απορρίφθηκε.",
                "UC06", JOptionPane.INFORMATION_MESSAGE);
    }
}
