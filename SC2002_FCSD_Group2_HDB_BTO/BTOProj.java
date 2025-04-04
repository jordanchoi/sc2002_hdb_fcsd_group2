import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Enumeration.MaritalStatus;
import Enumeration.Neighbourhoods;
import Enumeration.ProjStatus;

public class BTOProj {
    private int btoProjId;
    private String projectName;
    private LocalDateTime appOpenDate;
    private LocalDateTime appCloseDate;
    private boolean isVisible;
    private ProjStatus status;

    private List<MaritalStatus> openTo;
    private Neighbourhoods neighbourhood;
    private HDBManager managerIC;
    private List<Block> blocks;
    private List<Application> applications;
    private List<FlatType> flatTypesAvail;
    private List<Enquiry> enquiries;
    private List<HDBOfficer> officersList;

    public BTOProj() {
        openTo = new ArrayList<>();
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

    // Marital Status eligibility
    public List<MaritalStatus> getOpenTo() {
        return openTo;
    }

    public void setOpenTo(List<MaritalStatus> list) {
        this.openTo = list;
    }

    public void addOpenTo(MaritalStatus status) {
        if (!openTo.contains(status))
            openTo.add(status);
    }

    public void removeOpenTo(MaritalStatus status) {
        openTo.remove(status);
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
            if (blocks.get(i).getBlockNo() == blkNo) {
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
     * public Application getAppByNric(String nric) {
     * 
     * }
     **/

    public List<Application> getAllApps() {
        return applications;
    }

     // Flat Types
     public List<FlatType> getAvailableFlatTypes() {
        return flatTypesAvail;
    }

    public void setAvailableFlatTypes(List<FlatType> list) {
        this.flatTypesAvail = list;
    }

    public void addAvailFlatType(FlatType type) {
        flatTypesAvail.add(type);
    }

    public boolean removeAvailFlatType(int typeId) {
        for (int i = 0; i < flatTypesAvail.size(); i++) {
            if (flatTypesAvail.get(i).getFlatTypeId() == typeId) {
                flatTypesAvail.remove(i);
                return true; // removed successfully
            }
        }
        return false;
    }

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
            if (officer.userId == officerId) { 
                officersList.remove(officer);
                return true;
            }
        }
        return false;
    }
}