package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.ProjectMessage;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ApplicationService;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.EnquiryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicantController implements canApplyFlat {
    private final HDBApplicant model;
    private final ApplicationService appService = new ApplicationService();
    private final EnquiryServiceImpl enquiryService = new EnquiryServiceImpl();
    private final BTORepository projRepo = BTORepository.getInstance();


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
            System.out.println("- " + project.getProjName() + " (" + project.getProjNbh() + ")");
        }
    }

    public BTOProj selectProject(List<BTOProj> availableProjects) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Available BTO Projects ===");

        if (availableProjects.isEmpty()) {
            System.out.println("No available projects found.");
            return null;
        }

        for (BTOProj project : availableProjects) {
            System.out.println(project.getProjId() + ". " + project.getProjName() + " (" + project.getProjNbh() + ")");
        }

        System.out.println("\nEnter the Project ID of the project you want to apply for, or 0 to return to the main menu:");

        int chosenProjId;
        try {
            chosenProjId = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid Project ID.");
            return null;
        }

        if (chosenProjId == 0) {
            return null;
        }

        for (BTOProj project : availableProjects) {
            if (project.getProjId() == chosenProjId) {
                return project;
            }
        }

        System.out.println("Invalid Project ID. Please try again.");
        return null;
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
            return project.getFlatUnits().containsKey(FlatTypes.TWO_ROOM);
        }
        return false;
    }

    public void applyForProject() {
        List<BTOProj> eligibleProjects = getEligibleProjs();
        BTOProj selectedProject = selectProject(eligibleProjects);
        boolean result = submitApplication(selectedProject);
        if (result){
            System.out.println("Application submitted for project: " + selectedProject.getProjName());
        }
        else {
            System.out.println("Application canceled.");
        }
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

//    public void showAllEnquiries(){
//        List<Enquiry>applicantEnquiry = enquiryService.getEnquiriesByApplicant(model);
//        if (applicantEnquiry.isEmpty()) {
//            System.out.println("No enquiries found.");
//        }
//        for (Enquiry e : applicantEnquiry) {
//            System.out.println("--------------------------------------------------");
//            System.out.println("Enquiry ID: " + e.getEnquiryId() +
//                    " | Applicant: " + e.getMadeBy().getFirstName() +
//                    " | Project: " + e.getForProj().getProjName());
//
//            for (int i = 0; i < e.getEnquiries().size(); i++) {
//                System.out.println("[" + i + "] " + e.getEnquiries().get(i).toString());
//            }
//            System.out.println("--------------------------------------------------");
//        }
//    }
//
//    public void submitEnquiry() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter the message for your enquiry: ");
//        String message = scanner.nextLine();
//
//        List<BTOProj> eligibleProjects = getEligibleProjs();
//        BTOProj selectedProject = selectProject(eligibleProjects);
//
//        if (selectedProject != null) {
//            enquiryService.submitEnquiry(message, model, selectedProject);
//            System.out.println("Enquiry submitted successfully!");
//        } else {
//            System.out.println("Enquiry submission cancelled.");
//        }
//    }
////
//    public void submitExisting() {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter the Enquiry ID to add a message to: ");
//        int enquiryId = scanner.nextInt();
//        scanner.nextLine();
//
//        System.out.println("Enter your message: ");
//        String message = scanner.nextLine();
//
//        enquiryService.addMessage(enquiryId,message,model.getNric());
//    }
//
//    public void editEnquiryMessage() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter Enquiry ID to edit message:");
//        int enquiryId = scanner.nextInt();
//        scanner.nextLine(); // consume leftover newline
//
//        Enquiry enquiry = enquiryService.getEnquiryById(enquiryId);
//
//        if (enquiry == null) {
//            System.out.println("Enquiry not found.");
//            return;
//        }
//
//        // Display all messages
//        System.out.println("Messages in this Enquiry:");
//        for (ProjectMessage message : enquiry.getEnquiries()) {
//            System.out.println("Message ID: " + message.getMessageId());
//            System.out.println("Sender: " + message.getSender().getFirstName());
//            System.out.println("Content: " + message.getContent());
//            System.out.println("---------------------------");
//        }
//
//        System.out.println("Enter Message ID you want to edit:");
//        int messageId = scanner.nextInt();
//        scanner.nextLine(); // consume leftover newline
//
//        System.out.println("Enter new message content:");
//        String newMessage = scanner.nextLine();
//
//        boolean success = enquiryService.editOwnMessage(enquiryId, messageId, model, newMessage);
//
//        if (success) {
//            System.out.println("Message updated successfully.");
//        } else {
//            System.out.println("Failed to update message. Maybe you're not the sender?");
//        }
//    }
//
//    public void deleteEnquiry() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter Enquiry ID to delete:");
//        int enquiryId = scanner.nextInt();
//        scanner.nextLine(); // consume newline
//
//        boolean success = enquiryService.deleteEnquiry(enquiryId, model);
//
//        if (success) {
//            System.out.println("Enquiry deleted successfully.");
//        } else {
//            System.out.println("Failed to delete enquiry.");
//        }
//    }
//
//    public void deleteMessageInEnquiry() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter Enquiry ID:");
//        int enquiryId = scanner.nextInt();
//        scanner.nextLine();
//
//        Enquiry enquiry = enquiryService.getEnquiryById(enquiryId);
//
//        if (enquiry == null) {
//            System.out.println("Enquiry not found.");
//            return;
//        }
//
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
