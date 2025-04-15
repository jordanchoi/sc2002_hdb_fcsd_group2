package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.User;
import java.util.*;
import java.util.Scanner;
import java.util.List;

class UserAuthController {
    public static User login(List<User> users) {
    /* REMOVED BY JORDAN - START FROM AFRESH
        boolean status = false;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter NRIC: ");
        String nric = scanner.nextLine().toUpperCase();

        // Validate NRIC using simple string operations
        if (!User.isValidNRIC(nric)) {
            System.out.println("Error: Invalid NRIC format! Example of a correct format: S1234567A.");
            return login(users); // Retry login
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.getNric().equals(nric) && user.getPwd().equals(password)) {
                System.out.println("Login successful! Welcome, " + user.getFullName());
                return user;
            }
            else if(user.getNric().equals(nric) && user.getPwd() != (password)){
                status = true;
                System.out.println("Incorrect password. Try again.");
            }
        }
        if (status != true){
            System.out.println("NRIC not found. Try again.");
            status = false;
        }
        return login(users);
     */
        return null; // Placeholder return statement
    }
}
