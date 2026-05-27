package unihousie.mock;

import javax.swing.*;

public class MockLandlordNotifier {

    public boolean notifyNewVisitRequest(String landlordId, String visitId,
                                         String listingTitle,
                                         String dateStr, String timeStr) {
        String console = "[MockLandlordNotifier] 🏠 Νέο αίτημα επίσκεψης" +
                "\n  → Landlord: " + landlordId +
                "\n  → Visit ID: " + visitId +
                "\n  → Listing:  " + listingTitle +
                "\n  → Date:     " + dateStr + "  " + timeStr;
        System.out.println(console);

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    null,
                    "<html><b>🏠 Νέο αίτημα επίσκεψης</b><br><br>"
                            + "Προς ιδιοκτήτη: <b>" + landlordId + "</b><br>"
                            + "Αγγελία: <i>" + listingTitle + "</i><br>"
                            + "Ημερομηνία: <b>" + dateStr + "</b><br>"
                            + "Ώρα: <b>" + timeStr + "</b><br><br>"
                            + "<i>Αναμένεται επιβεβαίωση.</i></html>",
                    "Mock Landlord Notification",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        return true;
    }
}