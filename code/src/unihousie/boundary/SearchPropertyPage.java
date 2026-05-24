package unihousie.boundary;

import unihousie.entity.HousingListing;

import javax.swing.*;
import java.util.List;

public class SearchPropertyPage extends JFrame {

    public SearchPropertyPage() {
        super("UC07 — Search Property");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void searchProperties(String location, double maxPrice, int roomCount) {

    }

    public void clickPropertyCard(String listingId) {

    }

    public void displayPropertyCards(List<HousingListing> listings) {

    }

    public void renderPropertyDetailsPage(HousingListing listing) {

    }
}
