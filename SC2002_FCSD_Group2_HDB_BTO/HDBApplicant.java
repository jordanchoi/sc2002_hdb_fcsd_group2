import java.util.ArrayList;
import java.util.List;
import Enumeration.ApplicationStatus;
import Enumeration.MaritalStatus;

class HDBApplicant extends User {
    private Application currentApplication;

    public Application getCurrentApplication() {
        return currentApplication;
    }

    public void setCurrentApplication(Application currentApplication) {
        this.currentApplication = currentApplication;
    }

    public HDBApplicant(int userId, String nric, String password, String firstName, String lastName, String middleName,int age, MaritalStatus maritalStatus) {
        super(userId, nric, password, firstName, lastName, middleName, age, maritalStatus);
        this.currentApplication = null;
    }

   public void applyForProject(BTOProj project) {
        if (this.currentApplication != null) {
            if (this.currentApplication.getStatusEnum() == ApplicationStatus.Unsuccessful || this.currentApplication.getStatusEnum() == ApplicationStatus.Withdrawn) {
                this.currentApplication = new Application(this, project);
                System.out.println("Application submitted successfully for project: " + project.getProjName());
                return;
            }
            System.out.println("You already have an ongoing application.");
            return;
        }
    
        this.currentApplication = new Application(this, project);
        System.out.println("Application submitted successfully for project: " + project.getProjName());
    }
    

    public String viewApplicationDetails() {
        if (this.currentApplication == null) {
            return "No application found for this applicant.";
        }

        StringBuilder details = new StringBuilder();
        details.append("Application ID: ").append(currentApplication.getAppId()).append("\n");
        details.append("Project: ").append(currentApplication.getAppliedProj().getProjName()).append("\n");
        details.append("Application Status: ").append(currentApplication.getStatus()).append("\n");

        if (currentApplication.getFlat() != null) {
            details.append("Chosen Flat: ")
                   .append("Block ").append(currentApplication.getFlat().getBlock().getBlkNo()).append(", ")
                   .append(currentApplication.getFlat().getBlock().getStreetAddr()).append(", ")
                   .append("Postal Code: ").append(currentApplication.getFlat().getBlock().getPostalCode()).append(", ")
                   .append(currentApplication.getFlat().getFloorUnit()).append("\n");
        } else {
            details.append("Chosen Flat: N/A\n");
        }
        
        return details.toString();
    }

    public ApplicationStatus getApplicationStatus(){
        return currentApplication.getStatusEnum();
    }

    public List<Enquiry> getApplicantEnquiries(EnquiryController enquiryController) {
        return enquiryController.getEnquiriesByApplicant(this);
    }

    public List<BTOProj> getEligibleProjs() {
        List<BTOProj> allProjs = BTORepository.getAllProjs();
        List<BTOProj> eligibleProjects = new ArrayList<>();
    
        for (BTOProj project : allProjs) {
            if (checkEligibility(this, project)) {
                eligibleProjects.add(project);
            }
        }
        return eligibleProjects;
    }


    public void submitEnquiry(EnquiryController enquiryController, String message, BTOProj project) {
        enquiryController.createEnquiry(message, this, project);
    }

    public void submitExistingEnquiry(EnquiryController enquiryController, int enquiryId, String newMessage) {
        Enquiry enquiry = enquiryController.getEnquiryById(enquiryId);  // Get the existing enquiry
        if (enquiry != null) {
            enquiry.addMessage(newMessage, this); // Add the new message to the enquiry
            System.out.println("Message added successfully to the enquiry.");
        } else {
            System.out.println("Enquiry not found.");
        }
    }

    public boolean editEnquiryMessage(EnquiryController enquiryController, int enquiryId, int messageId, String newMessage) {
        Enquiry enquiry = enquiryController.getEnquiryById(enquiryId);
        if (enquiry != null) {
            return enquiry.editMessageById(messageId, this, newMessage);  // sender check happens inside
        }
        return false;
    }
    

    public void removeEnquiry(EnquiryController enquiryController, int enquiryID) {
        enquiryController.deleteEnquiry(enquiryID);
    }

    public void withdrawApplication() {
        if (this.currentApplication == null) {
            System.out.println("You do not have an ongoing application.");
            return;
        }

        if (this.currentApplication.getStatusEnum() == ApplicationStatus.Withdrawn) {
            System.out.println("This application has already been withdrawn.");
            return;
        }

        if (this.currentApplication.getStatusEnum() == ApplicationStatus.withdrawRequested) {
            System.out.println("This application has already been requested to withdraw.");
            return;
        }

        this.currentApplication.requestWithdrawal();
        System.out.println("Withdrawal request has been made for application ID: " + currentApplication.getAppId());
    }


    public boolean checkEligibility(User user, BTOProj project) {
        if (!project.getVisibility()) {
            return false;
        }

        if (user instanceof HDBOfficer) {
            HDBOfficer officer = (HDBOfficer) user;
            if (officer.getProj().getProjId() == project.getProjId()){
                return false;
            }
        }

        HDBApplicant applicant = (HDBApplicant) user;
        int age = applicant.getAge();
        MaritalStatus maritalStatus = applicant.getMaritalStatus();

        if (maritalStatus == MaritalStatus.Married && age >= 21) {
            return true;
        } else if (maritalStatus == MaritalStatus.Single && age >= 35) {
            if(project.getAvailableFlatTypes().contains("2-Room")){
                return true;
            }
        }
        return false;
    }
}
