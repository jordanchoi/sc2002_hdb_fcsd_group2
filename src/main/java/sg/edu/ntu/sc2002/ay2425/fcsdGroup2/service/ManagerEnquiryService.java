package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import java.util.List;

public interface ManagerEnquiryService extends BaseEnquiryService {
    List<Enquiry> getAllEnquiries();
}
