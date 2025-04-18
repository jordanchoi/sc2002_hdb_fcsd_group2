package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BTOExercisesView implements UserView {
    private final HDBBTOExerciseController exerciseController;

    public BTOExercisesView(HDBBTOExerciseController exerciseController) {
        // constructor for logic if required
        this.exerciseController = exerciseController;
    }

    public void start() {
        System.out.println("You are in the BTO Exercises Management Console.\nHere, you can manage BTO exercises, including creating of BTO Exercises or get statistics for a particular exercise.\n");
        System.out.println("What would you like to do?\n");
        int choice = 0;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 5); // Assuming 10 is the exit option
    }

    @Override
    public void displayMenu() {
        System.out.println("1. View BTO Exercises");
        System.out.println("2. Create New BTO Exercise");
        System.out.println("3. Edit BTO Exercise");
        System.out.println("4. Delete BTO Exercise");
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
                System.out.println("Viewing all BTO Exercises...\n");
                viewAllExercises(exerciseController);
            } case 2-> {
                // Create New BTO Exercise
                System.out.println("Creating BTO Exercise...\n");
                createBTOExercise(exerciseController);
            } case 3-> {
                // Editing BTO Exercise
                System.out.println("Editing BTO Exercise...\n");
            } case 4-> {
                // Deleting BTO Exercise
                System.out.println("Deleting BTO Exercises\n");
                // Logic to delete a BTO exercise
            } case 5-> {
                // View All Projects
                System.out.println("Exiting to Main Manager Console...\n");
            }
        }
        System.out.println();
        return choice;
    }

    // Option 1
    public void viewAllExercises(HDBBTOExerciseController exerciseController) {
        List<BTOExercise> exerciseList = exerciseController.viewAllExercises(); // you must implement this method

        if (exerciseList.isEmpty()) {
            System.out.println("No BTO exercises found.");
            return;
        }

        System.out.println("=== List of All BTO Exercises ===");
        System.out.printf("%-4s %-20s %-12s %-10s%n",
                "ID", "Name", "Status", "Applicants");
        System.out.println("-------------------------------------------------");

        for (BTOExercise exercise : exerciseList) {
            System.out.printf("%-4d %-20s %-12s %-10d%n",
                    exercise.getExerciseId(),
                    exercise.getExerciseName(),
                    exercise.getProjStatus(),
                    exercise.getTotalApplicants());
        }
    }

    // Option 2
    public void createBTOExercise(HDBBTOExerciseController exerciseController){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Exercise ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Exercise Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Total Applicants: ");
        int totalApplicants = scanner.nextInt();
        scanner.nextLine();

        ProjStatus status = null;
        System.out.println("Select Project Status:");
        System.out.println("1. OPEN");
        System.out.println("2. CLOSED");
        System.out.println("3. BOOKING");
        System.out.println("4. COMPLETED");
        System.out.print("Enter your choice (1-4): ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> status = ProjStatus.OPEN;
            case 2 -> status = ProjStatus.CLOSED;
            case 3 -> status = ProjStatus.BOOKING;
            case 4 -> status = ProjStatus.COMPLETED;
            default -> System.out.println("Invalid choice. Defaulting to OPEN.");
        }

        List<BTOProj> ProjList = new ArrayList<>();

        exerciseController.createExercise(id, name, totalApplicants, status, ProjList);
        System.out.println("\nExercise created.");
    }


}
