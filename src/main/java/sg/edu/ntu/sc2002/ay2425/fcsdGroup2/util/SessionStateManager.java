package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.User;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;

import java.util.Enumeration;

/*
    * SessionStateManager.java
    * This class is a singleton that manages the session state of the application.
    * It stores the logged-in user and their role, and provides methods to access this information.
    * It is used to determine the current state of the application and to manage user sessions.
    * It is also used to check if the user is logged in and to log out the user.
    * It is used to manage the session state of the application.
    * It is a singleton class, meaning that there is only one instance of this class in the application.
 */

public class SessionStateManager {
    private static final SessionStateManager instance = new SessionStateManager();

    private User loggedInUser;
    private UserRoles loggedInUserType;
    private boolean isLoggedIn;

    // private constructor to prevent instantiation
    private SessionStateManager() {
    }

    // public accessor
    public static SessionStateManager getInstance() {
        return instance;
    }

    // Set current logged in user
    public void login(User user, UserRoles userType) {
        this.loggedInUser = user;
        this.isLoggedIn = true;
        this.loggedInUserType = userType;
    }

    // Clear session
    public void logout() {
        this.loggedInUser = null;
        this.isLoggedIn = false;
    }

    // Get current logged in user
    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    // Get current logged in user type
    public UserRoles getLoggedInUserType() {
        return this.loggedInUserType;
    }
    // Set current logged in user type
    public void setLoggedInUserType(UserRoles loggedInUserType) {
        this.loggedInUserType = loggedInUserType;
    }
}
