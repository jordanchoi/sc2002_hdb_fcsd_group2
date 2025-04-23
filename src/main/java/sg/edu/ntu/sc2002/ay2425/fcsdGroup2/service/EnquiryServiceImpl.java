package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.EnquiryRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

import java.util.List;
import java.util.Optional;

public class EnquiryServiceImpl implements ApplicantEnquiryService, OfficerEnquiryService, ManagerEnquiryService {
    EnquiryRepository enquiryRepo = EnquiryRepository.getInstance();
    BTORepository btoRepo = BTORepository.getInstance();
    SessionStateManager session = SessionStateManager.getInstance();

    // Common Enquiry Service Methods

    // Get enquiry by ID. ID can be fetched from the enquiry object, within the list in BTOProj, or call getByApplicant, getByProj first.
    @Override
    public Optional<Enquiry> getEnquiryById(int enquiryId) {
        return enquiryRepo.getById(enquiryId);
    }

    // Add message to an enquiry thread usable by all roles.
    @Override
    public boolean addMessage(int enquiryId, String msg, String senderNric) {
        Optional<Enquiry> enquiryOpt = getEnquiryById(enquiryId);
        if (enquiryOpt.isEmpty()) {
            return false;
        }

        Enquiry enquiry = enquiryOpt.get();
        User sender = session.getLoggedInUser();
        if (sender == null) {
            return false;
        }

        enquiry.addMessage(msg, sender);
        //?
        enquiryRepo.delete(enquiryId);
        //enquiryRepo.add(enquiry.getEnquiryId(), "", enquiry.getMadeBy(), enquiry.getForProj());
        return true;
    }

    // ========= Applicant Enquiry Service Methods ==============

    // Create enquiry with auto ID via repository
    @Override
    public Enquiry submitEnquiry(String message, HDBApplicant applicant, BTOProj proj) {
        return enquiryRepo.add(message, applicant, proj);
    }

    // Return list of enquiries made by the applicant ACROSS ALL PROJECTS!
    @Override
    public List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant) {
        return enquiryRepo.getByApplicant(applicant);
    }

    @Override
    public boolean editOwnMessage(int enquiryId, String msgIdStr, HDBApplicant applicant, String newContent) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);
        if (opt.isEmpty()) {
            return false;
        }

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
                enquiryRepo.delete(enquiryId);
                enquiryRepo.add(enquiry.getEnquiryId(), "", enquiry.getMadeBy(), enquiry.getForProj());
            }
            return success;
        }
        return false;
    }

    @Override
    public boolean deleteEnquiry(int enquiryId, HDBApplicant applicant) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);
        if(opt.isPresent() && opt.get().getMadeBy().equals(applicant)) {
          return enquiryRepo.delete(enquiryId);
        }
        return false;
    }

    // Manager Enquiry Service Methods
    @Override
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepo.getAll();
    }

    @Override
    public boolean replyEnquiry(int enquiryId, String reply, HDBManager manager) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);

        // Testing
        if (opt.isEmpty()) {
            System.out.println("Enquiry not found");
            return false;
        }
        // Testing
        System.out.println(opt.get().toString());

        if (opt.isEmpty()) {return false;}

        Enquiry enq = opt.get();

        // Check if the manager is assigned to the project
        if (enq.getForProj().getManagerIC().equals(manager)) {
            enq.addMessage(reply, manager);
            enquiryRepo.update(enq);
            //enquiryRepo.delete(enquiryId);
            //enquiryRepo.add(enq.getEnquiryId(), reply, enq.getMadeBy(), enq.getForProj());
            return true;
        }

        return false;
    }

    // Officer Enquiry Service Methods
    @Override
    public List<Enquiry> getEnquiryForAssignedProj(BTOProj proj) {
        return enquiryRepo.getByProject(proj);
    }

    @Override
    public boolean replyEnquiry(int enquiryId, String reply, HDBOfficer officer) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);
        if (opt.isEmpty()) {return false;}

        Enquiry enquiry = opt.get();
        List<HDBOfficer> assigned = List.of(enquiry.getForProj().getOfficersList());

        if (!assigned.contains(officer)) {return false;}

        enquiry.addMessage(reply, officer);
        enquiryRepo.delete(enquiryId);
        enquiryRepo.add(enquiry.getEnquiryId(), "", enquiry.getMadeBy(), enquiry.getForProj());
        return true;
    }

    @Override
    public List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer) {
        return btoRepo.getAllProjects().stream().filter(btoProj -> List.of(btoProj.getOfficersList()).contains(officer)).flatMap(btoProj -> enquiryRepo.getByProject(btoProj).stream()).toList();
    }
}
