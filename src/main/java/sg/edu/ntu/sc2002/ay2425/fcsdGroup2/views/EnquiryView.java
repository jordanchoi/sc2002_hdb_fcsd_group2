package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.User;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.EnquiryController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.RoleHandler;

import java.util.List;
import java.util.Scanner;

/**
 * Represents the view for handling enquiries for different user roles.
 * Delegates display and action control to the associated role handler.
 */
public class EnquiryView {
    private final RoleHandler roleHandler;

    /**
     * Constructs an EnquiryView with a specific role handler.
     *
     * @param roleHandler the handler for enquiry-related actions based on user role
     */
    public EnquiryView(RoleHandler roleHandler) {
        this.roleHandler = roleHandler;
    }

    /**
     * Displays enquiry options according to the role's permissions.
     */
    public void display() {
        roleHandler.displayEnquiryOptions();
    }

    /* Commented out by Jordan because it is very problematic
    private final EnquiryController controller;
    private final Scanner scanner;

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

     */
}
