package unihousie.entity;

import unihousie.mock.DataStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HousingListing {

    public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
    public static final String ACTIVE = "ACTIVE";
    public static final String REJECTED = "REJECTED";
    public static final String DELETED = "DELETED";

    private String listingId;
    private String landlordId;
    private String title;
    private String address;
    private String area;
    private String type;
    private int rooms;
    private double sqm;
    private double rent;
    private String availability;
    private String description;
    private List<String> photos;
    private String status;
    private double averageRating;
    private Date createdAt;

    public HousingListing(String listingId, String landlordId, String title,
                          String address, String area, String type,
                          int rooms, double sqm, double rent,
                          String availability, String description,
                          List<String> photos) {
        this.listingId = listingId;
        this.landlordId = landlordId;
        this.title = title;
        this.address = address;
        this.area = area;
        this.type = type;
        this.rooms = rooms;
        this.sqm = sqm;
        this.rent = rent;
        this.availability = availability;
        this.description = description;
        this.photos = photos != null ? photos : new ArrayList<>();
        this.status = PENDING_APPROVAL;
        this.averageRating = 0.0;
        this.createdAt = new Date();
    }

    public static HousingListing createNew(String landlordId, String title, String address,
                                           double rent, String description,
                                           List<String> amenities, List<String> photos) {
        String listingId = DataStore.nextId("list_", DataStore.listings.size());

        HousingListing listing = new HousingListing(listingId, landlordId, title, address,
                "", "APARTMENT", 2, 50, rent, "IMMEDIATE", description, photos);

        DataStore.listings.add(listing);
        return listing;
    }

    public static List<HousingListing> findPendingApproval() {
        List<HousingListing> pending = new ArrayList<>();
        for (HousingListing l : DataStore.listings) {
            if (PENDING_APPROVAL.equals(l.status)) {
                pending.add(l);
            }
        }
        return pending;
    }

    public static HousingListing getFullData(String listingId) {
        return DataStore.findListing(listingId);
    }

    public static List<HousingListing> findMatchingActive(String location, double maxPrice, int minRooms) {
        List<HousingListing> results = new ArrayList<>();
        for (HousingListing l : DataStore.listings) {
            if (ACTIVE.equals(l.status) &&
                    l.rent <= maxPrice &&
                    l.rooms >= minRooms) {
                results.add(l);
            }
        }
        return results;
    }

    public void recalculateAverageRating() {
        double sum = 0;
        int count = 0;
        for (Review r : DataStore.reviews) {
            if (r.getListingId().equals(this.listingId)) {
                sum += r.getStars();
                count++;
            }
        }
        this.averageRating = (count == 0) ? 0.0 : sum / count;
        System.out.println("[HousingListing] " + listingId + " new average rating: " +
                String.format("%.2f", averageRating) + " (" + count + " reviews)");
    }

    public String getListingId() { return listingId; }
    public String getLandlordId() { return landlordId; }
    public String getTitle() { return title; }
    public String getAddress() { return address; }
    public String getType() { return type; }
    public int getRooms() { return rooms; }
    public double getSqm() { return sqm; }
    public double getRent() { return rent; }
    public String getDescription() { return description; }
    public List<String> getPhotos() { return photos; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getAverageRating() { return averageRating; }
}