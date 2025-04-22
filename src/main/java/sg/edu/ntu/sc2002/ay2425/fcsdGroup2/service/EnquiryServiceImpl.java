package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.EnquiryRepository;

import java.util.List;
import java.util.Optional;

public class EnquiryServiceImpl implements ApplicantEnquiryService, OfficerEnquiryService, ManagerEnquiryService {
    // EnquiryRepository enquiryRepository = new EnquiryRepository();
    // Common Enquiry Service Methods
    @Override
    public Optional<Enquiry> getEnquiryById(int enquiryId) {
        return Optional.empty();
    }

    @Override
    public boolean addMessage(int enquiryId, String msg, String senderNric) {
        return false;
    }

    // Applicant Enquiry Service Methods
    @Override
    public Enquiry submitEnquiry(String message, HDBApplicant applicant, BTOProj proj) {
        return null;
    }

    @Override
    public List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant) {
        return List.of();
    }

    @Override
    public boolean editOwnMessage(int enquiryId, String msgId, HDBApplicant applicant, String newContent) {
        return false;
    }

    @Override
    public boolean deleteEnquiry(int enquiryId, HDBApplicant applicant) {
        return false;
    }

    // Manager Enquiry Service Methods
    @Override
    public List<Enquiry> getAllEnquiries() {
        return List.of();
    }

    // Officer Enquiry Service Methods
    @Override
    public List<Enquiry> getEnquiryForAssignedProj(BTOProj proj) {
        return List.of();
    }

    @Override
    public boolean replyEnquiry(int enquiryId, String reply, HDBOfficer officer) {
        return false;
    }

    @Override
    public List<Enquiry> getEnquiriesByOfficer(HDBOfficer officer) {
        return List.of();
    }
}
