package unihousie.boundary;

import unihousie.controller.MatchmakingController;
import unihousie.entity.LifestyleProfile;
import unihousie.entity.Student;
import unihousie.entity.UserSummary;
import unihousie.mock.DataStore;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowseRoommatesPage extends JFrame {

    private final Student student;
    private final MatchmakingController controller;

    private JTextField budgetMinField;
    private JTextField budgetMaxField;
    private JTextField smokeField;
    private JTextField habitsField;
    private JLabel statusLabel;
    private JPanel tilesPanel;

    public BrowseRoommatesPage(Student student) {
        super("UC03 — Browse Roommates");
        if (student == null) {
            throw new IllegalArgumentException("Πρέπει να επιλεγεί φοιτητής για το UC03.");
        }
        this.student = student;
        this.controller = new MatchmakingController();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 640);
        setLocationRelativeTo(null);
        buildUI();
        applyFilters(0.0, Double.MAX_VALUE, "", "");
    }

    public BrowseRoommatesPage() {
        this(DataStore.students.get(0));
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel header = new JPanel(new GridLayout(0, 1, 0, 3));
        JLabel title = new JLabel("UC03 — Περιήγηση Συγκατοίκων");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        header.add(title);
        JLabel sub = new JLabel("Αναζήτηση για: " + student.getFullName() + " (" + student.getDepartment() + ")");
        header.add(sub);
        root.add(header, BorderLayout.NORTH);

        JPanel filters = new JPanel(new GridBagLayout());
        filters.setBorder(BorderFactory.createTitledBorder("Φίλτρα"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0; gc.gridy = 0;
        filters.add(new JLabel("Budget από (€):"), gc);
        gc.gridx = 1; budgetMinField = new JTextField(6); filters.add(budgetMinField, gc);
        gc.gridx = 2; filters.add(new JLabel("έως (€):"), gc);
        gc.gridx = 3; budgetMaxField = new JTextField(6); filters.add(budgetMaxField, gc);
        gc.gridx = 4; filters.add(new JLabel("Προτίμηση Κάπνισμα:"), gc);
        gc.gridx = 5; smokeField = new JTextField(10); filters.add(smokeField, gc);
        gc.gridx = 6; filters.add(new JLabel("Επιπλέον Habits:"), gc);
        gc.gridx = 7; gc.weightx = 1.0; habitsField = new JTextField(16); filters.add(habitsField, gc);
        gc.gridx = 8; gc.weightx = 0;
        JButton searchBtn = new JButton("Αναζήτηση");
        searchBtn.addActionListener(e -> onSearchClicked());
        filters.add(searchBtn, gc);

        JPanel headerStack = new JPanel(new BorderLayout(0, 8));
        headerStack.add(header, BorderLayout.NORTH);
        headerStack.add(filters, BorderLayout.CENTER);
        statusLabel = new JLabel(" ");
        statusLabel.setBorder(new EmptyBorder(4, 4, 0, 4));
        headerStack.add(statusLabel, BorderLayout.SOUTH);
        root.add(headerStack, BorderLayout.NORTH);

        tilesPanel = new JPanel(new GridLayout(0, 2, 12, 12));
        tilesPanel.setBorder(new EmptyBorder(8, 4, 8, 4));
        JScrollPane scroll = new JScrollPane(tilesPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(BorderFactory.createTitledBorder("Συμβατοί Συγκάτοικοι"));
        root.add(scroll, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void onSearchClicked() {
        double bMin = parseOrDefault(budgetMinField.getText(), 0.0);
        double bMax = parseOrDefault(budgetMaxField.getText(), Double.MAX_VALUE);
        String smoke = smokeField.getText().trim();
        String habits = habitsField.getText().trim();
        applyFilters(bMin, bMax, smoke, habits);
    }

    private double parseOrDefault(String text, double fallback) {
        if (text == null) return fallback;
        String t = text.trim();
        if (t.isEmpty()) return fallback;
        try { return Double.parseDouble(t); } catch (NumberFormatException e) { return fallback; }
    }

    public void display() {
        setVisible(true);
    }

    public void applyFilters(double budgetMin, double budgetMax, String smokePreference, String habits) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("budgetMin", budgetMin);
        filters.put("budgetMax", budgetMax);
        if (smokePreference != null && !smokePreference.isEmpty()) filters.put("smokePreference", smokePreference);
        if (habits != null && !habits.isEmpty()) filters.put("habits", habits);

        List<LifestyleProfile> results = controller.searchCompatibleRoommates(student.getUserId(), filters);
        displayRoommatesList(results);
    }

    public void displayRoommatesList(List<LifestyleProfile> profiles) {
        tilesPanel.removeAll();
        LifestyleProfile own = student.getLifestyleProfile();
        if (own == null || !own.isCompleted()) {
            statusLabel.setText("Πρέπει πρώτα να συμπληρώσεις το Lifestyle Profile (UC02).");
            tilesPanel.add(emptyMessageTile("Δεν υπάρχει Lifestyle Profile", "Πήγαινε στο UC02 και συμπλήρωσε τις προτιμήσεις σου."));
            tilesPanel.revalidate();
            tilesPanel.repaint();
            return;
        }
        if (profiles.isEmpty()) {
            statusLabel.setText("Δεν βρέθηκαν συμβατοί συγκάτοικοι με τα τρέχοντα φίλτρα.");
            tilesPanel.add(emptyMessageTile("Δεν βρέθηκαν αποτελέσματα", "Δοκίμασε ευρύτερα κριτήρια budget."));
            tilesPanel.revalidate();
            tilesPanel.repaint();
            return;
        }
        statusLabel.setText("Βρέθηκαν " + profiles.size() + " συμβατοί συγκάτοικοι, ταξινομημένοι κατά score.");
        for (LifestyleProfile p : profiles) {
            tilesPanel.add(buildTile(p));
        }
        tilesPanel.revalidate();
        tilesPanel.repaint();
    }

    private JComponent buildTile(LifestyleProfile p) {
        Student owner = resolveOwner(p);
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 210), 1, true),
                new EmptyBorder(12, 14, 12, 14)));
        card.setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        String name = (owner == null) ? p.getProfileId() : owner.getFullName();
        String dept = (owner == null) ? "" : owner.getDepartment();
        JLabel nameLbl = new JLabel("<html><b>" + escape(name) + "</b><br><span style='color:#666;font-size:11px'>" + escape(dept) + "</span></html>");
        top.add(nameLbl, BorderLayout.WEST);

        int pct = (int) Math.round(p.getCompatibilityScore() * 100);
        JLabel scoreLbl = new JLabel(pct + "%", SwingConstants.RIGHT);
        scoreLbl.setFont(scoreLbl.getFont().deriveFont(Font.BOLD, 22f));
        scoreLbl.setForeground(scoreColor(pct));
        top.add(scoreLbl, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        JPanel mid = new JPanel(new GridLayout(0, 1, 0, 4));
        mid.setOpaque(false);
        mid.add(new JLabel("Συνήθειες: " + truncate(p.getHabits(), 60)));
        mid.add(new JLabel("Πρόγραμμα: " + (p.getSchedule() == null || p.getSchedule().isEmpty() ? "—" : p.getSchedule())));
        mid.add(new JLabel("Budget: " + String.format("%.0f€/μήνα", p.getBudget())));
        card.add(mid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottom.setOpaque(false);
        JButton more = new JButton("Δες περισσότερα & Like");
        if (owner != null) {
            more.addActionListener(e -> {
                if (owner != null) {
                    new RoommateCard(student.getUserId(), owner.getUserId()).setVisible(true);
                }
            });
        } else {
            more.setEnabled(false);
        }
        bottom.add(more);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private JComponent emptyMessageTile(String title, String subtitle) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 14f));
        card.add(t, BorderLayout.NORTH);
        card.add(new JLabel(subtitle), BorderLayout.CENTER);
        return card;
    }

    private Student resolveOwner(LifestyleProfile p) {
        for (Student s : DataStore.students) {
            if (s.getLifestyleProfile() != null && s.getLifestyleProfile().getProfileId().equals(p.getProfileId())) {
                return s;
            }
        }
        return null;
    }

    private static Color scoreColor(int pct) {
        if (pct >= 70) return new Color(34, 139, 34);
        if (pct >= 40) return new Color(218, 165, 32);
        return new Color(178, 34, 34);
    }

    private static String truncate(String s, int max) {
        if (s == null) return "—";
        if (s.length() <= max) return s;
        return s.substring(0, max - 1) + "…";
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}