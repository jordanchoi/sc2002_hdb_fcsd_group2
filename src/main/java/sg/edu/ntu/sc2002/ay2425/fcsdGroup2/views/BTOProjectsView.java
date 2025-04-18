package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BTOProjsController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.Neighbourhoods;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.util.ArrayList;
import java.util.List;
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
                // View BTO Exercises
                System.out.println("Viewing all BTO Project...\n");
                manageBTOProject();
            } case 2-> {
                // Create New BTO Exercise
                System.out.println("Creating BTO Project...\n");
                createBTOProjects(projsController, exerciseController);
            } case 3-> {
                // Editing BTO Exercise
                System.out.println("Editing BTO Project...\n");
            } case 4-> {
                // Deleting BTO Exercise
                System.out.println("Deleting BTO Project\n");
                // Logic to delete a BTO exercise
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
        List<BTOProj> projectList = projsController.viewAllProjs();
        List<BTOExercise> exerciseList = exerciseController.viewAllExercises();

        if (projectList.isEmpty()) {
            System.out.println("No BTO projects found.");
            return;
        }

        System.out.println("=== List of All BTO Projects ===");
        System.out.printf("%-5s %-22s %-12s %-12s %-8s %-15s %-10s %-10s %-15s %-15s%n",
                "ID", "Name", "Open", "Close", "Visible", "Neighbourhood", "2-Room", "3-Room", "Exercise", "Manager IC");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");

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

            int twoRoom = proj.getFlatUnits().getOrDefault(FlatTypes.TWO_ROOM, 0);
            int threeRoom = proj.getFlatUnits().getOrDefault(FlatTypes.THREE_ROOM, 0);

            // Manager in charge
            String managerName = (proj.getManagerIC() != null)
                    ? proj.getManagerIC().getFirstName()
                    : "N/A";

            System.out.printf("%-5d %-22s %-12s %-12s %-8s %-15s %-10d %-10d %-15s %-15s%n",
                    proj.getProjId(),
                    proj.getProjName(),
                    proj.getAppOpenDate().toLocalDate(),
                    proj.getAppCloseDate().toLocalDate(),
                    proj.getVisibility(),
                    proj.getProjNbh(),
                    twoRoom,
                    threeRoom,
                    exerciseName,
                    managerName);
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
            scanner.nextLine(); // consume invalid input
        }
        scanner.nextLine(); // consume newline
        BTOExercise selectedExercise = exercises.get(choice - 1);

        // Step 2: Project ID
        int id;
        while (true) {
            System.out.print("Enter Project ID: ");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                scanner.nextLine(); // consume newline

                // ✅ Ask controller to check for uniqueness
                if (projsController.isProjectIdUnique(id)) {
                    break; // valid and unique
                } else {
                    System.out.println("Project ID already exists. Please enter a unique ID.");
                }

            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // clear invalid input
            }
        }

        System.out.print("Enter Project Name: ");
        String name = scanner.nextLine();

        // Step 3: Dates
        LocalDateTime open = null;
        LocalDateTime close = null;
        while (open == null) {
            try {
                System.out.print("Enter Project Open Date (YYYY-MM-DD): ");
                String openDateStr = scanner.nextLine();
                open = LocalDateTime.parse(openDateStr + "T00:00:00");
            } catch (Exception e) {
                System.out.println("Invalid format. Try again.");
            }
        }
        while (close == null) {
            try {
                System.out.print("Enter Project Close Date (YYYY-MM-DD): ");
                String closeDateStr = scanner.nextLine();
                close = LocalDateTime.parse(closeDateStr + "T00:00:00");
            } catch (Exception e) {
                System.out.println("Invalid format. Try again.");
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
            System.out.println("You cannot manage overlapping projects.");
            return;
        }

        // Step 5: Neighbourhood selection
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
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

        // Step 6: Flat Unit Counts
        int twoRoomUnits = -1, threeRoomUnits = -1;
        while (twoRoomUnits < 0) {
            System.out.print("Enter number of 2-Room units: ");
            if (scanner.hasNextInt()) {
                twoRoomUnits = scanner.nextInt();
                if (twoRoomUnits < 0) System.out.println("Cannot be negative.");
            } else {
                System.out.println("Invalid input. Please enter a non-negative number.");
                scanner.nextLine();
            }
        }

        while (threeRoomUnits < 0) {
            System.out.print("Enter number of 3-Room units: ");
            if (scanner.hasNextInt()) {
                threeRoomUnits = scanner.nextInt();
                if (threeRoomUnits < 0) System.out.println("Cannot be negative.");
            } else {
                System.out.println("Invalid input. Please enter a non-negative number.");
                scanner.nextLine();
            }
        }

        // Step 7: Assign Manager and Officer Slots
        BTOProj newProj = null;
        int maxOfficerSlots = 10;

        // Step 8: Create Project
        newProj = projsController.CreateProj(id, name, open, close, visible);
        newProj.setProjNbh(selectedNeighbourhood);
        newProj.addFlatType(FlatTypes.TWO_ROOM, twoRoomUnits);
        newProj.addFlatType(FlatTypes.THREE_ROOM, threeRoomUnits);
        newProj.setManagerIC(manager);
        newProj.setOfficerSlotLimit(maxOfficerSlots);

        // Step 9: Link to Exercise
        selectedExercise.addProject(newProj);

        System.out.println("\nProject created and assigned to: " + selectedExercise.getExerciseName());
    }

    public void toggleProjectVisibility(BTOProjsController projsController) {
        Scanner scanner = new Scanner(System.in);
        List<BTOProj> projectList = projsController.viewAllProjs();

        if (projectList.isEmpty()) {
            System.out.println("\nNo projects available to toggle.");
            return;
        }

        System.out.print("Enter Project ID to toggle visibility: ");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            boolean success = projsController.toggleVisibilityById(id);
            if (success) {
                System.out.println("\nProject visibility toggled successfully.");
            } else {
                System.out.println("\nProject not found. Please try again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a valid Project ID.");
            scanner.nextLine();
        }
    }

    public void manageBTOProject(){
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            viewAllProjects(projsController, exerciseController);
            System.out.println("\n1. Toggle Visibility");
            System.out.println("4. Return to Main Menu");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }

            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> toggleProjectVisibility(projsController);
                case 4 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid input. Please try again.");
            }
        } while (choice != 4);
    }
}
