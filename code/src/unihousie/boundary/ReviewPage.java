package unihousie.boundary;

import javax.swing.*;

public class ReviewPage extends JFrame {

    public ReviewPage() {
        super("UC11 — Submit Review");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 420);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void selectRatingAndWriteComment(int stars, String text) {

    }

    public void showSuccessMessage() {
        JOptionPane.showMessageDialog(this, "Η κριτική σας υποβλήθηκε.",
                "UC11", JOptionPane.INFORMATION_MESSAGE);
    }
}
