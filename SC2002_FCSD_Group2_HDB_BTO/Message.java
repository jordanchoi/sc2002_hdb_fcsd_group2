import java.time.LocalDateTime;

public class Message {
    private static int messageCounter = 1;
    private int messageId;
    private String content;
    private User sender;
    private LocalDateTime timestamp;

    public Message(String content, User sender) {
        this.messageId = messageCounter++;
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }

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
