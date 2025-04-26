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

    /**
     * Private constructor to enforce singleton pattern.
     */
    private SessionStateManager() {
    }

    /**
     * Gets the singleton instance of SessionStateManager.
     *
     * @return the instance
     */
    public static SessionStateManager getInstance() {
        return instance;
    }

    /**
     * Logs a user into the session.
     *
     * @param user the logged-in user
     * @param userType the type of user
     */
    public void login(User user, UserRoles userType) {
        this.loggedInUser = user;
        this.isLoggedIn = true;
        this.loggedInUserType = userType;
    }

    /**
     * Logs out the current user and clears session state.
     */
    public void logout() {
        this.loggedInUser = null;
        this.isLoggedIn = false;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the logged-in user
     */
    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    /**
     * Checks whether a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    /**
     * Retrieves the role of the currently logged-in user.
     *
     * @return the user's role
     */
    public UserRoles getLoggedInUserType() {
        return this.loggedInUserType;
    }

    /**
     * Sets the role of the currently logged-in user.
     *
     * @param loggedInUserType the user's role
     */
    public void setLoggedInUserType(UserRoles loggedInUserType) {
        this.loggedInUserType = loggedInUserType;
    }
}
