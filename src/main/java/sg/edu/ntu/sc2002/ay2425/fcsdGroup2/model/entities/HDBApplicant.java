package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

public class HDBApplicant extends User {
    private Application currentApplication;

    public Application getCurrentApplication() {
        return currentApplication;
    }

    public void setCurrentApplication(Application currentApplication) {
        this.currentApplication = currentApplication;
    }

    public HDBApplicant(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.currentApplication = null;
    }

    @Override
    public void viewMenu(){
        System.out.println("\n=== Applicant Menu ===");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Apply for a BTO project");
        System.out.println("3. View applied project status");
        System.out.println("4. Withdraw application");
        System.out.println("5. Submit enquiry");
        System.out.println("6. View/Edit/Delete enquiries");
        System.out.println("7. Change password");
        System.out.println("8. Logout");
    }

//    @Override
//    public String toString() {
//        return "HDBApplicant{" +
//                "maritalStatus=" + maritalStatus +
//                ", age=" + age +
//                ", middleName='" + middleName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", firstName='" + firstName + '\'' +
//                ", password='" + password + '\'' +
//                ", nric='" + nric + '\'' +
//                ", userId=" + userId +
//                ", enquiries=" + enquiries +
//                ", applications=" + applications +
//                ", appliedProject=" + appliedProject +
//                '}';
//    }
}
