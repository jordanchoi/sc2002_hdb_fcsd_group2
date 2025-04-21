package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
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
    private HDBOfficer[] officersList;
    private int officerSlots;
    private Map<FlatTypes, FlatType> flatUnits = new HashMap<>();

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
                ", officerSlots=" + officerSlots +
                '}';
    }

    public BTOProj(int id, String name, Neighbourhoods nbh, List<FlatType> flatTypesAvail, LocalDateTime open, LocalDateTime close, HDBManager manager, int officerSlots, List<HDBOfficer> officerList) {
        this.btoProjId = id;
        this.projectName = name;
        this.neighbourhood = nbh;
        this.flatTypesAvail = flatTypesAvail;
        this.appOpenDate = open;
        this.appCloseDate = close;
        this.managerIC = manager;
        this.officerSlots = officerSlots;
        this.officersList = officerList.toArray(new HDBOfficer[0]);

        this.isVisible = open.isBefore(LocalDateTime.now()) && close.isAfter(LocalDateTime.now());
        this.status = this.isVisible ? ProjStatus.OPEN : ProjStatus.CLOSED;

        applications = new ArrayList<>();
        blocks = new ArrayList<>();
        enquiries = new ArrayList<>();
    }

    public BTOProj() {
        blocks = new ArrayList<>();
        applications = new ArrayList<>();
        flatTypesAvail = new ArrayList<>();
        enquiries = new ArrayList<>();
        officersList = new HDBOfficer[0];
    }

    public BTOProj(int id, String name, LocalDateTime open, LocalDateTime close, boolean visible) {
        this();
        this.btoProjId = id;
        this.projectName = name;
        this.appOpenDate = open;
        this.appCloseDate = close;
        this.isVisible = visible;
    }

    public int getProjId() { return btoProjId; }
    public void setProjId(int id) { this.btoProjId = id; }
    public String getProjName() { return projectName; }
    public void setProjName(String name) { this.projectName = name; }
    public LocalDateTime getAppOpenDate() { return appOpenDate; }
    public void setApplicationOpenDate(LocalDateTime datetime) { this.appOpenDate = datetime; }
    public LocalDateTime getAppCloseDate() { return appCloseDate; }
    public void setAppCloseDate(LocalDateTime datetime) { this.appCloseDate = datetime; }
    public boolean getVisibility() { return isVisible; }
    public void setVisibility(boolean visible) { this.isVisible = visible; }
    public void toggleVisibility() { this.isVisible = !this.isVisible; }
    public ProjStatus getProjStatus() { return status; }
    public void setProjStatus(ProjStatus status) { this.status = status; }
    public Neighbourhoods getProjNbh() { return neighbourhood; }
    public void setProjNbh(Neighbourhoods nbh) { this.neighbourhood = nbh; }
    public HDBManager getManagerIC() { return managerIC; }
    public void setManagerIC(HDBManager manager) { this.managerIC = manager; }
    public List<Block> getBlocks() { return blocks; }
    public void setBlocks(List<Block> blocks) { this.blocks = blocks; }
    public boolean addBlock(Block b) { return blocks.add(b); }
    public boolean removeBlock(int blkNo) {
        return blocks.removeIf(b -> b.getBlkNo() == blkNo);
    }
    public void setApplications(List<Application> apps) { this.applications = apps; }
    public boolean addApp(Application app) { return applications.add(app); }
    public boolean removeApp(int id) { return applications.removeIf(a -> a.getAppId() == id); }
    public Application getAppById(int id) {
        return applications.stream().filter(a -> a.getAppId() == id).findFirst().orElse(null);
    }
    public List<Application> getAllApp() { return applications; }
    public List<FlatType> getAvailableFlatTypes() { return flatTypesAvail; }
    public void setAvailableFlatTypes(List<FlatType> list) { this.flatTypesAvail = list; }
    public void addAvailFlatType(FlatType type) { flatTypesAvail.add(type); }
    public void addFlatTypeWithPrice(FlatTypes type, int units, float sellingPrice) {
        FlatType newFlat = new FlatType(type.getDisplayName(), units, sellingPrice);
        flatUnits.put(type, newFlat);
        flatTypesAvail.add(newFlat);
    }
    public Map<FlatTypes, FlatType> getFlatUnits() { return flatUnits; }
    public List<Enquiry> getAllEnquiries() { return enquiries; }
    public void setEnquiries(List<Enquiry> enquiryList) { this.enquiries = enquiryList; }
    public void addEnquiryToProj(Enquiry e) { enquiries.add(e); }
    public void removeEnquiryFromProj(int id) { enquiries.removeIf(e -> e.getEnquiryId() == id); }
    public Enquiry getEnquiryInProjById(int id) {
        return enquiries.stream().filter(e -> e.getEnquiryId() == id).findFirst().orElse(null);
    }

    // Officer List
    public HDBOfficer[] getOfficersList() { return officersList; }

    public void setOfficersList(HDBOfficer[] officersList) { this.officersList = officersList; }

    public boolean assignOfficer(HDBOfficer officer) {
        for (HDBOfficer o : officersList) {
            if (o.getUserId() == officer.getUserId()) return false;
        }
        HDBOfficer[] updated = new HDBOfficer[officersList.length + 1];
        System.arraycopy(officersList, 0, updated, 0, officersList.length);
        updated[officersList.length] = officer;
        officersList = updated;
        return true;
    }

    public boolean unassignOfficer(int officerId) {
        List<HDBOfficer> updated = new ArrayList<>();
        boolean removed = false;
        for (HDBOfficer o : officersList) {
            if (o.getUserId() != officerId) {
                updated.add(o);
            } else {
                removed = true;
            }
        }
        officersList = updated.toArray(new HDBOfficer[0]);
        return removed;
    }

    public int getOfficerSlots() { return officerSlots; }
    public void setOfficerSlots(int officerSlots) { this.officerSlots = officerSlots; }
    public BTOExercise getExercise() { return exercise; }
    public void setExercise(BTOExercise exercise) { this.exercise = exercise; }
}
