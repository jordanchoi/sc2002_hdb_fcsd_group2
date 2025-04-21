package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

public class ApplicantView implements UserView {
    SessionStateManager session = SessionStateManager.getInstance();

    @Override
    public void displayMenu() {

    }

    @Override
    public int handleUserInput() {
        // placeholder return statement
        return 0;
    }
}
