package unihousie.controller;

import unihousie.entity.HousingListing;

import java.util.List;

public class AdminApprovalController {

    public List<HousingListing> getPendingListings() {

        throw new UnsupportedOperationException("UC06 Phase 2 — pending implementation by Member 5");
    }

    public String getListingDetails(String listingId) {

        throw new UnsupportedOperationException("UC06 Phase 2 — pending implementation by Member 5");
    }

    public void processApproval(String listingId) {

        throw new UnsupportedOperationException("UC06 Phase 2 — pending implementation by Member 5");
    }

    public void processRejection(String listingId, String rejectionReason) {

        throw new UnsupportedOperationException("UC06 Phase 2 — pending implementation by Member 5");
    }

    public HousingListing renderPropertyDetailsPage(HousingListing listing) {

        return listing;
    }

    public void notifyApprovalSuccess() {  }

    public void notifyRejectionProcessed() {  }
}
