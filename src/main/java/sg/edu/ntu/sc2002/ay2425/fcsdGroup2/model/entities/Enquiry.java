package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an enquiry made by an applicant regarding a BTO project.
 * Tracks a thread of messages exchanged between the applicant and officers/managers.
 */
public class Enquiry {
    private final int enquiryID;
    private final List<ProjectMessage> thread;
    private final HDBApplicant madeBy;
    private final BTOProj forProj;

    /**
     * Constructs an Enquiry with an initial message.
     *
     * @param enquiryID the unique ID of the enquiry
     * @param message the initial message content
     * @param madeBy the applicant who made the enquiry
     * @param forProj the BTO project the enquiry is related to
     */
    public Enquiry(int enquiryID, String message, HDBApplicant madeBy, BTOProj forProj) {
        this.enquiryID = enquiryID;
        this.thread = new ArrayList<>();;
        this.madeBy = madeBy;
        this.forProj = forProj;

        this.thread.add(new ProjectMessage(message, madeBy));
    }

    /**
     * Constructs an empty Enquiry (used for loading from file).
     *
     * @param enquiryID the unique ID of the enquiry
     * @param madeBy the applicant who made the enquiry
     * @param forProj the BTO project the enquiry is related to
     */
    // For loading from file.
    public Enquiry(int enquiryID, HDBApplicant madeBy, BTOProj forProj) {
        this.enquiryID = enquiryID;
        this.thread = new ArrayList<>();
        this.madeBy = madeBy;
        this.forProj = forProj;
    }

    /**
     * Returns the unique ID of the enquiry.
     *
     * @return enquiry ID
     */
    public int getEnquiryId() { return enquiryID; }

    /**
     * Returns the list of messages in the enquiry thread.
     *
     * @return list of ProjectMessage objects
     */
    public List<ProjectMessage> getEnquiries() { return thread; }

    /**
     * Returns the applicant who created the enquiry.
     *
     * @return applicant who made the enquiry
     */
    public HDBApplicant getMadeBy() { return madeBy; }

    /**
     * Returns the BTO project related to this enquiry.
     *
     * @return BTO project
     */
    public BTOProj getForProj() { return forProj; }

    /**
     * Adds a new message to the enquiry thread if it does not already exist.
     *
     * @param enquiry the message content
     * @param sender the user sending the message
     */
    public void addMessage(String enquiry, User sender) {
        boolean alreadyExists = thread.stream().anyMatch(
                m -> m.getContent().equals(enquiry) && m.getSender().equals(sender)
        );
        if (!alreadyExists) {
            thread.add(new ProjectMessage(enquiry, sender));
        }
        //thread.add(new ProjectMessage(enquiry, sender));
    }


    /**
     * Retrieves a specific message from the thread by its ID.
     *
     * @param messageId the ID of the message
     * @return the ProjectMessage if found, otherwise null
     */
    public ProjectMessage getMessageById(int messageId) {
        for (ProjectMessage message : thread) {
            if (message.getMessageId() == messageId) {
                return message;
            }
        }
        return null;
    }

    /**
     * Edits the content of a message by its ID, if the current user is the sender.
     *
     * @param messageId the ID of the message to edit
     * @param currentUser the user attempting to edit the message
     * @param newContent the new content for the message
     * @return true if the edit was successful, false otherwise
     */
    public boolean editMessageById(int messageId, User currentUser, String newContent) {
        for (ProjectMessage m : thread) {
            if (m.getMessageId() == messageId && m.getSender().equals(currentUser)) {
                m.setContent(newContent);  // does not recreate message
                return true;
            }
        }
        return false;
    }
}
