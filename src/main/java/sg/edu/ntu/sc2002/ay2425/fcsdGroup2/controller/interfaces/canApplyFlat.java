package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;

public interface canApplyFlat {
    /*
     * This method checks if the user can apply for a flat.
     *
     * @return true if the user can apply for a flat, false otherwise.
     */
    public boolean checkEligibility(BTOProj project);
    public boolean submitApplication(BTOProj project);
}
