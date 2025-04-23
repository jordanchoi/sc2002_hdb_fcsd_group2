package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ApplicationStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;

public class Application {
    private static int idCounter = 1;
    private int applicationID;
    private HDBApplicant applicant;
    private BTOProj project;
    private ApplicationStatus status;
    private FlatType flatType;
    private Flat flatBooked;
    private ApplicationStatus previousStatus;


    public Application(int id, HDBApplicant applicant, BTOProj project){
        this.applicationID = id;
        this.applicant = applicant;
        this.project = project;
        this.status = ApplicationStatus.PENDING;
        this.flatType = null;
    }

    public void approve(){
        this.status = ApplicationStatus.SUCCESSFUL;
    }
    public void reject(){
        this.status = ApplicationStatus.UNSUCCESSFUL;
    }
    public void booked(){
        this.status = ApplicationStatus.BOOKED;
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
        this.status = ApplicationStatus.WITHDRAW_REQ;
    }

    public void approveWithdrawal() {
        this.status = ApplicationStatus.WITHDRAWN;
    }

    public void rejectWithdrawal() {
        this.status = this.previousStatus;
    }

    public String getStatus() {
        return status.name();
    }

    public BTOProj getProject() {
        return project;
    }

    public ApplicationStatus getPreviousStatus() {
        return previousStatus;
    }
}
