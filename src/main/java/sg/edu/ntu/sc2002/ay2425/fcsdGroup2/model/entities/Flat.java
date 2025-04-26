package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatBookingStatus;

/**
 * Represents a flat (unit) within a residential block.
 * Contains information about floor number, unit number, booking status, type, and block.
 */
public class Flat {
    private int floorNo;
    private int unitNo;
    private FlatBookingStatus status;
    private FlatType type;
    private Block inBlock;

    /**
     * Constructs a new Flat instance.
     *
     * @param floorNo the floor number where the flat is located
     * @param unitNo the unit number
     * @param status the booking status of the flat
     * @param type the type of the flat (e.g., 2-Room, 3-Room)
     * @param inBlock the block where the flat belongs
     */
    public Flat(int floorNo, int unitNo, FlatBookingStatus status, FlatType type, Block inBlock) {
        this.floorNo = floorNo;
        this.unitNo = unitNo;
        this.status = status;
        this.type = type;
        this.inBlock = inBlock;
    }

    /**
     * Returns the floor number of the flat.
     *
     * @return floor number
     */
    public int getFloorNo() {
        return floorNo;
    }

    /**
     * Sets the floor number of the flat.
     *
     * @param floor floor number
     */
    public void setFloorNo(int floor) {
        this.floorNo = floor;
    }

    /**
     * Returns the unit number of the flat.
     *
     * @return unit number
     */
    public int getUnitNo() {
        return unitNo;
    }

    /**
     * Sets the unit number of the flat.
     *
     * @param unit unit number
     */
    public void setUnitNo(int unit) {
        this.unitNo = unit;
    }

    /**
     * Returns a string combining floor and unit number.
     *
     * @return formatted floor and unit string
     */
    public String getFloorUnit() {
        return "Level " + floorNo + " - Unit " + unitNo;
    }

    /**
     * Returns the booking status of the flat.
     *
     * @return booking status
     */
    public FlatBookingStatus getStatus() {
        return status;
    }

    /**
     * Sets the booking status of the flat.
     *
     * @param status booking status
     */
    public void setStatus(FlatBookingStatus status) {
        this.status = status;
    }

    /**
     * Returns the flat type.
     *
     * @return flat type
     */
    public FlatType getFlatType() {
        return type;
    }

    /**
     * Sets the flat type.
     *
     * @param type flat type
     */
    public void setFlatType(FlatType type) {
        this.type = type;
    }

    /**
     * Returns the block where the flat is located.
     *
     * @return block
     */
    public Block getBlock() {
        return inBlock;
    }

    /**
     * Sets the block for the flat.
     *
     * @param blk block
     */
    public void setBlock(Block blk) {
        this.inBlock = blk;
    }
}
