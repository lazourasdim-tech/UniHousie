package unihousie.controller;

import unihousie.boundary.LifestyleProfilePage;
import unihousie.entity.LifestyleProfile;
import unihousie.entity.Student;
import unihousie.mock.DataStore;

public class ProfileController {

    private LifestyleProfilePage page;

    public ProfileController() { }

    public ProfileController(LifestyleProfilePage page) {
        this.page = page;
    }

    public void setPage(LifestyleProfilePage page) {
        this.page = page;
    }

    public LifestyleProfile getProfileData(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("studentId δεν μπορεί να είναι κενό.");
        }
        Student s = DataStore.findStudent(studentId);
        if (s == null) {
            throw new IllegalArgumentException("Δεν βρέθηκε φοιτητής με id: " + studentId);
        }

        return LifestyleProfile.findOrCreate(studentId);
    }

    public void saveProfile(String studentId, String habits, String schedule, double budget, String notes) {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("studentId δεν μπορεί να είναι κενό.");
        }
        if (habits == null || habits.trim().isEmpty()) {
            throw new IllegalArgumentException("Τα habits είναι υποχρεωτικά.");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("Το budget πρέπει να είναι μη-αρνητικό.");
        }

        Student s = DataStore.findStudent(studentId);
        if (s == null) {
            throw new IllegalArgumentException("Δεν βρέθηκε φοιτητής με id: " + studentId);
        }

        LifestyleProfile profile = LifestyleProfile.findOrCreate(studentId);

        profile.updateDetails(habits, schedule == null ? "" : schedule, budget, notes == null ? "" : notes);
        profile.save();

        s.markProfileAsCompleted();
        s.save();

        notifySaveSuccess();
    }

    public void notifySaveSuccess() {
        if (page != null) {
            page.showSuccessToast();
        }
    }
}
