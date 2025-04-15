package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

public class Enquiry {
    private String enquiry;
    private int enquiryId;
    private String reply;
    private HDBApplicant madeBy;
    private BTOProj forProj;

    public Enquiry(int id, String enquiry) {
        this.enquiryId = id;
        this.enquiry = enquiry;
    }

    public void delete() {
        this.enquiry = null;
        this.reply = null;
    }

    public void setEnquiry(String enquiry) {
        this.enquiry = enquiry;
    }

    public String getEnquiry() {
        return enquiry;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }

    public void setEnquiryId(int enquiryId) {
        this.enquiryId = enquiryId;
    }

    public int getEnquiryId() {
        return enquiryId;
    }
}
