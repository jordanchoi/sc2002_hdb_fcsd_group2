package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.EnquiryServiceImpl;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ManagerEnquiryService;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.OfficerEnquiryService;

import java.util.List;
import java.util.Optional;

public class ManagerEnquiryController {
    private final ManagerEnquiryService enquiryService = new EnquiryServiceImpl();

    public List<Enquiry> getAllEnquiries() {
        return enquiryService.getAllEnquiries();
    }

    public Optional<Enquiry> getEnquiryById(int id) {
        return enquiryService.getEnquiryById(id);
    }

    public boolean replyToEnquiry(int enquiryId, String reply, HDBManager manager) {
        return enquiryService.replyEnquiry(enquiryId, reply, manager);
    }
}
