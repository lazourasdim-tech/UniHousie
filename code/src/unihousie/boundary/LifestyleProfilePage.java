package unihousie.boundary;

import unihousie.controller.ProfileController;
import unihousie.entity.LifestyleProfile;
import unihousie.entity.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LifestyleProfilePage extends JFrame {

    private final Student student;
    private final ProfileController controller;

    // UC02: σταθερές (fixed) τιμές ώστε ο jaccard να συγκρίνει όμοια tokens
    private static final String[] HABIT_OPTIONS    = { "non-smoker", "smoker", "pets-ok", "no-pets", "quiet", "social" };
    private static final String[] SCHEDULE_OPTIONS = { "morning-person", "night-owl", "flexible" };

    private JCheckBox[] habitChecks;
    private JCheckBox[] scheduleChecks;
    private JTextField budgetField;
    private JTextArea  notesArea;
    private JLabel     statusLabel;

    public LifestyleProfilePage(Student student) {
        super("UC02 — Lifestyle Profile");
        if (student == null) {
            throw new IllegalArgumentException("Πρέπει να επιλεγεί φοιτητής για το UC02.");
        }
        this.student = student;
        this.controller = new ProfileController(this);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 540);
        setLocationRelativeTo(null);
        buildUI();
    }

    public LifestyleProfilePage() {
        this(unihousie.mock.DataStore.students.get(0));
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel header = new JPanel(new GridLayout(0, 1, 0, 4));
        JLabel title = new JLabel("UC02 — Συμπλήρωση Lifestyle Profile");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        header.add(title);
        JLabel sub = new JLabel("Φοιτητής: " + student.getFullName() + "  (" + student.getDepartment() + ")");
        header.add(sub);
        statusLabel = new JLabel();
        header.add(statusLabel);
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        int row = 0;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0; gc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Συνήθειες:"), gc);
        gc.gridx = 1; gc.weightx = 1.0; gc.anchor = GridBagConstraints.WEST;
        JPanel habitsPanel = new JPanel(new GridLayout(0, 3, 8, 2));
        habitChecks = new JCheckBox[HABIT_OPTIONS.length];
        for (int i = 0; i < HABIT_OPTIONS.length; i++) {
            habitChecks[i] = new JCheckBox(HABIT_OPTIONS[i]);
            habitsPanel.add(habitChecks[i]);
        }
        form.add(habitsPanel, gc);

        row++;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        form.add(new JLabel("Πρόγραμμα:"), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        JPanel schedulePanel = new JPanel(new GridLayout(0, 3, 8, 2));
        scheduleChecks = new JCheckBox[SCHEDULE_OPTIONS.length];
        for (int i = 0; i < SCHEDULE_OPTIONS.length; i++) {
            scheduleChecks[i] = new JCheckBox(SCHEDULE_OPTIONS[i]);
            schedulePanel.add(scheduleChecks[i]);
        }
        form.add(schedulePanel, gc);

        row++;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0; form.add(new JLabel("Budget (€):"), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        budgetField = new JTextField();
        form.add(budgetField, gc);

        row++;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0; gc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Σημειώσεις:"), gc);
        gc.gridx = 1; gc.weightx = 1.0; gc.fill = GridBagConstraints.BOTH; gc.weighty = 1.0;
        notesArea = new JTextArea(4, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        form.add(new JScrollPane(notesArea), gc);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Αποθήκευση Προφίλ");
        saveBtn.addActionListener(e -> onSaveClicked());
        actions.add(saveBtn);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);

        LifestyleProfile data = controller.getProfileData(student.getUserId());
        renderForm(data);
    }

    private void onSaveClicked() {

        String habits   = joinSelected(habitChecks);
        String schedule = joinSelected(scheduleChecks);
        String notes    = notesArea.getText().trim();
        double budget;
        try {
            budget = Double.parseDouble(budgetField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Το budget πρέπει να είναι αριθμός (π.χ. 300).",
                    "UC02 — Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (habits.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Επίλεξε τουλάχιστον μία συνήθεια.",
                    "UC02 — Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            fillPreferences(habits, schedule, budget, notes);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "UC02 — Validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Ενώνει τις επιλεγμένες σταθερές τιμές σε comma-joined String (συμβατό με LifestyleProfile.habits : String). */
    private static String joinSelected(JCheckBox[] boxes) {
        StringBuilder sb = new StringBuilder();
        for (JCheckBox b : boxes) {
            if (b.isSelected()) {
                if (sb.length() > 0) sb.append(",");
                sb.append(b.getText());
            }
        }
        return sb.toString();
    }

    public void display() {
        setVisible(true);
    }

    public void openLifestyleForm() {
        if (!isVisible()) setVisible(true);
        toFront();
        requestFocus();
    }

    public void fillPreferences(String habits, String schedule, double budget, String notes) {

        controller.saveProfile(student.getUserId(), habits, schedule, budget, notes);
    }

    public void renderForm(LifestyleProfile profileData) {
        if (profileData == null) {
            statusLabel.setText("Κατάσταση Προφίλ: ΝΕΟ");
            return;
        }
        applySelection(habitChecks, profileData.getHabits());
        applySelection(scheduleChecks, profileData.getSchedule());
        budgetField.setText(profileData.getBudget() > 0 ? String.valueOf(profileData.getBudget()) : "");
        notesArea.setText(profileData.getNotes() == null ? "" : profileData.getNotes());
        statusLabel.setText("Κατάσταση Προφίλ: " + (profileData.isCompleted() ? "ΟΛΟΚΛΗΡΩΜΕΝΟ" : "ΝΕΟ"));
    }

    /** Τσεκάρει τα κουτιά που αντιστοιχούν στις αποθηκευμένες (comma-joined) τιμές. */
    private static void applySelection(JCheckBox[] boxes, String stored) {
        java.util.Set<String> selected = new java.util.HashSet<>();
        if (stored != null) {
            for (String t : stored.split(",")) selected.add(t.trim().toLowerCase());
        }
        for (JCheckBox b : boxes) {
            b.setSelected(selected.contains(b.getText().toLowerCase()));
        }
    }

    public void showSuccessToast() {
        statusLabel.setText("Κατάσταση Προφίλ: ΟΛΟΚΛΗΡΩΜΕΝΟ");
        JOptionPane.showMessageDialog(this,
                "Το προφίλ αποθηκεύτηκε επιτυχώς.\nΜπορείς τώρα να περιηγηθείς σε συγκατοίκους (UC03).",
                "UC02", JOptionPane.INFORMATION_MESSAGE);
    }
}
