package unihousie.boundary;

import unihousie.entity.LifestyleProfile;

import javax.swing.*;

public class LifestyleProfilePage extends JFrame {

    public LifestyleProfilePage() {
        super("UC02 — Lifestyle Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void openLifestyleForm() {

    }

    public void fillPreferences(String habits, String schedule, double budget, String notes) {

    }

    public void renderForm(LifestyleProfile profileData) {

    }

    public void showSuccessToast() {
        JOptionPane.showMessageDialog(this, "Το προφίλ αποθηκεύτηκε επιτυχώς.",
                "UC02", JOptionPane.INFORMATION_MESSAGE);
    }
}
