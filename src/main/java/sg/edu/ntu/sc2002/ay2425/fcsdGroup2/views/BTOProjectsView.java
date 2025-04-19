package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BTOProjsController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.Neighbourhoods;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.util.*;
import java.time.LocalDateTime;

public class BTOProjectsView implements UserView {
    private final BTOProjsController projsController;
    private final HDBBTOExerciseController exerciseController;
    private SessionStateManager session = SessionStateManager.getInstance();

    public BTOProjectsView(BTOProjsController projsController, HDBBTOExerciseController exerciseController) {
        this.projsController = projsController;
        this.exerciseController = exerciseController;
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

                if (choice >= 1 && choice <= 5) break; // ✅ Valid range
                else System.out.println("Invalid option. Please enter a number between 1 and 5.");
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        switch (choice) {
            case 1 -> {
                System.out.println("Choose projects to view...\n");
                manageBTOProject();
            }
            case 2 -> {
                System.out.println("Creating BTO Project...\n");
                createBTOProjects(projsController, exerciseController);
            }
            case 3 -> {
                System.out.println("Editing BTO Project...\n");
            }
            case 4 -> {
                System.out.println("Deleting BTO Project\n");
            }
            case 5 -> {
                System.out.println("Exiting to Main Manager Console...\n");
            }
        }

        System.out.println();
        return choice;
    }


    //Option 1
    public void viewAllProjects(BTOProjsController projsController, HDBBTOExerciseController exerciseController) {
        projsController.insertProjectsFromRepo();
        List<BTOProj> projectList = projsController.viewAllProjs();
        List<BTOExercise> exerciseList = exerciseController.viewAllExercises();

        if (projectList.isEmpty()) {
            System.out.println("No BTO projects found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Display basic project info
        System.out.println("=== List of BTO Projects ===");
        System.out.printf("%-5s %-25s %-15s %-20s %-20s%n", "ID", "Project Name", "Neighbourhood", "Manager IC", "Exercise");
        System.out.println("----------------------------------------------------------------------------------------");

        for (BTOProj proj : projectList) {
            String exerciseName = "Unassigned";
            for (BTOExercise exercise : exerciseList) {
                for (BTOProj p : exercise.getExerciseProjs()) {
                    if (p.getProjId() == proj.getProjId()) {
                        exerciseName = exercise.getExerciseName();
                        break;
                    }
                }
            }

            String managerName = (proj.getManagerIC() != null) ? proj.getManagerIC().getFirstName() : "N/A";

            System.out.printf("%-5d %-25s %-15s %-20s %-20s%n",
                    proj.getProjId(),
                    proj.getProjName(),
                    proj.getProjNbh(),
                    managerName,
                    exerciseName);
        }

        // Prompt for more detail
        System.out.print("\nEnter Project ID to view more details and manage (or -1 to return): ");
        int targetId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (targetId == -1) return;

        BTOProj selected = null;
        for (BTOProj proj : projectList) {
            if (proj.getProjId() == targetId) {
                selected = proj;
                break;
            }
        }

        if (selected == null) {
            System.out.println("No project found with that ID.");
            return;
        }

        // Show full details
        System.out.println("\n=== Project Details ===");
        System.out.println("Project Name     : " + selected.getProjName());
        System.out.println("Open Date        : " + selected.getAppOpenDate().toLocalDate());
        System.out.println("Close Date       : " + selected.getAppCloseDate().toLocalDate());
        System.out.println("Visibility       : " + selected.getVisibility());
        System.out.println("Neighbourhood    : " + selected.getProjNbh());

        // Flat type details from map
        System.out.println("Flat Types");

        List<FlatType> flatTypes = selected.getAvailableFlatTypes();

        if (flatTypes == null || flatTypes.isEmpty()) {
            System.out.println("                    None");
        } else {
            Set<String> printed = new HashSet<>();
            for (FlatType type : flatTypes) {
                String key = type.getTypeName() + ":" + type.getTotalUnits() + ":" + type.getSellingPrice();
                if (!printed.contains(key)) {
                    System.out.printf("  - %-12s : %d units at $%.2f%n",
                            type.getTypeName(),
                            type.getTotalUnits(),
                            type.getSellingPrice());
                    printed.add(key);
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


    // Option 2
    public void createBTOProjects(BTOProjsController projsController, HDBBTOExerciseController exerciseController) {
        Scanner scanner = new Scanner(System.in);
        SessionStateManager session = SessionStateManager.getInstance();
        HDBManager manager = (HDBManager) session.getLoggedInUser();

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
            System.out.print("Enter exercise number: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
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
            System.out.printf("Enter number of %s units: ", type.getDisplayName());
            int units = scanner.nextInt();
            System.out.printf("Enter selling price for %s units: ", type.getDisplayName());
            float price = scanner.nextFloat();
            flatUnitMap.put(type, new FlatType(type.getDisplayName(), units, price));
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
                new HDBOfficer[0]
        );

        // Link to Exercise and save
        selectedExercise.addProject(newProj);
        projsController.addProject(newProj);

        System.out.println("\nProject created and assigned to: " + selectedExercise.getExerciseName());
    }

    // NEED FIX
    public void viewMyProjects(BTOProjsController projsController) {
        SessionStateManager session = SessionStateManager.getInstance();
        User currentUser = session.getLoggedInUser();

        if (!(currentUser instanceof HDBManager manager)) {
            System.out.println("Access denied. Only managers can view their own projects.");
            return;
        }

        // Get by manager ID instead of object equality
        List<BTOProj> myProjects = projsController.getProjectsByManagerName(manager.getFirstName());

        if (myProjects.isEmpty()) {
            System.out.println("You have not created any projects yet.");
            return;
        }

        System.out.println("=== Your BTO Projects ===");
        System.out.printf("%-5s %-25s %-15s %-20s%n", "ID", "Project Name", "Neighbourhood", "Visible");
        System.out.println("---------------------------------------------------------------");

        for (BTOProj proj : myProjects) {
            System.out.printf("%-5d %-25s %-15s %-20s%n",
                    proj.getProjId(),
                    proj.getProjName(),
                    proj.getProjNbh(),
                    proj.getVisibility());
        }
    }

    public void manageBTOProject(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. View All Projects");
        System.out.println("2. View My Project");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                viewAllProjects(projsController, exerciseController);
                break;
            case 2:
                viewMyProjects(projsController);
            default:
        }

    }
}
