package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a type of flat in a BTO project.
 * Contains details like size, total units, selling price, and target applicant groups.
 */
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

    /**
     * Constructs a FlatType from given parameters (Excel file version).
     *
     * @param name flat type name
     * @param totalUnits number of units
     * @param sellingPrice price per unit
     */
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

    /**
     * Constructs a FlatType with given parameters (no selling price).
     */
    public FlatType(int id, String name, int sqm, int totalUnits) {
        //this.flatTypeId = id;
        this.typeName = name;
        this.sqm = sqm;
        this.totalUnits = totalUnits;
        this.unitsBooked = 0;
        this.unitsAvail = totalUnits;
        this.flats = new ArrayList<>();
    }

    /**
     * Constructs a FlatType with full details.
     */
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

    /**
     * Adds a flat to the list if not already present.
     *
     * @param flat the flat to add
     * @return true if added successfully, false if duplicate
     */
    public boolean addFlatsToList(Flat flat) {
        if (!flats.contains(flat)) {
            flats.add(flat);
            return true;
        }
        return false;
    }

    /**
     * Removes a flat from the list based on floor and unit number.
     *
     * @param floorNo floor number
     * @param unitNo unit number
     * @return true if removed successfully
     */
    public boolean removeFlatFromList(int floorNo, int unitNo) {
        return flats.removeIf(f -> f.getFloorNo() == floorNo && f.getUnitNo() == unitNo);
    }

    /**
     * Recalculates the number of available units.
     */
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

    /**
     * Adds an allowed marital status for this flat type.
     */
    public void addOpenTo(MaritalStatus status) {
        if (!openTo.contains(status))
            openTo.add(status);
    }

    /**
     * Removes an allowed marital status from this flat type.
     */
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
