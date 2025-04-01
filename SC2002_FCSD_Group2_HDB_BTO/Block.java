import java.util.ArrayList;
import java.util.List;

import Enumeration.FlatBookingStatus;

public class Block {
    private String streetAddr;
    private int blkNo;
    private String postalCode;
    private List<Flat> flatsListing;

    // Constructor
    public Block(String streetAddr, int blkNo, String postalCode) {
        this.streetAddr = streetAddr;
        this.blkNo = blkNo;
        this.postalCode = postalCode;
        this.flatsListing = new ArrayList<>();
    }

    // Getters and Setters
    public String getStreetAddr() {
        return streetAddr;
    }

    public void setStreetAddr(String streetAddr) {
        this.streetAddr = streetAddr;
    }

    public int getBlockNo() {
        return blkNo;
    }

    public void setBlockNo(int blkNo) {
        this.blkNo = blkNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<Flat> getFlatsListing() {
        return flatsListing;
    }

    public void setFlatsListing(List<Flat> flatsListing) {
        this.flatsListing = flatsListing;
    }

}
