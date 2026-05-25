package unihousie.entity;

import unihousie.mock.DataStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LifestyleProfile {
    private String profileId;
    private String habits;
    private String schedule;
    private double budget;
    private String notes;
    private boolean isCompleted;

    private transient double compatibilityScore = 0.0;

    public LifestyleProfile(String profileId, String habits, String schedule, double budget, String notes) {
        this.profileId = profileId;
        this.habits = habits;
        this.schedule = schedule;
        this.budget = budget;
        this.notes = notes;
        this.isCompleted = false;
    }

    public String getProfileId() { return profileId; }

    public String getHabits() { return habits; }
    public void setHabits(String habits) { this.habits = habits; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public double getCompatibilityScore() { return compatibilityScore; }
    public void setCompatibilityScore(double s) { this.compatibilityScore = s; }

    public void updateDetails(String habits, String schedule, double budget, String notes) {
        this.habits = habits;
        this.schedule = schedule;
        this.budget = budget;
        this.notes = notes;
    }

    public void save() {  }

    public static LifestyleProfile findOrCreate(String studentId) {
        Student s = DataStore.findStudent(studentId);
        if (s == null) {
            throw new IllegalArgumentException("Άγνωστος φοιτητής: " + studentId);
        }
        LifestyleProfile existing = s.getLifestyleProfile();
        if (existing != null) return existing;

        String newProfileId = "prof_" + studentId;
        LifestyleProfile fresh = new LifestyleProfile(newProfileId, "", "", 0.0, "");
        s.setLifestyleProfile(fresh);
        return fresh;
    }

    public static LifestyleProfile findOwnProfile(String studentId) {
        Student s = DataStore.findStudent(studentId);
        if (s == null) return null;
        return s.getLifestyleProfile();
    }

    public static List<LifestyleProfile> queryMatches(Map<String, Object> filters,
                                                     LifestyleProfile ownProfile) {
        double budgetMin = readDouble(filters, "budgetMin", 0.0);
        double budgetMax = readDouble(filters, "budgetMax", Double.MAX_VALUE);
        String extraSmoke  = readString(filters, "smokePreference", "");
        String extraHabits = readString(filters, "habits", "");

        String referenceHabits = combine(ownProfile == null ? "" : ownProfile.getHabits(),
                                         extraSmoke, extraHabits);
        Set<String> referenceTokens = tokenize(referenceHabits);

        List<LifestyleProfile> result = new ArrayList<>();
        for (Student s : DataStore.students) {
            LifestyleProfile p = s.getLifestyleProfile();
            if (p == null || !p.isCompleted()) continue;
            if (ownProfile != null && p.getProfileId().equals(ownProfile.getProfileId())) continue;

            if (p.getBudget() < budgetMin || p.getBudget() > budgetMax) continue;

            double score = jaccard(referenceTokens, tokenize(p.getHabits()));
            p.setCompatibilityScore(score);
            result.add(p);
        }

        result.sort(Comparator.comparingDouble(LifestyleProfile::getCompatibilityScore).reversed());
        return result;
    }

    public static Set<String> tokenize(String habits) {
        Set<String> tokens = new HashSet<>();
        if (habits == null) return tokens;
        for (String raw : habits.split(",")) {
            String t = raw.trim().toLowerCase();
            if (!t.isEmpty()) tokens.add(t);
        }
        return tokens;
    }

    public static double jaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() && b.isEmpty()) return 0.0;
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        return (double) intersection.size() / (double) union.size();
    }

    private static String combine(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p == null || p.isEmpty()) continue;
            if (sb.length() > 0) sb.append(", ");
            sb.append(p);
        }
        return sb.toString();
    }

    private static double readDouble(Map<String, Object> filters, String key, double fallback) {
        if (filters == null) return fallback;
        Object v = filters.get(key);
        if (v instanceof Number) return ((Number) v).doubleValue();
        if (v instanceof String) {
            try { return Double.parseDouble((String) v); } catch (NumberFormatException e) { return fallback; }
        }
        return fallback;
    }

    private static String readString(Map<String, Object> filters, String key, String fallback) {
        if (filters == null) return fallback;
        Object v = filters.get(key);
        return v == null ? fallback : v.toString();
    }
}
