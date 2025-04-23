package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.ProjectMessage;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.EnquiryServiceImpl;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ManagerEnquiryService;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.RoleHandler;

import java.util.List;
import java.util.Scanner;

public class ManagerViewHandler implements RoleHandler {
    private final HDBManager manager;
    private ManagerEnquiryService enquiryService = new EnquiryServiceImpl();
    private Scanner scanner = new Scanner(System.in);

    public ManagerViewHandler(HDBManager manager) {
        this.manager = manager;
    }
    @Override
    public void displayEnquiryOptions() {
        while (true) {
            System.out.println("\n[Manager Enquiries]");
            System.out.println("1. View All Enquiries");
            System.out.println("2. View My Project Enquiries");
            System.out.println("3. View Enquiry by ID");
            System.out.println("4. Reply to My Project Enquiry");
            System.out.println("0. Back");

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

    private void displayAll() {
        List<Enquiry> all = enquiryService.getAllEnquiries();
        for (Enquiry enq : all) {
            System.out.println("Enquiry #" + enq.getEnquiryId() + " | " + enq.getForProj().getProjName() + " | " + enq.getMadeBy().getFirstName());
            for (ProjectMessage msg : enq.getEnquiries()) {
                System.out.println("[" + msg.getSender().getFirstName() + "] " + msg.getContent());
            }
        }
    }

    private void displayOwnProjectEnquiries() {
        List<Enquiry> own = manager.getAllProjs().stream().flatMap(p -> enquiryService.getEnquiryForAssignedProj(p).stream()).toList();

        if (own.isEmpty()) {
            System.out.println("No enquiries found for your projects or you do not have any projects");
        } else {
            for (Enquiry enq : own) {
                System.out.println("Enquiry #" + enq.getEnquiryId() + " | " + enq.getForProj().getProjName() + " | " + enq.getMadeBy().getFirstName());
                for (ProjectMessage msg : enq.getEnquiries()) {
                    System.out.println("[" + msg.getSender().getFirstName() + "] " + msg.getContent());
                }
            }
        }
    }

    private void displayEnquiryById() {
        System.out.print("Enter Enquiry ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        enquiryService.getEnquiryById(id).ifPresentOrElse(e -> {
            for (ProjectMessage m : e.getEnquiries()) {
                System.out.println("[" + m.getSenderRole() + "] " + m.getSender().getFirstName() + ": " + m.getContent());
            }
        }, () -> System.out.println("Enquiry not found."));
    }

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
