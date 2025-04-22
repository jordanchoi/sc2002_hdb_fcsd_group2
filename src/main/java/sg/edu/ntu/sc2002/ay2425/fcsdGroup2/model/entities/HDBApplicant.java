package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

public class HDBApplicant extends User {
    /*
        * The HDBApplicant class represents an applicant for HDB BTO projects.
        * It extends the User class and includes additional attributes and methods specific to HDB applicants.
     */
    private BTOProj appliedProject;
    private List<Application> applications;
    private List<Enquiry> enquiries;

    // Constructor we will use for this project
    public HDBApplicant(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        // do we need this? -
        appliedProject = null;
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    // Constructor that is closer to the real world
    public HDBApplicant(int userId, String nric, String password, String firstName, String lastName, String middleName,int age, MaritalStatus maritalStatus) {
        super(userId, nric, password, firstName, lastName, middleName, age, maritalStatus);
        appliedProject = null;
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }
    /* 
    @Override
    public void viewProjects() {
        System.out.println(name + " is viewing available BTO projects...");
        for (model.entities.BTOProj project : model.entities.BTOProj.getAllProjects()) {
            if (project.isVisible()) {
                System.out.println("- " + project.getProjectName() + " (" + project.getNeighborhood() + ")");
            }
        }
    }*/

    @Override
    public void viewMenu(){
        System.out.println("\n=== Applicant Menu ===");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Apply for a BTO project");
        System.out.println("3. View applied project status");
        System.out.println("4. Withdraw application");
        System.out.println("5. Submit enquiry");
        System.out.println("6. View/Edit/Delete enquiries");
        System.out.println("7. Change password");
        System.out.println("8. Logout");
    }

    @Override
    public String toString() {
        return "HDBApplicant{" +
                "maritalStatus=" + maritalStatus +
                ", age=" + age +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", nric='" + nric + '\'' +
                ", userId=" + userId +
                ", enquiries=" + enquiries +
                ", applications=" + applications +
                ", appliedProject=" + appliedProject +
                '}';
    }
}
