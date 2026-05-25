package unihousie.entity;

import unihousie.mock.DataStore;

public abstract class User {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_SUSPENDED = "SUSPENDED";

    private String userId;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private String status;

    public User(String userId, String email, String fullName, String phone, String role) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.status = STATUS_ACTIVE;
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getStatus() { return status; }

    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStatus(String status) { this.status = status; }

    public boolean isVerified() { return true; }

    public void setVerificationStatus(String status) {  }

    public void save() {  }

    public static User findByUserId(String userId) {
        return DataStore.findUser(userId);
    }

    public static UserSummary getUserSummaryDetails(String targetUserId) {
        User u = DataStore.findUser(targetUserId);
        if (!(u instanceof Student)) {
            throw new IllegalArgumentException("Δεν είναι φοιτητής: " + targetUserId);
        }
        Student s = (Student) u;
        LifestyleProfile p = s.getLifestyleProfile();
        String habits = (p == null) ? "" : p.getHabits();
        double budget = (p == null) ? 0.0 : p.getBudget();
        return new UserSummary(
                s.getUserId(),
                s.getFullName(),
                s.getDepartment(),
                habits,
                budget,
                maskEmail(s.getEmail())
        );
    }

    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***@***";
        int at = email.indexOf('@');
        String local = email.substring(0, at);
        String domain = email.substring(at);
        if (local.length() <= 3) return local.charAt(0) + "***" + domain;
        return local.substring(0, 3) + "***" + domain;
    }
}
