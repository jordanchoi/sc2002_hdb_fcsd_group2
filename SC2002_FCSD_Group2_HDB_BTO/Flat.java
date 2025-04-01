import Enumeration.FlatBookingStatus;

public class Flat {
    private int floorNo;
    private int unitNo;
    private FlatBookingStatus status;
    private FlatType type;
    private Block inBlock;

    // Constructor
    public Flat(int floorNo, int unitNo, FlatBookingStatus status, FlatType type, Block inBlock) {
        this.floorNo = floorNo;
        this.unitNo = unitNo;
        this.status = status;
        this.type = type;
        this.inBlock = inBlock;
    }

    // Getters and Setters
    public int getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(int floor) {
        this.floorNo = floor;
    }

    public int getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(int unit) {
        this.unitNo = unit;
    }

    public String getFloorUnit() {
        return "Level " + floorNo + " - Unit " + unitNo;
    }

    public FlatBookingStatus getStatus() {
        return status;
    }

    public void setStatus(FlatBookingStatus status) {
        this.status = status;
    }

    public FlatType getFlatType() {
        return type;
    }

    public void setFlatType(FlatType type) {
        this.type = type;
    }

    public Block getBlock() {
        return inBlock;
    }

    public void setBlock(Block blk) {
        this.inBlock = blk;
    }
}
