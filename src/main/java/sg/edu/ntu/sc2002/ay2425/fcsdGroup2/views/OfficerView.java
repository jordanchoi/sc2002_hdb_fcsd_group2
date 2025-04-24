package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import java.util.List;
import java.util.Scanner;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BookingController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBOfficerController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

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
        int choice = 0;
        while (choice != 12) {
            displayMenu();
            choice = handleUserInput();
        }

        for (HDBOfficer o : officerList) {
            if (o.getOfficerId() == session.getLoggedInUser().getUserId()) {
                currentOfficer = o;
            }
        }
    }

    HDBOfficerController currentController = new HDBOfficerController(currentOfficer, repo);

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
        System.out.print("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                ApplicantView applicantView = new ApplicantView();
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
                System.out.print("Enter the project name to view details: ");
                String detailProjName = scanner.nextLine();
                BTOProj foundProj = null;
                for (BTOProj project : repo.getAllProjects()) {
                    if (project.getProjName().equalsIgnoreCase(detailProjName)) {
                        foundProj = project;
                        System.out.println(foundProj.toString());
                    }
                }
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


}
