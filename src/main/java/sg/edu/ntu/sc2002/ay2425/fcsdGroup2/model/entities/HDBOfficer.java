package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

public class HDBOfficer extends User {
    private int officerId;
    private List<BTOProj> projectsHandled;
    private List<OfficerProjectApplication> registrationApps;


    // Constructor we will use in this project
    public HDBOfficer(int officerId, String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.officerId = officerId;
        this.projectsHandled = new ArrayList<>();
    }

    // Constructor for HDBOfficer, closer to the real world
    public HDBOfficer(int userId, String nric, String password, String firstName, String lastName, String middleName,int age, MaritalStatus maritalStatus) {
        super(userId, nric, password, firstName, lastName, middleName, age, maritalStatus);
        this.projectsHandled = new ArrayList<>();
    }



    /*
    @Override
    public void viewProjects() {
        if (assignedProject != null) {
            System.out.println(name + " is managing BTO Project: " + assignedProject.getProjectName());
        } else {
            System.out.println(name + " is not assigned to any project.");
        }
    }
    */
    @Override
    public void viewMenu() {
        System.out.println("\n=== HDB Officer Menu ===");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Apply for a BTO project");
        System.out.println("3. View applied project status");
        System.out.println("4. Withdraw application");
        System.out.println("5. Submit enquiry");
        System.out.println("6. View/Edit/Delete enquiries");
        System.out.println("7. Register as HDB Officer for a project");
        System.out.println("8. View registration status for Officer role");
        System.out.println("9. View details of assigned project");
        System.out.println("10. View and respond to enquiries regarding assigned project");
        System.out.println("11. Process flat bookings for successful applicants");
        System.out.println("12. Generate receipt for flat bookings");
        System.out.println("13. Change password");
        System.out.println("14. Logout");
    }

    @Override
    public String toString() {
        return "HDBOfficer{" +
                "officerId=" + officerId +
                ", projectsHandled=" + projectsHandled +
                ", registrationApps=" + registrationApps +
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
