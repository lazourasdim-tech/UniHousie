package unihousie.mock;

import unihousie.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DataStore {

    public static List<Student> students = new ArrayList<>();
    public static List<Landlord> landlords = new ArrayList<>();
    public static List<Admin> admins = new ArrayList<>();

    public static List<HousingListing> listings = new ArrayList<>();
    public static List<MutualMatch> matches = new ArrayList<>();
    public static List<Message> messages = new ArrayList<>();
    public static List<InterestExpression> interests = new ArrayList<>();
    public static List<PropertyVisit> visits = new ArrayList<>();
    public static List<Review> reviews = new ArrayList<>();
    public static List<Report> reports = new ArrayList<>();
    public static List<VerificationAttempt> verificationAttempts = new ArrayList<>();

    static {
        Student s1 = new Student("stud_1", "kos_pap@ceid.upatras.gr", "Κώστας Παπαδόπουλος", "6911111111", "CEID");
        s1.setVerificationStatus(Student.VERIFIED);
        Student s2 = new Student("stud_2", "mar_ant@ceid.upatras.gr", "Μαρία Αντωνίου", "6922222222", "CEID");
        Student s3 = new Student("stud_3", "nik_oik@ceid.upatras.gr", "Νίκος Οικονόμου", "6933333333", "CEID");
        s3.setVerificationStatus(Student.VERIFIED);
        Student s4 = new Student("stud_4", "el_dim@ceid.upatras.gr", "Ελένη Δημητρίου", "6944444444", "Μαθηματικό");
        Student s5 = new Student("stud_5", "gio_nik@ceid.upatras.gr", "Γιώργος Νικολάου", "6955555555", "Φυσικό");
        s5.setVerificationStatus(Student.VERIFIED);
        Student s6 = new Student("stud_6", "ann_vas@ceid.upatras.gr", "Άννα Βασιλείου", "6966666666", "Φιλολογία");
        Student s7 = new Student("stud_7", "dim_mak@ceid.upatras.gr", "Δημήτρης Μακρής", "6977777777", "CEID");
        s7.setVerificationStatus(Student.VERIFIED);
        Student s8 = new Student("stud_8", "chr_rap@ceid.upatras.gr", "Χριστίνα Ράπτη", "6988888888", "Οικονομικό");
        Student s9 = new Student("stud_9", "and_geo@ceid.upatras.gr", "Ανδρέας Γεωργίου", "6999999999", "Νομική");
        s9.setVerificationStatus(Student.VERIFIED);
        Student s10 = new Student("stud_10", "sof_kla@ceid.upatras.gr", "Σοφία Κλάδη", "6900000000", "Ιατρική");

        students.add(s1); students.add(s2); students.add(s3); students.add(s4); students.add(s5);
        students.add(s6); students.add(s7); students.add(s8); students.add(s9); students.add(s10);

        LifestyleProfile p1 = new LifestyleProfile("prof_1", "non-smoker, no pets, quiet", "morning person", 300.0, "Ήσυχος, οργανωμένος, μου αρέσει η μαγειρική.");
        p1.setCompleted(true); s1.setLifestyleProfile(p1);

        LifestyleProfile p3 = new LifestyleProfile("prof_3", "non-smoker, pets ok, social", "flexible", 350.0, "Έχω σκύλο, αγαπώ τη γυμναστική.");
        p3.setCompleted(true); s3.setLifestyleProfile(p3);

        LifestyleProfile p5 = new LifestyleProfile("prof_5", "smoker, no pets, very social", "night owl", 280.0, "Νυχτοπούλι, βγαίνω σχεδόν κάθε Σάββατο.");
        p5.setCompleted(true); s5.setLifestyleProfile(p5);

        LifestyleProfile p7 = new LifestyleProfile("prof_7", "non-smoker, no pets, quiet", "morning person", 320.0, "Πρωινός τύπος, συγκεντρωμένος στο διάβασμα.");
        p7.setCompleted(true); s7.setLifestyleProfile(p7);

        LifestyleProfile p9 = new LifestyleProfile("prof_9", "non-smoker, pets ok, balanced", "flexible", 340.0, "Έχω γάτα, μου αρέσουν τα board games.");
        p9.setCompleted(true); s9.setLifestyleProfile(p9);

        for (int i = 1; i <= 12; i++) {
            String phone = "693" + String.format("%07d", 1000000 + i);
            landlords.add(new Landlord("land_" + i, "landlord" + i + "@gmail.com", "Ιδιοκτήτης " + i, phone));
        }

        admins.add(new Admin("admin_1", "admin@unihousie.gr", "Διαχειριστής Συστήματος", "6900000001"));

        HousingListing l1 = new HousingListing("list_1", "land_1", "Γκαρσονιέρα 30τμ πλησίον Ψηλαλωνίων", "Ψηλαλώνια 5, Πάτρα", "Ψηλαλώνια", "STUDIO", 1, 30, 250.0, "IMMEDIATE", "Κατάλληλο για φοιτητές, αυτόνομη θέρμανση.", Arrays.asList("photo_l1_1.jpg", "photo_l1_2.jpg"));
        HousingListing l2 = new HousingListing("list_2", "land_2", "Διαμέρισμα 45τμ στην Αγία Σοφία", "Αγίας Σοφίας 24, Πάτρα", "Αγία Σοφία", "APARTMENT", 1, 45, 300.0, "IMMEDIATE", "Ήσυχη περιοχή, κοντά σε στάση λεωφορείου για Πανεπιστήμιο.", Arrays.asList("photo_l2_1.jpg"));
        HousingListing l3 = new HousingListing("list_3", "land_3", "Λουξ Δυάρι 55τμ στο Κέντρο", "Ρήγα Φεραίου 88, Πάτρα", "Κέντρο", "APARTMENT", 2, 55, 400.0, "IMMEDIATE", "Πλήρως ανακαινισμένο, minimal σχεδιασμός.", Arrays.asList("photo_l3_1.jpg", "photo_l3_2.jpg"));
        HousingListing l4 = new HousingListing("list_4", "land_4", "Studio 25τμ διαμπερές", "Καραϊσκάκη 120, Πάτρα", "Κέντρο", "STUDIO", 1, 25, 220.0, "IMMEDIATE", "Οικονομικό, χαμηλά κοινόχρηστα.", Arrays.asList("photo_l4_1.jpg"));
        HousingListing l5 = new HousingListing("list_5", "land_5", "Διαμέρισμα 60τμ με θέα θάλασσα", "Όθωνος Αμαλίας 45, Πάτρα", "Παραλία", "APARTMENT", 2, 60, 380.0, "FROM_SEPT_2026", "Μεγάλο μπαλκόνι, δίπλα σε προαστιακό.", Arrays.asList("photo_l5_1.jpg"));
        HousingListing l6 = new HousingListing("list_6", "land_6", "Γκαρσονιέρα ανακαινισμένη", "Κορίνθου 312, Πάτρα", "Κέντρο", "STUDIO", 1, 32, 280.0, "IMMEDIATE", "Κοντά στην κεντρική βιβλιοθήκη και αγορά.", Arrays.asList("photo_l6_1.jpg"));
        HousingListing l7 = new HousingListing("list_7", "land_7", "Δυάρι 50τμ στην περιοχή Ταμπάχανα", "Γερμανού 15, Πάτρα", "Ταμπάχανα", "APARTMENT", 2, 50, 290.0, "IMMEDIATE", "Ιδανικό για φοιτητές CEID, κοντά στην παλιά πόλη.", Arrays.asList("photo_l7_1.jpg"));
        HousingListing l8 = new HousingListing("list_8", "land_8", "Ευρύχωρο Διαμέρισμα 65τμ", "Γούναρη 75, Πάτρα", "Κέντρο", "APARTMENT", 3, 65, 360.0, "FROM_SEPT_2026", "Κεντρική θέρμανση, δίπλα σε στάσεις και super market.", Arrays.asList("photo_l8_1.jpg"));
        HousingListing l9 = new HousingListing("list_9", "land_9", "Φοιτητικό Studio κοντά στα CEID", "Πατρέως 14, Πάτρα", "Κέντρο", "STUDIO", 1, 28, 260.0, "IMMEDIATE", "Πλήρως επιπλωμένο με ηλεκτρικές συσκευές.", Arrays.asList("photo_l9_1.jpg"));
        HousingListing l10 = new HousingListing("list_10", "land_10", "Διαμέρισμα 52τμ στην Ακτή Δυμαίων", "Ακτή Δυμαίων 12, Πάτρα", "Παραλία", "APARTMENT", 2, 52, 310.0, "IMMEDIATE", "Φωτεινό, με εύκολο πάρκινγκ.", Arrays.asList("photo_l10_1.jpg"));
        HousingListing l11 = new HousingListing("list_11", "land_11", "Ρετιρέ 40τμ με μεγάλη βεράντα", "Καλαβρύτων 50, Πάτρα", "Άνω Πόλη", "APARTMENT", 1, 40, 330.0, "IMMEDIATE", "A/C, ηλιακός θερμοσίφωνας, απεριόριστη θέα.", Arrays.asList("photo_l11_1.jpg"));
        HousingListing l12 = new HousingListing("list_12", "land_12", "Γκαρσονιέρα 35τμ Ζαρουχλέικα", "Ανθείας 99, Πάτρα", "Ζαρουχλέικα", "STUDIO", 1, 35, 200.0, "IMMEDIATE", "Πολύ οικονομική, αυτόνομη είσοδος.", Arrays.asList("photo_l12_1.jpg"));

        l1.setStatus(HousingListing.ACTIVE);
        l2.setStatus(HousingListing.ACTIVE);
        l3.setStatus(HousingListing.ACTIVE);
        l4.setStatus(HousingListing.ACTIVE);
        l5.setStatus(HousingListing.ACTIVE);
        l6.setStatus(HousingListing.ACTIVE);
        l7.setStatus(HousingListing.ACTIVE);
        l8.setStatus(HousingListing.ACTIVE);
        l9.setStatus(HousingListing.ACTIVE);
        l10.setStatus(HousingListing.ACTIVE);

        listings.add(l1); listings.add(l2); listings.add(l3); listings.add(l4);
        listings.add(l5); listings.add(l6); listings.add(l7); listings.add(l8);
        listings.add(l9); listings.add(l10); listings.add(l11); listings.add(l12);
    }

    public static Student findStudent(String id) {
        for (Student s : students) if (s.getUserId().equals(id)) return s;
        return null;
    }

    public static Landlord findLandlord(String id) {
        for (Landlord l : landlords) if (l.getUserId().equals(id)) return l;
        return null;
    }

    public static Admin findAdmin(String id) {
        for (Admin a : admins) if (a.getUserId().equals(id)) return a;
        return null;
    }

    public static User findUser(String id) {
        Student s = findStudent(id);
        if (s != null) return s;
        Landlord l = findLandlord(id);
        if (l != null) return l;
        return findAdmin(id);
    }

    public static HousingListing findListing(String id) {
        for (HousingListing h : listings) if (h.getListingId().equals(id)) return h;
        return null;
    }

    public static MutualMatch findMatch(String id) {
        for (MutualMatch m : matches) if (m.getMatchId().equals(id)) return m;
        return null;
    }

    public static VerificationAttempt findAttempt(String id) {
        for (VerificationAttempt v : verificationAttempts) if (v.getAttemptId().equals(id)) return v;
        return null;
    }

    public static Report findReport(String id) {
        for (Report r : reports) if (r.getReportId().equals(id)) return r;
        return null;
    }

    public static String nextId(String prefix, int currentSize) {
        return prefix + (currentSize + 1) + "_" + System.currentTimeMillis();
    }

    public static void save(Object obj) {
        if (obj instanceof MutualMatch) {
            MutualMatch m = (MutualMatch) obj;
            if (m.getId() == null) {
                m.setId(nextId("match_", matches.size()));
            }
            matches.removeIf(x -> x.getId().equals(m.getId()));
            matches.add(m);
        } else if (obj instanceof Message) {
            Message msg = (Message) obj;
            if (msg.getId() == null) {
                msg.setId(nextId("msg_", messages.size()));
            }
            messages.removeIf(x -> x.getId().equals(msg.getId()));
            messages.add(msg);
        } else if (obj instanceof Report) {
            Report r = (Report) obj;
            reports.removeIf(x -> x.getReportId().equals(r.getReportId()));
            reports.add(r);
        } else if (obj instanceof PropertyVisit) {
            PropertyVisit v = (PropertyVisit) obj;
            visits.removeIf(x -> x.getVisitId().equals(v.getVisitId()));
            visits.add(v);
        }
    }

    public static <T> List<T> findAll(Class<T> clazz) {
        if (clazz == MutualMatch.class) return (List<T>) new ArrayList<>(matches);
        if (clazz == Message.class) return (List<T>) new ArrayList<>(messages);
        return new ArrayList<>();
    }

    public static List<MutualMatch> getActiveMatchesForUser(String userId) {
        List<MutualMatch> active = new ArrayList<>();
        for (MutualMatch m : matches) {
            if ("ACTIVE".equals(m.getStatus()) &&
                    (m.getStudentAId().equals(userId) || m.getStudentBId().equals(userId))) {
                active.add(m);
            }
        }
        return active;
    }

    public static <T> Optional<T> findById(Class<T> clazz, String id) {
        if (clazz == MutualMatch.class) {
            return (Optional<T>) Optional.ofNullable(findMatch(id));
        }
        if (clazz == Message.class) {
            for (Message m : messages) {
                if (m.getId().equals(id)) return Optional.of((T) m);
            }
        }
        return Optional.empty();
    }
}