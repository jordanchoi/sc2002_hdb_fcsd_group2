package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ApplicationStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatBookingStatus;

/**
 * Represents an application made by an HDB applicant for a BTO project.
 * Tracks the application status, flat type selection, and booking information.
 */
public class Application {
    private static int idCounter = 1;
    private int applicationID;
    private HDBApplicant applicant;
    private BTOProj project;
    private ApplicationStatus status;
    private FlatType flatType;
    private Flat flatBooked;
    private ApplicationStatus previousStatus;
    private FlatBookingStatus flatStatus;

    /**
     * Constructs a new Application instance.
     *
     * @param id the unique application ID
     * @param applicant the applicant making the application
     * @param project the project the applicant is applying for
     */
    public Application(int id, HDBApplicant applicant, BTOProj project) {
        this.applicationID = id;
        this.applicant = applicant;
        this.project = project;
        this.status = ApplicationStatus.PENDING;
        this.flatType = null;
    }

    /**
     * Approves the application and updates status to SUCCESSFUL.
     */
    public void approve() {
        this.status = ApplicationStatus.SUCCESSFUL;
    }

    /**
     * Rejects the application and updates status to UNSUCCESSFUL.
     */
    public void reject() {
        this.status = ApplicationStatus.UNSUCCESSFUL;
    }

    /**
     * Marks the application as BOOKED after successful flat selection.
     */
    public void booked() {
        this.status = ApplicationStatus.BOOKED;
    }

    /**
     * Returns the application ID.
     *
     * @return application ID
     */
    public int getAppId() {
        return applicationID;
    }

    /**
     * Returns the applicant associated with this application.
     *
     * @return applicant
     */
    public HDBApplicant getApplicant() {
        return applicant;
    }

    /**
     * Returns the current status of the application.
     *
     * @return application status enum
     */
    public ApplicationStatus getStatusEnum() {
        return this.status;
    }

    /**
     * Returns the BTO project this application is tied to.
     *
     * @return BTO project
     */
    public BTOProj getAppliedProj() {
        return this.project;
    }

    /**
     * Sets the selected flat type for the application.
     *
     * @param flatType the flat type chosen
     */
    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    /**
     * Returns the selected flat type for this application.
     *
     * @return flat type
     */
    public FlatType getFlatType() {
        return this.flatType;
    }

    /**
     * Sets the booked flat for this application.
     *
     * @param flat the booked flat
     */
    public void setFlat(Flat flat) {
        this.flatBooked = flat;
    }

    /**
     * Returns the flat booked by the applicant.
     *
     * @return booked flat
     */
    public Flat getFlat() {
        return this.flatBooked;
    }

    /**
     * Requests withdrawal from the project.
     * Changes status to WITHDRAW_REQ while remembering previous status.
     */
    public void requestWithdrawal() {
        this.previousStatus = this.status;
        this.status = ApplicationStatus.WITHDRAW_REQ;
    }

    /**
     * Approves the withdrawal request.
     * Changes status to WITHDRAWN.
     */
    public void approveWithdrawal() {
        this.status = ApplicationStatus.WITHDRAWN;
    }

    /**
     * Rejects the withdrawal request and restores previous status.
     */
    public void rejectWithdrawal() {
        this.status = this.previousStatus;
    }

    /**
     * Returns the current status name as a string.
     *
     * @return current status name
     */
    public String getStatus() {
        return status.name();
    }

    /**
     * Returns the project associated with this application.
     *
     * @return BTO project
     */
    public BTOProj getProject() {
        return project;
    }

    /**
     * Returns the previous status before a withdrawal request was made.
     *
     * @return previous application status
     */
    public ApplicationStatus getPreviousStatus() {
        return previousStatus;
    }

    /**
     * Returns the flat booking status of the application.
     *
     * @return flat booking status
     */
    public FlatBookingStatus getFlatBookingStatus() {
        return flatStatus;
    }

    /**
     * Sets the flat booking status of the application.
     *
     * @param flatStatus new flat booking status
     */
    public void setFlatBookingStatus(FlatBookingStatus flatStatus) {
        this.flatStatus = flatStatus;
    }
}
