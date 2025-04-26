package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces;

/**
 * Interface defining enquiry handling operations for different user roles.
 * Each role (e.g., Officer, Manager) will implement how enquiry options are displayed.
 */
public interface RoleHandler {

    /**
     * Displays enquiry-related options for the user based on their role.
     */
    void displayEnquiryOptions();
}
