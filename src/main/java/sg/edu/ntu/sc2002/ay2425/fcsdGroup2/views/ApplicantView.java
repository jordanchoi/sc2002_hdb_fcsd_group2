package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

public class ApplicantView implements UserView {
    private SessionStateManager session = SessionStateManager.getInstance();

    public ApplicantView() {
        // Constructor logic if needed
    }

    @Override
    public void start() {
        System.out.println("Welcome! Applicant " + session.getLoggedInUser().getFirstName() + "!\nYou are in the Applicant Main Menu.\n");
    }

    @Override
    public void displayMenu() {
    }

    @Override
    public int handleUserInput() {
        // placeholder return statement
        return 0;
    }
}
