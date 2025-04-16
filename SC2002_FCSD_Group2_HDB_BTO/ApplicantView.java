import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ApplicantView{

    public BTOProj selectProject(List<BTOProj> availableProjects) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Available BTO Projects ===");
        
        if (availableProjects.isEmpty()) {
            System.out.println("No available projects found.");
            return null;
        }
    
        for (BTOProj project : availableProjects) {
            System.out.println(project.getProjId() + ". " + project.getProjName() + " (" + project.getProjNbh() + ")");
        }
    
        System.out.println("\nEnter the Project ID of the project you want to apply for, or 0 to return to the main menu:");
    
        int chosenProjId;
        try {
            chosenProjId = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid Project ID.");
            return null;
        }
    
        if (chosenProjId == 0) {
            return null;
        }
    
        for (BTOProj project : availableProjects) {
            if (project.getProjId() == chosenProjId) {
                return project;
            }
        }
    
        System.out.println("Invalid Project ID. Please try again.");
        return null;
    }

    public int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid option.");
        }
        return choice;
    }


    public void viewApplicationStatus(HDBApplicant applicant) {
        System.out.println("Your application status for " +  "[insert project name] is " + applicant.getApplicationStatus());
    }

    public void viewMenu(){
        System.out.println("\n=== Applicant Menu ===");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Apply for a BTO project");
        System.out.println("3. View applied project status");
        System.out.println("4. Withdraw application");
        System.out.println("5. Submit enquiry");
        System.out.println("6. Add message to existing enquiry");
        System.out.println("7. View enquiries");
        System.out.println("8. Edit enquiries");
        System.out.println("9. Delete enquiry");
        System.out.println("10. Change password");
        System.out.println("11. Logout");
    }

    public void viewEligibleProjects(List<BTOProj> eligibleProjects) {
        System.out.println("\n=== Eligible BTO Projects ===");
        if (eligibleProjects.isEmpty()) {
            System.out.println("No eligible projects found.");
            return;
        }
        for (BTOProj project : eligibleProjects) {
            System.out.println("- " + project.getProjName() + " (" + project.getProjNbh() + ")");
        }
    }

    public void displayApplicationDetails(String applicationDetails) {
        System.out.println(applicationDetails);
    }

    public void applyForProject(BTOProj project) {
        System.out.println("Application submitted for project: " + project.getProjName());
    }
    
}
