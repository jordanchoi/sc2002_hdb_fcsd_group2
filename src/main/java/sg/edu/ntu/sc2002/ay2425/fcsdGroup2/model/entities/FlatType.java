package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

public class FlatType {
    // Should be not needed
    // private int flatTypeId;
    private String typeName;
    private int sqm;
    private int totalUnits;
    private int unitsBooked;
    private int unitsAvail;
    private List<Flat> flats;
    private BTOProj inProj;
    private float sellingPrice;
    private List<MaritalStatus> openTo;

    // Constructor for Given Data in Provided Excel File

    // This constructor is used to create a FlatType object with the given parameters
    public FlatType(String name, int totalUnits, float sellingPrice) {
        // this.flatTypeId = id;
        this.typeName = name;
        this.sqm = (name.equals("2-Room") ? 45 : name.equals("3-Room") ? 65 : name.equals("4-Room") ? 93 : name.equals("5-Room") ? 110 : 0);
        this.totalUnits = totalUnits;
        this.unitsBooked = 0;
        this.unitsAvail = this.totalUnits-unitsBooked;
        this.sellingPrice = sellingPrice;

        // Initialize an empty array to store the list of flats first.
        this.flats = new ArrayList<>();
        this.openTo = new ArrayList<>();

        if (name.equals("2-Room")) {
            this.openTo.add(MaritalStatus.SINGLE);
        } else {
            this.openTo.add(MaritalStatus.SINGLE);
            this.openTo.add(MaritalStatus.MARRIED);
            this.openTo.add(MaritalStatus.DIVORCED);
            this.openTo.add(MaritalStatus.WIDOWED);
            this.openTo.add(MaritalStatus.SEPARATED);
        }
    }

    // Constructor 1
    public FlatType(int id, String name, int sqm, int totalUnits) {
        //this.flatTypeId = id;
        this.typeName = name;
        this.sqm = sqm;
        this.totalUnits = totalUnits;
        this.unitsBooked = 0;
        this.unitsAvail = totalUnits;
        this.flats = new ArrayList<>();
    }

    // Constructor 2
    public FlatType(int id, String name, int sqm, int totalUnits, int bookedUnits, int availUnits, List<Flat> flatsList) {
        //this.flatTypeId = id;
        this.typeName = name;
        this.sqm = sqm;
        this.totalUnits = totalUnits;
        this.unitsBooked = bookedUnits;
        this.unitsAvail = availUnits;
        this.flats = flatsList;
    }

    // Getters and Setters
    /*
    public int getFlatTypeId() {
        return flatTypeId;
    }

    public void setFlatTypeId(int id) {
        this.flatTypeId = id;
    }
    */

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String name) {
        this.typeName = name;
    }

    public int getSqm() {
        return sqm;
    }

    public void setSqm(int sqm) {
        this.sqm = sqm;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
        recalculateAvailability();
    }

    public int getUnitsBooked() {
        return unitsBooked;
    }

    public void setUnitsBooked(int unitsBooked) {
        this.unitsBooked = unitsBooked;
        recalculateAvailability();
    }

    public int getUnitsAvail() {
        return unitsAvail;
    }

    public void setUnitsAvail(int unitsAvail) {
        this.unitsAvail = unitsAvail;
    }

    public List<Flat> getFlats() {
        return flats;
    }

    public void setFlats(List<Flat> flatsList) {
        this.flats = flatsList;
    }

    // Add model.entities.Flat
    public boolean addFlatsToList(Flat flat) {
        if (!flats.contains(flat)) {
            flats.add(flat);
            return true;
        }
        return false;
    }

    // Remove model.entities.Flat by floor number and unit number
    public boolean removeFlatFromList(int floorNo, int unitNo) {
        return flats.removeIf(f -> f.getFloorNo() == floorNo && f.getUnitNo() == unitNo);
    }

    // Update availability when booking or canceling
    private void recalculateAvailability() {
        this.unitsAvail = totalUnits - unitsBooked;
    }

    public BTOProj getInProj() {
        return inProj;
    }

    public void setInProj(BTOProj inProj) {
        this.inProj = inProj;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public List<MaritalStatus> getOpenTo() {
        return openTo;
    }

    public void setOpenTo(List<MaritalStatus> openTo) {
        this.openTo = openTo;
    }

    public void addOpenTo(MaritalStatus status) {
        if (!openTo.contains(status))
            openTo.add(status);
    }

    public void removeOpenTo(MaritalStatus status) {
        openTo.remove(status);
    }

    @Override
    public String toString() {
        return "FlatType{" +
                "typeName='" + typeName + '\'' +
                ", sqm=" + sqm +
                ", totalUnits=" + totalUnits +
                ", unitsBooked=" + unitsBooked +
                ", unitsAvail=" + unitsAvail +
                ", flats=" + flats +
                ", inProj=" + inProj +
                ", sellingPrice=" + sellingPrice +
                ", openTo=" + openTo +
                '}';
    }
}
