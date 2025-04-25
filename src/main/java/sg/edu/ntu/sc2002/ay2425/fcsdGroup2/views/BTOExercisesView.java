package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the view for managing BTO Exercises.
 * Allows creating, editing, deleting, and viewing BTO exercises.
 */
public class BTOExercisesView implements UserView {
    private final HDBBTOExerciseController exerciseController;

    /**
     * Constructs a new BTOExercisesView instance.
     *
     * @param exerciseController the controller managing BTO exercises
     */
    public BTOExercisesView(HDBBTOExerciseController exerciseController) {
        // constructor for logic if required
        this.exerciseController = exerciseController;
    }

    /**
     * Starts the BTO Exercise Management Console session.
     */
    public void start() {
        System.out.println("You are in the BTO Exercises Management Console.\nHere, you can manage BTO exercises, including creating of BTO Exercises or get statistics for a particular exercise.\n");
        System.out.println("What would you like to do?\n");
        int choice = 0;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 5); // Assuming 10 is the exit option
    }

    /**
     * Displays the main menu for BTO Exercise management.
     */
    @Override
    public void displayMenu() {
        System.out.println("1. View BTO Exercises");
        System.out.println("2. Create New BTO Exercise");
        System.out.println("3. Edit BTO Exercise");
        System.out.println("4. Delete BTO Exercise");
        System.out.println("5. Return to Main Menu");
    }

    /**
     * Handles user input selection from the BTO Exercise menu.
     *
     * @return the selected menu option
     */
    @Override
    public int handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (true) {
            System.out.print("\nPlease select an option (1–5): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume leftover newline

                if (choice >= 1 && choice <= 5) {
                    break; // valid input
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // discard invalid token
            }
        }

        switch (choice) {
            case 1 -> {
                System.out.println("Viewing all BTO Exercises...\n");
                viewAllExercises(exerciseController);
            }
            case 2 -> {
                System.out.println("Creating BTO Exercise...\n");
                createBTOExercise(exerciseController);
            }
            case 3 -> {
                System.out.println("Editing BTO Exercise...\n");
                editBTOExercise();
            }
            case 4 -> {
                System.out.println("Deleting BTO Exercises\n");
                deleteExercise();
            }
            case 5 -> {
                System.out.println("Exiting to Main Manager Console...\n");
            }
        }

        System.out.println();
        return choice;
    }

    /**
     * Displays all existing BTO Exercises.
     *
     * @param exerciseController the controller used to fetch exercises
     */
    // Displays all BTO exercises by fetching from the controller.
    public void viewAllExercises(HDBBTOExerciseController exerciseController) {
        exerciseController.insertExercisesFromRepo();
        List<BTOExercise> exerciseList = exerciseController.viewAllExercises();

        if (exerciseList.isEmpty()) {
            System.out.println("No exercises found.");
            return;
        }

        printAllExercises(exerciseList);
    }

    /**
     * Helper method to print a list of BTO exercises in a tabular format.
     *
     * @param exercises the list of exercises to print
     */
    // Helper method to format and print a list of BTO exercises in tabular format.
    private void printAllExercises(List<BTOExercise> exercises) {
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

    /**
     * Prompts user to create a new BTO Exercise.
     *
     * @param exerciseController the controller to create the exercise
     */
    // Creates a new BTO exercise by prompting the user for ID, name, applicants, and status.
    public void createBTOExercise(HDBBTOExerciseController exerciseController) {
        Scanner scanner = new Scanner(System.in);

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
                scanner.nextLine(); // clear buffer
            }
        }

        String name;
        while (true) {
            System.out.print("Enter Exercise Name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
            System.out.println("Exercise name cannot be empty. Please try again.");
        }

        int totalApplicants;
        while (true) {
            System.out.print("Enter Total Applicants: ");
            if (scanner.hasNextInt()) {
                totalApplicants = scanner.nextInt();
                scanner.nextLine();
                if (totalApplicants >= 0) break;
                else System.out.println("Total applicants cannot be negative.");
            } else {
                System.out.println("Invalid input. Please enter a non-negative integer.");
                scanner.nextLine();
            }
        }

        ProjStatus status = null;
        while (status == null) {
            System.out.println("Select Project Status:");
            System.out.println("1. OPEN");
            System.out.println("2. CLOSED");
            System.out.println("3. BOOKING");
            System.out.println("4. COMPLETED");
            System.out.print("Enter your choice (1–4): ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> status = ProjStatus.OPEN;
                    case 2 -> status = ProjStatus.CLOSED;
                    case 3 -> status = ProjStatus.BOOKING;
                    case 4 -> status = ProjStatus.COMPLETED;
                    default -> System.out.println("Invalid choice. Please enter 1–4.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
        List<BTOProj> projList = new ArrayList<>();
        exerciseController.createExercise(id, name, totalApplicants, status, projList);
        System.out.println("\nExercise created");
    }

    /**
     * Allows editing the details of an existing BTO Exercise,
     * including name, total applicants, and project status.
     */
    // Allows the user to edit a selected BTO exercise's name, applicants, and status.
    // Changes are saved using the controller.
    private void editBTOExercise() {
        Scanner scanner = new Scanner(System.in);
        exerciseController.insertExercisesFromRepo();
        List<BTOExercise> exercises = exerciseController.viewAllExercises();

        if (exercises.isEmpty()) {
            System.out.println("No exercises found.");
            return;
        }

        printAllExercises(exercises);

        System.out.print("\nEnter Exercise ID to edit: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        BTOExercise selected = null;
        for (BTOExercise ex : exercises) {
            if (ex.getExerciseId() == id) {
                selected = ex;
                break;
            }
        }

        if (selected == null) {
            System.out.println("No exercise found with that ID.");
            return;
        }

        boolean done = false;
        String newName = selected.getExerciseName();
        int newApplicants = selected.getTotalApplicants();
        ProjStatus newStatus = selected.getProjStatus();

        while (!done) {
            System.out.println("\nEditing Exercise: " + selected.getExerciseName());
            System.out.println("Select field to edit:");
            System.out.println("1. Name (current: " + newName + ")");
            System.out.println("2. Total Applicants (current: " + newApplicants + ")");
            System.out.println("3. Status (current: " + newStatus + ")");
            System.out.println("4. Save and Exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    String input = scanner.nextLine().trim();
                    if (!input.isEmpty()) newName = input;
                }
                case "2" -> {
                    System.out.print("Enter new total applicants: ");
                    try {
                        newApplicants = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number.");
                    }
                }
                case "3" -> {
                    System.out.println("Select new status:");
                    System.out.println("1. OPEN");
                    System.out.println("2. CLOSED");
                    System.out.println("3. BOOKING");
                    System.out.println("4. COMPLETED");
                    System.out.print("Your choice: ");
                    String statusInput = scanner.nextLine().trim();

                    switch (statusInput) {
                        case "1" -> newStatus = ProjStatus.OPEN;
                        case "2" -> newStatus = ProjStatus.CLOSED;
                        case "3" -> newStatus = ProjStatus.BOOKING;
                        case "4" -> newStatus = ProjStatus.COMPLETED;
                        default -> System.out.println("Invalid choice.");
                    }
                }
                case "4" -> {
                    boolean updated = exerciseController.editExercise(id, newName, newApplicants, newStatus);
                    System.out.println(updated ? "Exercise updated successfully." : "Update failed.");
                    done = true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * Deletes a selected BTO Exercise after confirmation.
     */
    // Deletes a BTO exercise selected by ID after user confirmation.
    // The controller removes the exercise and persists changes to Excel.
    private void deleteExercise() {
        exerciseController.insertExercisesFromRepo();
        List<BTOExercise> exercises = exerciseController.viewAllExercises();

        if (exercises.isEmpty()) {
            System.out.println("No exercises available to delete.");
            return;
        }

        printAllExercises(exercises);

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter Exercise ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Are you sure you want to delete this exercise? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean success = exerciseController.deleteExerciseId(id);
        System.out.println(success ? "Exercise deleted successfully." : "No exercise found with the given ID.");
    }
}
