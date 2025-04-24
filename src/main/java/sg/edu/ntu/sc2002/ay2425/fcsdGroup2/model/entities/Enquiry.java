package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Enquiry {
    private final int enquiryID;
    private final List<ProjectMessage> thread;
    private final HDBApplicant madeBy;
    private final BTOProj forProj;

    public Enquiry(int enquiryID, String message, HDBApplicant madeBy, BTOProj forProj) {
        this.enquiryID = enquiryID;
        this.thread = new ArrayList<>();;
        this.madeBy = madeBy;
        this.forProj = forProj;

        this.thread.add(new ProjectMessage(message, madeBy));
    }

    // For loading from file.
    public Enquiry(int enquiryID, HDBApplicant madeBy, BTOProj forProj) {
        this.enquiryID = enquiryID;
        this.thread = new ArrayList<>();
        this.madeBy = madeBy;
        this.forProj = forProj;
    }

    /* Problematic constructor
    public Enquiry(String enquiry, HDBApplicant madeBy, BTOProj forProj) {
        this.enquiryID = idCounter++;
        this.thread = new ArrayList<>();
        this.madeBy = madeBy;
        this.forProj = forProj;

        this.thread.add(new ProjectMessage(enquiry, madeBy));
    }

     */

    public int getEnquiryId() { return enquiryID; }
    public List<ProjectMessage> getEnquiries() { return thread; }
    public HDBApplicant getMadeBy() { return madeBy; }
    public BTOProj getForProj() { return forProj; }

    public void addMessage(String enquiry, User sender) {
        boolean alreadyExists = thread.stream().anyMatch(
                m -> m.getContent().equals(enquiry) && m.getSender().equals(sender)
        );
        if (!alreadyExists) {
            thread.add(new ProjectMessage(enquiry, sender));
        }
        //thread.add(new ProjectMessage(enquiry, sender));
    }

    public ProjectMessage getMessageById(int messageId) {
        for (ProjectMessage message : thread) {
            if (message.getMessageId() == messageId) {
                return message;
            }
        }
        return null;
    }

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
