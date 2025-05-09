package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.ProjectMessage;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.EnquiryServiceImpl;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ManagerEnquiryService;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.RoleHandler;

import java.util.List;
import java.util.Scanner;
/**
 * Handles the manager-side view for managing project enquiries.
 * Allows managers to view, search, and reply to enquiries related to their projects.
 */
public class ManagerViewHandler implements RoleHandler {
    private final HDBManager manager;
    private ManagerEnquiryService enquiryService = new EnquiryServiceImpl();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a ManagerViewHandler with the specified manager.
     *
     * @param manager the HDB manager using this handler
     */
    public ManagerViewHandler(HDBManager manager) {
        this.manager = manager;
    }

    /**
     * Displays the enquiry management options for the manager.
     * Provides options to view all enquiries, view own project enquiries,
     * view a specific enquiry by ID, and reply to project enquiries.
     */
    @Override
    public void displayEnquiryOptions() {
        while (true) {
            System.out.println("\n== Manager Enquiries ==");
            System.out.println("1. View All Enquiries");
            System.out.println("2. View My Project Enquiries");
            System.out.println("3. View Enquiry by ID");
            System.out.println("4. Reply to My Project Enquiry");
            System.out.println("0. Back");

            System.out.print("\nPlease select an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> displayAll();
                case "2" -> displayOwnProjectEnquiries();
                case "3" -> displayEnquiryById();
                case "4" -> replyToProjectEnquiry();
                case "0" -> { return; }
                default -> System.out.println("Invalid input");
            }
        }
    }

    /**
     * Displays all enquiries across all projects.
     */
    private void displayAll() {
        List<Enquiry> all = enquiryService.getAllEnquiries();
        for (Enquiry enq : all) {
            System.out.println("==================================================");
            System.out.println("Enquiry #" + enq.getEnquiryId());
            System.out.println("Project : " + enq.getForProj().getProjName());
            System.out.println("Started by : " + enq.getMadeBy().getFirstName());
            System.out.println();

            for (ProjectMessage msg : enq.getEnquiries()) {
                System.out.println("[" + msg.getSender().getFirstName() + "]");
                System.out.println(msg.getContent());
                System.out.println();
            }

            System.out.println("--------------------------------------------------");
        }
    }

    /**
     * Displays enquiries only for projects managed by the current manager.
     */
    private void displayOwnProjectEnquiries() {
        List<Enquiry> own = manager.getAllProjs().stream()
                .flatMap(p -> enquiryService.getEnquiryForAssignedProj(p).stream())
                .toList();

        if (own.isEmpty()) {
            System.out.println("No enquiries found for your projects or you do not have any projects.");
        } else {
            for (Enquiry enq : own) {
                System.out.println("==================================================");
                System.out.println("Enquiry #" + enq.getEnquiryId());
                System.out.println("Project : " + enq.getForProj().getProjName());
                System.out.println("Started by : " + enq.getMadeBy().getFirstName());
                System.out.println();

                for (ProjectMessage msg : enq.getEnquiries()) {
                    System.out.println("[" + msg.getSender().getFirstName() + "]");
                    System.out.println(msg.getContent());
                    System.out.println();
                }

                System.out.println("--------------------------------------------------");
            }
        }
    }

    /**
     * Displays a single enquiry identified by its ID.
     */
    private void displayEnquiryById() {
        System.out.print("Enter Enquiry ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        enquiryService.getEnquiryById(id).ifPresentOrElse(e -> {
            System.out.println("==================================================");
            System.out.println("Enquiry #" + e.getEnquiryId());
            System.out.println("Project : " + e.getForProj().getProjName());
            System.out.println("Started by : " + e.getMadeBy().getFirstName());
            System.out.println();

            for (ProjectMessage m : e.getEnquiries()) {
                System.out.println("[" + m.getSender().getFirstName() + "]");
                System.out.println(m.getContent());
                System.out.println();
            }

            System.out.println("--------------------------------------------------");
        }, () -> System.out.println("Enquiry not found."));
    }

    /**
     * Allows the manager to reply to a selected project enquiry.
     */
    private void replyToProjectEnquiry() {
        displayOwnProjectEnquiries();
        System.out.print("Enter Enquiry ID to reply: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();

        boolean success = enquiryService.replyEnquiry(id, reply, manager);
        System.out.println(success ? "Reply sent." : "Failed — not assigned to this project.");
    }
}
