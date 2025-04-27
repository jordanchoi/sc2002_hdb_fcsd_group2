package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.ApplicantController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.EnquiryServiceImpl;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ApplicantEnquiryService;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.RoleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Handles the applicant's view for managing project enquiries.
 * Allows applicants to create, view, add, edit and delete to enquiries.
 */
public class ApplicantViewHandler implements RoleHandler {
    private final HDBApplicant applicant;
    private final ApplicantController applicantController;
    private final ApplicantEnquiryService enquiryService = new EnquiryServiceImpl();
    private final Scanner scanner = new Scanner(System.in);
    /**
     * Constructs a new ApplicantViewHandler.
     *
     * @param applicant the HDB applicant using the system
     * @param applicantController the controller to manage applicant-related operations
     */
    public ApplicantViewHandler(HDBApplicant applicant, ApplicantController applicantController) {
        this.applicant = applicant;
        this.applicantController = applicantController;
    }

    /**
     * Displays the menu options for enquiry management and handles user interaction.
     */
    @Override
    public void displayEnquiryOptions() {
        int choice;
        do {
            System.out.println("\n=== Enquiry Menu ===");
            System.out.println("1. Submit new enquiry");
            System.out.println("2. Add message to existing enquiry");
            System.out.println("3. View enquiries");
            System.out.println("4. Edit enquiry message");
            System.out.println("5. Delete entire enquiry");
            System.out.println("6. Back to main menu");

            choice = promptForInt("Please select an enquiry option: ");

            switch (choice) {
                case 1 -> submitEnquiry();
                case 2 -> submitExisting();
                case 3 -> showAllEnquiries();
                case 4 -> editEnquiryMessage();
                case 5 -> deleteEnquiry();
                case 6 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 6);
    }

    /**
     * Displays all enquiries submitted by the current applicant.
     */
    private void showAllEnquiries() {
        List<Enquiry> applicantEnquiry = enquiryService.getEnquiriesByApplicant(applicant);
        if (applicantEnquiry.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }
        for (Enquiry e : applicantEnquiry) {
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

    /**
     * Submits a new enquiry linked to a selected eligible project.
     * Prompts the applicant for a message and project selection.
     */
    private void submitEnquiry() {
        System.out.println("Enter the message for your enquiry (or 0 to return): ");
        String message = scanner.nextLine();
        if (message.equals("0")) {
            System.out.println("Returning to main menu.");
            return;
        }

        List<BTOProj> eligibleProjects = applicantController.getEligibleProjs();
        BTOProj selectedProject = applicantController.selectProject(eligibleProjects);

        if (selectedProject != null) {
            enquiryService.submitEnquiry(message, applicant, selectedProject);
            System.out.println("Enquiry submitted successfully!");
        } else {
            System.out.println("Enquiry submission cancelled.");
        }
    }

    /**
     * Adds a new message to an existing enquiry.
     * Applicant must provide a valid enquiry ID and a new message.
     */
    private void submitExisting() {
        Integer enquiryId = promptForInt("Enter the Enquiry ID to add a message to (0 to return): ");
        if (enquiryId == null || enquiryId == 0) return;

        Optional<Enquiry> enquiry = enquiryService.getEnquiryById(enquiryId);
        if (enquiry.isEmpty()) {
            System.out.println("Enquiry ID not found.");
            return;
        }

        System.out.println("Enter your message (or 0 to cancel): ");
        String message = scanner.nextLine();
        if (message.equals("0")) return;

        enquiryService.addMessage(enquiryId, message, applicant.getNric());
    }

    /**
     * Edits an existing message in an enquiry submitted by the applicant.
     * Applicant must specify the Enquiry ID and Message ID to edit their own message.
     */
    private void editEnquiryMessage() {
        Integer enquiryId = promptForInt("Enter Enquiry ID to edit message (0 to return):");
        if (enquiryId == null || enquiryId == 0) return;

        Optional<Enquiry> enquiryOpt = enquiryService.getEnquiryById(enquiryId);
        if (enquiryOpt.isEmpty()) {
            System.out.println("Enquiry ID not found.");
            return;
        }

        Enquiry enquiry = enquiryOpt.get();

        System.out.println("Your messages in this enquiry:");
        List<ProjectMessage> ownMessages = new ArrayList<>();
        for (ProjectMessage message : enquiry.getEnquiries()) {
            if (message.getSender().equals(applicant)) {
                System.out.println("Message ID: " + message.getMessageId());
                System.out.println("Content: " + message.getContent());
                System.out.println("---------------------------");
                ownMessages.add(message);
            }
        }

        if (ownMessages.isEmpty()) {
            System.out.println("You have no messages in this enquiry.");
            return;
        }

        System.out.println("Enter Message ID you want to edit (or 0 to return):");
        String messageIdStr = scanner.nextLine();
        if (messageIdStr.equals("0")) return;

        int parsedMessageId;
        try {
            parsedMessageId = Integer.parseInt(messageIdStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Message ID.");
            return;
        }

        boolean isValid = ownMessages.stream().anyMatch(m -> m.getMessageId() == parsedMessageId);
        if (!isValid) {
            System.out.println("Invalid Message ID.");
            return;
        }

        System.out.println("Enter new message content (or 0 to cancel):");
        String newMessage = scanner.nextLine();
        if (newMessage.equals("0")) return;

        boolean success = enquiryService.editOwnMessage(enquiryId, messageIdStr, applicant, newMessage);
        System.out.println(success ? "Message updated successfully." : "Failed to update message.");
    }

    /**
     * Deletes an enquiry created by the applicant, provided no officers or managers have replied to it.
     */
    private void deleteEnquiry() {
        Integer enquiryId = promptForInt("Enter Enquiry ID to delete (0 to return):");
        if (enquiryId == null || enquiryId == 0) return;

        Optional<Enquiry> enquiryOpt = enquiryService.getEnquiryById(enquiryId);
        if (enquiryOpt.isEmpty()) {
            System.out.println("Enquiry ID not found.");
            return;
        }

        Enquiry enquiry = enquiryOpt.get();

        if (!enquiry.getMadeBy().equals(applicant)) {
            System.out.println("You can only delete your own enquiries.");
            return;
        }

        boolean hasRepliesFromOthers = enquiry.getEnquiries().stream()
                .anyMatch(msg -> !msg.getSender().equals(applicant));

        if (hasRepliesFromOthers) {
            System.out.println("Cannot delete enquiry. It has replies from a manager or officer.");
            return;
        }

        boolean success = enquiryService.deleteEnquiry(enquiryId, applicant);
        System.out.println(success ? "Enquiry deleted successfully." : "Failed to delete enquiry.");
    }

    /**
     * Prompts the user to enter an integer, validating input until a correct integer is entered.
     *
     * @param promptMessage the message displayed to prompt the user
     * @return the integer value entered by the user
     */
    private Integer promptForInt(String promptMessage) {
        while (true) {
            System.out.println(promptMessage);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
