package unihousie.mock;

import unihousie.entity.Report;

public class MockAdminNotificationSystem {

    public boolean pushToReviewQueue(Report report) {
        System.out.println("[MockAdminNotificationSystem] 🔔 Νέα καταγγελία στην ουρά admin");
        System.out.println("  → Report ID: " + report.getReportId());
        System.out.println("  → Reporter: " + report.getReporterId());
        System.out.println("  → Target: " + report.getReportedEntityId() + " (" + report.getTargetType() + ")");
        System.out.println("  → Reason: " + report.getReasonCode());
        return true;
    }
}