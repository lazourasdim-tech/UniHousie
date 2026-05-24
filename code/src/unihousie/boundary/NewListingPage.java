package unihousie.boundary;

import javax.swing.*;
import java.util.List;

public class NewListingPage extends JFrame {

    public NewListingPage() {
        super("UC06 — New Listing");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 600);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void enterListingData(String title, String address, double price, String description,
                                  List<String> amenities, List<String> photos) {

    }

    public void showValidationError(List<String> errors) {

    }

    public void showPendingApprovalScreen() {

    }
}
