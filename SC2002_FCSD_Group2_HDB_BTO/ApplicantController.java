import java.util.List;
import java.util.Scanner;

public class ApplicantController {
    HDBApplicant model;
    ApplicantView view;
    EnquiryController enquiryController;

    public ApplicantController(HDBApplicant model, ApplicantView view){
        this.model = model;
        this.view = view;
        this.enquiryController = EnquiryController.getInstance();
    }

    public void applyProjs() {
        List<BTOProj> eligibleProjects = model.getEligibleProjs();

        BTOProj selectedProject = view.selectProject(eligibleProjects);

        if (selectedProject != null) {
            model.applyForProject(selectedProject);
            view.applyForProject(selectedProject);
        } else {
            System.out.println("Application canceled.");
        }
    }

    public void viewSubmittedEnquiries() {
        List<Enquiry> enquiries = model.getApplicantEnquiries(enquiryController);
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
        } else {
            for (Enquiry enquiry : enquiries) {
                System.out.println("Enquiry ID: " + enquiry.getEnquiryId());
                System.out.println("Enquiry Thread: ");
                for (Message msg : enquiry.getEnquiries()) {
                    System.out.println("  - " + msg.getContent());
                }
                System.out.println("-----------------------------------------");
            }
        }
    }

    public void deleteEnquiry() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Enquiry ID to delete: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();

        model.removeEnquiry(enquiryController, enquiryId);
        System.out.println("Enquiry deleted successfully.");
    }

    public void viewEligibleProjects() {
        List<BTOProj> eligibleProjects = model.getEligibleProjs();
        view.viewEligibleProjects(eligibleProjects);
    }

    public void showApplicantApplicationDetails() {
        String applicationDetails = model.viewApplicationDetails();
        view.displayApplicationDetails(applicationDetails);
    }
    

    public void submitEnquiry() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the message for your enquiry: ");
        String message = scanner.nextLine();

        List<BTOProj> eligibleProjects = model.getEligibleProjs();
        BTOProj selectedProject = view.selectProject(eligibleProjects);

        if (selectedProject != null) {
            model.submitEnquiry(enquiryController, message, selectedProject);
            System.out.println("Enquiry submitted successfully!");
        } else {
            System.out.println("Enquiry submission cancelled.");
        }
    }

    public void submitExisting() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Enquiry ID to add a message to: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter your message: ");
        String message = scanner.nextLine();

        model.submitExistingEnquiry(enquiryController, enquiryId, message);
    }

    public void editMessageInEnquiry() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the Enquiry ID: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();
    
        Enquiry enquiry = enquiryController.getEnquiryById(enquiryId);
        if (enquiry == null) {
            System.out.println("Enquiry not found.");
            return;
        }
    
        // Display messages with ID and content
        for (Message msg : enquiry.getEnquiries()) {
            if (msg.getSender() == model) {
                System.out.println("Message ID: " + msg.getMessageId());
                System.out.println("Content: " + msg.getContent());
                System.out.println("--------------------");
            }
        }
    
        System.out.print("Enter the Message ID to edit: ");
        int messageId = scanner.nextInt();
        scanner.nextLine();
    
        Message target = enquiry.getMessageById(messageId);
        if (target == null || target.getSender() != model) {
            System.out.println("Invalid message or permission denied.");
            return;
        }
    
        System.out.println("Original Message: " + target.getContent());
        System.out.print("Enter the new message: ");
        String newMessage = scanner.nextLine();
    
        if (model.editEnquiryMessage(enquiryController, enquiryId, messageId, newMessage)) {
            System.out.println("Message edited successfully.");
        } else {
            System.out.println("Failed to edit message.");
        }
    }
    

    public void withdrawApplication() {
        model.withdrawApplication();
    }
}
