package unihousie.controller;

import unihousie.entity.HousingListing;
import unihousie.entity.Report;
import unihousie.entity.Student;
import unihousie.entity.User;
import unihousie.mock.DataStore;
import java.util.List;

public class AdminReportController {

    public static final String ACTION_DISMISS = "DISMISS";
    public static final String ACTION_WARNING = "WARNING";
    public static final String ACTION_SUSPEND = "SUSPEND";
    public static final String ACTION_DELETE = "DELETE";

    public List<Report> getPendingReports() {
        return Report.findPendingReports();
    }

    public Report getReportDetails(String reportId) {
        return Report.getFullData(reportId);
    }

    public void processResolution(String reportId, String decisionAction) {
        Report report = Report.getFullData(reportId);
        if (report == null) {
            throw new IllegalArgumentException("Η καταγγελία δεν βρέθηκε: " + reportId);
        }
        if (!Report.OPEN.equals(report.getStatus())) {
            throw new IllegalArgumentException("Η καταγγελία έχει ήδη επιλυθεί.");
        }

        switch (decisionAction) {
            case ACTION_DISMISS:
                report.setStatus(Report.DISMISSED);
                System.out.println("[AdminReport] Report " + reportId + " → DISMISSED");
                break;

            case ACTION_WARNING:
                applyWarning(report);
                report.setStatus(Report.RESOLVED);
                System.out.println("[AdminReport] Report " + reportId + " → WARNING applied");
                break;

            case ACTION_SUSPEND:
                applySuspend(report);
                report.setStatus(Report.RESOLVED);
                System.out.println("[AdminReport] Report " + reportId + " → User SUSPENDED");
                break;

            case ACTION_DELETE:
                applyDelete(report);
                report.setStatus(Report.RESOLVED);
                System.out.println("[AdminReport] Report " + reportId + " → Listing DELETED");
                break;

            default:
                throw new IllegalArgumentException("Άγνωστη ενέργεια: " + decisionAction);
        }

        report.save();
        DataStore.save(report);
        notifyResolutionSuccess();
    }

    private void applyWarning(Report report) {
        if (!Report.TARGET_STUDENT.equals(report.getTargetType())) {
            throw new IllegalArgumentException("Η ενέργεια Warning εφαρμόζεται μόνο σε φοιτητές.");
        }
        Student s = DataStore.findStudent(report.getReportedEntityId());
        if (s == null) {
            throw new IllegalArgumentException("Ο φοιτητής δεν βρέθηκε.");
        }
        s.incrementWarningCount();
        s.save();
    }

    private void applySuspend(Report report) {
        if (!Report.TARGET_STUDENT.equals(report.getTargetType())) {
            throw new IllegalArgumentException("Η ενέργεια Suspend εφαρμόζεται μόνο σε χρήστες (Student/Landlord).");
        }
        User u = DataStore.findUser(report.getReportedEntityId());
        if (u == null) {
            throw new IllegalArgumentException("Ο χρήστης δεν βρέθηκε.");
        }
        u.setStatus(User.STATUS_SUSPENDED);
        u.save();
    }

    private void applyDelete(Report report) {
        if (!Report.TARGET_LISTING.equals(report.getTargetType())) {
            throw new IllegalArgumentException("Η ενέργεια Delete εφαρμόζεται μόνο σε αγγελίες.");
        }
        HousingListing l = DataStore.findListing(report.getReportedEntityId());
        if (l == null) {
            throw new IllegalArgumentException("Η αγγελία δεν βρέθηκε.");
        }
        l.setStatus(HousingListing.DELETED);
    }

    public void notifyResolutionSuccess() {
        System.out.println("✅ Η επίλυση καταγγελίας ολοκληρώθηκε επιτυχώς.");
    }
}