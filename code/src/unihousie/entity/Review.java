package unihousie.entity;

import unihousie.mock.DataStore;
import java.util.Date;

public class Review {
    private String reviewId;
    private String studentId;
    private String listingId;
    private int stars;
    private String text;
    private Date timestamp;

    public Review(String reviewId, String studentId, String listingId, int stars, String text) {
        this.reviewId = reviewId;
        this.studentId = studentId;
        this.listingId = listingId;
        this.stars = stars;
        this.text = text;
        this.timestamp = new Date();
    }

    public static Review createNew(String studentId, String listingId, int stars, String text) {
        String reviewId = DataStore.nextId("rev_", DataStore.reviews.size());
        Review r = new Review(reviewId, studentId, listingId, stars, text);
        DataStore.reviews.add(r);
        return r;
    }

    public static boolean existsForProperty(String studentId, String listingId) {
        for (Review r : DataStore.reviews) {
            if (r.studentId.equals(studentId) && r.listingId.equals(listingId)) {
                return true;
            }
        }
        return false;
    }

    public String getReviewId() { return reviewId; }
    public String getStudentId() { return studentId; }
    public String getListingId() { return listingId; }

    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Date getTimestamp() { return timestamp; }

    public void save() {  }
}