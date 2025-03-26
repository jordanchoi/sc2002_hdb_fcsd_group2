import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        loadUsers(users); // Load predefined users

        System.out.println("Welcome to BTO Management System");

        while (true) {
            User loggedInUser = LoginManager.login(users); // User logs in

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
        if (user instanceof Applicant) {
            return processApplicantChoice((Applicant) user, choice);
        } else if (user instanceof HDBOfficer) {
            return processOfficerChoice((HDBOfficer) user, choice);
        } else if (user instanceof HDBManager) {
            return processManagerChoice((HDBManager) user, choice);
        } else {
            System.out.println("Invalid selection. Please try again.");
            return false;
        }
    }

    private static boolean processApplicantChoice(Applicant user, int choice) {
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
        users.add(new Applicant("John", "S1234567A", "password", 35, "Single"));
        users.add(new Applicant("Sarah", "T7654321B", "password", 40, "Married"));
        users.add(new Applicant("Grace", "S9876543C", "password", 37, "Married"));
        users.add(new Applicant("James", "T2345678D", "password", 30, "Married"));
        users.add(new Applicant("Rachel", "S3456789E", "password", 25, "Single"));

        users.add(new HDBOfficer("Daniel", "T2109876H", "password", 36, "Single"));
        users.add(new HDBOfficer("Emily", "S6543210I", "password", 28, "Single"));
        users.add(new HDBOfficer("David", "T1234567J", "password", 29, "Married"));

        users.add(new HDBManager("Michael", "T8765432F", "password", 36, "Single"));
        users.add(new HDBManager("Jessica", "S5678901G", "password", 26, "Married"));
    }
}
