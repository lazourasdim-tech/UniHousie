package unihousie.entity;

import unihousie.mock.DataStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report {
    public static final String OPEN = "OPEN";
    public static final String RESOLVED = "RESOLVED";
    public static final String DISMISSED = "DISMISSED";

    public static final String TARGET_STUDENT = "STUDENT";
    public static final String TARGET_LISTING = "LISTING";

    public static final String REASON_INAPPROPRIATE = "INAPPROPRIATE";
    public static final String REASON_SPAM = "SPAM";
    public static final String REASON_FRAUD = "FRAUD";
    public static final String REASON_OTHER = "OTHER";

    private String reportId;
    private String reporterId;
    private String reportedEntityId;
    private String targetType;
    private String reasonCode;
    private String text;
    private String status;
    private Date createdAt;

    public Report(String reportId, String reporterId, String reportedEntityId,
                  String targetType, String reasonCode, String text, String status) {
        this.reportId = reportId;
        this.reporterId = reporterId;
        this.reportedEntityId = reportedEntityId;
        this.targetType = targetType;
        this.reasonCode = reasonCode;
        this.text = text;
        this.status = status;
        this.createdAt = new Date();
    }

    public static Report createNew(String reporterId, String reportedEntityId,
                                   String targetType, String reasonCode, String text) {
        String reportId = DataStore.nextId("rep_", DataStore.reports.size());
        Report r = new Report(reportId, reporterId, reportedEntityId,
                targetType, reasonCode, text, OPEN);
        DataStore.reports.add(r);
        return r;
    }

    public static List<Report> findPendingReports() {
        List<Report> pending = new ArrayList<>();
        for (Report r : DataStore.reports) {
            if (OPEN.equals(r.status)) pending.add(r);
        }
        return pending;
    }

    public static Report getFullData(String reportId) {
        for (Report r : DataStore.reports) {
            if (r.reportId.equals(reportId)) return r;
        }
        return null;
    }

    public String getReportId() { return reportId; }
    public String getReporterId() { return reporterId; }
    public String getReportedEntityId() { return reportedEntityId; }
    public String getTargetType() { return targetType; }

    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }

    public void save() {  }
}
