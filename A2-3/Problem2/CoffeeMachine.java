/*
 * COMP2240 - Operating Systems
 * Assignment 2 - Answers
 * @author  Jimmy Nguyen - c3440776
 * Last edit: 25/9/2024
 * 
 * This file contains coffee machine object, use to assign dispensers to clients
 */
import java.util.*;
public class CoffeeMachine {
    private String currentType; 
    private int dispenserNumber;
    private boolean[] dispensers;

    CoffeeMachine(Counter dCount){
        this.currentType="";
        this.dispenserNumber=0;
        this.dispensers = new boolean[3];
        Arrays.fill(dispensers, false);
    }

    public synchronized void useDispenser() {
        // Iterate through the list of dispensers
        for (int i = 0; i < dispensers.length; i++) {
            // Check if the dispenser at index i is not in use
            if (!dispensers[i]) {
                // Mark the dispenser as in use by setting it to 'true'
                dispensers[i] = true;
                // Set the current dispenser number to i + 1 (1-based index)
                setDispenserNumber(i + 1);
                // Exit the loop once a free dispenser is found and assigned
                break;
            }
        }
    }
    public void setDispenserNumber(int dispenserNumber) {
        this.dispenserNumber = dispenserNumber;
    }

    public synchronized int getDispenserNo(){
        return dispenserNumber;
    }

    public synchronized void freeDispenser(int dispenserNo){            //free dispenser
        dispensers[dispenserNo-1] = false;
    }

    public void setCurrentType(String type){
        currentType = type;
    }

    public String getCurrentType(){
        return currentType;
    }
    
    public boolean[] getDispensers(){
        return dispensers;
    }
}
