import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ApplicantView {
    private ApplicantController controller;
    private Scanner scanner;

    public ApplicantView(ApplicantController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome! Applicant " + session.getLoggedInUser().getFirstName());
        int choice = 0;
        do {
            displayMenu();
            choice = getUserChoice();
            handleUserInput(choice);
        } while (choice != 11);

    }

    public void displayMenu() {
        System.out.println("\n=== Applicant Menu ===");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Apply for a BTO project");
        System.out.println("3. View applied project status");
        System.out.println("4. Withdraw application");
        System.out.println("5. Submit enquiry");
        System.out.println("6. Add message to existing enquiry");
        System.out.println("7. View enquiries");
        System.out.println("8. Edit enquiries");
        System.out.println("9. Delete enquiry");
        System.out.println("10. Change password");
        System.out.println("11. Logout");
    }

    private int getUserChoice() {
        System.out.print("Please select an option: ");
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 11.");
            scanner.nextLine();
            return -1;
        }
    }

    private void handleUserInput(int choice) {
        switch (choice) {
            case 1 -> controller.viewEligibleProjects();
            case 2 -> controller.applyProjs();
            case 3 -> controller.showApplicantApplicationDetails();
            case 4 -> controller.withdrawApplication();
            case 5 -> controller.submitEnquiry();
            case 6 -> controller.submitExisting();
            case 7 -> controller.viewSubmittedEnquiries();
            case 8 -> controller.editMessageInEnquiry();
            case 9 -> controller.deleteEnquiry();
            //case 10 -> changePassword();
            case 11 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }

    public BTOProj selectProject(List<BTOProj> availableProjects) {
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
    public void viewEligibleProjects(List<BTOProj> eligibleProjects) {
        System.out.println("\n=== Eligible BTO Projects ===");
        if (eligibleProjects.isEmpty()) {
            System.out.println("No eligible projects found.");
            return;
        }
        for (BTOProj project : eligibleProjects) {
            System.out.println("- " + project.getProjName() + " (" + project.getProjNbh() + ")");
        }
    }

    public void displayApplicationDetails(String applicationDetails) {
        System.out.println(applicationDetails);
    }

    public void applyForProject(BTOProj project) {
        System.out.println("Application submitted for project: " + project.getProjName());
    }
}
