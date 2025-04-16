import Enumeration.ApplicationStatus;

public class Application {
    private static int idCounter = 1;
    private int applicationID;
    private HDBApplicant applicant;
    private BTOProj project;
    private ApplicationStatus status;
    private FlatType flatType;
    private Flat flatBooked;
    private ApplicationStatus previousStatus;

    Application(HDBApplicant applicant, BTOProj project){
        this.applicationID = idCounter++;
        this.applicant = applicant;
        this.project = project;
        this.status = ApplicationStatus.Pending;
        this.flatType = null;
    }


    public void approve(){
        this.status = ApplicationStatus.Successful;
    }
    public void reject(){
        this.status = ApplicationStatus.Unsuccessful;
    }
    public void booked(){
        this.status = ApplicationStatus.Booked;
    }

    public int getAppId() {
        return applicationID;
    }

    public HDBApplicant getApplicant() {
        return applicant;
    }

    public ApplicationStatus getStatusEnum() {
        return this.status;
    }

    public BTOProj getAppliedProj(){
        return this.project;
    }

    public FlatType getFlatType() {
        return this.flatType;
    }

    public Flat getFlat() {
        return this.flatBooked;
    }

    public void requestWithdrawal() {
        this.previousStatus = this.status;
        this.status = ApplicationStatus.withdrawRequested;
    }

    public void approveWithdrawal() {
        this.status = ApplicationStatus.Withdrawn;
    }

    public void rejectWithdrawal() {
        this.status = this.previousStatus;
    }

    public String getStatus() {
        return status.name();
    }
}
