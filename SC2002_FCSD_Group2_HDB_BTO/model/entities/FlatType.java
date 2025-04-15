package model.entities;

import java.util.ArrayList;
import java.util.List;

public class FlatType {
    private int flatTypeId;
    private String typeName;
    private int sqm;
    private int totalUnits;
    private int unitsBooked;
    private int unitsAvail;
    private List<Flat> flats;

    // Constructor 1
    public FlatType(int id, String name, int sqm, int totalUnits) {
        this.flatTypeId = id;
        this.typeName = name;
        this.sqm = sqm;
        this.totalUnits = totalUnits;
        this.unitsBooked = 0;
        this.unitsAvail = totalUnits;
        this.flats = new ArrayList<>();
    }

    // Constructor 2
    public FlatType(int id, String name, int sqm, int totalUnits, int bookedUnits, int availUnits, List<Flat> flatsList) {
        this.flatTypeId = id;
        this.typeName = name;
        this.sqm = sqm;
        this.totalUnits = totalUnits;
        this.unitsBooked = bookedUnits;
        this.unitsAvail = availUnits;
        this.flats = flatsList;
    }

    // Getters and Setters
    public int getFlatTypeId() {
        return flatTypeId;
    }

    public void setFlatTypeId(int id) {
        this.flatTypeId = id;
    }

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
}
