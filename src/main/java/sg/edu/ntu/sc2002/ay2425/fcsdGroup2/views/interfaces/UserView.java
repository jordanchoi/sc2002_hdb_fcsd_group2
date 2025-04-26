package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces;

/**
 * Interface defining the structure of a user view in the system.
 * Each role's view (e.g., ApplicantView, OfficerView, ManagerView) will implement this.
 */
public interface UserView {

    /**
     * Starts the view and handles session initialization.
     */
    void start();

    /**
     * Displays the main menu options for the user.
     */
    void displayMenu();

    /**
     * Handles and processes the user's input selection from the menu.
     *
     * @return the menu choice selected by the user
     */
    int handleUserInput();
}
