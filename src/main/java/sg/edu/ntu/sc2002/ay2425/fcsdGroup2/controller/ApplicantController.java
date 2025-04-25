package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ApplicationService;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.EnquiryServiceImpl;

import java.util.*;

public class ApplicantController implements canApplyFlat {
    private final HDBApplicant model;
    private final ApplicationService appService = new ApplicationService();
    private final EnquiryServiceImpl enquiryService = new EnquiryServiceImpl();
    private final BTORepository projRepo = BTORepository.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public ApplicantController(HDBApplicant model) {
        this.model = model;
    }

    public void withdrawApplication() {
        appService.withdrawApplication(model);
    }

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


    public void applyForProject() {
        List<BTOProj> eligibleProjects = getEligibleProjs();
        BTOProj selectedProject = selectProject(eligibleProjects);
        if (selectedProject == null) {
            System.out.println("Returning to main menu.");
            return;
        }
        submitApplication(selectedProject);
    }

    @Override
    public boolean submitApplication(BTOProj selectedProject) {
        if (selectedProject != null) {
            appService.applyForProject(model, selectedProject);
            return true;
        }
        return false;
    }

    public void showApplicantApplicationDetails() {
        String applicationDetails = appService.viewApplicationDetails(model);
        System.out.println(applicationDetails);
    }

    public void showAllEnquiries() {
        List<Enquiry> applicantEnquiry = enquiryService.getEnquiriesByApplicant(model);
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

    public void submitEnquiry() {
        System.out.println("Enter the message for your enquiry (or 0 to return): ");
        String message = scanner.nextLine();
        if (message.equals("0")) {
            System.out.println("Returning to main menu.");
            return;
        }

        List<BTOProj> eligibleProjects = getEligibleProjs();
        BTOProj selectedProject = selectProject(eligibleProjects);

        if (selectedProject != null) {
            enquiryService.submitEnquiry(message, model, selectedProject);
            System.out.println("Enquiry submitted successfully!");
        } else {
            System.out.println("Enquiry submission cancelled.");
        }
    }

    public void submitExisting() {
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

        enquiryService.addMessage(enquiryId, message, model.getNric());
    }

    public void editEnquiryMessage() {
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
            if (message.getSender().equals(model)) {
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

        boolean success = enquiryService.editOwnMessage(enquiryId, messageIdStr, model, newMessage);
        System.out.println(success ? "Message updated successfully." : "Failed to update message.");
    }

    public void deleteEnquiry() {
        Integer enquiryId = promptForInt("Enter Enquiry ID to delete (0 to return):");
        if (enquiryId == null || enquiryId == 0) return;

        Optional<Enquiry> enquiryOpt = enquiryService.getEnquiryById(enquiryId);
        if (enquiryOpt.isEmpty()) {
            System.out.println("Enquiry ID not found.");
            return;
        }

        Enquiry enquiry = enquiryOpt.get();

        // Check if any message is not by the applicant
        boolean hasRepliesFromOthers = enquiry.getEnquiries().stream()
                .anyMatch(msg -> !msg.getSender().equals(model));

        if (hasRepliesFromOthers) {
            System.out.println("Cannot delete enquiry. It has replies from a manager or officer.");
            return;
        }

        boolean success = enquiryService.deleteEnquiry(enquiryId, model);
        System.out.println(success ? "Enquiry deleted successfully." : "Failed to delete enquiry.");
    }


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
