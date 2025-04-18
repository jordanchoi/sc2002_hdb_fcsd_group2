package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BTOProjsController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;

public class BTOProjectsView implements UserView {
    private final BTOProjsController projsController;
    private final HDBBTOExerciseController exerciseController;

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
        System.out.println("1. View BTO Project");
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
                viewAllProjects(projsController);
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
    public void viewAllProjects(BTOProjsController projsController){
        List<BTOProj> projectList = projsController.viewAllProjs();
        if (projectList.isEmpty()) {
            System.out.println("No BTO projects found.");
            return;
        }

        System.out.println("=== List of All BTO Projects ===");
        System.out.printf("%-5s %-20s %-18s %-18s %-10s%n",
                "ID", "Name", "Open Date", "Close Date", "Visible");
        System.out.println("------------------------------------------------------------------------");

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
    
    // Option 2
    public void createBTOProjects(BTOProjsController projsController, HDBBTOExerciseController exerciseController){
        Scanner scanner = new Scanner(System.in);

        // Select BTO exercise
        List<BTOExercise> exercises = exerciseController.viewAllExercises();
        if (exercises.isEmpty()) {
            System.out.println("No BTO exercises found. Please create an exercise first.");
            return;
        }

        System.out.println("Select a BTO Exercise to assign this project to:");
        for (int i = 0; i < exercises.size(); i++) {
            BTOExercise ex = exercises.get(i);
            System.out.printf("%d. %s (ID: %d)\n", i + 1, ex.getExerciseName(), ex.getExerciseId());
        }

        System.out.print("Enter exercise number: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice < 1 || choice > exercises.size()) {
            System.out.println("Invalid choice. Aborting project creation.");
            return;
        }

        BTOExercise selectedExercise = exercises.get(choice - 1);

        System.out.print("Enter Project ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Project Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Project Open Year-Month-Day (e.g., 2025-01-01): ");
        String openDateStr = scanner.nextLine();
        LocalDateTime open = LocalDateTime.parse(openDateStr + "T00:00:00");

        System.out.print("Enter Project Close Year-Month-Day (e.g., 2025-12-31): ");
        String closeDateStr = scanner.nextLine();
        LocalDateTime close = LocalDateTime.parse(closeDateStr + "T00:00:00");

        System.out.print("Is project visible? (true/false): ");
        boolean visible = scanner.nextBoolean();

        projsController.CreateProj(id, name, open, close, visible);
        System.out.println("\nProject created.");
    }

}
