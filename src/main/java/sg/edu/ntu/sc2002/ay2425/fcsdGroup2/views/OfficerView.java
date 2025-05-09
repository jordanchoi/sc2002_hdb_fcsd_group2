package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BTOProjsController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BookingController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBOfficerController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Block;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Flat;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.FlatType;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ApplicationStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FilterOption;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatBookingStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BlockListRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.FlatsListRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

/**
 * Represents the view for HDB Officers.
 * Provides options for managing applicants, projects, flat bookings,
 * generating receipts and reports, and handling enquiries.
 */
public class OfficerView implements UserView {
    SessionStateManager session = SessionStateManager.getInstance();
    UserAuthController controller = UserAuthController.getInstance();
    UserRepository user = UserRepository.getInstance();
    BTORepository repo = BTORepository.getInstance();
    BookingController bookingController = new BookingController();
    List<HDBOfficer> officerList = user.getOfficers();
    HDBOfficer currentOfficer = null;
    List<HDBApplicant> applicantList = user.getApplicants();
    private final Set<FilterOption> activeFilters = new HashSet<>();

    /**
     * Constructs a new OfficerView instance.
     */
    public OfficerView() {}

    /**
     * Starts the Officer Main Menu session.
     * Displays a welcome message and repeatedly shows menu options
     * until the officer chooses to exit.
     */
    @Override
    public void start() {
        System.out.println("You are logged in as an officer.");
        System.out.println("\nWelcome! Officer " + session.getLoggedInUser().getFirstName() + "!");
        System.out.println("You are in the Officer Main Menu");
        for (HDBOfficer o : officerList) {
            if (o.getNric().equalsIgnoreCase(session.getLoggedInUser().getNric())) {
                currentOfficer = o;
            }
        }
        int choice = 0;
        while (choice != 6) {
            displayMenu();
            choice = handleUserInput();
        }
    }

    /**
     * Displays the main menu options for the officer.
     */
    @Override
    public void displayMenu() {
        System.out.println("\nWhat would you like to do?\n");
        System.out.println("1. Applicant Management");
        System.out.println("2. Project Management");
        System.out.println("3. Flat Booking");
        System.out.println("4. Receipt & Reports");
        System.out.println("5. Manage Enquiry");
        System.out.println("6. Exit");
    }

    /**
     * Displays the main menu options for the officer.
     */
    @Override
    public int handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        HDBOfficerController currentController = new HDBOfficerController(currentOfficer, repo);

        switch (choice) {
            case 1 -> {
                System.out.println("\n-- Applicant Management --");
                ApplicantView applicantView = new ApplicantView();
                applicantView.start();
            }
            case 2 -> handleProjectSubMenu();
            case 3 -> {
                System.out.println("\n-- Flat Booking --");
                handleFlatBookingByOfficer();
            }
            case 4 -> handleReceiptSubMenu();
            case 5 -> currentController.viewEnquiries();
            case 6 -> System.out.println("Exiting Officer Menu...");
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return choice;
    }

    private void handleProjectSubMenu() {
        HDBOfficerController currentController = new HDBOfficerController(currentOfficer, repo);
        BTOProjsController projsController = new BTOProjsController();
        HDBBTOExerciseController exerciseController = new HDBBTOExerciseController();
        Scanner scanner = new Scanner(System.in);
        int subChoice = -1;

        do {
            /*HDBOfficerController currentController = new HDBOfficerController(currentOfficer, repo);
            BTOProjsController projsController = new BTOProjsController();
            HDBBTOExerciseController exerciseController = new HDBBTOExerciseController();*/
            System.out.println("\n-- Project Management --");
            System.out.println("1. View Project Details");
            System.out.println("2. Check Project Registration Status");
            System.out.println("3. Apply Project as Officer");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select: ");
            String input = scanner.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            subChoice = Integer.parseInt(input);

            switch (subChoice) {
                case 1 -> {
                    viewMyProjects(projsController, exerciseController);
                }
                case 2 -> {
                    System.out.println("Check registration status: ");
                    currentController.projRegStatus(currentOfficer.getNric());
                }
                case 3 -> {
                    System.out.println("Register to handle BTO project as officer: ");
                    boolean backToMenu = registerProjectAsOfficer(projsController, exerciseController, currentController);
                    if (!backToMenu) {
                        // User entered -1, returning to project menu
                        continue;
                    }
                }   
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid selection. Try again.");
            }
        } while (subChoice != 0);
    }

    private boolean registerProjectAsOfficer(BTOProjsController projsController, HDBBTOExerciseController exerciseController, HDBOfficerController currentController) {
        boolean validResponse = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Register to handle BTO project as officer: ");
    
        do {
            try {
                BTOProj proj = findProject(projsController, exerciseController);
    
                if (proj == null) {
                    // User wants to go back to project selection, returning false to indicate this.
                    System.out.println("No project selected, returning to project menu...");
                    return false;
                }
    
                System.out.print("Register to handle this BTO project as officer? (y/n): ");
                String confirmToRegister = scanner.nextLine().trim();
    
                if (confirmToRegister.equalsIgnoreCase("y")) {
                    if (currentController.submitApplication(proj)) {
                        System.out.println("Successfully applied as officer for " + proj.getProjName());
                    } else {
                        System.out.println("Not allowed to apply for " + proj.getProjName() + " as officer.");
                    }
                    validResponse = true;  // exit loop after successful registration
                } else if (confirmToRegister.equalsIgnoreCase("n")) {
                    System.out.println("Returning to the list of projects...");
                    validResponse = false;  // continue loop to select new project
                } else {
                    throw new IllegalArgumentException("Invalid input, please enter 'y' or 'n'.");
                }
    
            } catch (IllegalArgumentException e) {
                // Handle invalid input exception here
                System.out.println(e.getMessage());  // Print the error message
                validResponse = false;  // Continue the loop for invalid input
            }
    
        } while (!validResponse);
    
        return true;  // Successfully registered, return true
    }
    
    
    /**
     * Handles the receipt submenu operations for generating receipts.
     */
    private void handleReceiptSubMenu() {
        Scanner scanner = new Scanner(System.in);
        int subChoice;
        do {
            System.out.println("\n-- Receipt --");
            System.out.println("1. Generate Receipt");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select: ");
            subChoice = scanner.nextInt();
            scanner.nextLine();

            switch (subChoice) {
                case 1 -> {
                    Application receipt = findApplication();
                    bookingController.generateReceipt(receipt);
                }
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid selection. Try again.");
            }
        } while (subChoice != 0);
    }

    /**
     * Finds and returns an application selected by the officer.
     *
     * @return the selected application or null if invalid
     */
    public Application findApplication() {
        BTORepository btoRepo = BTORepository.getInstance();
        UserRepository userRepo = UserRepository.getInstance();
        ApplicationRepository appRepo = new ApplicationRepository(btoRepo, userRepo);
        List<Application> apps = appRepo.getApplications();

        if (apps.isEmpty()) {
            System.out.println("No applications found.");
            return null;
        }

        System.out.println("\n=== List of Applicant Applications ===");
        System.out.printf("%-5s %-20s %-15s %-25s %-10s %-15s%n",
                "No.", "Applicant Name", "NRIC", "Project", "Flat", "Status");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            String name = app.getApplicant().getFirstName();
            String nric = app.getApplicant().getNric();
            String projectName = app.getProject().getProjName();
            String flatType = app.getFlatType() != null ? app.getFlatType().getTypeName() : "NIL";
            String status = app.getStatus() != null ? app.getStatus() : "N/A";

            System.out.printf("%-5d %-20s %-15s %-25s %-10s %-15s%n",
                    i + 1, name, nric, projectName, flatType, status);
        }

        System.out.println("---------------------------------------------------------------------------------------------");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number corresponding to the application to proceed: ");
        String input = scanner.nextLine().trim();

        if (!input.matches("\\d+")) {
            System.out.println("Invalid input. Please enter a numeric value.");
            return null;
        }

        int selectedIndex = Integer.parseInt(input) - 1;
        if (selectedIndex < 0 || selectedIndex >= apps.size()) {
            System.out.println("Invalid selection. Please enter a valid number between 1 and " + apps.size());
            return null;
        }

        return apps.get(selectedIndex);
    }


    /**
     * Lists all available projects and returns the selected project.
     *
     * @param projsController the BTO projects controller
     * @param exerciseController the BTO exercises controller
     * @return the selected project or null
     */
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

    /**
     * Lists all available projects and returns the selected project.
     *
     * @param projsController the BTO projects controller
     * @param exerciseController the BTO exercises controller
     * @return the selected project or null
     */
    public BTOProj findProject(BTOProjsController projsController, HDBBTOExerciseController exerciseController) {
        projsController.insertProjectsFromRepo();
        exerciseController.insertExercisesFromRepo();
        List<BTOProj> allProjects = projsController.viewAllProjs();
        List<BTOExercise> allExercises = exerciseController.viewAllExercises();
        BTOProj selectedProj = null;

        if (allProjects == null || allProjects.isEmpty()) {
            System.out.println("There are no projects available.");
            return selectedProj;
        }
        //return selected;
        selectedProj =  selectProjectFromTable(allProjects, allExercises);
        if (selectedProj != null) {
            displayProjectDetails(selectedProj);
        } 
        return selectedProj;
    }

    /**
     * Displays a table of projects with their associated exercises, prompts for selection.
     *
     * @param projects list of available projects
     * @param exercises list of exercises
     * @return the selected project or null
     */
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

    /**
     * Displays detailed information about a selected BTO project.
     *
     * @param selected the selected project to view
     */
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

    /**
     * Handles the flat booking process for applicants.
     * Includes listing successful applicants, selecting flats, booking, and updating records.
     */
    public void handleFlatBookingByOfficer(){
        try {
            Scanner sc = new Scanner(System.in);

            BlockListRepository blockRepo = new BlockListRepository(BTORepository.getInstance());
            FlatsListRepository flatRepo = FlatsListRepository.getInstance(blockRepo);
            ApplicationRepository appRepo = new ApplicationRepository(BTORepository.getInstance(), UserRepository.getInstance());

            List<Application> allApplications = appRepo.getApplications();
            List<Application> successfulApplications = new ArrayList<>();
            List<Application> pendingApplications = new ArrayList<>();

            for (Application app : allApplications) {
                if (app.getStatusEnum() == ApplicationStatus.SUCCESSFUL) {
                    successfulApplications.add(app);
                } else if (app.getStatusEnum() == ApplicationStatus.PENDING) {
                    pendingApplications.add(app);
                }
            }

            // Display successful
            System.out.println("\n=== List of Successful Applications (Select to Book Flat) ===");
            if (successfulApplications.isEmpty()) {
                System.out.println("No successful applications available for booking.");
                return;
            } else {
                System.out.printf("%-5s %-20s %-15s %-30s %-15s%n", "No.", "Applicant Name", "NRIC", "Project (Postal)", "Status");
                int counter = 1;
                for (Application app : successfulApplications) {
                    BTOProj proj1 = app.getAppliedProj();
                    System.out.printf("%-5d %-20s %-15s %-30s %-15s%n",
                            counter++, app.getApplicant().getFirstName(), app.getApplicant().getNric(),
                            proj1.getProjName() + " (" + proj1.getPostalCode() + ")", app.getStatusEnum().name());
                }
            }

            // Select applicant
            System.out.print("\nSelect successful applicant by number to book flat: ");
            int selectedIndex = Integer.parseInt(sc.nextLine()) - 1;
            if (selectedIndex < 0 || selectedIndex >= successfulApplications.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Application selectedApp = successfulApplications.get(selectedIndex);
            BTOProj project = selectedApp.getAppliedProj();

            // Load flats for selected project
            List<Flat> allFlats = flatRepo.getAllFlats();
            List<Flat> availableFlats = new ArrayList<>();
            for (Flat flat : allFlats) {
                Block block = flat.getBlock();
                BTOProj flatProj = block.getProject();

                if (flatProj != null && flatProj.getProjId() == project.getProjId()
                        && flat.getStatus() == FlatBookingStatus.AVAILABLE) {
                    availableFlats.add(flat);
                }
            }

            if (availableFlats.isEmpty()) {
                System.out.println("No available flats in the applicant's project.");
                return;
            }

            // Display available flats
            System.out.println("\nAvailable Flats:");
            for (int i = 0; i < availableFlats.size(); i++) {
                Flat f = availableFlats.get(i);
                System.out.printf("%d. Block: %d | Floor: %d | Unit: %d | Type: %s%n",
                        i + 1, f.getBlock().getBlkNo(), f.getFloorNo(), f.getUnitNo(), f.getFlatType().getTypeName());
            }

            // Select flat
            System.out.print("\nSelect flat by number: ");
            int flatSelectedIndex = Integer.parseInt(sc.nextLine()) - 1;
            if (flatSelectedIndex < 0 || flatSelectedIndex >= availableFlats.size()) {
                System.out.println("Invalid flat selection.");
                return;
            }

            Flat selectedFlat = availableFlats.get(flatSelectedIndex);

            // (1) Mark flat as BOOKED
            selectedFlat.setStatus(FlatBookingStatus.BOOKED);
            flatRepo.updateFlat(selectedFlat);

            // (2) Update applicant status to BOOKED & assign flat type
            selectedApp.booked();
            selectedApp.setFlatType(selectedFlat.getFlatType());
            selectedApp.setFlat(selectedFlat);
            appRepo.update(selectedApp);

            // (3) Decrease flat count in project
            FlatTypes type = FlatTypes.fromDisplayName(selectedFlat.getFlatType().getTypeName());
            FlatType projFlat = project.getFlatMap().get(type);
            if (projFlat != null) {
                int updatedCount = projFlat.getTotalUnits() - 1;
                projFlat.setTotalUnits(Math.max(updatedCount, 0));
            }

            // Summary
            System.out.println("\nFlat successfully booked for applicant!");
            System.out.println("Booking Summary:");
            System.out.printf("NRIC         : %s%n", selectedApp.getApplicant().getNric());
            System.out.printf("Project      : %s (%s)%n", project.getProjName(), project.getPostalCode());
            System.out.printf("Flat Type    : %s%n", selectedFlat.getFlatType().getTypeName());
            System.out.printf("Block No     : %d%n", selectedFlat.getBlock().getBlkNo());
            System.out.printf("Floor-Unit   : %d-%d%n", selectedFlat.getFloorNo(), selectedFlat.getUnitNo());

        } catch (Exception e) {
            System.out.println("Error during flat booking: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
