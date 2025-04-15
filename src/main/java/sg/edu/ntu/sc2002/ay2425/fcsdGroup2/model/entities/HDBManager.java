package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

public class HDBManager extends User {
    private int managerId;
    private final List<BTOProj> managedProjs;

    // Constructor we will use for this project
    public HDBManager(int managerId, String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.managerId = 0;
        managedProjs = new ArrayList<>();
    }

    // Constructor closer to the real world
    public HDBManager(int userId, String nric, String password, String firstName, String lastName, String middleName,int age, MaritalStatus maritalStatus, int managerId) {
        super(userId, nric, password, firstName, lastName, middleName, age, maritalStatus);
        this.managerId = managerId;
        managedProjs = new ArrayList<>();
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int id) {
        this.managerId = id;
    }

    public void assignProj(BTOProj project) {
        managedProjs.add(project);
    }

    public void unAssignProj(BTOProj project) {
        managedProjs.remove(project);
    }

    public BTOProj getProj(int id) {
        for (BTOProj p : managedProjs) {
            if (p.getProjId() == id) return p;
        }
        return null;
    }

    public BTOProj getCurrentProj() {
        if (managedProjs.isEmpty()) return null;
        return managedProjs.get(managedProjs.size() - 1);
    }

    public boolean canBeAssigned() {
        return managedProjs.size() < 3;
    }

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
