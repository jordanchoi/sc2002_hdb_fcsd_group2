package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.*;
import java.util.List;

public class BookingController {

    public BookingController () {}
    UserRepository user = new UserRepository();
    List<HDBApplicant> applicantList = user.getApplicants();

    public HDBApplicant retrieveApp(String NRIC) {
        HDBApplicant app = null;
        for (HDBApplicant applicant : applicantList) {
            if (applicant.getNric().equalsIgnoreCase(NRIC)) {
                app = applicant;
            }
        }
        return app;
    }

    public boolean updateFlatAvail(Application app) {
        //FlatType flatToUpdate = app.getFlatTypeObj();
        List<FlatType> project = app.getAppliedProj().getAvailableFlatTypes();
        FlatType flatToUpdate = null;
        for (FlatType f : project) {
            if (f.getTypeName().equalsIgnoreCase(app.getFlatType().name())) {
                flatToUpdate = f;
            }
        }
        int oldUnitsAvail = flatToUpdate.getUnitsAvail();
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
        HDBApplicant user = app.getApplicant();

        System.out.println("Applicant's Name: " + user.getFullName());
        System.out.println("Applicant's NRIC: " + user.getNric());
        System.out.println("Applicant's Age: " + user.getAge());
        System.out.println("Applicant's Marital Status: " + user.getMaritalStatus());
        System.out.println("Applicant's Flat Type: " + app.getFlat());
        System.out.println("Project Details: " + user.viewApplicationDetails());
    }

}
