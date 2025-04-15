package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import java.util.ArrayList;
import java.util.List;

public class EnquiryController {
    private final List<Enquiry> enquiries = new ArrayList<>();
    private int nextId = 1;

    public void createEnquiry(String msg) {
        Enquiry e = new Enquiry(nextId++, msg);
        enquiries.add(e);
    }

    public Enquiry viewEnquiry(int id) {
        for (Enquiry e : enquiries) {
            if (e.getEnquiryId() == id) {
                return e;
            }
        }
        return null;
    }

    public void updateEnquiry(int id, String reply) {
        Enquiry e = viewEnquiry(id);
        if (e != null) {
            e.setReply(reply);
        }
    }

    public boolean deleteEnquiry(int id) {
        for (int i = 0; i < enquiries.size(); i++) {
            if (enquiries.get(i).getEnquiryId() == id) {
                enquiries.remove(i);
                return true; 
            }
        }
        return false;
    }

    public List<Enquiry> listEnquiry() {
        return enquiries;
    }
}
