package unihousie.controller;

import unihousie.entity.HousingListing;
import unihousie.entity.PropertyVisit;
import unihousie.entity.Review;
import unihousie.entity.Student;
import unihousie.entity.User;
import unihousie.mock.DataStore;

public class ReviewController {

    public String postReview(String studentId, String listingId, int stars, String text) {

        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Λείπει ο φοιτητής.");
        }
        if (listingId == null || listingId.isEmpty()) {
            throw new IllegalArgumentException("Λείπει η αγγελία.");
        }
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Η βαθμολογία πρέπει να είναι από 1 έως 5 αστέρια.");
        }

        User u = DataStore.findUser(studentId);
        if (!(u instanceof Student)) {
            throw new IllegalArgumentException("Ο χρήστης δεν είναι φοιτητής.");
        }
        Student student = (Student) u;
        if (!student.isVerified()) {
            throw new IllegalArgumentException("Μόνο επαληθευμένοι φοιτητές μπορούν να αξιολογήσουν.");
        }

        if (!PropertyVisit.hasCompletedVisit(studentId, listingId)) {
            throw new IllegalArgumentException(
                    "Δεν έχετε ολοκληρώσει επίσκεψη ή διαμονή σε αυτό το ακίνητο.");
        }

        if (Review.existsForProperty(studentId, listingId)) {
            throw new IllegalArgumentException(
                    "Έχετε ήδη υποβάλει αξιολόγηση για αυτό το ακίνητο.");
        }

        Review review = Review.createNew(studentId, listingId, stars, text);
        review.save();
        DataStore.save(review);

        HousingListing listing = DataStore.findListing(listingId);
        if (listing != null) {
            listing.recalculateAverageRating();
        }

        notifySuccess();
        return review.getReviewId();
    }

    public void notifySuccess() {
        System.out.println("✅ Η αξιολόγηση καταχωρήθηκε επιτυχώς.");
    }
}