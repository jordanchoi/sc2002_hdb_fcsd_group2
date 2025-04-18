package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.util.Scanner;

public class BTOProjectsView implements UserView {

    public BTOProjectsView() {
        // constructor for logic if required
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
                // Logic to view all BTO exercises
            } case 2-> {
                // Create New BTO Exercise
                System.out.println("Creating BTO Exercise...\n");
                // Logic to create a new BTO exercise
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
        return choice;
    }
}
