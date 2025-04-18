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
        exerciseController.insertExercisesFromRepo();
        List<BTOExercise> exercises = exerciseController.viewAllExercises();

        if (exercises.isEmpty()) {
            System.out.println("No BTO exercises found.");
            return;
        }

        System.out.println("=== All BTO Exercises ===");
        System.out.printf("%-5s %-27s %-10s %-15s %-20s%n",
                "ID", "Name", "Status", "Applicants", "Projects");

        System.out.println("----------------------------------------------------------------------------------");

        for (BTOExercise ex : exercises) {
            StringBuilder projectNames = new StringBuilder();
            for (BTOProj proj : ex.getExerciseProjs()) {
                if (!projectNames.isEmpty()) projectNames.append(", ");
                projectNames.append(proj.getProjName());
            }

            System.out.printf("%-5d %-27s %-10s %-15d %-20s%n",
                    ex.getExerciseId(),
                    ex.getExerciseName(),
                    ex.getProjStatus(),
                    ex.getTotalApplicants(),
                    projectNames.toString());
        }
    }

    // Option 2
    public void createBTOExercise(HDBBTOExerciseController exerciseController) {
        Scanner scanner = new Scanner(System.in);

        // Exercise ID (validated)
        int id;
        while (true) {
            System.out.print("Enter Exercise ID: ");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (exerciseController.isExerciseIdUnique(id)) {
                    break;
                } else {
                    System.out.println("This Exercise ID already exists. Please enter a unique ID.");
                }

            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // clear invalid input
            }
        }

        // Exercise Name (non-empty)
        String name;
        while (true) {
            System.out.print("Enter Exercise Name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("Exercise name cannot be empty. Please try again.");
        }

        // Total Applicants (non-negative)
        int totalApplicants;
        while (true) {
            System.out.print("Enter Total Applicants: ");
            if (scanner.hasNextInt()) {
                totalApplicants = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (totalApplicants >= 0) break;
                else System.out.println("Total applicants cannot be negative.");
            } else {
                System.out.println("Invalid input. Please enter a non-negative integer.");
                scanner.nextLine(); // discard invalid input
            }
        }

        // Status selection
        ProjStatus status = null;
        while (status == null) {
            System.out.println("Select Project Status:");
            System.out.println("1. OPEN");
            System.out.println("2. CLOSED");
            System.out.println("3. BOOKING");
            System.out.println("4. COMPLETED");
            System.out.print("Enter your choice (1-4): ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> status = ProjStatus.OPEN;
                    case 2 -> status = ProjStatus.CLOSED;
                    case 3 -> status = ProjStatus.BOOKING;
                    case 4 -> status = ProjStatus.COMPLETED;
                    default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

        // Create empty project list
        List<BTOProj> projList = new ArrayList<>();

        // Create the exercise
        exerciseController.createExercise(id, name, totalApplicants, status, projList);
        System.out.println("\nExercise created successfully.");
    }
}
