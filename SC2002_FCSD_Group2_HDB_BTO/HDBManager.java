import java.util.ArrayList;
import java.util.List;

class HDBManager extends User {
    private List<BTOProject> managedProjects;

    public HDBManager(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.managedProjects = new ArrayList<>();
    }

    @Override
    public void viewProjects() {
        System.out.println(name + " is managing the following BTO Projects:");
        for (BTOProject project : managedProjects) {
            System.out.println("- " + project.getProjectName());
        }
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
}
