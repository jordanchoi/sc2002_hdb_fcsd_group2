package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;

public interface canApplyFlat {
    /*
     * This method checks if the user can apply for a flat.
     *
     * @return true if the user can apply for a flat, false otherwise.
     */
    public boolean checkEligibility();
    public boolean submitApplication();
    public Application applyFlat();
}
