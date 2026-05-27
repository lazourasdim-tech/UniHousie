package unihousie.boundary;

import unihousie.Session;
import unihousie.entity.Admin;
import unihousie.entity.Landlord;
import unihousie.entity.Student;
import unihousie.mock.DataStore;
import unihousie.entity.HousingListing;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class MainWindow extends JFrame {

    public MainWindow() {
        super("UniHousie — Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("UniHousie", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        title.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Φοιτητής",     buildStudentTab());
        tabs.addTab("Ιδιοκτήτης",   buildLandlordTab());
        tabs.addTab("Διαχειριστής", buildAdminTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JComponent buildStudentTab() {
        DefaultListModel<Student> model = new DefaultListModel<>();
        for (Student s : DataStore.students) model.addElement(s);
        JList<Student> list = makeUserList(model, s ->
                s.getFullName() + "   [" + (s.isVerified() ? "VERIFIED" : "UNVERIFIED") + "]   — " + s.getDepartment());

        Runnable refreshList = () -> list.repaint();
        Consumer<Window> hookRefresh = w -> w.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { refreshList.run(); }
        });

        JPanel actions = new JPanel(new GridLayout(0, 1, 6, 6));
        actions.setBorder(BorderFactory.createTitledBorder("Use cases φοιτητή"));
        actions.add(actionButton("UC01 — Verification", e -> {
            Student s = list.getSelectedValue();
            if (s == null) { warn("Επίλεξε φοιτητή."); return; }
            VerificationPage p = new VerificationPage(s);
            hookRefresh.accept(p);
            p.display();
        }));
        actions.add(actionButton("UC02 — Lifestyle profile", e -> {
            Student s = list.getSelectedValue();
            if (s == null) { warn("Επίλεξε φοιτητή."); return; }
            LifestyleProfilePage p = new LifestyleProfilePage(s);
            hookRefresh.accept(p);
            p.display();
        }));
        actions.add(actionButton("UC03 — Browse roommates", e -> {
            Student s = list.getSelectedValue();
            if (s == null) { warn("Επίλεξε φοιτητή."); return; }
            BrowseRoommatesPage p = new BrowseRoommatesPage(s);
            hookRefresh.accept(p);
            p.display();
        }));
        actions.add(actionButton("UC04 — Roommate card / Like", e -> {
            Student s = list.getSelectedValue();
            if (s == null) {
                warn("Επίλεξε φοιτητή από τη λίστα.");
                return;
            }
            Session.setCurrentUser(s.getUserId());
            RoommateCard card = new RoommateCard(s.getUserId());
            open(card);
        }));
        actions.add(actionButton("UC05 — Chat window", e -> {
            Student s = list.getSelectedValue();
            if (s == null) {
                warn("Επίλεξε φοιτητή.");
                return;
            }
            Session.setCurrentUser(s.getUserId());
            new ChatWindow().setVisible(true);
        }));
        actions.add(actionButton("UC07 — Search property", e -> {
            Student s = list.getSelectedValue();
            if (s != null) Session.setCurrentUser(s.getUserId());
            open(new SearchPropertyPage());
        }));
        actions.add(actionButton("UC08 — Express interest", e -> {
            Student s = list.getSelectedValue();
            if (s != null) Session.setCurrentUser(s.getUserId());
            if (!DataStore.listings.isEmpty()) {
                HousingListing demoListing = DataStore.listings.get(0);
                new PropertyDetailsPage(demoListing).setVisible(true);
            } else {
                warn("Δεν υπάρχουν διαθέσιμα ακίνητα για demo.");
            }
        }));

        return splitPane(list, actions);
    }

    private JComponent buildLandlordTab() {
        DefaultListModel<Landlord> model = new DefaultListModel<>();
        for (Landlord l : DataStore.landlords) model.addElement(l);
        JList<Landlord> list = makeUserList(model, l -> l.getFullName() + "   — " + l.getEmail());

        JPanel actions = new JPanel(new GridLayout(0, 1, 6, 6));
        actions.setBorder(BorderFactory.createTitledBorder("Use cases ιδιοκτήτη"));
        actions.add(actionButton("UC06 — New listing", e -> open(new NewListingPage())));

        return splitPane(list, actions);
    }

    private JComponent buildAdminTab() {
        DefaultListModel<Admin> model = new DefaultListModel<>();
        for (Admin a : DataStore.admins) model.addElement(a);
        JList<Admin> list = makeUserList(model, a -> a.getFullName() + "   — " + a.getEmail());

        JPanel actions = new JPanel(new GridLayout(0, 1, 6, 6));
        actions.setBorder(BorderFactory.createTitledBorder("Use cases διαχειριστή"));
        actions.add(actionButton("UC06 — Listing approval queue", e -> open(new AdminApprovalPage())));
        actions.add(actionButton("UC09 — Reports queue",          e -> open(new AdminReportPage())));

        return splitPane(list, actions);
    }

    private <T> JList<T> makeUserList(DefaultListModel<T> model, java.util.function.Function<T, String> renderer) {
        JList<T> list = new JList<>(model);
        list.setCellRenderer((lst, value, idx, sel, foc) -> {
            JLabel l = new JLabel(renderer.apply(value));
            l.setOpaque(true);
            if (sel) { l.setBackground(lst.getSelectionBackground()); l.setForeground(lst.getSelectionForeground()); }
            l.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            return l;
        });
        list.setSelectedIndex(0);
        return list;
    }

    private JComponent splitPane(JList<?> list, JComponent actions) {
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(list), actions);
        sp.setResizeWeight(0.4);
        sp.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        return sp;
    }

    private JButton actionButton(String label, java.awt.event.ActionListener action) {
        JButton b = new JButton(label);
        b.addActionListener(action);
        return b;
    }

    private void open(Object page) {
        if (page instanceof JFrame) {
            JFrame f = (JFrame) page;
            if (f instanceof VerificationPage) {
                ((VerificationPage) f).display();
            } else {
                f.setVisible(true);
            }
        } else if (page instanceof JComponent) {
            JFrame frame = new JFrame("UniHousie — Roommate Card");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add((JComponent) page);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);
        }
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "UniHousie", JOptionPane.WARNING_MESSAGE);
    }
}