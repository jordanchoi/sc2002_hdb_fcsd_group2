import java.util.ArrayList;
import java.util.List;

class Applicant extends User {
    private BTOProject appliedProject;
    private String applicationStatus;
    private List<Enquiry> enquiries;

    public Applicant(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.applicationStatus = "Pending";
        this.enquiries = new ArrayList<>();
    }

    @Override
    public void viewProjects() {
        System.out.println(name + " is viewing available BTO projects...");
        for (BTOProject project : BTOProject.getAllProjects()) {
            if (project.isVisible()) {
                System.out.println("- " + project.getProjectName() + " (" + project.getNeighborhood() + ")");
            }
        }
    }

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
}
