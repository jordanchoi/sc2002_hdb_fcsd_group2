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

public class EnquiryController {
    private static EnquiryController instance;
    private final UserRepository userRepo = UserRepository.getInstance();
    private final BTORepository btoRepo = BTORepository.getInstance();
    private final EnquiryRepository enquiryRepository = new EnquiryRepository(userRepo, btoRepo);
    //private final List<Enquiry> enquiries;

    public EnquiryController() {
        //this.enquiries = enquiryRepository.getAllEnquiries();
    }

    public static EnquiryController getInstance() {
        if (instance == null) {
            instance = new EnquiryController();
        }
        return instance;
    }

    public void createEnquiry(String msg, HDBApplicant applicant, BTOProj project) {
        Enquiry enquiry = enquiryRepository.add(msg, applicant, project);
        //enquiries.add(enquiry);
    }

    public Enquiry getEnquiryById(int id) {
        Optional<Enquiry> e = enquiryRepository.getById(id);
        if (e.isPresent()) {
            return e.get();
        }
        return null;
    }

    public boolean addMessage(int id, String msg, User sender) {
        Enquiry e = getEnquiryById(id);
        if (e != null) {
            e.addMessage(msg, sender);
            return true;
        }
        return false;
    }

    public boolean editMessageById(int enquiryId, int messageId, User currentUser, String newContent) {
        Enquiry e = getEnquiryById(enquiryId);
        if (e != null) {
            return e.editMessageById(messageId, currentUser, newContent);
        }
        return false;
    }



    public boolean deleteEnquiry(int id) {
        if (enquiryRepository.getById(id).isPresent()) {
            enquiryRepository.delete(id);
            return true;
        }
        return false;
    }

    public List<Enquiry> listEnquiries() {
        return enquiryRepository.getAll();
    }

    public List<Enquiry> getEnquiriesByApplicant(HDBApplicant applicant) {
        return enquiryRepository.getByApplicant(applicant);
    }
}
