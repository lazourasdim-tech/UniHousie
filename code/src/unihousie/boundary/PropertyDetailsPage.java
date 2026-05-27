package unihousie.boundary;

import unihousie.Session;
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
        setSize(820, 660);
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
                        "Ενοίκιο: " + listing.getRent() + " €/μήνα\n" +
                        "Μέσος όρος βαθμολογίας: " + String.format("%.2f", listing.getAverageRating()) + " / 5\n\n" +
                        "Περιγραφή:\n" + listing.getDescription()
        );

        JButton interestBtn = new JButton("Εκδήλωση Ενδιαφέροντος");
        interestBtn.setFont(new Font("Arial", Font.BOLD, 14));
        interestBtn.addActionListener(e -> clickExpressInterest());

        JButton scheduleBtn = new JButton("📅 Προγραμματισμός Επίσκεψης");
        scheduleBtn.setFont(new Font("Arial", Font.BOLD, 14));
        scheduleBtn.addActionListener(e -> {
            new ScheduleVisitPage(listing, Session.getCurrentUserId()).setVisible(true);
        });

        JButton reviewBtn = new JButton("⭐ Αξιολόγηση");
        reviewBtn.setFont(new Font("Arial", Font.BOLD, 14));
        reviewBtn.addActionListener(e -> {
            new ReviewPage(listing, Session.getCurrentUserId()).setVisible(true);
        });

        JButton reportBtn = new JButton("🚩 Καταγγελία Αγγελίας");
        reportBtn.addActionListener(e -> {
            new ReportUserForm(
                    Session.getCurrentUserId(),
                    listing.getListingId(),
                    unihousie.entity.Report.TARGET_LISTING
            ).setVisible(true);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottom.add(interestBtn);
        bottom.add(scheduleBtn);
        bottom.add(reviewBtn);
        bottom.add(reportBtn);

        add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void display() {
        setVisible(true);
    }

    public void clickExpressInterest() {
        interestController.registerInterest(Session.getCurrentUserId(), listing.getListingId());
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