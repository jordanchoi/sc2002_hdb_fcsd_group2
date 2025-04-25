package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.ApplicationController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BTOProjsController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.OfficerProjectApplicationController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.ReportView;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.*;
import java.util.*;
import java.time.LocalDateTime;

public class BTOProjectsView implements UserView {
    private final BTOProjsController projsController;
    private final HDBBTOExerciseController exerciseController;
    private final ApplicationController applicationController;
    private final OfficerProjectApplicationController officerProjectApplicationController;
    private SessionStateManager session = SessionStateManager.getInstance();

    public BTOProjectsView(BTOProjsController projsController, HDBBTOExerciseController exerciseController, ApplicationController applicationController, OfficerProjectApplicationController officerProjectApplication) {
        this.projsController = projsController;
        this.exerciseController = exerciseController;
        this.applicationController = new ApplicationController();
        this.officerProjectApplicationController = officerProjectApplication;
    }

    public void start() {
        System.out.println("You are in the BTO Project Management Console.\nHere, you can manage BTO projects, including creating of BTO Project or get statistics for a particular Project.\n");
        System.out.println("What would you like to do?\n");
        int choice = 0;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 5); // Assuming 10 is the exit option
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Manage BTO Project");
        System.out.println("2. Create New BTO Project");
        System.out.println("3. Edit BTO Project");
        System.out.println("4. Delete BTO Project");
        System.out.println("5. Return to Main Menu");
    }

    @Override
    public int handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (true) {
            System.out.print("\nPlease select an option: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();

                if (choice >= 1 && choice <= 5) break; //
                else System.out.println("Invalid option. Please enter a number between 1 and 5.");
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        switch (choice) {
            case 1 -> {
                manageBTOProject();
            }
            case 2 -> {
                System.out.println("Creating BTO Project...\n");
                createBTOProjects(projsController, exerciseController);
            }
            case 3 -> {
                System.out.println("Editing BTO Project...\n");
                editBTOProject();
            }
            case 4 -> {
                System.out.println("Deleting BTO Project\n");
                deleteBTOProject();
            }
            case 5 -> {
                System.out.println("Exiting to Main Manager Console...\n");
            }
        }

        System.out.println();
        return choice;
    }

    // Allows the user to create a new BTO project and assign it to a selected BTO exercise.
    public void createBTOProjects(BTOProjsController projsController, HDBBTOExerciseController exerciseController) {
        Scanner scanner = new Scanner(System.in);
        SessionStateManager session = SessionStateManager.getInstance();
        HDBManager manager = (HDBManager) session.getLoggedInUser();
        exerciseController.insertExercisesFromRepo();
        List<BTOExercise> exercises = exerciseController.viewAllExercises();
        if (exercises.isEmpty()) {
            System.out.println("No BTO exercises found. Please create one first.");
            return;
        }

        System.out.println("Select a BTO Exercise to assign this project to:");
        for (int i = 0; i < exercises.size(); i++) {
            BTOExercise ex = exercises.get(i);
            System.out.printf("%d. %s (ID: %d)\n", i + 1, ex.getExerciseName(), ex.getExerciseId());
        }

        int choice;
        while (true) {
            System.out.print("Enter exercise number (0 to cancel): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice == 0) return; // Cancel creating project
                if (choice >= 1 && choice <= exercises.size()) break;
            }
            System.out.println("Invalid choice. Please enter a number between 1 and " + exercises.size());
            scanner.nextLine();
        }
        scanner.nextLine();
        BTOExercise selectedExercise = exercises.get(choice - 1);

        int id;
        while (true) {
            System.out.print("Enter Project ID: ");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                scanner.nextLine();
                if (projsController.isProjectIdUnique(id)) break;
                else System.out.println("Project ID already exists. Enter a unique ID.");
            } else {
                System.out.println("Invalid input. Enter a valid integer.");
                scanner.nextLine();
            }
        }

        System.out.print("Enter Project Name: ");
        String name = scanner.nextLine();

        LocalDateTime open = null, close = null;
        while (open == null) {
            try {
                System.out.print("Enter Project Open Date (YYYY-MM-DD): ");
                open = LocalDateTime.parse(scanner.nextLine() + "T00:00:00");
            } catch (Exception e) {
                System.out.println("Invalid date. Try again.");
            }
        }
        while (close == null) {
            try {
                System.out.print("Enter Project Close Date (YYYY-MM-DD): ");
                close = LocalDateTime.parse(scanner.nextLine() + "T00:00:00");
            } catch (Exception e) {
                System.out.println("Invalid date. Try again.");
            }
        }

        boolean visible = !open.isAfter(LocalDateTime.now()) && !close.isBefore(LocalDateTime.now());

        if (!projsController.isManagerAvailable(manager, open, close, exerciseController.viewAllExercises())) {
            System.out.println("\nYou are already managing a project within this time period.");
            return;
        }

        Neighbourhoods[] allHoods = Neighbourhoods.values();
        System.out.println("Select Neighbourhood:");
        for (int i = 0; i < allHoods.length; i++) {
            System.out.printf("%d. %s\n", i + 1, allHoods[i]);
        }

        Neighbourhoods selectedNeighbourhood = null;
        while (selectedNeighbourhood == null) {
            System.out.print("Enter neighbourhood number: ");
            if (scanner.hasNextInt()) {
                int hoodChoice = scanner.nextInt();
                if (hoodChoice >= 1 && hoodChoice <= allHoods.length) {
                    selectedNeighbourhood = allHoods[hoodChoice - 1];
                } else {
                    System.out.println("Out of range. Try again.");
                }
            } else {
                System.out.println("Invalid input. Enter a number.");
                scanner.nextLine();
            }
        }

        Map<FlatTypes, FlatType> flatUnitMap = new HashMap<>();
        for (FlatTypes type : FlatTypes.values()) {
            if (type == FlatTypes.NIL) {
                flatUnitMap.put(type, new FlatType(type.getDisplayName(), 0, 0f));
            } else {
                System.out.printf("Enter number of %s units: ", type.getDisplayName());
                int units = scanner.nextInt();
                System.out.printf("Enter selling price for %s units: ", type.getDisplayName());
                float price = scanner.nextFloat();
                flatUnitMap.put(type, new FlatType(type.getDisplayName(), units, price));
            }
        }
        scanner.nextLine();

        int officerSlots = -1;
        while (officerSlots < 0 || officerSlots > 10) {
            System.out.print("Enter number of available officer slots (max 10): ");
            if (scanner.hasNextInt()) {
                officerSlots = scanner.nextInt();
                if (officerSlots < 0 || officerSlots > 10) System.out.println("Value must be between 0 and 10.");
            } else {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }
        scanner.nextLine();

        System.out.print("Enter Postal Code for the project: ");
        String postalCode = scanner.nextLine().trim();

        // Create project and save
        BTOProj newProj = projsController.CreateProj(
                id,
                name,
                selectedNeighbourhood,
                flatUnitMap,
                open,
                close,
                visible,
                manager,
                officerSlots,
                new HDBOfficer[0],
                postalCode
        );

        // Link to Exercise and save
        selectedExercise.addProject(newProj);
        projsController.addProject(newProj);

        System.out.println("\nProject created and assigned to: " + selectedExercise.getExerciseName());
    }

    // Displays all BTO projects in the system (regardless of manager or visibility).
    // Shows a table view with basic info, allows user to select a project,
    // then displays its full details and opens the management menu.
    public void viewAllProjects(BTOProjsController projsController, HDBBTOExerciseController exerciseController) {
        projsController.insertProjectsFromRepo();
        exerciseController.insertExercisesFromRepo();
        List<BTOProj> allProjects = projsController.viewAllProjs();
        List<BTOExercise> exercises = exerciseController.viewAllExercises();

        if (allProjects == null || allProjects.isEmpty()) {
            System.out.println("No BTO projects found.");
            return;
        }

        BTOProj selected = selectProjectFromTable(allProjects, exercises);
        if (selected != null) {
            displayProjectDetails(selected);
            manageSelectedProject(selected);
        }
    }

    // Displays only BTO projects created by the currently logged-in HDB Manager.
    // Uses the SessionStateManager to identify the manager.
    // If any are found, displays a table and allows selection for details and management.
    public void viewMyProjects(BTOProjsController projsController, HDBBTOExerciseController exerciseController) {
        projsController.insertProjectsFromRepo();
        exerciseController.insertExercisesFromRepo();
        List<BTOProj> myProjects = projsController.viewOwnProjs();
        List<BTOExercise> exercises = exerciseController.viewAllExercises();

        if (myProjects == null || myProjects.isEmpty()) {
            System.out.println("You have not created any projects.");
            return;
        }

        BTOProj selected = selectProjectFromTable(myProjects, exercises);
        if (selected != null) {
            displayProjectDetails(selected);
            manageSelectedProject(selected);
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
        System.out.println("Postal Code      : " + selected.getPostalCode());

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
    }

    public void manageBTOProject() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose projects to view...\n");
        System.out.println("0. Return to previous menu");
        System.out.println("1. View All Projects");
        System.out.println("2. View My Project");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 0:
                System.out.println("Returning to previous menu...");
                return;
            case 1:
                viewAllProjects(projsController, exerciseController);
                break;
            case 2:
                viewMyProjects(projsController, exerciseController);
                break;
            default:
                System.out.println("Invalid input.");
        }
    }

    public void manageSelectedProject(BTOProj selected) {
        System.out.println("\nWhat would you like to do next?");
        System.out.println("0. Return to previous menu");
        System.out.println("1. Toggle Project Visibility");
        System.out.println("2. Manage HDB Officer");
        System.out.println("3. Manage Applications");
        Scanner scanner = new Scanner(System.in);

        int choice = -1;
        while (choice < 0 || choice > 3) {
            System.out.print("Enter choice: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Try again.");
                scanner.nextLine();
            }
        }

        switch (choice) {
            case 0 -> {
                System.out.println("Returning to project list...");
            }
            case 1 -> {
                projsController.toggleProjVisibility(selected);
                System.out.println("\nVisibility updated: " + (selected.getVisibility() ? "ON" : "OFF"));
            }
            case 2 -> {
                System.out.println("Managing HDB Officer...");
                manageOfficerByProjectId(selected.getProjId());
            }
            case 3 -> {
                System.out.println("Managing Applications...");
                manageApplicationsByProjectId(applicationController, selected.getProjId());
            }
        }
    }

    public void manageOfficerByProjectId(int projectId) {
        BTORepository btoRepo = BTORepository.getInstance();
        UserRepository userRepo = UserRepository.getInstance();
        ProjectApplicationRepository projAppRepo = new ProjectApplicationRepository(userRepo, btoRepo);
        OfficerProjectApplicationController controller = new OfficerProjectApplicationController();
        List<OfficerProjectApplication> applications = projAppRepo.getByProjectId(projectId);

        System.out.println("=== Officer Assignments for Project ID: " + projectId + " ===");

        if (applications.isEmpty()) {
            System.out.println("No officer applications found for this project.");
            return;
        }

        System.out.printf("%-5s %-12s %-25s %-20s\n", "ID", "NRIC", "Officer Name", "Status");

        for (OfficerProjectApplication app : applications) {
            System.out.printf("%-5d %-12s %-25s %-20s\n",
                    app.getOfficerAppId(),
                    app.getOfficer().getNric(),
                    app.getOfficer().getFirstName(),
                    app.getAssignmentStatus());
        }

        Scanner scanner = new Scanner(System.in);
        int appId;

        while (true) {
            System.out.print("\nEnter Officer Application ID to process (or -1 to cancel): ");
            String input = scanner.nextLine().trim();

            try {
                appId = Integer.parseInt(input);
                if (appId == -1) return;
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        int finalAppId = appId;
        OfficerProjectApplication selectedApp = applications.stream()
                .filter(a -> a.getOfficerAppId() == finalAppId)
                .findFirst()
                .orElse(null);

        if (selectedApp == null) {
            System.out.println("\nApplication ID not found in this project.");
            return;
        }

        if (!selectedApp.getAssignmentStatus().equals(AssignStatus.PENDING)) {
            System.out.println("\nThis application has already been processed.");
            return;
        }

        String decision;
        while (true) {
            System.out.print("Approve this officer application? (yes/no): ");
            decision = scanner.nextLine().trim().toLowerCase();

            if (decision.equals("yes") || decision.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        boolean approved = decision.equals("yes");

        BTOProj project = btoRepo.getProjById(projectId);
        if (project == null) {
            System.out.println("\nProject not found.");
            return;
        }

        if (approved) {
            if (project.getOfficersList().length >= project.getOfficerSlots()) {
                System.out.println("\nCannot approve: Officer slots are already filled.");
                return;
            } else {
                boolean added = project.assignOfficer(selectedApp.getOfficer());
                if (!added) {
                    System.out.println("\nThis officer is already assigned to the project.");
                    return;
                }
                btoRepo.saveProject();  // Save changes to Excel
            }
        }

        boolean result = controller.processOfficerDecision(selectedApp, approved);
        if (result) {
            System.out.println(approved ? "\nOfficer application approved.\n" : "\nOfficer application rejected.\n");
        } else {
            System.out.println("\nAction failed. Please check application status or data consistency.");
        }
    }

    public void manageApplicationsByProjectId(ApplicationController applicationController, int projectId) {
        BTORepository btoRepo = BTORepository.getInstance();
        UserRepository userRepo = UserRepository.getInstance();
        ApplicationRepository appRepo = new ApplicationRepository(btoRepo, userRepo);
        List<Application> apps = appRepo.getApplicationsByProjectId(projectId);

        System.out.println("=== Project: (ID: " + projectId + ") ===");
        if (apps.isEmpty()) {
            System.out.println("No applications available.");
            return;
        }

        System.out.println("=== List of All Applications ===");
        System.out.printf("%-5s %-12s %-20s %-12s %-12s %-15s\n",
                "ID", "NRIC", "Project Name", "Status", "Flat Type", "Booked Flat");

        for (Application app : apps) {
            String applicantNric = (app.getApplicant() != null) ? app.getApplicant().getNric() : "-";
            String projectName = (app.getProject() != null) ? app.getProject().getProjName() : "-";
            String status = (app.getStatusEnum() != null) ? app.getStatusEnum().name() : "-";
            String flatType = (app.getFlatType() != null) ? app.getFlatType().getTypeName() : "-";

            Flat flat = app.getFlat();
            String bookedFlat = (flat != null && flat.getBlock() != null)
                    ? "Blk " + flat.getBlock().getBlkNo() + " #" + flat.getFloorNo() + "-" + flat.getUnitNo()
                    : "-";

            System.out.printf("%-5d %-12s %-20s %-12s %-12s %-15s\n",
                    app.getAppId(),
                    applicantNric,
                    projectName,
                    status,
                    flatType,
                    bookedFlat);
        }

        Scanner scanner = new Scanner(System.in);
        int appId;

        while (true) {
            System.out.print("\nEnter Application ID to process (or -1 to cancel): ");
            String input = scanner.nextLine().trim();

            try {
                appId = Integer.parseInt(input);
                if (appId == -1) return;
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        final int selectedAppId = appId;
        Application selectedApp = apps.stream()
                .filter(a -> a.getAppId() == selectedAppId)
                .findFirst()
                .orElse(null);

        if (selectedApp == null) {
            System.out.println("Application ID not found in this project.");
            return;
        }

        // Handle withdrawal requests
        if (selectedApp.getStatusEnum() == ApplicationStatus.WITHDRAW_REQ) {
            System.out.print("Approve withdrawal request? (yes/no): ");
            String decision = scanner.nextLine().trim().toLowerCase();

            if (decision.equals("yes")) {
                selectedApp.approveWithdrawal();
                appRepo.update(selectedApp); // Save to Excel
                System.out.println("Withdrawal approved.");
            } else if (decision.equals("no")) {
                selectedApp.rejectWithdrawal();
                appRepo.update(selectedApp); // Save to Excel
                System.out.println("Withdrawal rejected.");
            } else {
                System.out.println("Invalid input. No changes made.");
            }
            return;
        }

        if (selectedApp.getStatusEnum() != null && !selectedApp.getStatusEnum().equals(ApplicationStatus.PENDING)) {
            System.out.println("This application has already been processed.");
            return;
        }

        String decision;
        while (true) {
            System.out.print("Approve this application? (yes/no): ");
            decision = scanner.nextLine().trim().toLowerCase();

            if (decision.equals("yes") || decision.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        boolean approved = decision.equals("yes");
        boolean result = applicationController.processApplicationDecision(selectedApp, approved);

        if (result) {
            System.out.println(approved ? "\nApplication approved.\n" : "\nApplication rejected.\n");
        } else {
            System.out.println("Action failed. Possibly due to unavailable flats.");
        }
    }



    // Allows the user to edit a selected BTO project.
    // Supports editing name, neighbourhood, dates, officer slots, and flat type details.
    private void editBTOProject() {
        Scanner scanner = new Scanner(System.in);
        projsController.insertProjectsFromRepo();
        List<BTOProj> allProjects = projsController.viewAllProjs();

        if (allProjects.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }

        BTOProj selected = selectProjectFromTable(allProjects, exerciseController.viewAllExercises());
        if (selected == null) return;

        String name = selected.getProjName();
        Neighbourhoods nbh = selected.getProjNbh();
        LocalDateTime open = selected.getAppOpenDate();
        LocalDateTime close = selected.getAppCloseDate();
        int slots = selected.getOfficerSlots();
        Map<String, FlatType> updatedFlatTypes = new HashMap<>();

        boolean done = false;
        while (!done) {
            System.out.println("\nEditing Project: " + name);
            System.out.println("0. Cancel Editing");
            System.out.println("1. Name (current: " + name + ")");
            System.out.println("2. Neighbourhood (current: " + nbh + ")");
            System.out.println("3. Open Date (current: " + open.toLocalDate() + ")");
            System.out.println("4. Close Date (current: " + close.toLocalDate() + ")");
            System.out.println("5. Officer Slots (current: " + slots + ")");
            System.out.println("6. Edit Flat Types");
            System.out.println("7. Save and Exit");
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "0" -> {
                    System.out.println("Cancelling editing...");
                    return;
                }
                case "1" -> {
                    System.out.print("Enter new name: ");
                    String inputName = scanner.nextLine().trim();
                    if (!inputName.isEmpty()) name = inputName;
                }
                case "2" -> {
                    Neighbourhoods[] all = Neighbourhoods.values();
                    for (int i = 0; i < all.length; i++) {
                        System.out.printf("%d. %s\n", i + 1, all[i]);
                    }
                    System.out.print("Choose new neighbourhood: ");
                    try {
                        int choice = Integer.parseInt(scanner.nextLine().trim());
                        if (choice >= 1 && choice <= all.length) nbh = all[choice - 1];
                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "3" -> {
                    System.out.print("Enter new open date (YYYY-MM-DD): ");
                    try {
                        open = LocalDateTime.parse(scanner.nextLine().trim() + "T00:00:00");
                    } catch (Exception e) {
                        System.out.println("Invalid date.");
                    }
                }
                case "4" -> {
                    System.out.print("Enter new close date (YYYY-MM-DD): ");
                    try {
                        close = LocalDateTime.parse(scanner.nextLine().trim() + "T00:00:00");
                    } catch (Exception e) {
                        System.out.println("Invalid date.");
                    }
                }
                case "5" -> {
                    System.out.print("Enter officer slots (0–10): ");
                    try {
                        int newSlots = Integer.parseInt(scanner.nextLine().trim());
                        if (newSlots >= 0 && newSlots <= 10) slots = newSlots;
                        else System.out.println("Out of range.");
                    } catch (Exception e) {
                        System.out.println("Invalid number.");
                    }
                }
                case "6" -> {
                    List<FlatType> flatTypes = selected.getAvailableFlatTypes();
                    Set<String> seen = new HashSet<>();
                    List<FlatType> uniqueFlatTypes = new ArrayList<>();

                    for (FlatType ft : flatTypes) {
                        String key = ft.getTypeName() + ":" + ft.getTotalUnits() + ":" + ft.getSellingPrice();
                        if (!seen.contains(key)) {
                            seen.add(key);
                            uniqueFlatTypes.add(ft);
                        }
                    }

                    if (uniqueFlatTypes.isEmpty()) {
                        System.out.println("No flat types available.");
                        break;
                    }

                    System.out.println("\nAvailable Flat Types:");
                    for (int i = 0; i < uniqueFlatTypes.size(); i++) {
                        FlatType ft = uniqueFlatTypes.get(i);
                        System.out.printf("%d. %s - %d units @ $%.2f\n", i + 1, ft.getTypeName(), ft.getTotalUnits(), ft.getSellingPrice());
                    }
                }
                case "7" -> {
                    boolean updated = projsController.editProj(selected, name, nbh, open, close, slots, updatedFlatTypes);
                    System.out.println(updated ? "Project updated and saved." : "Update failed.");
                    done = true;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Allows the user to delete a BTO project after selecting from the list.
    // Confirms the action before delegating to the controller to persist changes.
    private void deleteBTOProject() {
        projsController.insertProjectsFromRepo();
        List<BTOProj> allProjects = projsController.viewAllProjs();
        List<BTOExercise> exercises = exerciseController.viewAllExercises();

        if (allProjects == null || allProjects.isEmpty()) {
            System.out.println("No BTO projects found.");
            return;
        }

        BTOProj selected = selectProjectFromTable(allProjects, exercises);
        if (selected == null) return;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to delete this project? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean success = projsController.deleteProjId(selected.getProjId());
        System.out.println(success ? "Project deleted successfully." : "Project not found.");
    }
}
