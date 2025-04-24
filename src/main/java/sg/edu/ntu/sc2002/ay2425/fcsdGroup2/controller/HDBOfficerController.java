package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.OfficerProjectApplication;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.ProjectMessage;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.EnquiryView;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers.OfficerViewHandler;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.RoleHandler;

public class HDBOfficerController implements canApplyFlat {
    private HDBOfficer officer;
    private BTORepository btoRepository;

    BTOProj selectedProject = null;

    public HDBOfficerController (HDBOfficer officer, BTORepository btoRepository) {
        this.officer = officer;
        this.btoRepository = btoRepository;
    }
    
    @Override
    public boolean checkEligibility(BTOProj proj) {
        //questionable need to wait for Applicant to know the different applications applied before
        //boolean appliedAsApplicant = proj.getAllApp().stream().anyMatch(app -> app.getApplicant().getUserId() == this.getUserId());
        Application currentApp = officer.getCurrentApplication();
        if (currentApp != null && proj == currentApp.getAppliedProj()) {return false;}

        LocalDateTime startNew = proj.getAppOpenDate();
        LocalDateTime endNew   = proj.getAppCloseDate();
        for (BTOProj handled : officer.getAllProj()) {
            LocalDateTime startOld = handled.getAppOpenDate();
            LocalDateTime endOld   = handled.getAppCloseDate();

            // overlap iff NOT (new ends before old starts OR new starts after old ends)
            boolean overlap = !( endNew.isBefore(startOld) || startNew.isAfter(endOld) );

            if (overlap) {return false;}
        }
        // passed both checks!
        return true;
    }

    @Override
    public boolean submitApplication(BTOProj proj) {
        // Need to be implemented.
        if (!checkEligibility(proj)) return false;
        for (OfficerProjectApplication a : officer.getRegisteredApps()) {
            if (proj.getProjName().equalsIgnoreCase(a.getProj().getProjName())) {return false;}
        }

        OfficerProjectApplication app = new OfficerProjectApplication(officer, proj, AssignStatus.PENDING);
        officer.addApps(app);

        OfficerProjectApplicationController appController = new OfficerProjectApplicationController();

        // Use the addOfficerProjectApplication function to add it to the list and save it to Excel
        appController.addOfficerProjectApplication(app);  // Adds the application to ProjectApplicationList.xlsx

        return true;
    }

    /*public boolean submitApplication(String projName) {
        BTOProj proj = findProject(projName, btoRepository);
        if (!checkEligibility(proj)) return false;
        for (OfficerProjectApplication a : officer.getRegisteredApps()) {
            if (proj.getProjName().equalsIgnoreCase(a.getProj().getProjName())) {return false;}
        }
       
        OfficerProjectApplication app = new OfficerProjectApplication(officer, proj, AssignStatus.PENDING);
        officer.addApps(app);

        OfficerProjectApplicationController appController = new OfficerProjectApplicationController();
        
        // Use the addOfficerProjectApplication function to add it to the list and save it to Excel
        appController.addOfficerProjectApplication(app);  // Adds the application to ProjectApplicationList.xlsx

        return true;

    }*/

    public String projRegStatus(String projectName) {
        for (BTOProj proj : btoRepository.getAllProjects()) {
            if (proj.getProjName().equalsIgnoreCase(projectName)) {
                for (HDBOfficer o : proj.getOfficersList()) {
                    if (o.equals(officer)) {return "Registered";} else {return "Not Registered";}
                }
            }
        }
        return "Project not found";
    }

    public java.util.List<String> viewProjDetails(BTOProj project) {
        java.util.List<String> details = new java.util.ArrayList<>();
        if (project == null) {return details;}

        details.add("Project ID: " + project.getProjId());
        details.add("Project Name: " + project.getProjName());
        details.add("Status: " + project.getProjStatus());
        // Additional details can be appended as required.
        return details;
    }

    public void viewEnquiries() {
        SessionStateManager session = SessionStateManager.getInstance();
        HDBOfficer officer = (HDBOfficer) session.getLoggedInUser();

        // Let the OfficerViewHandler handle project selection and enquiry actions
        RoleHandler handler = new OfficerViewHandler(officer);
        EnquiryView enquiryView = new EnquiryView(handler);
        enquiryView.display();
    }


    public BTOProj findProject(String projName, BTORepository repo) {
        BTOProj proj = null;
        for (BTOProj p : repo.getAllProjects()) {
            if (projName.equalsIgnoreCase(p.getProjName()))  {proj = p;}
        }
        return proj;
    }

    public void replyEnquiries() {
        Scanner scanner = new Scanner(System.in);
        
        // Ask the officer to select an enquiry to reply to
        System.out.print("Enter the enquiry ID to reply to: ");
        int enquiryId = scanner.nextInt(); // Officer selects which enquiry to reply to
        
        // Get the selected enquiry from EnquiryController
        EnquiryController enquiryController = EnquiryController.getInstance();
        Enquiry selectedEnquiry = enquiryController.getEnquiryById(enquiryId);  // Retrieve enquiry by ID
        
        if (selectedEnquiry == null) {
            System.out.println("Enquiry not found.");
            return;  // Exit if enquiry not found
        }
    
        // Check that the officer is assigned to the project the enquiry is related to
        BTOProj project = selectedEnquiry.getForProj();
        boolean officerAssigned = false;
    
        // Iterate over the officersList (which is an array) to check if the officer is handling this project
        for (HDBOfficer officerInList : project.getOfficersList()) {
            if (officerInList.equals(officer)) {
                officerAssigned = true;
                break;
            }
        }
    
        if (!officerAssigned) {
            System.out.println("You are not assigned to this project, cannot reply.");
            return;  // Exit if the officer is not handling the project
        }
    
        // Ask the officer to enter the message ID they want to reply to
        System.out.print("Enter the message ID to reply to: ");
        int messageId = scanner.nextInt();
        
        // Get the message from the enquiry by messageId
        ProjectMessage selectedMessage = selectedEnquiry.getMessageById(messageId);
        
        if (selectedMessage == null) {
            System.out.println("Message not found.");
            return;  // Exit if message with that ID is not found
        }
    
        // Ask for the reply message content
        System.out.print("Enter your reply message: ");
        scanner.nextLine(); // Consume the leftover newline character
        String replyContent = scanner.nextLine();
        
        // Use EnquiryController to add the reply message to the selected enquiry
        boolean success = enquiryController.addMessage(enquiryId, replyContent, officer);  // Add reply to the message thread
        
        if (success) {
            System.out.println("Your reply has been added to the enquiry thread.");
        } else {
            System.out.println("Failed to add your reply.");
        }
    }
    
}
