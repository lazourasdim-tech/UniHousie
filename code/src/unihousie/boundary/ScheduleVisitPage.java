package unihousie.boundary;

import unihousie.Session;
import unihousie.controller.VisitController;
import unihousie.entity.HousingListing;
import unihousie.entity.InterestExpression;
import unihousie.mock.DataStore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleVisitPage extends JFrame {

    private final HousingListing listing;
    private final String studentId;
    private final VisitController controller = new VisitController();

    private JTextField dateField;
    private JTextField timeField;
    private JLabel headerLabel;
    private JLabel statusLabel;

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy");

    public ScheduleVisitPage(HousingListing listing, String studentId) {
        super("UC10 — Προγραμματισμός Επίσκεψης");
        if (listing == null) {
            throw new IllegalArgumentException("Πρέπει να επιλεγεί ακίνητο για το UC10.");
        }
        this.listing = listing;
        this.studentId = studentId != null ? studentId : Session.getCurrentUserId();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(560, 460);
        setLocationRelativeTo(null);
        buildUI();
    }

    public ScheduleVisitPage() {
        this(DataStore.listings.get(0), Session.getCurrentUserId());
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(new EmptyBorder(18, 22, 18, 22));

        headerLabel = new JLabel();
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 16f));
        headerLabel.setText("<html>Προγραμματισμός επίσκεψης για:<br><b>" + escape(listing.getTitle()) + "</b><br>" +
                "<span style='color:#666;font-size:11px'>" + escape(listing.getAddress()) + " — " +
                listing.getRent() + " €/μήνα</span></html>");
        root.add(headerLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 6, 10, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        int row = 0;

        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(new JLabel("Ημερομηνία (dd/MM/yyyy):"), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        dateField = new JTextField();
        form.add(dateField, gc);

        row++;
        gc.gridx = 1; gc.gridy = row;
        JLabel dateHint = new JLabel("π.χ. 15/06/2026");
        dateHint.setForeground(Color.GRAY);
        form.add(dateHint, gc);

        row++;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(new JLabel("Ώρα (HH:mm):"), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        timeField = new JTextField();
        form.add(timeField, gc);

        row++;
        gc.gridx = 1; gc.gridy = row;
        JLabel timeHint = new JLabel("π.χ. 10:00, 14:30, 18:00");
        timeHint.setForeground(Color.GRAY);
        form.add(timeHint, gc);

        root.add(form, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setBorder(new EmptyBorder(4, 4, 4, 4));

        JPanel south = new JPanel(new BorderLayout());
        south.add(statusLabel, BorderLayout.NORTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Ακύρωση");
        cancelBtn.addActionListener(e -> dispose());
        JButton submitBtn = new JButton("📅 Υποβολή Αιτήματος Επίσκεψης");
        submitBtn.setFont(submitBtn.getFont().deriveFont(Font.BOLD));
        submitBtn.addActionListener(e -> onSubmitClicked());
        actions.add(cancelBtn);
        actions.add(submitBtn);
        south.add(actions, BorderLayout.SOUTH);

        root.add(south, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void onSubmitClicked() {
        String dateStr = dateField.getText().trim();
        String timeStr = timeField.getText().trim();

        if (dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ συμπληρώστε ημερομηνία.",
                    "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (timeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ συμπληρώστε ώρα.",
                    "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date date;
        try {
            DATE_FMT.setLenient(false);
            date = DATE_FMT.parse(dateStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Μη έγκυρη ημερομηνία. Χρησιμοποίησε τη μορφή dd/MM/yyyy (π.χ. 15/06/2026).",
                    "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!timeStr.matches("\\d{1,2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                    "Μη έγκυρη ώρα. Χρησιμοποίησε τη μορφή HH:mm (π.χ. 14:30).",
                    "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!hasExpressedInterest()) {
            int proceed = JOptionPane.showConfirmDialog(this,
                    "Δεν έχετε εκδηλώσει ενδιαφέρον για αυτή την αγγελία.\n" +
                            "Συνιστάται να εκδηλώσετε πρώτα ενδιαφέρον (UC08).\n\n" +
                            "Θέλετε να συνεχίσετε με τον προγραμματισμό επίσκεψης;",
                    "Προσοχή", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (proceed != JOptionPane.YES_OPTION) return;
        }

        try {
            String visitId = controller.createVisitRequest(
                    studentId, listing.getListingId(), date, timeStr);
            notifyBookingPending();
            showPendingConfirmationMessage();
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Σφάλμα Υποβολής", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean hasExpressedInterest() {
        for (InterestExpression i : DataStore.interests) {
            if (i.getSeekerId().equals(studentId) &&
                    i.getListingId().equals(listing.getListingId())) {
                return true;
            }
        }
        return false;
    }

    public void display() {
        setVisible(true);
    }

    public void selectDateTime(Date scheduledDate, String scheduledTime) {
        dateField.setText(DATE_FMT.format(scheduledDate));
        timeField.setText(scheduledTime);
    }

    public void notifyBookingPending() {
        statusLabel.setText("✓ Το αίτημα στάλθηκε στον ιδιοκτήτη.");
        statusLabel.setForeground(new Color(0x1d6e2c));
    }

    public void showPendingConfirmationMessage() {
        JOptionPane.showMessageDialog(this,
                "Η αίτηση επίσκεψης εστάλη.\nΑναμονή επιβεβαίωσης από τον ιδιοκτήτη.",
                "UC10 — Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}