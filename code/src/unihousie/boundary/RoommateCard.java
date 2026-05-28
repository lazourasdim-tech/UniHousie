package unihousie.boundary;

import unihousie.controller.InteractionController;
import unihousie.entity.LifestyleProfile;
import unihousie.entity.MutualMatch;
import unihousie.entity.Student;
import unihousie.entity.UserSummary;
import unihousie.mock.DataStore;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoommateCard extends JFrame {

    private final InteractionController controller;
    private final String currentUserId;
    private final String targetUserId;
    private final List<UserSummary> profiles = new ArrayList<>();
    private int currentIndex = 0;
    private boolean singleMode = false;

    private JPanel cardPanel;
    private JLabel nameLabel;
    private JLabel bioLabel;
    private JLabel statusLabel;


    public RoommateCard(String currentUserId, String targetUserId) {
        super("UC04 — Roommate Card");
        this.currentUserId = currentUserId != null ? currentUserId : "stud_1";
        this.targetUserId = targetUserId;
        this.controller = new InteractionController();
        this.singleMode = true;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 680);
        setLocationRelativeTo(null);

        loadSingleProfile(targetUserId);
        buildUI();
        showCurrentProfile();
    }

    public RoommateCard(String currentUserId) {
        super("UC04 — Browse & Match");
        this.currentUserId = currentUserId != null ? currentUserId : "stud_1";
        this.targetUserId = null;
        this.controller = new InteractionController();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 680);
        setLocationRelativeTo(null);

        loadAllProfiles();
        buildUI();
        showCurrentProfile();
    }

    public RoommateCard() {
        this("stud_1");
    }


    private void loadAllProfiles() {
        profiles.clear();
        for (Student s : DataStore.students) {
            if (s.getUserId().equals(currentUserId)) continue;
            if (!s.isVerified()) continue;

            LifestyleProfile profile = s.getLifestyleProfile();
            if (profile == null || !profile.isCompleted()) continue;

            if (hasSentLike(currentUserId, s.getUserId()) || hasActiveMatch(currentUserId, s.getUserId())) {
                continue;
            }

            UserSummary summary = new UserSummary(
                    s.getUserId(),
                    s.getFullName(),
                    s.getDepartment(),
                    profile.getHabits() != null ? profile.getHabits() : "—",
                    profile.getBudget(),
                    s.getEmail() != null ? s.getEmail() : ""
            );
            profiles.add(summary);
        }
    }

    private boolean hasSentLike(String fromUser, String toUser) {
        for (MutualMatch m : DataStore.matches) {
            if (m == null || !"PENDING".equals(m.getStatus())) continue;
            if (m.getStudentAId().equals(fromUser) && m.getStudentBId().equals(toUser)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasActiveMatch(String user1, String user2) {
        for (MutualMatch m : DataStore.matches) {
            if (m == null || !"ACTIVE".equals(m.getStatus())) continue;
            if ((m.getStudentAId().equals(user1) && m.getStudentBId().equals(user2)) ||
                    (m.getStudentAId().equals(user2) && m.getStudentBId().equals(user1))) {
                return true;
            }
        }
        return false;
    }

    private void loadSingleProfile(String targetId) {
        profiles.clear();
        Student s = DataStore.findStudent(targetId);
        if (s == null) return;
        if (!s.isVerified()) return;

        LifestyleProfile profile = s.getLifestyleProfile();
        if (profile == null || !profile.isCompleted()) return;

        UserSummary summary = new UserSummary(
                s.getUserId(),
                s.getFullName(),
                s.getDepartment(),
                profile.getHabits() != null ? profile.getHabits() : "—",
                profile.getBudget(),
                s.getEmail() != null ? s.getEmail() : ""
        );
        profiles.add(summary);
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 10));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        String titleText = singleMode ? "UC04 — Roommate Card" : "UC04 — Browse & Match";
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(title, BorderLayout.WEST);

        statusLabel = new JLabel(" ");
        header.add(statusLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Card Panel
        cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 3, true),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        cardPanel.setBackground(Color.WHITE);

        nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));

        bioLabel = new JLabel("", SwingConstants.CENTER);
        bioLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        cardPanel.add(nameLabel, BorderLayout.NORTH);
        cardPanel.add(bioLabel, BorderLayout.CENTER);
        add(cardPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));
        buttonsPanel.setOpaque(false);

        JButton reportBtn = new JButton("🚩 Report");
        reportBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        reportBtn.setBackground(Color.BLACK);
        reportBtn.setForeground(Color.BLACK);
        reportBtn.setPreferredSize(new Dimension(110, 50));
        reportBtn.setToolTipText("Καταγγελία χρήστη");
        reportBtn.addActionListener(e -> handleReport());

        JButton likeBtn = new JButton("❤️ Like");
        likeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        likeBtn.setBackground(Color.GREEN);
        likeBtn.setForeground(Color.BLACK);
        likeBtn.setPreferredSize(new Dimension(170, 62));
        likeBtn.setFocusPainted(false);
        likeBtn.addActionListener(e -> handleLike());

        JButton skipBtn = new JButton("⏭ Skip");
        skipBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        skipBtn.setBackground(Color.RED);
        skipBtn.setForeground(Color.BLACK);
        skipBtn.setPreferredSize(new Dimension(110, 50));
        skipBtn.addActionListener(e -> nextProfile());

        if (singleMode) {
            skipBtn.setVisible(false);
        }

        buttonsPanel.add(reportBtn);
        buttonsPanel.add(likeBtn);
        buttonsPanel.add(skipBtn);

        add(buttonsPanel, BorderLayout.SOUTH);
    }


    private void showCurrentProfile() {
        if (profiles.isEmpty() || currentIndex >= profiles.size()) {
            JOptionPane.showMessageDialog(this,
                    "Δεν υπάρχουν άλλα διαθέσιμα προφίλ!",
                    "Τέλος", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        UserSummary current = profiles.get(currentIndex);
        nameLabel.setText(current.getFullName());

        String info = "<html><b>Τμήμα:</b> " + current.getDepartment() +
                "<br><br><b>Συνήθειες:</b><br>" + current.getHabits() +
                "<br><br><b>Budget:</b> " + String.format("%.0f €/μήνα", current.getBudget()) +
                "</html>";

        bioLabel.setText(info);
        statusLabel.setText("Προφίλ " + (currentIndex + 1) + " / " + profiles.size());

        revalidate();
        repaint();
    }


    private void handleLike() {
        if (currentIndex >= profiles.size()) return;

        UserSummary target = profiles.get(currentIndex);
        controller.registerLike(currentUserId, target.getUserId());

        if (singleMode) {
            JOptionPane.showMessageDialog(this, "Like καταχωρήθηκε επιτυχώς!",
                    "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            currentIndex++;
            SwingUtilities.invokeLater(this::showCurrentProfile);
        }
    }

    private void nextProfile() {
        currentIndex++;
        SwingUtilities.invokeLater(this::showCurrentProfile);
    }

    private void handleReport() {
        if (currentIndex >= profiles.size()) return;
        UserSummary target = profiles.get(currentIndex);
        new ReportUserForm(currentUserId, target.getUserId(), unihousie.entity.Report.TARGET_STUDENT)
                .setVisible(true);
    }
}