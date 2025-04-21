package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ApplicationStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;

public class Application {
    private static int idCounter = 1;
    private final int applicationId;
    private final HDBApplicant applicant;
    private final BTOProj project;
    private ApplicationStatus status;
    private final FlatTypes flatType;

    // Constructor
    public Application(HDBApplicant applicant, BTOProj project, ApplicationStatus status, FlatTypes flatType) {
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

    public FlatTypes getFlatType() {
        return flatType;
    }

    public ApplicationStatus getStatusEnum() {
        return status;
    }

    // Methods
    public void approve() {
        this.status = ApplicationStatus.SUCCESSFUL;
    }
    
    public void reject() {
        this.status = ApplicationStatus.UNSUCCESSFUL;
    }
    
    public void booked() {
        this.status = ApplicationStatus.BOOKED;
    }

    public void approveWithdrawal() {
        this.status = ApplicationStatus.WITHDRAWN;
    }

    public void requestWithdrawal() {
        this.status = ApplicationStatus.WITHDRAW_REQUESTED;
    }

    public String getStatus() {
        return status.name();
    }
}