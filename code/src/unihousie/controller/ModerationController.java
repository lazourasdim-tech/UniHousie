package unihousie.controller;

import unihousie.entity.Report;
import unihousie.entity.Student;
import unihousie.entity.User;
import unihousie.mock.DataStore;
import unihousie.mock.MockAdminNotificationSystem;

public class ModerationController {

    private final MockAdminNotificationSystem notificationSystem = new MockAdminNotificationSystem();

    public String submitReport(String reporterId, String reportedEntityId,
                               String targetType, String reasonCode, String text) {

        if (reporterId == null || reporterId.isEmpty()) {
            throw new IllegalArgumentException("Λείπει ο καταγγέλλων.");
        }
        if (reportedEntityId == null || reportedEntityId.isEmpty()) {
            throw new IllegalArgumentException("Λείπει ο καταγγελλόμενος.");
        }

        if (reporterId.equals(reportedEntityId)) {
            throw new IllegalArgumentException("Δεν μπορείτε να καταγγείλετε τον εαυτό σας.");
        }

        if (reasonCode == null || reasonCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Παρακαλώ επιλέξτε αιτία καταγγελίας.");
        }

        User reporter = DataStore.findUser(reporterId);
        if (reporter instanceof Student && !((Student) reporter).isVerified()) {
            throw new IllegalArgumentException("Μόνο επαληθευμένοι χρήστες μπορούν να υποβάλλουν καταγγελία.");
        }

        Report report = Report.createNew(reporterId, reportedEntityId,
                targetType, reasonCode, text);
        report.save();

        notificationSystem.pushToReviewQueue(report);
        notifyReportReceived();

        return report.getReportId();
    }

    public void notifyReportReceived() {
        System.out.println("✅ Η καταγγελία υποβλήθηκε επιτυχώς και θα εξεταστεί από διαχειριστή.");
    }
}