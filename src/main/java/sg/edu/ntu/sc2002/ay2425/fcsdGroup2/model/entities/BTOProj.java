package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.Neighbourhoods;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;

public class BTOProj {
    private int btoProjId;
    private String projectName;
    private LocalDateTime appOpenDate;
    private LocalDateTime appCloseDate;
    private boolean isVisible;
    private ProjStatus status;
    private BTOExercise exercise;
    private Neighbourhoods neighbourhood;
    private HDBManager managerIC;
    private List<Block> blocks;
    private List<Application> applications;
    private List<FlatType> flatTypesAvail;
    private List<Enquiry> enquiries;
    private List<HDBOfficer> officersList;
<<<<<<< HEAD
    private Map<FlatTypes, Integer> flatUnits = new HashMap<>();
=======
    private int officerSlots;

    @Override
    public String toString() {
        return "BTOProj{" +
                "btoProjId=" + btoProjId +
                ", projectName='" + projectName + '\'' +
                ", appOpenDate=" + appOpenDate +
                ", appCloseDate=" + appCloseDate +
                ", isVisible=" + isVisible +
                ", status=" + status +
                ", neighbourhood=" + neighbourhood +
                ", managerIC=" + managerIC +
                ", blocks=" + blocks +
                ", applications=" + applications +
                ", flatTypesAvail=" + flatTypesAvail +
                ", enquiries=" + enquiries +
                ", officersList=" + officersList +
                ", officerSlots=" + officerSlots +
                '}';
    }

    // Constructor for loading from file
    public BTOProj(int id, String name, Neighbourhoods nbh, List<FlatType> flatTypesAvail, LocalDateTime open, LocalDateTime close, HDBManager manager, int officerSlots, List<HDBOfficer> officerList) {
        this.btoProjId = id;
        this.projectName = name;
        this.neighbourhood = nbh;
        this.flatTypesAvail = flatTypesAvail;
        this.appOpenDate = open;
        this.appCloseDate = close;
        this.managerIC = manager;
        this.officerSlots = officerSlots;
        this.officersList = officerList;

        this.isVisible = open.isBefore(LocalDateTime.now()) && close.isAfter(LocalDateTime.now()) ? true : false;
        this.status = open.isBefore(LocalDateTime.now()) && close.isAfter(LocalDateTime.now()) ? ProjStatus.OPEN : ProjStatus.CLOSED;


        // Need to fetch all these from file for persistent storage - placeholder for now, empty list
        applications = new ArrayList<>();
        blocks = new ArrayList<>();
        enquiries = new ArrayList<>();
    }
>>>>>>> fc1d97aedc9b9bf64232ce2d8e3540c87fc7a56c

    public BTOProj() {
        blocks = new ArrayList<>();
        applications = new ArrayList<>();
        flatTypesAvail = new ArrayList<>();
        enquiries = new ArrayList<>();
        officersList = new ArrayList<>();
    }

    public BTOProj(int id, String name, LocalDateTime open, LocalDateTime close, boolean visible) {
        this();
        this.btoProjId = id;
        this.projectName = name;
        this.appOpenDate = open;
        this.appCloseDate = close;
        this.isVisible = visible;
    }

    // ID, Name
    public int getProjId() {
        return btoProjId;
    }

    public void setProjId(int id) {
        this.btoProjId = id;
    }

    public String getProjName() {
        return projectName;
    }

    public void setProjName(String name) {
        this.projectName = name;
    }

    // Dates
    public LocalDateTime getAppOpenDate() {
        return appOpenDate;
    }

    public void setApplicationOpenDate(LocalDateTime datetime) {
        this.appOpenDate = datetime;
    }

    public LocalDateTime getAppCloseDate() {
        return appCloseDate;
    }

    public void setAppCloseDate(LocalDateTime datetime) {
        this.appCloseDate = datetime;
    }

    // Visibility
    public boolean getVisibility() {
        return isVisible;
    }

    public void setVisibility(boolean visible) {
        this.isVisible = visible;
    }

    public void toggleVisibility() {
        this.isVisible = !this.isVisible;
    }

    // Project Status
    public ProjStatus getProjStatus() {
        return status;
    }

    public void setProjStatus(ProjStatus status) {
        this.status = status;
    }

    // Neighbourhood
    public Neighbourhoods getProjNbh() {
        return neighbourhood;
    }

    public void setProjNbh(Neighbourhoods nbh) {
        this.neighbourhood = nbh;
    }

    // Manager
    public HDBManager getManagerIC() {
        return managerIC;
    }

    public void setManagerIC(HDBManager manager) {
        this.managerIC = manager;
    }

    // Blocks
    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public boolean addBlock(Block b) {
        return blocks.add(b);
    }

    public boolean removeBlock(int blkNo) {
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).getBlkNo() == blkNo) {
                blocks.remove(i);
                return true; 
            }
        }
        return false;
    }

    public void setApplications(List<Application> apps) {
        this.applications = apps;
    }

    public boolean addApp(Application app) {
        return applications.add(app);
    }

    public boolean removeApp(int id) {
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getAppId() == id) {
                applications.remove(i);
                return true; 
            }
        }
        return false;
    }

    public Application getAppById(int id) {
        for (Application a : applications) {
            if (a.getAppId() == id)
                return a;
        }
        return null;
    }

    /**
     * public model.entities.Application getAppByNric(String nric) {
     * 
     * }
     **/

     public List<Application> getAllApp() {
        return applications;
    }

     // model.entities.Flat Types
     public List<FlatType> getAvailableFlatTypes() {
        return flatTypesAvail;
    }

    public void setAvailableFlatTypes(List<FlatType> list) {
        this.flatTypesAvail = list;
    }

    public void addAvailFlatType(FlatType type) {
        flatTypesAvail.add(type);
    }

<<<<<<< HEAD
    public void addFlatType(FlatTypes type, int units) {
        flatUnits.put(type, units);
    }

    public Map<FlatTypes, Integer> getFlatUnits() {
        return flatUnits;
    }

=======
    /*
>>>>>>> fc1d97aedc9b9bf64232ce2d8e3540c87fc7a56c
    public boolean removeAvailFlatType(int typeId) {
        for (int i = 0; i < flatTypesAvail.size(); i++) {
            if (flatTypesAvail.get(i).getFlatTypeId() == typeId) {
                flatTypesAvail.remove(i);
                return true; // removed successfully
            }
        }
        return false;
    }
    */

    // Enquiries
    public List<Enquiry> getAllEnquiries() {
        return enquiries;
    }

    public void setEnquiries(List<Enquiry> enquiryList) {
        this.enquiries = enquiryList;
    }

    public void addEnquiryToProj(Enquiry e) {
        enquiries.add(e);
    }

    public void removeEnquiryFromProj(int id) {
        for (int i = 0; i < enquiries.size(); i++) {
            if (enquiries.get(i).getEnquiryId() == id) {
                enquiries.remove(i);
                break; 
            }
        }
    }

    public Enquiry getEnquiryInProjById(int id) {
        for (Enquiry e : enquiries) {
            if (e.getEnquiryId() == id) {
                return e;
            }
        }
        return null; 
    }

    // Officer List
    public List<HDBOfficer> getOfficersList() {
        return officersList;
    }

    public HDBOfficer setOfficersList(List<HDBOfficer> officersList) {
        this.officersList = officersList;
        return officersList.isEmpty() ? null : officersList.get(0);
    }

    public boolean assignOfficer(HDBOfficer officer) {
        if (!officersList.contains(officer)) {
            officersList.add(officer);
            return true;
        }
        return false;
    }

    public boolean unassignOfficer(int officerId) {
        for (HDBOfficer officer : officersList) {

            if (officer.getUserId() == officerId) {
                officersList.remove(officer);
                return true;
            }
        }
        return false;
    }

<<<<<<< HEAD
    public void setOfficerSlotLimit(int maxOfficerSlots) {
=======
    public int getBtoProjId() {
        return btoProjId;
    }

    public void setBtoProjId(int btoProjId) {
        this.btoProjId = btoProjId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setAppOpenDate(LocalDateTime appOpenDate) {
        this.appOpenDate = appOpenDate;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public ProjStatus getStatus() {
        return status;
    }

    public void setStatus(ProjStatus status) {
        this.status = status;
    }

    public Neighbourhoods getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(Neighbourhoods neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public List<FlatType> getFlatTypesAvail() {
        return flatTypesAvail;
    }

    public void setFlatTypesAvail(List<FlatType> flatTypesAvail) {
        this.flatTypesAvail = flatTypesAvail;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public int getOfficerSlots() {
        return officerSlots;
    }

    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }

    public BTOExercise getExercise() {
        return exercise;
    }

    public void setExercise(BTOExercise exercise) {
        this.exercise = exercise;
>>>>>>> fc1d97aedc9b9bf64232ce2d8e3540c87fc7a56c
    }
}