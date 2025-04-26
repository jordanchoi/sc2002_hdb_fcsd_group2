package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ApplicationService;

import java.util.*;

/**
 * Controller class for managing applicant actions.
 * Handles project applications, enquiries, and eligibility checking for HDB applicants.
 */
public class ApplicantController implements canApplyFlat {
    private final HDBApplicant model;
    private final ApplicationService appService = new ApplicationService();

    private final BTORepository projRepo = BTORepository.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    private final EnquiryServiceImpl enquiryService = new EnquiryServiceImpl();

    /**
     * Constructs the ApplicantController with a given applicant model.
     *
     * @param model the HDBApplicant
     */
  
    public ApplicantController(HDBApplicant model) {
        this.model = model;
    }

    /** Withdraws the current application of the applicant. */
    public void withdrawApplication() {
        appService.withdrawApplication(model);
    }

    /** Displays all BTO projects the applicant is eligible to apply for. */
    public void viewEligibleProjects() {
        List<BTOProj> eligibleProjects = getEligibleProjs();
        System.out.println("\n=== Eligible BTO Projects ===");

        if (eligibleProjects.isEmpty()) {
            System.out.println("No eligible projects found.");
            return;
        }

        for (BTOProj project : eligibleProjects) {
            System.out.println("---------------------------------------");
            System.out.println("Project ID          : " + project.getProjId());
            System.out.println("Project Name        : " + project.getProjName());
            System.out.println("Neighbourhood       : " + project.getProjNbh());
            System.out.println("Application Opens   : " + project.getAppOpenDate());
            System.out.println("Application Closes  : " + project.getAppCloseDate());
            System.out.println("Available Flat Types:");
            for (Map.Entry<FlatTypes, FlatType> entry : project.getFlatUnits().entrySet()) {
                FlatType ft = entry.getValue();
                System.out.printf("  - %s: %d units available\n", ft.getTypeName(), ft.getUnitsAvail());

            }

            System.out.println("---------------------------------------\n");
        }
    }

    /**
     * Allows the applicant to select a project from a list.
     *
     * @param availableProjects list of available BTO projects
     * @return selected BTO project or null
     */

    public BTOProj selectProject(List<BTOProj> availableProjects) {
        System.out.println("\n=== Available BTO Projects ===");

        if (availableProjects.isEmpty()) {
            System.out.println("No available projects found.");
            return null;
        }

        for (BTOProj project : availableProjects) {
            System.out.println(project.getProjId() + ". " + project.getProjName() + " (" + project.getProjNbh() + ")");
        }

        while (true) {
            System.out.println("\nEnter the Project ID of the project you want to apply for, or 0 to return:");
            String input = scanner.nextLine();

            try {
                int chosenProjId = Integer.parseInt(input);
                if (chosenProjId == 0) return null;

                for (BTOProj project : availableProjects) {
                    if (project.getProjId() == chosenProjId) {
                        return project;
                    }
                }
                System.out.println("Invalid Project ID. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Retrieves a list of BTO projects the applicant is eligible for.
     *
     * @return list of eligible BTO projects
     */
    public List<BTOProj> getEligibleProjs() {
        List<BTOProj> allProjs = projRepo.getAllProjects();
        List<BTOProj> eligibleProjects = new ArrayList<>();

        for (BTOProj project : allProjs) {
            if (checkEligibility(project)) {
                eligibleProjects.add(project);
            }
        }
        return eligibleProjects;
    }

    /**
     * Checks if the applicant is eligible for a given project.
     *
     * @param project BTO project
     * @return true if eligible
     */
    @Override
    public boolean checkEligibility(BTOProj project) {
        if (project.getProjStatus() == ProjStatus.CLOSED) {
            return false;
        }

        int age = model.getAge();
        MaritalStatus maritalStatus = model.getMaritalStatus();

        if (maritalStatus == MaritalStatus.MARRIED && age >= 21) {
            return true;
        } else if (maritalStatus == MaritalStatus.SINGLE && age >= 35) {
            FlatType twoRoom = project.getFlatUnits().get(FlatTypes.TWO_ROOM);
            return twoRoom != null && twoRoom.getUnitsAvail() > 0;
        }
        return false;
    }


    /** Guides applicant through project application flow. */
    public void applyForProject() {
        List<BTOProj> eligibleProjects = getEligibleProjs();
        BTOProj selectedProject = selectProject(eligibleProjects);
        if (selectedProject == null) {
            System.out.println("Returning to main menu.");
            return;
        }
        submitApplication(selectedProject);
    }

    /**
     * Submits an application to the selected project.
     *
     * @param selectedProject project to apply for
     * @return true if submission successful
     */
    @Override
    public boolean submitApplication(BTOProj selectedProject) {
        if (selectedProject != null) {
            appService.applyForProject(model, selectedProject);
            return true;
        }
        return false;
    }

    /** Displays the details of the current application. */
    public void showApplicantApplicationDetails() {
        String applicationDetails = appService.viewApplicationDetails(model);
        System.out.println(applicationDetails);
    }

    /** Displays all enquiries made by the applicant. */
    public void showAllEnquiries(){
        List<Enquiry> applicantEnquiry = enquiryService.getEnquiriesByApplicant(model);
        if (applicantEnquiry.isEmpty()) {
            System.out.println("No enquiries found.");
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

    /** Submits a new enquiry regarding a selected project. */
    public void submitEnquiry() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the message for your enquiry: ");
        String message = scanner.nextLine();

        List<BTOProj> eligibleProjects = getEligibleProjs();
        BTOProj selectedProject = selectProject(eligibleProjects);

        if (selectedProject != null) {
            enquiryService.submitEnquiry(message, model, selectedProject);
            System.out.println("Enquiry submitted successfully!");
        } else {
            System.out.println("Enquiry submission cancelled.");
        }
    }

    /** Submits a message to an existing enquiry. */
    public void submitExisting() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Enquiry ID to add a message to: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter your message: ");
        String message = scanner.nextLine();

        enquiryService.addMessage(enquiryId, message, model.getNric());
    }

    /** Edits a message in an existing enquiry. */
    public void editEnquiryMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Enquiry ID to edit message:");
        int enquiryId = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline

        Optional<Enquiry> enquiry = enquiryService.getEnquiryById(enquiryId);

        if (enquiry.isEmpty()) {
            System.out.println("Enquiry not found.");
            return;
        }

        System.out.println("Messages in this Enquiry:");
        for (ProjectMessage message : enquiry.get().getEnquiries()) {
            System.out.println("Message ID: " + message.getMessageId());
            System.out.println("Sender: " + message.getSender().getFirstName());
            System.out.println("Content: " + message.getContent());
            System.out.println("---------------------------");
        }

        System.out.println("Enter Message ID you want to edit:");
        String messageId = scanner.nextLine();

        System.out.println("Enter new message content:");
        String newMessage = scanner.nextLine();

        boolean success = enquiryService.editOwnMessage(enquiryId, messageId, model, newMessage);

        if (success) {
            System.out.println("Message updated successfully.");
        } else {
            System.out.println("Failed to update message. Maybe you're not the sender?");
        }
    }

    /** Deletes an entire enquiry made by the applicant. */
    public void deleteEnquiry() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Enquiry ID to delete:");
        int enquiryId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        boolean success = enquiryService.deleteEnquiry(enquiryId, model);

        if (success) {
            System.out.println("Enquiry deleted successfully.");
        } else {
            System.out.println("Failed to delete enquiry.");
        }
    }

//    public void deleteMessageInEnquiry() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter Enquiry ID:");
//        int enquiryId = scanner.nextInt();
//        scanner.nextLine();
//
//        Optional<Enquiry> enquiryOpt = enquiryService.getEnquiryById(enquiryId);
//        if (enquiryOpt.isEmpty()) {
//            System.out.println("Enquiry not found.");
//            return;
//        }
//
//        Enquiry enquiry = enquiryOpt.get();
//
//        System.out.println("Your messages in this enquiry:");
//        for (ProjectMessage message : enquiry.getEnquiries()) {
//            if (message.getSender().equals(model)) {
//                System.out.println("Message ID: " + message.getMessageId());
//                System.out.println("Content: " + message.getContent());
//                System.out.println("-----------------------");
//            }
//        }
//
//        System.out.println("Enter Message ID to delete:");
//        int messageId = scanner.nextInt();
//        scanner.nextLine();
//
//        boolean success = enquiryService.deleteMessageInEnquiry(enquiryId, messageId, model);
//
//        if (success) {
//            System.out.println("Message deleted successfully.");
//        } else {
//            System.out.println("Failed to delete message. You may not be the sender.");
//        }
//    }
}
