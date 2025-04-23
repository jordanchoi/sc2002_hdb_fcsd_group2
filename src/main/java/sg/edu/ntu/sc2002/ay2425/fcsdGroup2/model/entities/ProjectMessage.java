package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.time.LocalDateTime;

public class ProjectMessage {
    private static int messageCounter = 1;
    private final int messageId;
    private String content;
    private final User sender;
    private final LocalDateTime timestamp;

    public ProjectMessage(String content, User sender) {
        this.messageId = messageCounter++;
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for loading from file
    public ProjectMessage(String content, User sender, int messageId, LocalDateTime timestamp) {
        this.messageId = messageId;
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
    };



    public int getMessageId() { return messageId; }
    public String getContent() { return content; }
    public User getSender() { return sender; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setContent(String content) { this.content = content; }

    public String getSenderRole() {
        if (sender instanceof HDBApplicant) return "Applicant";
        if (sender instanceof HDBManager) return "Manager";
        if (sender instanceof HDBOfficer) return "Officer";
        return "Unknown";
    }

    @Override
    public String toString() {
        return "[#" + messageId + "] [" + getSenderRole() + " - " + sender.getFirstName() + "] " +
               timestamp + ": " + content;
    }
}
