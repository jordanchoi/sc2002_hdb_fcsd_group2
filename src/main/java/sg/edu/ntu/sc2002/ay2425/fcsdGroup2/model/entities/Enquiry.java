package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Enquiry {
    private final int enquiryID;
    private final List<ProjectMessage> thread;
    private final HDBApplicant madeBy;
    private final BTOProj forProj;
    private int nextMessageId = 1;

    // Constructor when user sends a new enquiry with a message
    public Enquiry(int enquiryID, String message, HDBApplicant madeBy, BTOProj forProj) {
        this.enquiryID = enquiryID;
        this.thread = new ArrayList<>();
        this.madeBy = madeBy;
        this.forProj = forProj;

        this.thread.add(new ProjectMessage(nextMessageId++, message, madeBy));
    }

    // Constructor for loading from file (initialise with empty thread)
    public Enquiry(int enquiryID, HDBApplicant madeBy, BTOProj forProj) {
        this.enquiryID = enquiryID;
        this.thread = new ArrayList<>();
        this.madeBy = madeBy;
        this.forProj = forProj;
    }

    public int getEnquiryId() { return enquiryID; }
    public List<ProjectMessage> getEnquiries() { return thread; }
    public HDBApplicant getMadeBy() { return madeBy; }
    public BTOProj getForProj() { return forProj; }

    // Add message with auto-assigned ID
    public void addMessage(String enquiry, User sender) {
        boolean alreadyExists = thread.stream().anyMatch(
                m -> m.getContent().equals(enquiry) && m.getSender().equals(sender)
        );
        if (!alreadyExists) {
            thread.add(new ProjectMessage(nextMessageId++, enquiry, sender));
        }
    }

    // Add pre-created message (e.g., when loading from file)
    public void addMessage(ProjectMessage message) {
        thread.add(message);
        if (message.getMessageId() >= nextMessageId) {
            nextMessageId = message.getMessageId() + 1;
        }
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
                m.setContent(newContent);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMessageById(int messageId, User sender) {
        Iterator<ProjectMessage> iterator = thread.iterator();
        while (iterator.hasNext()) {
            ProjectMessage message = iterator.next();
            if (message.getMessageId() == messageId && message.getSender().equals(sender)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
