package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import java.util.List;

/**
 * Service interface defining operations related to applicants' enquiries.
 * Allows submitting, editing, retrieving, and deleting enquiries.
 */
public interface ApplicantEnquiryService extends BaseEnquiryService {

    /**
     * Submits a new enquiry by an applicant for a BTO project.
     *
     * @param message the enquiry content
     * @param applicant the applicant submitting
     * @param proj the BTO project concerned
     * @return the created Enquiry
     */
    Enquiry submitEnquiry(String message, HDBApplicant applicant, BTOProj proj);

    /**
     * Retrieves all enquiries made by a specific applicant.
     *
     * @param applicant the applicant
     * @return list of enquiries
     */
    List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant);

    /**
     * Allows an applicant to edit their own enquiry message.
     *
     * @param enquiryId ID of the enquiry
     * @param msgId ID of the specific message
     * @param applicant the applicant attempting to edit
     * @param newContent new message content
     * @return true if successful
     */
    boolean editOwnMessage(int enquiryId, String msgId, HDBApplicant applicant, String newContent);

    /**
     * Allows an applicant to delete an entire enquiry.
     *
     * @param enquiryId the enquiry ID
     * @param applicant the applicant requesting deletion
     * @return true if deletion successful
     */
    boolean deleteEnquiry(int enquiryId, HDBApplicant applicant);
}
