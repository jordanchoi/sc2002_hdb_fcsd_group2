package sg.edu.ntu.sc2002.ay2425.fcsdGroup2;

import java.util.List;
import java.util.Optional;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.User;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.LoginView;

public class Main {
    public static void main(String[] args) {
        /*
         * The main method serves as the entry point for the BTO Management System.
         * It initializes a list of users, loads predefined users, and manages user sessions.
         * The program allows users to log in and interact with the system based on their roles.
         */

        System.out.println("Welcome to BTO Management System");
        LoginView loginView = new LoginView();
        loginView.start();
        /*

        // Fetch users from the repository, which loads from the Excel file in the constructor.
        UserRepository userRepository = new UserRepository();

        // TESTING ONLY
        // Load users
        List<HDBApplicant> applicants = userRepository.getApplicants();
        List<HDBOfficer> officers = userRepository.getOfficers();
        List<HDBManager> managers = userRepository.getManagers();

        for (HDBApplicant applicant : applicants) {
            System.out.println("Applicant: " + applicant.toString());
        }
        for (HDBOfficer officer : officers) {
            System.out.println("Officer: " + officer.toString());
        }
        for (HDBManager manager : managers) {
            System.out.println("Manager: " + manager.toString());
        }

        // Try writing to the file by adding users
        userRepository.addUser(new HDBApplicant("Jordan Chua", "S9876555J", 28, MaritalStatus.MARRIED, "PasswordTest"));

        Optional<User> testUser = userRepository.getUserByNric("S9876543J");
        if (testUser.isPresent()) {
            System.out.println(testUser.toString());
        }

        // PASSED ABOVE.

        /* START AFRESH - REMOVED BY JORDAN - REVIEW IF THE CODE IS OK WHOEVER IS DOING
        List<User> users = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        loadUsers(users); // Load predefined users

        System.out.println("Welcome to BTO Management System");

        while (true) {
            User loggedInUser = UserAuthController.login(users); // model.entities.User logs in

            while (true) {  // model.entities.User session loop
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
        */
    }

    private static boolean processMenuChoice(User user, int choice) {
        /* START AFRESH - REMOVED BY JORDAN
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
        */
        return true; // placeholder return statement
    }

    private static boolean processApplicantChoice(HDBApplicant user, int choice) {
        /* START AFRESH - REMOVED BY JORDAN - REVIEW IF THE CODE IS OK WHOEVER IS DOING
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
        */
        return false; // Continue session
    }

    private static boolean processOfficerChoice(HDBOfficer user, int choice) {
        /* START AFRESH - REMOVED BY JORDAN - REVIEW IF THE CODE IS OK WHOEVER IS DOING
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
        */
        return false;
    }

    private static boolean processManagerChoice(HDBManager user, int choice) {
        /* START AFRESH - REMOVED BY JORDAN - REVIEW IF THE CODE IS OK WHOEVER IS DOING
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
        */        // Placeholder return statement
        return false;
    }

    private static void loadUsers(List<User> users) {
    }
}
