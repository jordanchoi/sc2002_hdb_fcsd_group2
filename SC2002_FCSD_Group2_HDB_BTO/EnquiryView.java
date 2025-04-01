public class EnquiryView {
    public void showEnquiry(Enquiry e) {
        if (e == null) {
            System.out.println("Enquiry not found.");
            return;
        }

        System.out.println("Enquiry ID: " + e.getEnquiryId());
        System.out.println("Message: " + e.getEnquiry());
        System.out.println("Reply: " + (e.getReply() != null ? e.getReply() : "[No reply yet]"));
    }

    public void submitEnquiry(String msg, EnquiryController controller) {
        controller.createEnquiry(msg);
        System.out.println("Enquiry submitted.");
    }
}
