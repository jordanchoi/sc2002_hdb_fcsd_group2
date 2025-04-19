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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
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
        System.out.print("\nPlease select an option: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1-> {
                // View BTO Project
                System.out.println("Viewing all BTO Project...\n");
                manageBTOProject();
            } case 2-> {
                // Create New BTO Project
                System.out.println("Creating BTO Project...\n");
                createBTOProjects(projsController, exerciseController);
            } case 3-> {
                // Editing BTO Project
                System.out.println("Editing BTO Project...\n");
            } case 4-> {
                // Deleting BTO Project
                System.out.println("Deleting BTO Project\n");
                // Logic to delete a BTO Project
            } case 5-> {
                // View All Projects
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

        FlatType twoRoom = selected.getFlatUnits().get(FlatTypes.TWO_ROOM);
        FlatType threeRoom = selected.getFlatUnits().get(FlatTypes.THREE_ROOM);

        System.out.println("2-Room Units     : " + (twoRoom != null ? twoRoom.getTotalUnits() : 0));
        System.out.println("2-Room Price     : $" + (twoRoom != null ? String.format("%.2f", twoRoom.getSellingPrice()) : "0.00"));
        System.out.println("3-Room Units     : " + (threeRoom != null ? threeRoom.getTotalUnits() : 0));
        System.out.println("3-Room Price     : $" + (threeRoom != null ? String.format("%.2f", threeRoom.getSellingPrice()) : "0.00"));

        System.out.println("Officer Slots    : " + selected.getOfficerSlots());

        if (selected.getOfficersList() != null && !selected.getOfficersList().isEmpty()) {
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

        // Step 1: Select BTO Exercise
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

        // Step 2: Project ID
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

        // Step 3: Dates
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

        // Step 4: Visibility
        boolean visible = false;
        LocalDateTime now = LocalDateTime.now();
        if ((open.isBefore(now) || open.isEqual(now)) && (close.isAfter(now) || close.isEqual(now))) {
            visible = true;
        }

        if (!projsController.isManagerAvailable(manager, open, close, exerciseController.viewAllExercises())) {
            System.out.println("\nYou are already managing a project within this time period.");
            return;
        }

        // Step 5: Neighbourhood
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

        // Step 6: Flat Units and Selling Prices
        int twoRoomUnits = -1, threeRoomUnits = -1;
        float twoRoomPrice = -1, threeRoomPrice = -1;

        while (twoRoomUnits < 0) {
            System.out.print("Enter number of 2-Room units: ");
            if (scanner.hasNextInt()) {
                twoRoomUnits = scanner.nextInt();
                if (twoRoomUnits < 0) System.out.println("Cannot be negative.");
            } else {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }

        while (twoRoomPrice < 0) {
            System.out.print("Enter selling price for 2-Room units: ");
            if (scanner.hasNextFloat()) {
                twoRoomPrice = scanner.nextFloat();
                if (twoRoomPrice < 0) System.out.println("Cannot be negative.");
            } else {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }

        while (threeRoomUnits < 0) {
            System.out.print("Enter number of 3-Room units: ");
            if (scanner.hasNextInt()) {
                threeRoomUnits = scanner.nextInt();
                if (threeRoomUnits < 0) System.out.println("Cannot be negative.");
            } else {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }

        while (threeRoomPrice < 0) {
            System.out.print("Enter selling price for 3-Room units: ");
            if (scanner.hasNextFloat()) {
                threeRoomPrice = scanner.nextFloat();
                if (threeRoomPrice < 0) System.out.println("Cannot be negative.");
            } else {
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        }

        // Step 7: Officer Slot Input
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
        scanner.nextLine(); // clear newline

        // Step 8: Create Project
        BTOProj newProj = projsController.CreateProj(id, name, open, close, visible);
        newProj.setProjNbh(selectedNeighbourhood);
        newProj.addFlatTypeWithPrice(FlatTypes.TWO_ROOM, twoRoomUnits, twoRoomPrice);
        newProj.addFlatTypeWithPrice(FlatTypes.THREE_ROOM, threeRoomUnits, threeRoomPrice);
        newProj.setManagerIC(manager);
        newProj.setOfficerSlots(officerSlots);

        // Step 9: Assign Officers
        System.out.println("Enter officer names to assign (comma-separated, max " + officerSlots + "):");
        String[] names = scanner.nextLine().split(",");
        int assignedCount = 0;
        for (String n : names) {
            Optional<User> officerUser = new UserRepository().getUserByName(n.trim(), UserRoles.OFFICER);
            if (officerUser.isPresent() && assignedCount < officerSlots) {
                newProj.assignOfficer((HDBOfficer) officerUser.get());
                assignedCount++;
            }
        }

        // Step 10: Link to Exercise
        selectedExercise.addProject(newProj);

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
