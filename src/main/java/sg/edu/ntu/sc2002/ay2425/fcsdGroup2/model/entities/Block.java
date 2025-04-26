package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a residential block within a BTO project.
 * Contains information about block number, address, postal code, associated flats, and project.
 */
public class Block {
    private int blockId;
    private int blkNo;
    private String streetAddr;
    private String postalCode;
    private List<Flat> flatsListing;
    private BTOProj project;

    /**
     * Constructs a Block with block details and associated project.
     *
     * @param blockId unique block ID
     * @param streetAddr street address of the block
     * @param blkNo block number
     * @param postal postal code
     * @param project associated BTO project
     */
    public Block(int blockId, String streetAddr, int blkNo, String postal, BTOProj project) {
        this.blockId = blockId;
        this.streetAddr = streetAddr;
        this.blkNo = blkNo;
        this.postalCode = postal;
        this.flatsListing = new ArrayList<>();
        this.project = project;
    }

    /**
     * Constructs a Block with existing list of flats.
     *
     * @param streetAddr street address
     * @param blkNo block number
     * @param postalCode postal code
     * @param flatsListing list of flats
     * @param project associated project
     */
    public Block(String streetAddr, int blkNo, String postalCode, List<Flat> flatsListing, BTOProj project) {
        this.streetAddr = streetAddr;
        this.blkNo = blkNo;
        this.postalCode = postalCode;
        this.flatsListing = flatsListing;
        this.project = project;
    }

    // Getters and Setters with JavaDoc below...

    /**
     * Returns the block ID.
     *
     * @return block ID
     */
    public int getBlockId() { return blockId; }

    /**
     * Returns the block number.
     *
     * @return block number
     */
    public int getBlkNo() { return blkNo; }

    /**
     * Sets the block number.
     *
     * @param blkNo block number
     */
    public void setBlkNo(int blkNo) { this.blkNo = blkNo; }

    /**
     * Returns the street address.
     *
     * @return street address
     */
    public String getStreetAddr() { return streetAddr; }

    /**
     * Sets the street address.
     *
     * @param streetAddr street address
     */
    public void setStreetAddr(String streetAddr) { this.streetAddr = streetAddr; }

    /**
     * Returns the postal code.
     *
     * @return postal code
     */
    public String getPostalCode() { return postalCode; }

    /**
     * Sets the postal code.
     *
     * @param postalCode postal code
     */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /**
     * Returns the list of flats in the block.
     *
     * @return list of flats
     */
    public List<Flat> getFlatsListing() { return flatsListing; }

    /**
     * Sets the list of flats in the block.
     *
     * @param flatsListing list of flats
     */
    public void setFlatsListing(List<Flat> flatsListing) { this.flatsListing = flatsListing; }

    /**
     * Returns the associated BTO project.
     *
     * @return associated project
     */
    public BTOProj getProject() { return project; }

    /**
     * Sets the associated BTO project.
     *
     * @param project project to associate
     */
    public void setProject(BTOProj project) { this.project = project; }
}
