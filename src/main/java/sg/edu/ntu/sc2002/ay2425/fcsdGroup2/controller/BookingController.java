package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingController {

    public BookingController () {}
    UserRepository user = UserRepository.getInstance();
    List<HDBApplicant> applicantList = user.getApplicants();
    BTORepository repo = BTORepository.getInstance();

    public HDBApplicant retrieveApp(String NRIC) {
        HDBApplicant app = null;
        for (HDBApplicant applicant : applicantList) {
            if (applicant.getNric().equalsIgnoreCase(NRIC)) {
                app = applicant;
            }
        }
        return app;
    }

    public Application findApplication(String nric) {
        /*Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the flat to update (based on NRIC): ");
        String nric = scanner.nextLine();*/

        ApplicationRepository appRepo = new ApplicationRepository(repo, user);
        Application selectedApp = appRepo.getApplicationByNric(nric);
        if (selectedApp == null) {
            System.out.println("No application found for NRIC: " + nric);
        }
        return selectedApp;

        /*BTORepository btoRepo = BTORepository.getInstance();
        UserRepository userRepo = UserRepository.getInstance();
        ApplicationRepository appRepo = new ApplicationRepository(btoRepo, userRepo);
        List<Application> apps = appRepo.getApplications();

        if (apps.isEmpty()) {
            System.out.println("No applications found.");
            return null;
        }

        System.out.println("\n=== List of Applicant Applications ===");
        System.out.printf("%-20s %-15s %-25s %-10s %-15s%n",
                "Applicant Name", "NRIC", "Project", "Flat", "Status");
        System.out.println("---------------------------------------------------------------------------------------");

        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            String name = app.getApplicant().getFirstName();
            String nric = app.getApplicant().getNric();
            String projectName = app.getProject().getProjName();
            String flatType = app.getFlatType() != null ? app.getFlatType().getTypeName() : "NIL";
            String status = app.getStatus() != null ? app.getStatus() : "N/A";

            System.out.printf("%-20s %-15s %-25s %-10s %-15s%n",
                    name, nric, projectName, flatType, status);
        }

        System.out.println("---------------------------------------------------------------------------------------");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter NRIC of the applicant to proceed: ");
        String nricInput = scanner.nextLine().trim();

        Application selectedApp = appRepo.getApplicationByNric(nricInput);
        if (selectedApp == null) {
            System.out.println("No application found for NRIC: " + nricInput);
        }

        return selectedApp;
    */}

    public boolean updateFlatAvail(Application app) {
        //FlatType flatToUpdate = app.getFlatTypeObj();
        List<FlatType> project = app.getAppliedProj().getAvailableFlatTypes();
        FlatType flatToUpdate = null;
        for (FlatType f : project) {
            if (f.getTypeName().equalsIgnoreCase(app.getFlatType().getTypeName())) {
                flatToUpdate = f;
            }
        }

        int oldUnitsAvail = 0;
        try {
            oldUnitsAvail = flatToUpdate.getUnitsAvail();
        } catch (NullPointerException e) {
            System.out.println("FlatType not found in the project." + e.getMessage());
            return false;
        }

        if (oldUnitsAvail == 0) {
            return false;
        }
        else{
        flatToUpdate.setUnitsAvail(oldUnitsAvail--);
            return true;
        }
    }

    public boolean updateAppStatus(Application app) {
        app.approve();
        return true;
    }

    public boolean updateAppProfile(Application app, String newtype, BTORepository repo) {
        BTOProj proj = null;
        for (BTOProj p : repo.getAllProjects()) {
            if (p.getProjName().equalsIgnoreCase(app.getAppliedProj().getProjName())) {
                proj = p;
            }
        }
        for (FlatType f : proj.getAvailableFlatTypes()) {
            if (f.getTypeName().equalsIgnoreCase(newtype) && f.getTotalUnits() > 0){
                //app.setFlatType(newtype);
                return true;
            }
        }
        return false;
    }

    public void generateReceipt(Application app) {
        if (app == null) {
            System.out.println("Cannot generate receipt: Application is null.");
            return;
        }

        if (app.getApplicant() == null) {
            System.out.println("Missing applicant info. Receipt generation aborted.");
            return;
        }

        String fileName = "output/Receipt_" + app.getApplicant().getNric() + ".pdf";

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);

            float margin = 60;
            float y = 780;
            float spacing = 18;
            float labelX = margin + 20;
            float valueX = margin + 160;

            // Title
            content.setFont(PDType1Font.HELVETICA_BOLD, 18);
            content.beginText();
            content.newLineAtOffset(margin, y);
            content.showText("HDB Flat Booking Confirmation Receipt");
            content.endText();
            y -= spacing;

            // Timestamp
            content.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
            content.beginText();
            content.newLineAtOffset(margin, y);
            content.showText("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")));
            content.endText();
            y -= spacing + 8;

            content.moveTo(margin, y);
            content.lineTo(page.getMediaBox().getWidth() - margin, y);
            content.stroke();
            y -= spacing;

            // Applicant Details
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.beginText();
            content.newLineAtOffset(margin, y);
            content.showText("Applicant Details");
            content.endText();
            y -= spacing;

            content.setFont(PDType1Font.HELVETICA, 11);
            String[][] applicantInfo = {
                    {"Full Name", app.getApplicant().getFirstName() != null ? app.getApplicant().getFirstName() : "-"},
                    {"NRIC", app.getApplicant().getNric() != null ? app.getApplicant().getNric() : "-"},
                    {"Age", String.valueOf(app.getApplicant().getAge())},
                    {"Marital Status", app.getApplicant().getMaritalStatus() != null ? app.getApplicant().getMaritalStatus().name() : "-"}
            };
            for (String[] row : applicantInfo) {
                content.beginText();
                content.newLineAtOffset(labelX, y);
                content.showText(row[0]);
                content.endText();

                content.beginText();
                content.newLineAtOffset(valueX, y);
                content.showText(": " + row[1]);
                content.endText();

                y -= spacing;
            }

            y -= 8;
            content.moveTo(margin, y);
            content.lineTo(page.getMediaBox().getWidth() - margin, y);
            content.stroke();
            y -= spacing;

            // Booking Details
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.beginText();
            content.newLineAtOffset(margin, y);
            content.showText("Booking Details");
            content.endText();
            y -= spacing;

            content.setFont(PDType1Font.HELVETICA, 11);
            String flatStr = "-";
            if (app.getFlat() != null && app.getFlat().getBlock() != null) {
                flatStr = "Blk " + app.getFlat().getBlock().getBlkNo() +
                        " #" + app.getFlat().getFloorNo() + "-" + app.getFlat().getUnitNo();
            }

            String[][] bookingInfo = {
                    {"Project", app.getProject() != null ? app.getProject().getProjName() : "-"},
                    {"Application Status", app.getStatus() != null ? app.getStatus() : "-"},
                    {"Flat Type", app.getFlatType() != null ? app.getFlatType().getTypeName() : "-"},
                    {"Flat Booked", flatStr}
            };

            for (String[] row : bookingInfo) {
                content.beginText();
                content.newLineAtOffset(labelX, y);
                content.showText(row[0]);
                content.endText();

                content.beginText();
                content.newLineAtOffset(valueX, y);
                content.showText(": " + row[1]);
                content.endText();

                y -= spacing;
            }

            // Footer
            y -= spacing;
            content.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
            content.beginText();
            content.newLineAtOffset(margin, y);
            content.showText("This is a system-generated receipt. No signature is required.");
            content.endText();

            content.close();

            File outputDir = new File("output");
            if (!outputDir.exists()) outputDir.mkdir();
            doc.save(fileName);
            System.out.println("Receipt saved to: " + fileName);
        } catch (Exception e) {
            System.out.println("Error generating receipt: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

