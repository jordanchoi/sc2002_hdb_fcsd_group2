package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;

import java.util.List;
import java.util.Optional;

public interface BaseEnquiryService {
    Optional<Enquiry> getEnquiryById(int enquiryId);
    boolean addMessage(int enquiryId, String msg, String senderNric);
}
