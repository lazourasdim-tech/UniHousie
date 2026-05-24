package unihousie.entity;

public class ContactInfo {
    private String landlordId;
    private String fullName;
    private String email;
    private String phone;

    public ContactInfo(String landlordId, String fullName, String email, String phone) {
        this.landlordId = landlordId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public String getLandlordId() { return landlordId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
