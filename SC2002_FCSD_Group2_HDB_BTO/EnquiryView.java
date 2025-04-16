import java.util.List;
import java.util.Scanner;

public class EnquiryView {
    private EnquiryController controller;
    private Scanner scanner;

    public EnquiryView(EnquiryController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void showAllEnquiries() {
        List<Enquiry> enquiries = controller.listEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        for (Enquiry e : enquiries) {
            System.out.println("--------------------------------------------------");
            System.out.println("Enquiry ID: " + e.getEnquiryId() +
                               " | Applicant: " + e.getMadeBy().getFirstName() +
                               " | Project: " + e.getForProj().getProjName());

            for (int i = 0; i < e.getEnquiries().size(); i++) {
                System.out.println("[" + i + "] " + e.getEnquiries().get(i).toString());
            }
            System.out.println("--------------------------------------------------");
        }
    }

    public void submitEnquiry(HDBApplicant applicant, BTOProj project) {
        System.out.print("Enter enquiry message: ");
        String msg = scanner.nextLine();
        controller.createEnquiry(msg, applicant, project);
        System.out.println("Enquiry submitted.");
    }

    public void addMessage(User sender) {
        System.out.print("Enter Enquiry ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter your message: ");
        String msg = scanner.nextLine();

        if (controller.addMessage(id, msg, sender)) {
            System.out.println("Message added.");
        } else {
            System.out.println("Enquiry not found.");
        }
    }

    public void editMessageById(User currentUser) {
        System.out.print("Enter Enquiry ID: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();
    
        System.out.print("Enter Message ID to edit: ");
        int msgId = scanner.nextInt();
        scanner.nextLine();
    
        Enquiry enquiry = controller.getEnquiryById(enquiryId);
        if (enquiry == null) {
            System.out.println("Enquiry not found.");
            return;
        }
    
        Message msgToEdit = null;
        for (Message m : enquiry.getEnquiries()) {
            if (m.getMessageId() == msgId) {
                msgToEdit = m;
                break;
            }
        }
    
        if (msgToEdit == null) {
            System.out.println("Message ID not found.");
            return;
        }
    
        if (!msgToEdit.getSender().equals(currentUser)) {
            System.out.println("You can only edit your own messages.");
            return;
        }
    
        System.out.println("Current message: " + msgToEdit.getContent());
        System.out.print("Enter new message content: ");
        String newMsg = scanner.nextLine();
    
        if (controller.editMessageById(enquiryId, msgId, currentUser, newMsg)) {
            System.out.println("Message updated.");
        } else {
            System.out.println("Failed to update message.");
        }
    }
    
    
}
