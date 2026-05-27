package unihousie.entity;

import unihousie.mock.DataStore;
import java.util.Date;

public class PropertyVisit {
    public static final String PENDING_CONFIRMATION = "PENDING_CONFIRMATION";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String COMPLETED = "COMPLETED";
    public static final String CANCELLED = "CANCELLED";

    private String visitId;
    private String listingId;
    private String studentId;
    private Date scheduledDate;
    private String scheduledTime;
    private String status;

    public PropertyVisit(String visitId, String listingId, String studentId,
                         Date scheduledDate, String scheduledTime, String status) {
        this.visitId = visitId;
        this.listingId = listingId;
        this.studentId = studentId;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.status = status;
    }

    public static PropertyVisit createNew(String listingId, String studentId,
                                          Date scheduledDate, String scheduledTime) {
        String visitId = DataStore.nextId("visit_", DataStore.visits.size());
        PropertyVisit v = new PropertyVisit(visitId, listingId, studentId,
                scheduledDate, scheduledTime, PENDING_CONFIRMATION);
        DataStore.save(v);
        return v;
    }

    public static boolean hasCompletedVisit(String studentId, String listingId) {
        for (PropertyVisit v : DataStore.visits) {
            if (v.studentId.equals(studentId) &&
                    v.listingId.equals(listingId) &&
                    COMPLETED.equals(v.status)) {
                return true;
            }
        }
        return false;
    }

    public String getVisitId() { return visitId; }
    public String getListingId() { return listingId; }
    public String getStudentId() { return studentId; }

    public Date getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(Date scheduledDate) { this.scheduledDate = scheduledDate; }

    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isCompleted() { return COMPLETED.equals(status); }

    public void save() {  }
}