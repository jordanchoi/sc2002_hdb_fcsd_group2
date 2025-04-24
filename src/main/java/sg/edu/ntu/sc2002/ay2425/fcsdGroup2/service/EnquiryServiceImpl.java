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

    @Override
    public Optional<Enquiry> getEnquiryById(int enquiryId) {
        return enquiryRepo.getById(enquiryId);
    }

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

    @Override
    public Enquiry submitEnquiry(String message, HDBApplicant applicant, BTOProj proj) {
        return enquiryRepo.add(message, applicant, proj);
    }

    @Override
    public List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant) {
        return enquiryRepo.getByApplicant(applicant);
    }

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
                enquiryRepo.update(enquiry); // Preserve message IDs
            }
            return success;
        }
        return false;
    }

    @Override
    public boolean deleteEnquiry(int enquiryId, HDBApplicant applicant) {
        Optional<Enquiry> opt = enquiryRepo.getById(enquiryId);
        return opt.isPresent() && opt.get().getMadeBy().equals(applicant)
                && enquiryRepo.delete(enquiryId);
    }

    @Override
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepo.getAll();
    }

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

    @Override
    public List<Enquiry> getEnquiryForAssignedProj(BTOProj proj) {
        return enquiryRepo.getByProject(proj);
    }

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

    @Override
    public List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer) {
        return btoRepo.getAllProjects().stream()
                .filter(p -> List.of(p.getOfficersList()).contains(officer))
                .flatMap(p -> enquiryRepo.getByProject(p).stream())
                .toList();
    }
}
