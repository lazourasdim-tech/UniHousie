package unihousie.boundary;

import unihousie.Session;
import unihousie.controller.ReviewController;
import unihousie.entity.HousingListing;
import unihousie.mock.DataStore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReviewPage extends JFrame {

    private final HousingListing listing;
    private final String studentId;
    private final ReviewController controller = new ReviewController();

    private int selectedStars = 0;
    private JButton[] starButtons = new JButton[5];
    private JTextArea commentArea;
    private JLabel headerLabel;
    private JLabel starsLabel;

    public ReviewPage(HousingListing listing, String studentId) {
        super("UC11 — Αξιολόγηση Ακινήτου & Ιδιοκτήτη");
        if (listing == null) {
            throw new IllegalArgumentException("Πρέπει να επιλεγεί ακίνητο για το UC11.");
        }
        this.listing = listing;
        this.studentId = studentId != null ? studentId : Session.getCurrentUserId();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(560, 520);
        setLocationRelativeTo(null);
        buildUI();
    }

    public ReviewPage() {
        this(DataStore.listings.get(0), Session.getCurrentUserId());
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(18, 22, 18, 22));

        headerLabel = new JLabel();
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 16f));
        headerLabel.setText("<html>Αξιολόγηση για:<br><b>" + escape(listing.getTitle()) + "</b><br>" +
                "<span style='color:#666;font-size:11px'>" + escape(listing.getAddress()) + " — " +
                listing.getRent() + " €/μήνα</span></html>");
        root.add(headerLabel, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel starsTitle = new JLabel("Βαθμολογία (αστέρια):");
        starsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(starsTitle);
        center.add(Box.createVerticalStrut(6));

        JPanel starsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        starsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 5; i++) {
            final int starValue = i + 1;
            JButton btn = new JButton("☆");
            btn.setFont(new Font("Arial", Font.PLAIN, 28));
            btn.setMargin(new Insets(2, 8, 2, 8));
            btn.setFocusPainted(false);
            btn.addActionListener(e -> setStars(starValue));
            starButtons[i] = btn;
            starsRow.add(btn);
        }
        starsLabel = new JLabel(" — Δεν έχει επιλεγεί βαθμολογία");
        starsLabel.setForeground(Color.GRAY);
        starsRow.add(starsLabel);
        center.add(starsRow);

        center.add(Box.createVerticalStrut(16));

        JLabel commentTitle = new JLabel("Σχόλιο (προαιρετικό):");
        commentTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(commentTitle);
        center.add(Box.createVerticalStrut(6));

        commentArea = new JTextArea(6, 30);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(commentScroll);

        root.add(center, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Ακύρωση");
        cancelBtn.addActionListener(e -> dispose());
        JButton submitBtn = new JButton("⭐ Υποβολή Αξιολόγησης");
        submitBtn.setFont(submitBtn.getFont().deriveFont(Font.BOLD));
        submitBtn.addActionListener(e -> onSubmitClicked());
        actions.add(cancelBtn);
        actions.add(submitBtn);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void setStars(int stars) {
        this.selectedStars = stars;
        for (int i = 0; i < 5; i++) {
            if (i < stars) {
                starButtons[i].setText("★");
                starButtons[i].setForeground(new Color(0xE6B800));
            } else {
                starButtons[i].setText("☆");
                starButtons[i].setForeground(Color.DARK_GRAY);
            }
        }
        starsLabel.setText(" — " + stars + " / 5");
        starsLabel.setForeground(new Color(0x444444));
    }

    private void onSubmitClicked() {
        if (selectedStars < 1) {
            JOptionPane.showMessageDialog(this,
                    "Παρακαλώ επιλέξτε βαθμολογία (1-5 αστέρια).",
                    "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String text = commentArea.getText().trim();

        try {
            controller.postReview(studentId, listing.getListingId(), selectedStars, text);
            showSuccessMessage();
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Σφάλμα Υποβολής", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void display() {
        setVisible(true);
    }

    public void selectRatingAndWriteComment(int stars, String text) {
        setStars(stars);
        commentArea.setText(text == null ? "" : text);
    }

    public void showSuccessMessage() {
        JOptionPane.showMessageDialog(this,
                "Η αξιολόγησή σας υποβλήθηκε.\nΟ μέσος όρος βαθμολογίας ενημερώθηκε.",
                "UC11 — Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}