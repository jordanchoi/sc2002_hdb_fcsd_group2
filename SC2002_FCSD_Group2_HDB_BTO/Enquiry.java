import java.util.ArrayList;
import java.util.List;

public class Enquiry {
    private static int idCounter = 1;
    private int enquiryID;
    private List<Message> thread;
    private HDBApplicant madeBy;
    private BTOProj forProj;

    public Enquiry(String enquiry, HDBApplicant madeBy, BTOProj forProj) {
        this.enquiryID = idCounter++;
        this.thread = new ArrayList<>();
        this.madeBy = madeBy;
        this.forProj = forProj;
        
        this.thread.add(new Message(enquiry, madeBy));
    }

    public int getEnquiryId() { return enquiryID; }
    public List<Message> getEnquiries() { return thread; }
    public HDBApplicant getMadeBy() { return madeBy; }
    public BTOProj getForProj() { return forProj; }

    public void addMessage(String enquiry, User sender) { 
        thread.add(new Message(enquiry, sender));
    }

    public Message getMessageById(int messageId) {
        for (Message message : thread) {
            if (message.getMessageId() == messageId) {
                return message;
            }
        }
        return null;
    }
    

    public boolean editMessageById(int messageId, User currentUser, String newContent) {
        for (Message m : thread) {
            if (m.getMessageId() == messageId && m.getSender().equals(currentUser)) {
                m.setContent(newContent);
                return true;
            }
        }
        return false;
    } 
}
