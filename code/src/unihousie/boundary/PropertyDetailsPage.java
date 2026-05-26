package unihousie.boundary;

import unihousie.controller.InterestController;
import unihousie.entity.ContactInfo;
import unihousie.entity.HousingListing;
import javax.swing.*;
import java.awt.*;

public class PropertyDetailsPage extends JFrame {

    private final InterestController interestController = new InterestController();
    private final HousingListing listing;

    public PropertyDetailsPage(HousingListing listing) {
        super("UC08 — Property Details: " + listing.getTitle());
        this.listing = listing;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(780, 620);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setMargin(new Insets(15, 15, 15, 15));
        detailsArea.setText(
                "Τίτλος: " + listing.getTitle() + "\n\n" +
                        "Διεύθυνση: " + listing.getAddress() + "\n" +
                        "Τύπος: " + listing.getType() + " • " + listing.getRooms() + " δωμάτια\n" +
                        "Εμβαδόν: " + listing.getSqm() + " τ.μ.\n" +
                        "Ενοίκιο: " + listing.getRent() + " €/μήνα\n\n" +
                        "Περιγραφή:\n" + listing.getDescription()
        );

        JButton interestBtn = new JButton("Εκδήλωση Ενδιαφέροντος");
        interestBtn.setFont(new Font("Arial", Font.BOLD, 16));
        interestBtn.addActionListener(e -> clickExpressInterest());

        JButton reportBtn = new JButton("🚩 Καταγγελία Αγγελίας");
        reportBtn.addActionListener(e -> {
            new ReportUserForm(
                    unihousie.Session.getCurrentUserId(),
                    listing.getListingId(),
                    unihousie.entity.Report.TARGET_LISTING
            ).setVisible(true);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.add(interestBtn);
        bottom.add(reportBtn);

        add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void display() {
        setVisible(true);
    }

    public void clickExpressInterest() {
        interestController.registerInterest("stud_1", listing.getListingId());
        showLandlordContactDetails(new ContactInfo(
                listing.getLandlordId(),
                "Ιδιοκτήτης",
                "landlord@example.com",
                "6912345678"
        ));
    }

    public void showLandlordContactDetails(ContactInfo contactInfo) {
        JOptionPane.showMessageDialog(this,
                "✅ Ενδιαφέρον εκδηλώθηκε επιτυχώς!\n\n" +
                        "Στοιχεία Ιδιοκτήτη:\n" +
                        "Όνομα: " + contactInfo.getFullName() + "\n" +
                        "Email: " + contactInfo.getEmail() + "\n" +
                        "Τηλέφωνο: " + contactInfo.getPhone(),
                "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
    }
}