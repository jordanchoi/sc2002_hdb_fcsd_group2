class Enquiry {
    private Applicant applicant;
    private String message;

    public Enquiry(Applicant applicant, String message) {
        this.applicant = applicant;
        this.message = message;
    }

    public void setMessage(String newMessage) {
        this.message = newMessage;
    }
}
