package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.EnquiryController;

public class EnquiryView {
    public void showEnquiry(Enquiry e) {
    /* REMOVED BY JORDAN - START AFRESH
        if (e == null) {
            System.out.println("model.entities.Enquiry not found.");
            return;
        }

        System.out.println("model.entities.Enquiry ID: " + e.getEnquiryId());
        System.out.println("Message: " + e.getEnquiry());
        System.out.println("Reply: " + (e.getReply() != null ? e.getReply() : "[No reply yet]"));
    */
    }

    public void submitEnquiry(String msg, EnquiryController controller) {
    /* Removed by Jordan - Start from afresh
        controller.createEnquiry(msg);
        System.out.println("model.entities.Enquiry submitted.");
    */
    }
}
