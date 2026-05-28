package unihousie.boundary;

import unihousie.controller.ListingController;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class NewListingPage extends JFrame {

    private final ListingController controller = new ListingController();

    private JTextField titleField;
    private JTextField addressField;
    private JTextField priceField;
    private JTextArea descriptionArea;
    private JTextField photosField;

    public NewListingPage() {
        super("UC06 — New Listing");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 680);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 10, 8, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        int row = 0;

        gc.gridx = 0; gc.gridy = row++;
        formPanel.add(new JLabel("Τίτλος Αγγελίας:"), gc);
        gc.gridx = 1;
        titleField = new JTextField(30);
        formPanel.add(titleField, gc);

        gc.gridx = 0; gc.gridy = row++;
        formPanel.add(new JLabel("Διεύθυνση:"), gc);
        gc.gridx = 1;
        addressField = new JTextField(30);
        formPanel.add(addressField, gc);

        gc.gridx = 0; gc.gridy = row++;
        formPanel.add(new JLabel("Μηνιαίο Ενοίκιο (€):"), gc);
        gc.gridx = 1;
        priceField = new JTextField(10);
        formPanel.add(priceField, gc);

        gc.gridx = 0; gc.gridy = row++;
        formPanel.add(new JLabel("Περιγραφή:"), gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.BOTH; gc.weighty = 1.0;
        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        formPanel.add(new JScrollPane(descriptionArea), gc);

        JButton submitBtn = new JButton("Υποβολή Αγγελίας");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.addActionListener(e -> submitListing());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void submitListing() {
        try {
            String title = titleField.getText().trim();
            String address = addressField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            String description = descriptionArea.getText().trim();
            List<String> amenities = java.util.Collections.emptyList();

            if (title.isEmpty() || address.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ συμπληρώστε όλα τα υποχρεωτικά πεδία.",
                        "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String listingId = controller.submitListing(
                    "land_1", title, address, price, description, amenities, null);

            JOptionPane.showMessageDialog(this,
                    "✅ Η αγγελία υποβλήθηκε επιτυχώς!\nListing ID: " + listingId + "\n\nΕκκρεμεί έγκριση από Διαχειριστή.",
                    "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);

            clearForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Η τιμή πρέπει να είναι αριθμός.",
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        titleField.setText("");
        addressField.setText("");
        priceField.setText("");
        descriptionArea.setText("");
    }

    public void display() {
        setVisible(true);
    }
}