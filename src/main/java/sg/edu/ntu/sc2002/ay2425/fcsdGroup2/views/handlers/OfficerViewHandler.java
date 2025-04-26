package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.ProjectMessage;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.EnquiryServiceImpl;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.OfficerEnquiryService;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.RoleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Handles the officer-side view for managing project enquiries.
 * Allows officers to view and reply to enquiries for their assigned projects.
 */
public class OfficerViewHandler implements RoleHandler {
    private final HDBOfficer officer;
    private final OfficerEnquiryService enquiryService = new EnquiryServiceImpl();
    private final Scanner scanner = new Scanner(System.in);


    /**
     * Constructs an OfficerViewHandler with the given officer.
     *
     * @param officer the HDB officer using this handler
     */
    public OfficerViewHandler(HDBOfficer officer) {
        this.officer = officer;
    }

    /**
     * Displays the menu for officer enquiry management.
     * Provides options to view project enquiries or reply to them.
     */
    @Override
    public void displayEnquiryOptions() {
        while (true) {
            System.out.println("\n== Officer Enquiries ==");
            System.out.println("1. View My Project Enquiries");
            System.out.println("2. Reply to My Project Enquiry");
            System.out.println("0. Back");

            System.out.print("\nPlease select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> displayOwnProjectEnquiries();
                case "2" -> replyToProjectEnquiry();
                case "0" -> { return; }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Displays all project enquiries related to the officer's assigned projects.
     */
    private void displayOwnProjectEnquiries() {
        String officerNric = officer.getNric().trim();

        // Match projects where this officer is assigned in the officersList
        List<BTOProj> myProjects = new ArrayList<>();
        for (BTOProj project : BTORepository.getInstance().getAllProjects()) {
            HDBOfficer[] assigned = project.getOfficersList();
            if (assigned != null) {
                for (HDBOfficer o : assigned) {
                    if (o.getNric().equalsIgnoreCase(officerNric)) {
                        myProjects.add(project);
                        break;
                    }
                }
            }
        }

        // Get all enquiries from matched projects
        List<Enquiry> own = myProjects.stream()
                .flatMap(p -> enquiryService.getEnquiryForAssignedProj(p).stream())
                .toList();

        if (own.isEmpty()) {
            System.out.println("No enquiries found for your projects or you are not assigned to any.");
        } else {
            for (Enquiry enq : own) {
                System.out.println("==================================================");
                System.out.println("Enquiry #" + enq.getEnquiryId());
                System.out.println("Project : " + enq.getForProj().getProjName());
                System.out.println("Started by : " + enq.getMadeBy().getFirstName());
                System.out.println();

                for (ProjectMessage msg : enq.getEnquiries()) {
                    System.out.println("[" + msg.getSender().getFirstName() + "] " + msg.getContent());
                    System.out.println();
                }

                System.out.println("--------------------------------------------------");
            }
        }
    }

    /**
     * Displays officer's enquiries and prompts to send a reply
     * to a selected enquiry.
     */
    private void replyToProjectEnquiry() {
        displayOwnProjectEnquiries();
        System.out.print("Enter Enquiry ID to reply: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();

        boolean success = enquiryService.replyEnquiry(id, reply, officer);
        System.out.println(success ? "Reply sent." : "Failed — you are not assigned to this project.");
    }
}
