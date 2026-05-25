package test;

import unihousie.controller.MatchmakingController;
import unihousie.controller.ProfileController;
import unihousie.entity.LifestyleProfile;
import unihousie.entity.Student;
import unihousie.entity.UserSummary;
import unihousie.mock.DataStore;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Member2Tests {

    private int passed = 0;
    private int failed = 0;
    private final StringBuilder failures = new StringBuilder();

    public static void main(String[] args) {
        new Member2Tests().runAll();
    }

    private void runAll() {
        System.out.println("====================================");
        System.out.println(" UniHousie — Member 2 logic tests");
        System.out.println(" UC02 (Lifestyle Profile) + UC03 (Browse Roommates)");
        System.out.println("====================================");

        for (Method m : Member2Tests.class.getDeclaredMethods()) {
            if (!m.getName().startsWith("test_")) continue;
            try {
                m.invoke(this);
                passed++;
                System.out.println("  ✓ " + m.getName());
            } catch (Throwable t) {
                failed++;
                Throwable cause = t.getCause() != null ? t.getCause() : t;
                System.out.println("  ✗ " + m.getName() + "  ->  " + cause.getMessage());
                failures.append("  ").append(m.getName()).append(": ").append(cause).append('\n');
            }
        }

        System.out.println("------------------------------------");
        System.out.println("Total: " + (passed + failed) + "   Passed: " + passed + "   Failed: " + failed);
        if (failed > 0) {
            System.out.println("\nFailure details:\n" + failures);
            System.exit(1);
        }
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) throw new AssertionError(msg);
    }
    private static void assertEquals(Object expected, Object actual, String msg) {
        boolean eq = (expected == null) ? actual == null : expected.equals(actual);
        if (!eq) throw new AssertionError(msg + " — expected=" + expected + " actual=" + actual);
    }
    private static void assertEquals(double expected, double actual, double eps, String msg) {
        if (Math.abs(expected - actual) > eps) {
            throw new AssertionError(msg + " — expected=" + expected + " actual=" + actual);
        }
    }
    private static void assertThrows(Class<? extends Throwable> type, Runnable body, String msg) {
        try {
            body.run();
        } catch (Throwable t) {
            if (type.isInstance(t)) return;
            throw new AssertionError(msg + " — wrong exception: " + t.getClass().getSimpleName());
        }
        throw new AssertionError(msg + " — no exception thrown");
    }

    public void test_uc02_getProfileData_returns_existing_profile_for_verified_student() {
        ProfileController c = new ProfileController();

        LifestyleProfile p = c.getProfileData("stud_1");
        assertTrue(p != null, "Profile should not be null for stud_1");
        assertEquals("prof_1", p.getProfileId(), "ProfileId mismatch for stud_1");
        assertTrue(p.isCompleted(), "stud_1 profile should be already completed");
    }

    public void test_uc02_getProfileData_creates_empty_profile_for_new_student() {
        ProfileController c = new ProfileController();

        Student s2before = DataStore.findStudent("stud_2");
        assertTrue(s2before.getLifestyleProfile() == null, "stud_2 should start without a profile");

        LifestyleProfile p = c.getProfileData("stud_2");
        assertTrue(p != null, "Profile should be created for stud_2");
        assertTrue(!p.isCompleted(), "Newly created profile should not be completed yet");
        assertTrue(DataStore.findStudent("stud_2").getLifestyleProfile() == p,
                "New profile should be attached to the student");
    }

    public void test_uc02_saveProfile_marks_student_as_completed() {

        ProfileController c = new ProfileController();
        c.saveProfile("stud_4", "non-smoker, pets ok, social", "morning person", 310.0, "ok");

        Student s4 = DataStore.findStudent("stud_4");
        assertTrue(s4.getLifestyleProfile() != null, "stud_4 should now have a profile");
        assertTrue(s4.getLifestyleProfile().isCompleted(), "stud_4 profile should be marked completed");
        assertEquals("non-smoker, pets ok, social", s4.getLifestyleProfile().getHabits(), "habits mismatch");
        assertEquals(310.0, s4.getLifestyleProfile().getBudget(), 0.001, "budget mismatch");
    }

    public void test_uc02_saveProfile_rejects_empty_habits() {
        ProfileController c = new ProfileController();
        assertThrows(IllegalArgumentException.class,
                () -> c.saveProfile("stud_6", "  ", "flexible", 300.0, ""),
                "Empty habits should be rejected");
    }

    public void test_uc02_saveProfile_rejects_unknown_student() {
        ProfileController c = new ProfileController();
        assertThrows(IllegalArgumentException.class,
                () -> c.saveProfile("stud_does_not_exist", "smoker", "flex", 300.0, ""),
                "Unknown student should be rejected");
    }

    public void test_uc03_searchCompatibleRoommates_returns_sorted_by_score_desc() {
        MatchmakingController c = new MatchmakingController();

        List<LifestyleProfile> results = c.searchCompatibleRoommates("stud_1", new HashMap<>());

        assertTrue(!results.isEmpty(), "Should return matches for stud_1");

        for (LifestyleProfile p : results) {
            assertTrue(!"prof_1".equals(p.getProfileId()), "Self profile must be excluded");
        }

        for (int i = 1; i < results.size(); i++) {
            assertTrue(results.get(i-1).getCompatibilityScore() >= results.get(i).getCompatibilityScore(),
                    "Results must be sorted by score descending");
        }

        assertEquals("prof_7", results.get(0).getProfileId(),
                "stud_1's top match should be stud_7 (identical habits)");
        assertEquals(1.0, results.get(0).getCompatibilityScore(), 0.001,
                "Identical habits should yield Jaccard 1.0");
    }

    public void test_uc03_searchCompatibleRoommates_applies_budget_hard_filter() {
        MatchmakingController c = new MatchmakingController();
        Map<String, Object> filters = new HashMap<>();
        filters.put("budgetMin", 330.0);
        filters.put("budgetMax", 360.0);

        List<LifestyleProfile> results = c.searchCompatibleRoommates("stud_1", filters);

        Set<String> ids = new HashSet<>();
        for (LifestyleProfile p : results) ids.add(p.getProfileId());

        assertTrue(ids.contains("prof_3"), "prof_3 (budget 350) should be in window");
        assertTrue(ids.contains("prof_9"), "prof_9 (budget 340) should be in window");
        assertTrue(!ids.contains("prof_5"), "prof_5 (budget 280) should be filtered out");
        assertTrue(!ids.contains("prof_7"), "prof_7 (budget 320) should be filtered out");
    }

    public void test_uc03_searchCompatibleRoommates_empty_when_own_profile_missing() {
        MatchmakingController c = new MatchmakingController();

        Student s8 = DataStore.findStudent("stud_8");
        assertTrue(s8.getLifestyleProfile() == null || !s8.getLifestyleProfile().isCompleted(),
                "Precondition: stud_8 has no completed profile");

        List<LifestyleProfile> results = c.searchCompatibleRoommates("stud_8", new HashMap<>());
        assertTrue(results.isEmpty(), "Should return empty list when own profile is missing");
    }

    public void test_uc03_getRoommateCardData_returns_summary_with_masked_email() {
        MatchmakingController c = new MatchmakingController();
        UserSummary summary = c.getRoommateCardData("stud_3");
        assertTrue(summary != null, "Summary should not be null");
        assertEquals("stud_3", summary.getUserId(), "userId mismatch");
        assertTrue(summary.getContactStub().contains("***"),
                "Contact stub should be masked (contain ***)");
        assertTrue(!summary.getContactStub().equals("nik_oik@ceid.upatras.gr"),
                "Contact stub must NOT be the raw email");
    }

    public void test_uc03_jaccard_on_isolated_sets() {

        LifestyleProfile a = new LifestyleProfile("p_a", "smoker, pets, quiet", "any", 300, "");
        LifestyleProfile b = new LifestyleProfile("p_b", "smoker, pets, quiet", "any", 300, "");
        Set<String> ta = LifestyleProfile.tokenize(a.getHabits());
        Set<String> tb = LifestyleProfile.tokenize(b.getHabits());
        assertEquals(1.0, LifestyleProfile.jaccard(ta, tb), 0.001,
                "Identical habit sets should yield Jaccard = 1.0");

        Set<String> tc = LifestyleProfile.tokenize("smoker, pets");
        Set<String> td = LifestyleProfile.tokenize("smoker, quiet");
        assertEquals(1.0 / 3.0, LifestyleProfile.jaccard(tc, td), 0.001,
                "Jaccard of {smoker,pets} vs {smoker,quiet} should be 1/3");

        Set<String> te = LifestyleProfile.tokenize("smoker");
        Set<String> tf = LifestyleProfile.tokenize("pets");
        assertEquals(0.0, LifestyleProfile.jaccard(te, tf), 0.001,
                "Disjoint sets should yield Jaccard 0.0");

        assertEquals(0.0, LifestyleProfile.jaccard(LifestyleProfile.tokenize(""), LifestyleProfile.tokenize("")),
                0.001, "Both-empty edge case should yield 0.0");
    }
}
