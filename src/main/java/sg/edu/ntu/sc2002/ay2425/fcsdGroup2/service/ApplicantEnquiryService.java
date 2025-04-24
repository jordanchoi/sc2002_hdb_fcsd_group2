package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import java.util.List;

public interface ApplicantEnquiryService extends BaseEnquiryService {
    Enquiry submitEnquiry(String message, HDBApplicant applicant, BTOProj proj);
    List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant);
    boolean editOwnMessage(int enquiryId, String msgId, HDBApplicant applicant, String newContent);
    boolean deleteEnquiry(int enquiryId, HDBApplicant applicant);
}
