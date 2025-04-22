package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;

import java.util.List;

public interface OfficerEnquiryService extends BaseEnquiryService {
    List<Enquiry> getEnquiryForAssignedProj(BTOProj proj);
    boolean replyEnquiry(int enquiryId, String reply, HDBOfficer officer);
    List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer);
}
