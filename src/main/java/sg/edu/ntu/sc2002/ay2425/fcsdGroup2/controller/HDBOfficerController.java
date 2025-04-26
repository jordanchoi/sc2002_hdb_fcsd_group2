package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import java.time.LocalDateTime;
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

/**
 * Controller class for managing HDB Officer operations.
 * Handles project applications, project registration statuses, and replying to project enquiries.
 */
public class HDBOfficerController implements canApplyFlat {
    private HDBOfficer officer;
    private BTORepository btoRepository;

    BTOProj selectedProject = null;

    /**
     * Constructs the HDBOfficerController with officer and repository references.
     *
     * @param officer the logged-in officer
     * @param btoRepository the project repository
     */
    public HDBOfficerController (HDBOfficer officer, BTORepository btoRepository) {
        this.officer = officer;
        this.btoRepository = btoRepository;
    }

    /**
     * Checks if the officer is eligible to apply for the given project.
     *
     * @param proj the project
     * @return true if eligible
     */
    @Override
    public boolean checkEligibility(BTOProj proj) {
        //questionable need to wait for Applicant to know the different applications applied before
        //boolean appliedAsApplicant = proj.getAllApp().stream().anyMatch(app -> app.getApplicant().getUserId() == this.getUserId());
        Application currentApp = officer.getCurrentApplication();
        if (currentApp != null && proj == currentApp.getAppliedProj()) {return false;}

        OfficerProjectApplicationController officerAppController = new OfficerProjectApplicationController();
        List<OfficerProjectApplication> officerAppList = officerAppController.getApplicationsByOfficer(officer.getNric());
        for(OfficerProjectApplication officerApp : officerAppList) {
            if(officerApp.getProj() == proj) {
                System.out.println("There is an existing application for project " + proj.getProjName());
                return false;
            }
        }

        HDBOfficer[] officerList = proj.getOfficersList();
        for (HDBOfficer newOfficer : officerList) {
            if (officer.getNric().equalsIgnoreCase(newOfficer.getNric())) {
                System.out.println("Already handling project " + proj.getProjName());
                return false;
            }
        }

        LocalDateTime startNew = proj.getAppOpenDate();
        LocalDateTime endNew   = proj.getAppCloseDate();
        for (BTOProj handled : officer.getAllProj()) {
            LocalDateTime startOld = handled.getAppOpenDate();
            LocalDateTime endOld   = handled.getAppCloseDate();

            // overlap iff NOT (new ends before old starts OR new starts after old ends)
            boolean overlap = !( endNew.isBefore(startOld) || startNew.isAfter(endOld) );

            if (overlap) {
                System.out.println("application date overlaps with existing handled project " + handled.getProjName());
                return false;
            }
        }

        for (OfficerProjectApplication a : officerAppList) {
            LocalDateTime startOld = a.getProj().getAppOpenDate();
            LocalDateTime endOld   = a.getProj().getAppCloseDate();

            // overlap iff NOT (new ends before old starts OR new starts after old ends)
            boolean overlap = !( endNew.isBefore(startOld) || startNew.isAfter(endOld) );

            if (overlap) {
                System.out.println("application date overlaps with existing applied project " + a.getProj().getProjName());
                return false;
            }
        }

        // passed both checks!
        return true;
    }

    /**
     * Submits an officer project application if eligible.
     *
     * @param proj the project to apply for
     * @return true if application submitted successfully
     */
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

    /**
     * Displays the registration status of all projects the officer has applied to.
     *
     * @param nric the officer's NRIC
     */
    public void projRegStatus(String nric) {
        OfficerProjectApplicationController appController = new OfficerProjectApplicationController();
        List<OfficerProjectApplication> appList = appController.getApplicationsByOfficer(nric);
        System.out.println("=== Status List of BTO Projects Applied ===");
        System.out.printf("%-15s %-20s %-15s %-20s%n", "Project ID", "Project Name", "Application ID", "Application Status");
        System.out.println("----------------------------------------------------------------------------------------");
        for (OfficerProjectApplication officerProjApp : appList) {
            System.out.printf("%-15d %-20s %-15d %-20s%n",
                officerProjApp.getProj().getProjId(),
                officerProjApp.getProj().getProjName(),
                officerProjApp.getOfficerAppId(),
                officerProjApp.getAssignmentStatus());
        }
    }

    /**
     * Displays detailed information about a project.
     *
     * @param project the project
     * @return list of details
     */
    public java.util.List<String> viewProjDetails(BTOProj project) {
        java.util.List<String> details = new java.util.ArrayList<>();
        if (project == null) {return details;}

        details.add("Project ID: " + project.getProjId());
        details.add("Project Name: " + project.getProjName());
        details.add("Status: " + project.getProjStatus());
        // Additional details can be appended as required.
        return details;
    }

    /**
     * Displays the list of enquiries related to the officer's assigned projects.
     */
    public void viewEnquiries() {
        SessionStateManager session = SessionStateManager.getInstance();
        HDBOfficer officer = (HDBOfficer) session.getLoggedInUser();

        // Let the OfficerViewHandler handle project selection and enquiry actions
        RoleHandler handler = new OfficerViewHandler(officer);
        EnquiryView enquiryView = new EnquiryView(handler);
        enquiryView.display();
    }


    /**
     * Finds a project by name in the repository.
     *
     * @param projName the project name
     * @param repo the repository
     * @return found project or null
     */
    public BTOProj findProject(String projName, BTORepository repo) {
        BTOProj proj = null;
        for (BTOProj p : repo.getAllProjects()) {
            if (projName.equalsIgnoreCase(p.getProjName()))  {proj = p;}
        }
        return proj;
    }

    /**
     * Allows the officer to reply to enquiries linked to projects they handle.
     */
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
