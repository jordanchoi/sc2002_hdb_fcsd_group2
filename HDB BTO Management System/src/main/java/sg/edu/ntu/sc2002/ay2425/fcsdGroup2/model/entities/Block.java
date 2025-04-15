package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Block {
    private int blkNo;
    private String streetAddr;
    private String postalCode;
    private List<Flat> flatsListing;
    private BTOProj project;

    // Constructor
    public Block(String streetAddr, int blkNo, String postal, BTOProj project) {
        this.streetAddr = streetAddr;
        this.blkNo = blkNo;
        this.postalCode = postal;
        this.flatsListing = new ArrayList<>();
        this.project = project;
    }

    // Constructor with flatListing
    public Block(String streetAddr, int blkNo, String postalCode, List<Flat> flatsListing, BTOProj project) {
        this.streetAddr = streetAddr;
        this.blkNo = blkNo;
        this.postalCode = postalCode;
        this.flatsListing = flatsListing;
        this.project = project;
    }

    // Getters and Setters
    public int getBlkNo() {
        return blkNo;
    }

    public void setBlkNo(int blkNo) {
        this.blkNo = blkNo;
    }

    public String getStreetAddr() {
        return streetAddr;
    }

    public void setStreetAddr(String streetAddr) {
        this.streetAddr = streetAddr;
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

    public BTOProj getProject() {
        return project;
    }

    public void setProject(BTOProj project) {
        this.project = project;
    }


}
