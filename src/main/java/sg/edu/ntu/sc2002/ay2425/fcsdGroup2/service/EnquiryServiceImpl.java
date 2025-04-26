package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.EnquiryRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

import java.util.List;
import java.util.Optional;

/**
 * Implementation class handling applicant, officer, and manager enquiry operations.
 * Implements ApplicantEnquiryService, OfficerEnquiryService, and ManagerEnquiryService.
 */
public class EnquiryServiceImpl implements ApplicantEnquiryService, OfficerEnquiryService, ManagerEnquiryService {
    EnquiryRepository enquiryRepo = EnquiryRepository.getInstance();
    BTORepository btoRepo = BTORepository.getInstance();
    SessionStateManager session = SessionStateManager.getInstance();

    /**
     * Retrieves an enquiry by ID.
     */
    @Override
    public Optional<Enquiry> getEnquiryById(int enquiryId) {
        return enquiryRepo.getById(enquiryId);
    }

    /**
     * Adds a message to an enquiry.
     */
    @Override
    public boolean addMessage(int enquiryId, String msg, String senderNric) {
        Optional<Enquiry> enquiryOpt = getEnquiryById(enquiryId);
        if (enquiryOpt.isEmpty()) return false;

        Enquiry enquiry = enquiryOpt.get();
        User sender = session.getLoggedInUser();
        if (sender == null) return false;

        enquiry.addMessage(msg, sender);
        enquiryRepo.update(enquiry); // Just use update, don't delete/add
        return true;
    }

    /**
     * Submits a new enquiry by an applicant.
     */
    @Override
    public Enquiry submitEnquiry(String message, HDBApplicant applicant, BTOProj proj) {
        return enquiryRepo.add(message, applicant, proj);
    }

    /**
     * Retrieves all enquiries made by a specific applicant.
     */
    @Override
    public List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant) {
        return enquiryRepo.getByApplicant(applicant);
    }

    /**
     * Allows an applicant to edit their own enquiry message.
     */
    @Override
    public boolean editOwnMessage(int enquiryId, String msgIdStr, HDBApplicant applicant, String newContent) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);
        if (opt.isEmpty()) return false;

        Enquiry enquiry = opt.get();

        int msgId;
        try {
            msgId = Integer.parseInt(msgIdStr);
        } catch (NumberFormatException e) {
            return false;
        }

        if (enquiry.getMadeBy().equals(applicant)) {
            boolean success = enquiry.editMessageById(msgId, applicant, newContent);
            if (success) {
                enquiryRepo.update(enquiry);
            }
            return success;
        }
        return false;
    }

    /**
     * Deletes an enquiry made by an applicant.
     */
    @Override
    public boolean deleteEnquiry(int enquiryId, HDBApplicant applicant) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);
        return opt.isPresent() && opt.get().getMadeBy().equals(applicant)
                && enquiryRepo.delete(enquiryId);
    }

    /**
     * Retrieves all enquiries in the system.
     */
    @Override
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepo.getAll();
    }

    /**
     * Manager replying to an enquiry regarding their project.
     */
    @Override
    public boolean replyEnquiry(int enquiryId, String reply, HDBManager manager) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);
        if (opt.isEmpty()) return false;

        Enquiry enq = opt.get();
        if (enq.getForProj().getManagerIC().equals(manager)) {
            enq.addMessage(reply, manager);
            enquiryRepo.update(enq);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all enquiries for a specific project assigned to an officer.
     */
    @Override
    public List<Enquiry> getEnquiryForAssignedProj(BTOProj proj) {
        return enquiryRepo.getByProject(proj);
    }

    /**
     * Officer replying to an enquiry of a project they are assigned to.
     */
    @Override
    public boolean replyEnquiry(int enquiryId, String reply, HDBOfficer officer) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);
        if (opt.isEmpty()) return false;

        Enquiry enquiry = opt.get();
        List<HDBOfficer> assigned = List.of(enquiry.getForProj().getOfficersList());
        if (!assigned.contains(officer)) return false;

        enquiry.addMessage(reply, officer);
        enquiryRepo.update(enquiry);
        return true;
    }

    /**
     * Retrieves all enquiries handled by a specific officer.
     */
    @Override
    public List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer) {
        return btoRepo.getAllProjects().stream()
                .filter(p -> List.of(p.getOfficersList()).contains(officer))
                .flatMap(p -> enquiryRepo.getByProject(p).stream())
                .toList();
    }
}
