package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EnquiryRepository {
    // This class is responsible for managing the data access layer for enquiries
    // It will handle CRUD operations and any other data-related tasks for enquiries.

    private final String FILE_PATH = "data/Enquiries.xlsx";
    private final List<Enquiry> enquiries = new ArrayList<>();

    // Dependencies
    private final UserRepository userRepo;
    private final BTORepository btoRepo;

    // Constructor
    public EnquiryRepository(UserRepository userRepo, BTORepository btoRepo) {
        this.userRepo = userRepo;
        this.btoRepo = btoRepo;
        // Load existing enquiries from file or database
        loadFromFile();
    }

    // Example method to get all enquiries
    public List<Enquiry> getAllEnquiries() {
        // Implementation here
        return enquiries;
    }

    private void loadFromFile() {
        enquiries.clear();
        List<List<String>> rows = FileIO.readExcelFile(FILE_PATH);
        Map<Integer, Enquiry> enquiryMap = new HashMap<>();

        for (List<String> row : rows) {
            if (row.size() < 8) continue;

            try {
                int enquiryId = (int) Double.parseDouble(row.get(0));
                String projectName = row.get(1);
                String applicantNric = row.get(2);
                int messageId = (int) Double.parseDouble(row.get(3));
                String content = row.get(4);
                String senderNric = row.get(5);
                String senderRole = row.get(6);
                LocalDateTime timestamp = LocalDateTime.parse(row.get(7), DateTimeFormatter.ISO_DATE_TIME);

                BTOProj project = btoRepo.getProjByName(projectName);
                HDBApplicant applicant = (HDBApplicant) userRepo.getUserByNric(applicantNric).orElse(null);
                User sender = userRepo.getUserByNric(senderNric).orElse(null);

                if (project == null || applicant == null || sender == null) {
                    System.out.println("Error loading enquiry: Missing project or user data.");
                    continue;
                }
                Enquiry enquiry = enquiryMap.computeIfAbsent(enquiryId, id -> new Enquiry(content, applicant, project));
                ProjectMessage message = new ProjectMessage(content, sender, messageId, timestamp);

                // Avoid duplicate initial message
                if (enquiry.getEnquiries().stream().noneMatch(m -> m.getMessageId() == messageId))
                    enquiry.getEnquiries().add(message);
            } catch (Exception e) {
                System.out.println("Error parsing row: " + row);
                e.printStackTrace();
            }
        }
        enquiries.addAll(enquiryMap.values());
    }

    private void saveToFile() {
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("Enquiry ID", "Project Name", "Applicant NRIC", "Message ID", "Content", "Sender NRIC", "Sender Role", "Timestamp"));

        for (Enquiry enquiry : enquiries) {
            for (ProjectMessage message : enquiry.getEnquiries()) {
                data.add(List.of(
                        String.valueOf(enquiry.getEnquiryId()),
                        enquiry.getForProj().getProjName(),
                        enquiry.getMadeBy().getNric(),
                        String.valueOf(message.getMessageId()),
                        message.getContent(),
                        message.getSender().getNric(),
                        message.getSenderRole(),
                        message.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME)
                ));
            }
        }
        FileIO.writeExcelFile(FILE_PATH, data);
    }

    // Example method to save an enquiry
    public void add(Enquiry enquiry) {
        // Implementation here
        enquiries.add(enquiry);
        saveToFile();
    }

    public void update(Enquiry updated) {
        delete(updated.getEnquiryId());
        add(updated);
        saveToFile();
    }

    public void delete(int id) {
        getById(id).ifPresent(enquiries::remove);
        saveToFile();
    }

    // Return ALL enquiries
    public List<Enquiry> getAll() {
        return new ArrayList<>(enquiries);
    }

    // Get specific enquiry by ID
    public Optional<Enquiry> getById(int id) {
        return enquiries.stream().filter(e -> e.getEnquiryId() == id).findFirst();
    }

    // Get list of enquiries by applicant
    public List<Enquiry> getByApplicant(HDBApplicant applicant) {
        return enquiries.stream().filter(e -> e.getMadeBy().equals(applicant)).toList();
    }

    public List<Enquiry> getByProject(BTOProj project) {
        return enquiries.stream().filter(e -> e.getForProj().equals(project)).toList();
    }
}
