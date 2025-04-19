import java.util.ArrayList;
import java.util.List;

public class EnquiryController {
    private static EnquiryController instance;
    private List<Enquiry> enquiries;

    public EnquiryController() {
        this.enquiries = new ArrayList<>();
    }

    public static EnquiryController getInstance() {
        if (instance == null) {
            instance = new EnquiryController();
        }
        return instance;
    }

    public void createEnquiry(String msg, HDBApplicant applicant, BTOProj project) {
        Enquiry newEnquiry = new Enquiry(msg, applicant, project);
        enquiries.add(newEnquiry);
    }

    public Enquiry getEnquiryById(int id) {
        for (Enquiry e : enquiries) {
            if (e.getEnquiryId() == id) return e;
        }
        return null;
    }

    public boolean addMessage(int id, String msg, User sender) {
        Enquiry e = getEnquiryById(id);
        if (e != null) {
            e.addMessage(msg, sender);
            return true;
        }
        return false;
    }

    public boolean editMessageById(int enquiryId, int messageId, User currentUser, String newContent) {
        Enquiry e = getEnquiryById(enquiryId);
        if (e != null) {
            return e.editMessageById(messageId, currentUser, newContent);
        }
        return false;
    }
    
    

    public boolean deleteEnquiry(int id) {
        Enquiry e = getEnquiryById(id);
        if (e != null) {
            enquiries.remove(e);
            return true;
        }
        return false;
    }

    public List<Enquiry> listEnquiries() {
        return new ArrayList<>(enquiries);
    }

    public List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant) {
        List<Enquiry> applicantEnquiries = new ArrayList<>();
        for (Enquiry e : enquiries) {
            if (e.getMadeBy().equals(applicant)) {
                applicantEnquiries.add(e);
            }
        }
        return applicantEnquiries;
    }
}
