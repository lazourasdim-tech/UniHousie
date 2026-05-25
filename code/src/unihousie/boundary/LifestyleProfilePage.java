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

    private JTextField habitsField;
    private JTextField scheduleField;
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
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0; form.add(new JLabel("Συνήθειες (comma-separated):"), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        habitsField = new JTextField();
        form.add(habitsField, gc);
        row++;
        gc.gridx = 1; gc.gridy = row;
        JLabel habitsHint = new JLabel("π.χ. non-smoker, pets ok, quiet");
        habitsHint.setForeground(Color.GRAY);
        form.add(habitsHint, gc);

        row++;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0; form.add(new JLabel("Πρόγραμμα:"), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        scheduleField = new JTextField();
        form.add(scheduleField, gc);
        row++;
        gc.gridx = 1; gc.gridy = row;
        JLabel scheduleHint = new JLabel("π.χ. morning person / night owl / flexible");
        scheduleHint.setForeground(Color.GRAY);
        form.add(scheduleHint, gc);

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

        String habits   = habitsField.getText().trim();
        String schedule = scheduleField.getText().trim();
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
                    "Συμπλήρωσε τουλάχιστον μία συνήθεια (comma-separated).",
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
        habitsField.setText(profileData.getHabits() == null ? "" : profileData.getHabits());
        scheduleField.setText(profileData.getSchedule() == null ? "" : profileData.getSchedule());
        budgetField.setText(profileData.getBudget() > 0 ? String.valueOf(profileData.getBudget()) : "");
        notesArea.setText(profileData.getNotes() == null ? "" : profileData.getNotes());
        statusLabel.setText("Κατάσταση Προφίλ: " + (profileData.isCompleted() ? "ΟΛΟΚΛΗΡΩΜΕΝΟ" : "ΝΕΟ"));
    }

    public void showSuccessToast() {
        statusLabel.setText("Κατάσταση Προφίλ: ΟΛΟΚΛΗΡΩΜΕΝΟ");
        JOptionPane.showMessageDialog(this,
                "Το προφίλ αποθηκεύτηκε επιτυχώς.\nΜπορείς τώρα να περιηγηθείς σε συγκατοίκους (UC03).",
                "UC02", JOptionPane.INFORMATION_MESSAGE);
    }
}
