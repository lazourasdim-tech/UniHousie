package unihousie.boundary;

import unihousie.entity.LifestyleProfile;
import unihousie.entity.UserSummary;

import javax.swing.*;
import java.util.List;

public class BrowseRoommatesPage extends JFrame {

    public BrowseRoommatesPage() {
        super("UC03 — Browse Roommates");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

    }

    public void display() {
        setVisible(true);
    }

    public void applyFilters(double budgetMin, double budgetMax, String smokePreference, String habits) {

    }

    public void selectRoommate(String targetUserId) {

    }

    public void displayRoommatesList(List<LifestyleProfile> profiles) {

    }

    public void showRoommateModal(UserSummary basicInfo) {

    }
}
