package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

/**
 * Represents an HDB Manager who manages BTO projects and officer applications.
 */
public class HDBManager extends User {
    private int managerId;
    private final List<BTOProj> managedProjs;

    /**
     * Constructs an HDB Manager with basic information.
     */
    public HDBManager(int managerId, String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.managerId = 0;
        managedProjs = new ArrayList<>();
    }

    /**
     * Constructs an HDB Manager with full real-world details.
     */
    public HDBManager(int userId, String nric, String password, String firstName, String lastName, String middleName,int age, MaritalStatus maritalStatus, int managerId) {
        super(userId, nric, password, firstName, lastName, middleName, age, maritalStatus);
        this.managerId = managerId;
        managedProjs = new ArrayList<>();
    }

    /** Returns the manager ID. */
    public int getManagerId() { return managerId; }

    /** Sets the manager ID. */
    public void setManagerId(int id) { this.managerId = id; }

    /** Assigns a project to the manager. */
    public void assignProj(BTOProj project) { managedProjs.add(project); }

    /** Unassigns a project from the manager. */
    public void unAssignProj(BTOProj project) { managedProjs.remove(project); }

    /**
     * Retrieves a project managed by this manager by project ID.
     *
     * @param id project ID
     * @return the project if found, else null
     */
    public BTOProj getProj(int id) {
        for (BTOProj p : managedProjs) {
            if (p.getProjId() == id) return p;
        }
        return null;
    }

    /** Returns the list of all managed projects. */
    public List<BTOProj> getAllProjs() { return managedProjs; }

    /** Returns a copy of the current list of managed projects. */
    public List<BTOProj> getCurrentProj() {
        List<BTOProj> result = new ArrayList<>();
        for (int i = 0; i < managedProjs.size(); i++) {
            result.add(managedProjs.get(i));
        }
        return result;
    }

    /**
     * Checks if the manager can still be assigned to new projects.
     *
     * @return true if under limit (3 projects), false otherwise
     */
    public boolean canBeAssigned() {
        return managedProjs.size() < 3;
    }

    /** Displays the menu options for an HDB Manager. */
    @Override
    public void viewMenu() {
        System.out.println("\n=== HDB Manager Menu ===");
        System.out.println("1. Create new BTO project");
        System.out.println("2. Edit existing BTO project"); // toggle project visibility should be in here
        System.out.println("3. Delete a BTO project");
        System.out.println("5. View all created BTO projects");
        System.out.println("6. View pending and approved HDB Officer registrations");
        System.out.println("7. Approve or reject HDB Officer registrations");
        System.out.println("8. Approve or reject Applicant's BTO applications");
        System.out.println("9. Approve or reject Applicant's withdrawal requests");
        System.out.println("10. Generate reports on applicant choices");
        System.out.println("11. View all project enquiries");
        System.out.println("12. Respond to enquiries regarding assigned project");
        System.out.println("13. Change password");
        System.out.println("14. Logout");
    }

    @Override
    public String toString() {
        return "HDBManager{" +
                "managerId=" + managerId +
                ", managedProjs=" + managedProjs +
                ", userId=" + userId +
                ", nric='" + nric + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", age=" + age +
                ", maritalStatus=" + maritalStatus +
                '}';
    }
}
