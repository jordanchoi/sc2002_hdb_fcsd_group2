import Enumeration.ApplicationStatus;

public class Application {
    private static int idCounter = 1;

    private int applicationID;
    private HDBApplicant applicant;
    private BTOProj project;
    private ApplicationStatus status;
    private FlatType flatType;

    // Constructor
    public Application(HDBApplicant applicant, BTOProj project, ApplicationStatus status, FlatType flatType) {
        this.applicationID = idCounter++;
        this.applicant = applicant;
        this.project = project;
        this.status = status;
        this.flatType = flatType;
    }

    // Getters
    public int getAppId() {
        return applicationID;
    }

    public HDBApplicant getApplicant() {
        return applicant;
    }

    public BTOProj getProject() {
        return project;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public ApplicationStatus getStatusEnum() {
        return status;
    }

    // Methods
    public void approve() {
        this.status = ApplicationStatus.Successful;
    }
    
    public void reject() {
        this.status = ApplicationStatus.Unsuccessful;
    }
    
    public void booked() {
        this.status = ApplicationStatus.Booked;
    }

    public void approveWithdrawal() {
        this.status = ApplicationStatus.Withdrawn;
    }

    public void requestWithdrawal() {
        this.status = ApplicationStatus.withdrawRequested;
    }

    public String getStatus() {
        return status.name();
    }
}