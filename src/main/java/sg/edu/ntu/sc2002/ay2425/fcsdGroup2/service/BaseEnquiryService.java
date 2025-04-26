package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import java.util.List;
import java.util.Optional;

/**
 * Base service interface defining common enquiry operations.
 * Shared by Applicant, Officer, and Manager services.
 */
public interface BaseEnquiryService {

    /**
     * Retrieves an enquiry by its ID.
     *
     * @param enquiryId the enquiry ID
     * @return Optional containing Enquiry if found
     */
    Optional<Enquiry> getEnquiryById(int enquiryId);

    /**
     * Adds a message to an existing enquiry.
     *
     * @param enquiryId the ID of the enquiry
     * @param msg the message content
     * @param senderNric NRIC of the user sending the message
     * @return true if successfully added, false otherwise
     */
    boolean addMessage(int enquiryId, String msg, String senderNric);
}
