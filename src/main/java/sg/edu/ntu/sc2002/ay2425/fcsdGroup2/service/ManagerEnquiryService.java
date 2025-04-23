package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;

import java.util.List;

public interface ManagerEnquiryService extends BaseEnquiryService {
    List<Enquiry> getAllEnquiries();
    boolean replyEnquiry(int enquiryId, String reply, HDBManager manager);
    List<Enquiry> getEnquiryForAssignedProj(BTOProj proj);
}
