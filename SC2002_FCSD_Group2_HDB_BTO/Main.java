import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Enumeration.MaritalStatus;

public class Main {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        loadUsers(users); // Load predefined users

        System.out.println("Welcome to BTO Management System");

        while (true) {
            User loggedInUser = UserAuthController.login(users); // User logs in

            while (true) {  // User session loop
                loggedInUser.viewMenu();
                System.out.print("\nEnter your choice: ");

                int choice;
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine(); // Consume newline
                } else {
                    System.out.println("Invalid input! Please enter a number.");
                    sc.next(); // Discard invalid input
                    continue; // Restart the loop
                }

                boolean logout = processMenuChoice(loggedInUser, choice); 
                if (logout) {
                    System.out.println("Logging out...\n");
                    break; // Exit to login screen
                }
            }
        }
    }

    private static boolean processMenuChoice(User user, int choice) {
        if (user instanceof HDBApplicant) {
            return processApplicantChoice((HDBApplicant) user, choice);
        } else if (user instanceof HDBOfficer) {
            return processOfficerChoice((HDBOfficer) user, choice);
        } else if (user instanceof HDBManager) {
            return processManagerChoice((HDBManager) user, choice);
        } else {
            System.out.println("Invalid selection. Please try again.");
            return false;
        }
    }

    private static boolean processApplicantChoice(HDBApplicant user, int choice) {
        switch (choice) {
            // case 1 -> user.viewProjects();
            // case 2 -> user.applyForProject();
            // case 3 -> user.viewApplicationStatus();
            // case 4 -> user.withdrawApplication();
            // case 5 -> user.submitEnquiry();
            // case 6 -> user.manageEnquiries();
            // case 7 -> user.changePassword();
            case 8 -> { return true; } // Logout
            default -> System.out.println("Invalid option. Please try again.");
        }
        return false; // Continue session
    }

    private static boolean processOfficerChoice(HDBOfficer user, int choice) {
        switch (choice) {
            // case 1 -> user.viewProjects();
            // case 2 -> user.applyForProject();
            // case 3 -> user.viewApplicationStatus();
            // case 4 -> user.withdrawApplication();
            // case 5 -> user.submitEnquiry();
            // case 6 -> user.manageEnquiries();
            // case 7 -> user.registerForProject();
            // case 8 -> user.viewOfficerRegistrationStatus();
            // case 9 -> user.viewAssignedProject();
            // case 10 -> user.viewAndRespondToEnquiries();
            // case 11 -> user.processFlatBooking();
            // case 12 -> user.generateReceipt();
            // case 13 -> user.changePassword();
            case 14 -> { return true; } // Logout
            default -> System.out.println("Invalid option. Please try again.");
        }
        return false;
    }

    private static boolean processManagerChoice(HDBManager user, int choice) {
        switch (choice) {
            // case 1 -> user.createProject();
            // case 2 -> user.editProject();
            // case 3 -> user.deleteProject();
            // case 4 -> user.toggleProjectVisibility();
            // case 5 -> user.viewAllProjects();
            // case 6 -> user.viewOfficerApplications();
            // case 7 -> user.approveOrRejectOfficer();
            // case 8 -> user.approveOrRejectApplicant();
            // case 9 -> user.approveOrRejectWithdrawals();
            // case 10 -> user.generateReport();
            // case 11 -> user.viewAllEnquiries();
            // case 12 -> user.respondToEnquiries();
            // case 13 -> user.changePassword();
            case 14 -> { return true; } // Logout
            default -> System.out.println("Invalid option. Please try again.");
        }
        return false;
    }
       
    private static void loadUsers(List<User> users) {
        users.add(new HDBApplicant(1, "S1234567A", "password", "John", null, null, 35, MaritalStatus.Single));
        users.add(new HDBApplicant(2, "T7654321B", "password", "Sarah", null, null, 40, MaritalStatus.Married));
        users.add(new HDBApplicant(3, "S9876543C", "password", "Grace", null, null, 37, MaritalStatus.Married));
        users.add(new HDBApplicant(4, "T2345678D", "password", "James", null, null, 30, MaritalStatus.Married));
        users.add(new HDBApplicant(5, "S3456789E", "password", "Rachel", null, null, 25, MaritalStatus.Single));

        users.add(new HDBOfficer(6, "T2109876H", "password", "Daniel", null, null, 36, MaritalStatus.Single));
        users.add(new HDBOfficer(7, "S6543210I", "password", "Emily", null, null, 28, MaritalStatus.Single));
        users.add(new HDBOfficer(8, "T1234567J", "password", "David", null, null, 29, MaritalStatus.Married));

        users.add(new HDBManager(9, "T8765432F", "password", "Michael", null, null, 36, MaritalStatus.Single, 100));
        users.add(new HDBManager(10, "S5678901G", "password", "Jessica", null, null, 26, MaritalStatus.Married, 200));

    }
}
