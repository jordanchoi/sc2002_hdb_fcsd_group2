package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.User;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.EnquiryRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller class to manage creation, editing, viewing, and deletion of project enquiries.
 */
public class EnquiryController {
    private static EnquiryController instance;
    private final UserRepository userRepo = UserRepository.getInstance();
    private final BTORepository btoRepo = BTORepository.getInstance();
    private final EnquiryRepository enquiryRepository = EnquiryRepository.getInstance();
    //private final List<Enquiry> enquiries;

    /** Default constructor. */
    public EnquiryController() {
        //this.enquiries = enquiryRepository.getAllEnquiries();
    }

    /**
     * Returns the singleton instance of EnquiryController.
     *
     * @return EnquiryController instance
     */
    public static EnquiryController getInstance() {
        if (instance == null) {
            instance = new EnquiryController();
        }
        return instance;
    }

    /**
     * Creates a new enquiry for a project by an applicant.
     *
     * @param msg the enquiry message
     * @param applicant the applicant making the enquiry
     * @param project the project related to the enquiry
     */
    public void createEnquiry(String msg, HDBApplicant applicant, BTOProj project) {
        Enquiry enquiry = enquiryRepository.add(msg, applicant, project);
        //enquiries.add(enquiry);
    }

    /**
     * Retrieves an enquiry by its ID.
     *
     * @param id enquiry ID
     * @return Enquiry object or null
     */
    public Enquiry getEnquiryById(int id) {
        Optional<Enquiry> e = enquiryRepository.getById(id);
        if (e.isPresent()) {
            return e.get();
        }
        return null;
    }

    /**
     * Adds a message to an existing enquiry.
     *
     * @param id enquiry ID
     * @param msg message content
     * @param sender user sending the message
     * @return true if message added successfully
     */
    public boolean addMessage(int id, String msg, User sender) {
        Enquiry e = getEnquiryById(id);
        if (e != null) {
            e.addMessage(msg, sender);
            return true;
        }
        return false;
    }

    /**
     * Edits a message within an enquiry if sender matches.
     *
     * @param enquiryId enquiry ID
     * @param messageId message ID
     * @param currentUser user attempting to edit
     * @param newContent new message content
     * @return true if edit successful
     */
    public boolean editMessageById(int enquiryId, int messageId, User currentUser, String newContent) {
        Enquiry e = getEnquiryById(enquiryId);
        if (e != null) {
            return e.editMessageById(messageId, currentUser, newContent);
        }
        return false;
    }

    /**
     * Deletes an enquiry by its ID.
     *
     * @param id enquiry ID
     * @return true if deleted successfully
     */
    public boolean deleteEnquiry(int id) {
        if (enquiryRepository.getById(id).isPresent()) {
            enquiryRepository.delete(id);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all enquiries.
     *
     * @return list of all enquiries
     */
    public List<Enquiry> listEnquiries() {
        return enquiryRepository.getAll();
    }

    /**
     * Retrieves all enquiries made by a specific applicant.
     *
     * @param applicant the applicant
     * @return list of enquiries
     */
    public List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant) {
        return enquiryRepository.getByApplicant(applicant);
    }
}
