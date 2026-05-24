package unihousie.boundary;

import javax.swing.*;
import java.util.Date;

public class ScheduleVisitPage extends JFrame {

    public ScheduleVisitPage() {
        super("UC10 — Schedule Visit");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 400);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void selectDateTime(Date scheduledDate, String scheduledTime) {

    }

    public void notifyBookingPending() {

    }

    public void showPendingConfirmationMessage() {
        JOptionPane.showMessageDialog(this, "Η αίτηση επίσκεψης εστάλη. Αναμονή επιβεβαίωσης από τον ιδιοκτήτη.",
                "UC10", JOptionPane.INFORMATION_MESSAGE);
    }
}
