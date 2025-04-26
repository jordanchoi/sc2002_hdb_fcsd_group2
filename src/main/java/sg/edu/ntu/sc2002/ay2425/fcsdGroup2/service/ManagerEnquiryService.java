package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;

import java.util.List;

/**
 * Service interface for handling manager-related enquiry operations.
 * Allows managers to view and respond to enquiries related to projects under their supervision.
 */
public interface ManagerEnquiryService extends BaseEnquiryService {

    /**
     * Retrieves all enquiries in the system.
     *
     * @return list of all enquiries
     */
    List<Enquiry> getAllEnquiries();

    /**
     * Allows a manager to reply to an enquiry.
     *
     * @param enquiryId the enquiry ID
     * @param reply the reply message
     * @param manager the manager replying
     * @return true if the reply was successful
     */
    boolean replyEnquiry(int enquiryId, String reply, HDBManager manager);

    /**
     * Retrieves all enquiries for a project supervised by a manager.
     *
     * @param proj the BTO project
     * @return list of enquiries
     */
    List<Enquiry> getEnquiryForAssignedProj(BTOProj proj);
}
