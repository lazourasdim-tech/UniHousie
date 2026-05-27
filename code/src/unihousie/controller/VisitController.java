package unihousie.controller;

import unihousie.entity.HousingListing;
import unihousie.entity.PropertyVisit;
import unihousie.mock.DataStore;
import unihousie.mock.MockLandlordNotifier;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VisitController {

    private final MockLandlordNotifier landlordNotifier = new MockLandlordNotifier();

    public String createVisitRequest(String studentId, String listingId,
                                     Date scheduledDate, String scheduledTime) {

        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Λείπει ο φοιτητής.");
        }
        if (listingId == null || listingId.isEmpty()) {
            throw new IllegalArgumentException("Λείπει η αγγελία.");
        }
        if (scheduledDate == null) {
            throw new IllegalArgumentException("Παρακαλώ επιλέξτε ημερομηνία.");
        }
        if (scheduledTime == null || scheduledTime.trim().isEmpty()) {
            throw new IllegalArgumentException("Παρακαλώ επιλέξτε ώρα.");
        }

        HousingListing listing = DataStore.findListing(listingId);
        if (listing == null) {
            throw new IllegalArgumentException("Η αγγελία δεν βρέθηκε.");
        }
        String landlordId = listing.getLandlordId();

        String visitId = DataStore.nextId("visit_", DataStore.visits.size());
        PropertyVisit visit = new PropertyVisit(
                visitId,
                listingId,
                studentId,
                scheduledDate,
                scheduledTime,
                PropertyVisit.PENDING_CONFIRMATION
        );
        DataStore.save(visit);

        sendVisitRequestNotification(visitId, landlordId, listing.getTitle(),
                scheduledDate, scheduledTime);

        return visitId;
    }

    public void sendVisitRequestNotification(String visitId, String landlordId,
                                             String listingTitle,
                                             Date scheduledDate, String scheduledTime) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        landlordNotifier.notifyNewVisitRequest(
                landlordId,
                visitId,
                listingTitle,
                fmt.format(scheduledDate),
                scheduledTime
        );
    }
}