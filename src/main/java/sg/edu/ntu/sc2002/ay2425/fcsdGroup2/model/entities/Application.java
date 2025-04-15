package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ApplicationStatus;


public class Application {
    private static int idCounter = 1;
    private final int applicationId;
    private final HDBApplicant applicant;
    private final BTOProj project;
    private ApplicationStatus status;
    private final FlatType flatType;

    // Constructor
    public Application(HDBApplicant applicant, BTOProj project, ApplicationStatus status, FlatType flatType) {
        this.applicationId = idCounter++;
        this.applicant = applicant;
        this.project = project;
        this.status = status;
        this.flatType = flatType;
    }

    // Getters
    public int getAppId() {
        return applicationId;
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