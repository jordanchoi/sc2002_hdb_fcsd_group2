package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BTOProjsController;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
public class ManagerView implements UserView {
    /*
     * This class is responsible for displaying the manager menu and handling user input.
     * It implements the UserView interface.
     * The manager menu includes options to manage BTO exercises, projects, applications,
     * enquiries, and view all projects.
     * The class uses the UserAuthController to manage user authentication and authorization.
     * It also provides a method to change the password.
     * The class uses the SessionStateManager to manage the session state and
     * user information.
     */

    private SessionStateManager session = SessionStateManager.getInstance();
    private UserAuthController controller = UserAuthController.getInstance();
    private BTOProjsController projsController = new BTOProjsController();

    public ManagerView() {
        // Constructor logic if needed
    }

    public void start() {
        System.out.println("Welcome! Manager " + session.getLoggedInUser().getFirstName() + "!\nYou are in the Manager Main Menu.\n");
        int choice = 0;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 10); // Assuming 10 is the exit option
    }


    @Override
    public void displayMenu() {
        System.out.println("What would you like to do?\n");
        System.out.println("1. Manage BTO Exercises");
        System.out.println("2. Manage BTO Projects");
        System.out.println("3. Manage BTO Applications");
        System.out.println("4. Manage All Enquiries");
        System.out.println("5. View All Projects");
        System.out.println("6. Change Password");
        System.out.println("7. Logout");
        System.out.println("10. Exit");
    }

    @Override
    public int handleUserInput() {
        System.out.print("\nPlease select an option: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1-> {
                // Manage BTO Exercises
                System.out.println("Managing BTO Exercises...");
            } case 2-> {
                // Manage BTO Projects
                System.out.println("Managing BTO Projects...");
                manageProjects(projsController);
            } case 3-> {
                // Manage BTO Applications
                System.out.println("Managing BTO Applications...");
            } case 4-> {
                // Manage All Enquiries
                System.out.println("Managing All Enquiries...");
            } case 5-> {
                // View All Projects
                System.out.println("Viewing All Projects...");
                viewAllProjects(projsController);
            } case 6-> {
                // Change Password
                System.out.println("Changing Password...");
                //changePassword();
            } case 7-> {
                // Logout
                System.out.println("Logging out...");
                session.logout();
            } case 10-> {
                // Exit
                session.logout();
                System.out.println("Exiting...");
            }
        }
        return choice;
    }

    // Option 2
    public void manageProjects(BTOProjsController projsController){
        while (true) {
            System.out.println("\n=== BTO Project Management ===");
            System.out.println("1. Create BTO Project");
            System.out.println("2. Edit BTO Project");
            System.out.println("3. Delete BTO Project");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Project ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Project Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Project Open Year-Month-Day (e.g., 2025-01-01): ");
                    String openDateStr = scanner.nextLine();
                    System.out.print("Enter Project Close Year-Month-Day (e.g., 2025-12-31): ");
                    String closeDateStr = scanner.nextLine();
                    System.out.print("Is project visible? (true/false): ");
                    boolean visible = scanner.nextBoolean();

                    LocalDateTime open = LocalDateTime.parse(openDateStr + "T00:00:00");
                    LocalDateTime close = LocalDateTime.parse(closeDateStr + "T23:59:59");

                    projsController.CreateProj(id, name, open, close, visible);
                    System.out.println("Project created.");
                    return;

                case 2:
                    break;
                case 3:
                    break;
                case 0:

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    //Option 5
    public void viewAllProjects(BTOProjsController projsController){
        List<BTOProj> projectList = projsController.viewAllProjs();
        if (projectList.isEmpty()) {
            System.out.println("No BTO projects found.");
            return;
        }

        System.out.println("\n=== List of All BTO Projects ===");
        System.out.printf("%-5s %-20s %-18s %-18s %-10s%n",
                "ID", "Name", "Open Date", "Close Date", "Visible");
        System.out.println("----------------------------------------------------------------------");

        // Project list
        for (BTOProj proj : projectList) {
            System.out.printf("%-5d %-20s %-18s %-18s %-10s%n",
                    proj.getProjId(),
                    proj.getProjName(),
                    proj.getAppOpenDate().toLocalDate(),
                    proj.getAppCloseDate().toLocalDate(),
                    proj.getVisibility());
        }
    }
}
