package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

/**
 * Handles the generation of applicant booking reports.
 * Allows filtering by marital status, flat type, and age group.
 * Generates a PDF file summarizing the filtered applicant booking data.
 */
public class ReportView {

    /**
     * Constructs a ReportView instance.
     */
    public ReportView() {}

    /**
     * Generates the applicant report as a PDF file.
     * Allows the user to apply filters such as marital status, flat type, and age group.
     * Fetches the necessary data from repositories, processes it, and outputs a formatted report.
     */
    public void generateReport() {
        BTORepository btoRepo = BTORepository.getInstance();
        UserRepository userRepo = UserRepository.getInstance();
        ApplicationRepository appRepo = new ApplicationRepository(btoRepo, userRepo);
        List<Application> apps = appRepo.getApplications();
        List<HDBApplicant> applicants = userRepo.getApplicants();

        Scanner scanner = new Scanner(System.in);

        // Input filters
        System.out.println("Select Marital Status to filter (press Enter to skip):");
        MaritalStatus[] statuses = MaritalStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i].name());
        }
        System.out.print("Choice: ");
        String maritalInput = scanner.nextLine().trim();
        String maritalFilter = "";
        if (!maritalInput.isEmpty()) {
            try {
                int maritalChoice = Integer.parseInt(maritalInput);
                if (maritalChoice > 0 && maritalChoice <= statuses.length) {
                    maritalFilter = statuses[maritalChoice - 1].name();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Skipping marital status filter.");
            }
        }

        System.out.println("\nSelect Flat Type to filter (press Enter to skip):");
        FlatTypes[] types = FlatTypes.values();
        FlatTypes[] displayTypes = java.util.Arrays.stream(types)
                .filter(t -> !t.equals(FlatTypes.NIL))
                .toArray(FlatTypes[]::new);
        for (int i = 0; i < displayTypes.length; i++) {
            System.out.println((i + 1) + ". " + displayTypes[i].getDisplayName());
        }
        System.out.print("Choice: ");
        String flatInput = scanner.nextLine().trim();
        String flatTypeFilter = "";
        if (!flatInput.isEmpty()) {
            try {
                int flatChoice = Integer.parseInt(flatInput);
                if (flatChoice > 0 && flatChoice <= displayTypes.length) {
                    flatTypeFilter = displayTypes[flatChoice - 1].getDisplayName();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Skipping flat type filter.");
            }
        }

        System.out.print("\nFilter by Age Group (e.g., 20-30) or press Enter to skip: ");
        String ageGroupInput = scanner.nextLine().trim();
        int ageMin = -1, ageMax = -1;
        if (!ageGroupInput.isEmpty() && ageGroupInput.contains("-")) {
            try {
                String[] parts = ageGroupInput.split("-");
                ageMin = Integer.parseInt(parts[0].trim());
                ageMax = Integer.parseInt(parts[1].trim());
            } catch (Exception e) {
                System.out.println("Invalid age group format. Skipping age filter.");
            }
        }

        String outputPath = "output/ApplicantReport.pdf";
        SessionStateManager session = SessionStateManager.getInstance();
        String generatedBy = session.isLoggedIn() ? session.getLoggedInUser().getFirstName() : "Unknown";

        // PDF generation
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);

            float yStart = 550;
            float rowHeight = 20;
            float y = yStart;

            // Document title
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText("Applicant Booking Report");
            content.endText();
            y -= rowHeight;

            // Date generated
            content.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            content.endText();
            y -= rowHeight;

            content.moveTo(50, y);
            content.lineTo(page.getMediaBox().getWidth() - 50, y);
            content.stroke();
            y -= rowHeight;

            // Filters applied
            StringBuilder filterSummary = new StringBuilder();
            if (!maritalFilter.isEmpty()) filterSummary.append("Marital: ").append(maritalFilter).append("  ");
            if (!flatTypeFilter.isEmpty()) filterSummary.append("Flat Type: ").append(flatTypeFilter).append("  ");
            if (ageMin != -1 && ageMax != -1) filterSummary.append("Age: ").append(ageMin).append("-").append(ageMax);

            content.beginText();
            content.newLineAtOffset(50, y);
            content.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
            content.showText("Filters - " + (filterSummary.length() > 0 ? filterSummary.toString().trim() : "None applied"));
            content.endText();
            y -= rowHeight * 2;

            // Table headers
            float[] colX = {50, 120, 200, 250, 320, 420, 520, 620};
            String[] headers = {"NRIC", "Name", "Age", "Marital", "Project", "Status", "Flat Type", "Flat Booked"};
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            for (int i = 0; i < headers.length; i++) {
                content.beginText();
                content.newLineAtOffset(colX[i], y);
                content.showText(headers[i]);
                content.endText();
            }
            y -= rowHeight;

            // Populate rows with filtered data
            content.setFont(PDType1Font.HELVETICA, 10);
            for (Application app : apps) {
                HDBApplicant matchedApplicant = applicants.stream()
                        .filter(a -> a.getNric().equals(app.getApplicant().getNric()))
                        .findFirst().orElse(null);

                if (matchedApplicant == null) continue;
                if (!maritalFilter.isEmpty() && !matchedApplicant.getMaritalStatus().name().equalsIgnoreCase(maritalFilter)) continue;
                if (!flatTypeFilter.isEmpty() && (app.getFlatType() == null ||
                        app.getFlatType().getTypeName().equalsIgnoreCase("NIL") ||
                        !app.getFlatType().getTypeName().equalsIgnoreCase(flatTypeFilter))) continue;
                int age = matchedApplicant.getAge();
                if (ageMin != -1 && ageMax != -1 && (age < ageMin || age > ageMax)) continue;

                Flat flat = app.getFlat();
                String flatStr = (flat != null)
                        ? "Blk " + flat.getBlock().getBlkNo() + " #" + flat.getFloorNo() + "-" + flat.getUnitNo()
                        : "-";

                String flatTypeName = (app.getFlatType() != null && !app.getFlatType().getTypeName().equalsIgnoreCase("NIL"))
                        ? app.getFlatType().getTypeName()
                        : "-";
                String projectName = (app.getProject() != null) ? app.getProject().getProjName() : "-";
                String status = (app.getStatus() != null) ? app.getStatus() : "-";

                String[] values = {
                        matchedApplicant.getNric(),
                        matchedApplicant.getFirstName(),
                        String.valueOf(matchedApplicant.getAge()),
                        matchedApplicant.getMaritalStatus().name(),
                        projectName,
                        status,
                        flatTypeName,
                        flatStr
                };

                for (int i = 0; i < values.length; i++) {
                    content.beginText();
                    content.newLineAtOffset(colX[i], y);
                    content.showText(values[i]);
                    content.endText();
                }
                y -= rowHeight;
            }

            // Footer
            y -= rowHeight;
            content.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText("This is a system-generated report. No signature is required.");
            content.endText();

            content.close();

            File dir = new File("output");
            if (!dir.exists()) dir.mkdir();
            doc.save(outputPath);
            System.out.println("\nFiltered report saved to: " + outputPath + "\n");
        } catch (Exception e) {
            System.out.println("\nError writing PDF: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
}
