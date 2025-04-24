package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BTOProjsController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;

public class OfficerView implements UserView {
    SessionStateManager session = SessionStateManager.getInstance();
    UserAuthController controller = UserAuthController.getInstance();
    UserRepository user = UserRepository.getInstance();
    BTORepository repo = BTORepository.getInstance();
    BookingController bookingController = new BookingController();
    List<HDBOfficer> officerList = user.getOfficers();
    HDBOfficer currentOfficer = null;
    List<HDBApplicant> applicantList = user.getApplicants();

    public OfficerView() {
    }

    @Override
    public void start() {
        System.out.println("You are logged in as an officer.");
        System.out.println("Welcome! Officer " + session.getLoggedInUser().getFirstName());
        for (HDBOfficer o : officerList) {
            if (o.getNric().equalsIgnoreCase(session.getLoggedInUser().getNric())) {
                currentOfficer = o;
            }
        }
        int choice = 0;
        while (choice != 12) {
            displayMenu();
            choice = handleUserInput();
        }   
    }
    @Override
    public void displayMenu() {
        System.out.println("Officer Menu:");
        System.out.println("1. Open Applicant Menu");
        System.out.println("2. Apply Project as Officer");
        System.out.println("3. Check Project Registration Status");
        System.out.println("4. View Project Details");
        System.out.println("5. Update Flat Availablility");
        System.out.println("6. Retrieve Applicant Application using NRIC");
        System.out.println("7. Update Applicant Application Status");
        System.out.println("8. Update Applicant Application Profile");
        System.out.println("9. Generate Receipt");
        System.out.println("10. View Enquiry");
        System.out.println("11. Reply Enquiry");
        System.out.println("12. Exit");
    }

    @Override
    public int handleUserInput() {
        HDBOfficerController currentController = new HDBOfficerController(currentOfficer,repo);
        System.out.print("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                ApplicantView applicantView = new ApplicantView();
                applicantView.start();
                break;

            case 2:
                System.out.print("Enter the project name to apply for as officer: ");
                scanner = new Scanner(System.in);
                String projName = scanner.nextLine();
                BTOProj proj = currentController.findProject(projName, repo);
                if (currentController.submitApplication(proj)) {
                    System.out.println("Successfully applied as officer for " + projName);
                } else {
                    System.out.println("Not allowed to apply for " + projName + "as officer.");
                }
                break;

            case 3:
                System.out.print("Enter the project name to check registration status: ");
                String regProjName = scanner.nextLine();
                String status = currentController.projRegStatus(regProjName);
                System.out.println("Registration status for project \"" + regProjName + "\": " + status);
                break;

            case 4:
                BTOProjsController projsController = new BTOProjsController();
                HDBBTOExerciseController exerciseController = new HDBBTOExerciseController();
                viewMyProjects(projsController, exerciseController);

                break;

            case 5:
                Application flatUpdate = findApplication();

                if (bookingController.updateFlatAvail(flatUpdate)) {
                    System.out.println("Flat updated successfully.");
                } else {
                    System.out.println("Unsuccessful attempt.");
                }
                break;

            case 6:
                System.out.print("Enter the flat to update (based on NRIC): ");
                String appID = scanner.nextLine();
                HDBApplicant result = bookingController.retrieveApp(appID);
                System.out.println(result);
                //result.viewApplicationDetails();
                break;

            case 7:
                Application statusUpdate = findApplication();

                if (bookingController.updateAppStatus(statusUpdate)) {
                    System.out.println("Status updated successfully.");
                } else {
                    System.out.println("Unsuccessful attempt.");
                }
                break;

            case 8:
                Application profileUpdate = findApplication();
                System.out.print("Enter the new type of flat (2-Room or 3-Room): ");
                String newProfile = scanner.nextLine();

                if (bookingController.updateAppProfile(profileUpdate, newProfile, repo)) {
                    System.out.println("Status updated successfully.");
                } else {
                    System.out.println("Unsuccessful attempt.");
                }
                break;

            case 9:
                Application receipt = findApplication();
                bookingController.generateReceipt(receipt);
                break;

            case 10:
                currentController.viewEnquiries();
                break;

            case 11:
                currentController.replyEnquiries();
                break;
            case 12:
                break;
        }
        return choice;
    }

    public Application findApplication() {
        BTORepository btoRepo = BTORepository.getInstance();
        UserRepository userRepo = UserRepository.getInstance();
        ApplicationRepository appRepo = new ApplicationRepository(btoRepo, userRepo);
        List<Application> apps = appRepo.getApplications();

        if (apps.isEmpty()) {
            System.out.println("⚠ No applications found.");
            return null;
        }

        System.out.println("\n=== List of Applicant Applications ===");
        System.out.printf("%-20s %-15s %-25s %-10s %-15s%n",
                "Applicant Name", "NRIC", "Project", "Flat", "Status");
        System.out.println("---------------------------------------------------------------------------------------");

        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            String name = app.getApplicant().getFirstName();
            String nric = app.getApplicant().getNric();
            String projectName = app.getProject().getProjName();
            String flatType = app.getFlatType() != null ? app.getFlatType().getTypeName() : "NIL";
            String status = app.getStatus() != null ? app.getStatus() : "N/A";

            System.out.printf("%-20s %-15s %-25s %-10s %-15s%n",
                    name, nric, projectName, flatType, status);
        }

        System.out.println("---------------------------------------------------------------------------------------");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter NRIC of the applicant to proceed: ");
        String nricInput = scanner.nextLine().trim();

        Application selectedApp = appRepo.getApplicationByNric(nricInput);
        if (selectedApp == null) {
            System.out.println("No application found for NRIC: " + nricInput);
        }

        return selectedApp;
    }

    // Displays only BTO projects created by the currently logged-in HDB Manager.
    // Uses the SessionStateManager to identify the manager.
    // If any are found, displays a table and allows selection for details and management.
    public void viewMyProjects(BTOProjsController projsController, HDBBTOExerciseController exerciseController) {
        projsController.insertProjectsFromRepo();
        exerciseController.insertExercisesFromRepo();
        List<BTOProj> myProjects = projsController.viewOwnProjsOfficer();
        List<BTOExercise> exercises = exerciseController.viewAllExercises();

        if (myProjects == null || myProjects.isEmpty()) {
            System.out.println("You have not created any projects.");
            return;
        }

        BTOProj selected = selectProjectFromTable(myProjects, exercises);
        if (selected != null) {
            displayProjectDetails(selected);
        }
    }

    // Displays a formatted table of BTO projects along with their associated exercises,
    // prompts the user to select a project by ID, and returns the selected project.
    // If -1 is entered, returns null to indicate no selection.
    private BTOProj selectProjectFromTable(List<BTOProj> projects, List<BTOExercise> exercises) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== List of BTO Projects ===");
        System.out.printf("%-5s %-25s %-15s %-20s %-20s%n", "ID", "Project Name", "Neighbourhood", "Manager IC", "Exercise");
        System.out.println("----------------------------------------------------------------------------------------");

        for (BTOProj proj : projects) {
            String exerciseName = "Unassigned";
            for (BTOExercise exercise : exercises) {
                for (BTOProj p : exercise.getExerciseProjs()) {
                    if (p.getProjId() == proj.getProjId()) {
                        exerciseName = exercise.getExerciseName();
                        break;
                    }
                }
            }

            HDBManager managerIC = proj.getManagerIC();
            String managerName = (managerIC != null && managerIC.getFirstName() != null)
                    ? managerIC.getFirstName()
                    : "N/A";

            System.out.printf("%-5d %-25s %-15s %-20s %-20s%n",
                    proj.getProjId(),
                    proj.getProjName(),
                    proj.getProjNbh(),
                    managerName,
                    exerciseName);
        }

        System.out.print("\nEnter Project ID to view more details and manage (or -1 to return): ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (id == -1) return null;

        for (BTOProj proj : projects) {
            if (proj.getProjId() == id) return proj;
        }

        System.out.println("No project found with that ID.");
        return null;
    }

    // Prints detailed information about a single BTO project, including:
    // - Project name, dates, visibility, and neighbourhood
    // - Unique flat types offered
    // - Officer slot information
    // - Assigned officer names
    private void displayProjectDetails(BTOProj selected) {
        System.out.println("\n=== Project Details ===");
        System.out.println("Project Name     : " + selected.getProjName());
        System.out.println("Open Date        : " + selected.getAppOpenDate().toLocalDate());
        System.out.println("Close Date       : " + selected.getAppCloseDate().toLocalDate());
        System.out.println("Visibility       : " + selected.getVisibility());
        System.out.println("Neighbourhood    : " + selected.getProjNbh());

        System.out.println("Flat Types");
        List<FlatType> flatTypes = selected.getAvailableFlatTypes();
        if (flatTypes == null || flatTypes.isEmpty()) {
            System.out.println("                    None");
        } else {
            Set<String> printed = new HashSet<>();
            for (FlatType type : flatTypes) {
                String key = type.getTypeName() + ":" + type.getTotalUnits() + ":" + type.getSellingPrice();
                if (printed.add(key)) {
                    System.out.printf("  - %-12s : %d units at $%.2f%n",
                            type.getTypeName(), type.getTotalUnits(), type.getSellingPrice());
                }
            }
        }

        System.out.println("Officer Slots    : " + selected.getOfficerSlots());
        if (selected.getOfficersList() != null && selected.getOfficersList().length > 0) {
            System.out.print("Assigned Officers: ");
            for (HDBOfficer officer : selected.getOfficersList()) {
                System.out.print(officer.getFirstName() + " ");
            }
            System.out.println();
        } else {
            System.out.println("Assigned Officers: None");
        }

        // Add this block to pause before returning
        System.out.print("\nPress Enter to return to the menu...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
