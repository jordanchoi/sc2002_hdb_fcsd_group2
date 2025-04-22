package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EnquiryRepository {
    // This class is responsible for managing the data access layer for enquiries
    // It will handle CRUD operations and any other data-related tasks for enquiries.

    private final String FILE_PATH = "data/Enquiries.xlsx";
    //private final String FILE_PATH = "./src/main/resources/data/Enquiries.xlsx";
    private final List<Enquiry> enquiries = new ArrayList<>();

    // Dependencies
    private final UserRepository userRepo;
    private final BTORepository btoRepo;

    // Singleton
    private static EnquiryRepository instance;

    // Constructor
    private EnquiryRepository() {
        this.userRepo = UserRepository.getInstance();
        this.btoRepo = BTORepository.getInstance();
        // Load existing enquiries from file or database
        loadFromFile();
    }

    // Singleton instance
    public static EnquiryRepository getInstance() {
        if (instance == null) {
            instance = new EnquiryRepository();
        }
        return instance;
    }

    // Example method to get all enquiries
    public List<Enquiry> getAllEnquiries() {
        // Implementation here
        return enquiries;
    }

    private void loadFromFile() {
        enquiries.clear();
        List<List<String>> rows = FileIO.readExcelFileLocal(FILE_PATH);
        if (!rows.isEmpty() && rows.get(0).get(0).equalsIgnoreCase("Enquiry ID")) {
            rows.remove(0); // remove header row
        }
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
                LocalDateTime timestamp;
                try {
                    timestamp = LocalDateTime.parse(row.get(7), DateTimeFormatter.ISO_DATE_TIME);
                } catch (DateTimeParseException e) {
                    double excelTimestamp = Double.parseDouble(row.get(7));
                    timestamp = LocalDateTime.of(1899, 12, 30, 0, 0).plusSeconds((long)(excelTimestamp * 86400));
                }


                BTOProj project = btoRepo.getProjByName(projectName);
                HDBApplicant applicant = (HDBApplicant) userRepo.getUserByNric(applicantNric).orElse(null);
                User sender = userRepo.getUserByNric(senderNric).orElse(null);

                if (project == null || applicant == null || sender == null) {
                    System.out.println("Error loading enquiry: Missing project or user data.");
                    continue;
                }
                Enquiry enquiry = enquiryMap.computeIfAbsent(enquiryId, id -> new Enquiry(enquiryId, applicant, project));
                ProjectMessage message = new ProjectMessage(content, sender, messageId, timestamp);
                // enquiry.getEnquiries().add(message);
                // Avoid duplicate initial message
                if (enquiry.getEnquiries().stream().noneMatch(m -> m.getMessageId() == messageId))
                    enquiry.getEnquiries().add(message);
            } catch (Exception e) {
                System.out.println("Error parsing row: " + row);
                e.printStackTrace();
            }
        }
        enquiries.addAll(enquiryMap.values());
        int maxId = enquiryMap.keySet().stream().max(Integer::compareTo).orElse(0);
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

    /*
    public void add(Enquiry enquiry) {
        // Implementation here
        enquiries.add(enquiry);
        saveToFile();
    }

     */

    public Enquiry add(String message, HDBApplicant applicant, BTOProj project) {
        int newId = getNextEnquiryId();
        Enquiry enquiry = new Enquiry(newId, message, applicant, project);
        enquiries.add(enquiry);
        saveToFile();
        return enquiry;
    }

    public Enquiry add(int id, String message, HDBApplicant applicant, BTOProj project) {
        Enquiry enquiry = new Enquiry(id, message, applicant, project);
        enquiries.add(enquiry);
        saveToFile();
        return enquiry;
    }

/*
    public void update(Enquiry updated) {
        int id = updated.getEnquiryId();
        delete(updated.getEnquiryId());
        add(id, updated.get, updated.getMadeBy(), updated.getForProj());
        saveToFile();
    }

 */

    public boolean delete(int id) {
        if (getById(id).isPresent()) {
            getById(id).ifPresent(enquiries::remove);
            saveToFile();
            return true;
        }
        return false;
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

    public boolean enquiryExists(HDBApplicant applicant, BTOProj project, String messageContent) {
        return enquiries.stream()
                .filter(e -> e.getMadeBy().equals(applicant) && e.getForProj().equals(project))
                .flatMap(e -> e.getEnquiries().stream())
                .anyMatch(m -> m.getContent().equalsIgnoreCase(messageContent));
    }

    private int getNextEnquiryId() {
        return enquiries.stream()
                .mapToInt(Enquiry::getEnquiryId)
                .max()
                .orElse(0) + 1;
    }
}
