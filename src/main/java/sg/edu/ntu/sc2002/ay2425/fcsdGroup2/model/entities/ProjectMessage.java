package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.time.LocalDateTime;

/**
 * Represents a single message exchanged in a project enquiry thread.
 */
public class ProjectMessage {
    private final int messageId;
    private String content;
    private final User sender;
    private final LocalDateTime timestamp;


    // Main constructor (message ID is passed in)
    public ProjectMessage(int messageId, String content, User sender) {
        this.messageId = messageId;
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }
    /**
     * Constructs a new project message.
     *
     * @param content the message content
     * @param sender the user who sent the message
     */

    /**
     * Constructs a project message with specific ID and timestamp (for loading from file).
     *
     * @param content the message content
     * @param sender the user who sent the message
     * @param messageId the ID of the message
     * @param timestamp the timestamp when the message was created
     */
    // Constructor for loading from file
    public ProjectMessage(String content, User sender, int messageId, LocalDateTime timestamp) {
        this.messageId = messageId;
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
    }


    /** @return the unique message ID. */
    public int getMessageId() { return messageId; }

    /** @return the content of the message. */
    public String getContent() { return content; }

    /** @return the sender of the message. */
    public User getSender() { return sender; }

    /** @return the timestamp of when the message was created. */
    public LocalDateTime getTimestamp() { return timestamp; }

    /** Updates the content of the message. */
    public void setContent(String content) { this.content = content; }

    /**
     * Determines the role of the sender (Applicant, Officer, Manager, or Unknown).
     *
     * @return sender's role as a String
     */
    public String getSenderRole() {
        if (sender instanceof HDBApplicant) return "Applicant";
        if (sender instanceof HDBManager) return "Manager";
        if (sender instanceof HDBOfficer) return "Officer";
        return "Unknown";
    }

    /**
     * Returns a formatted string representation of the message.
     *
     * @return formatted message string
     */
    @Override
    public String toString() {
        return "[#" + messageId + "] [" + getSenderRole() + " - " + sender.getFirstName() + "] " +
                timestamp + ": " + content;
    }
}
