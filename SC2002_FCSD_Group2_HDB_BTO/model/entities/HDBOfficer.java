package model.entities;

import model.enums.MaritalStatus;

public class HDBOfficer extends User {
    private BTOProj projectsHandled;

    public HDBOfficer(int userId, String nric, String password, String firstName, String lastName, String middleName,int age, MaritalStatus maritalStatus) {
        super(userId, nric, password, firstName, lastName, middleName, age, maritalStatus);
        this.projectsHandled = projectsHandled; // initialize subclass field
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
}
