package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;

import java.util.List;

/**
 * Service interface for handling officer-related enquiry operations.
 * Allows officers to view enquiries related to their projects and reply.
 */
public interface OfficerEnquiryService extends BaseEnquiryService {

    /**
     * Retrieves all enquiries related to a BTO project assigned to an officer.
     *
     * @param proj the BTO project
     * @return list of enquiries
     */
    List<Enquiry> getEnquiryForAssignedProj(BTOProj proj);

    /**
     * Allows an officer to reply to an enquiry.
     *
     * @param enquiryId the enquiry ID
     * @param reply the reply message
     * @param officer the officer replying
     * @return true if the reply was successful
     */
    boolean replyEnquiry(int enquiryId, String reply, HDBOfficer officer);

    /**
     * Retrieves all enquiries handled by a specific officer.
     *
     * @param officer the officer
     * @return list of enquiries
     */
    List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer);
}
