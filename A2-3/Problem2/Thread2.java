/*
* COMP2240 - Operating Systems
* Assignment 2 - Answers
* @author Jimmy Nguyen - c3440776
* Last edit: 25/9/2024
* 
* This file contains the run() method, which overrides the run() method from the Thread class.
*/

import java.util.concurrent.locks.*;

public class Thread2 extends Thread {
    // Fields representing client information, counters, coffee machine, and locks
    String id, type;
    int timeServe, dispenserNumber;
    Counter time, nDispenser, noClientServed;
    CoffeeMachine cm;
    Client nextClient;
    
    // Static fields for tracking time and managing concurrency
    static int timeAdd;
    static Lock lock = new ReentrantLock(); // General lock
    static Lock typeLock = new ReentrantLock(); // Lock for managing coffee type switching
    static Lock dispenserLock = new ReentrantLock(); // Lock for managing dispenser availability
    static Condition coffeeType = typeLock.newCondition(); // Condition to handle coffee type changes
    static Condition dispenserAvail = dispenserLock.newCondition(); // Condition for waiting on dispenser availability

    // Constructor: Takes client information, coffee machine, counters, and the next client as parameters
    Thread2(Client c, Client nextC, Counter time, Counter nDispenser, CoffeeMachine cm, Counter noClientServed) {
        this.id = c.getId();
        this.type = c.getType();
        this.timeServe = c.getTime();
        this.nextClient = nextC;
        this.nDispenser = nDispenser;
        this.time = time;
        this.cm = cm;
        this.noClientServed = noClientServed;
    }

    //@override
    public void run() {
        // Lock to ensure mutual exclusion while checking and waiting for dispenser availability
        dispenserLock.lock();
        try {
            // Wait until a dispenser is free (when the number of active dispensers is less than 3)
            while (nDispenser.get() == 3) {
                dispenserAvail.await(); // Wait for a signal when a dispenser becomes available
            }
        } catch (InterruptedException e) {
            System.out.println("Something is wrong :D");
        } finally {
            dispenserLock.unlock(); // Release the dispenser lock
        }

        // Lock to manage coffee type synchronization
        typeLock.lock();
        try {
            // Wait until the coffee machine is serving the correct coffee type
            while (!cm.getCurrentType().equals(type)) {
                if (cm.getCurrentType().equals("")) {
                    cm.setCurrentType(type); // Set the coffee type if it's not already set
                } else {
                    coffeeType.await(); // Wait until the coffee machine is ready to serve this type
                }
            }
            typeLock.unlock(); // Release the type lock once the coffee machine is set

            // Access and use the dispenser
            cm.useDispenser(); // Use a dispenser
            dispenserNumber = cm.getDispenserNo(); // Get the dispenser number currently being used

            nDispenser.increment(); // Increment the number of dispensers in use

            // Print the log message showing the current client's use of the dispenser
            System.out.println("(" + time.get() + ") " + id + " uses dispenser " + dispenserNumber + " (time:" + timeServe + ")");
            Thread.sleep(timeServe * 100); // Simulate the time taken to serve the client
            noClientServed.decrement(); // Decrement the number of clients left to be served
        } catch (InterruptedException e) {
            System.out.println("Something is wrong :D");
        }

        // Lock for managing the release of the dispenser
        dispenserLock.lock();
        try {
            cm.freeDispenser(dispenserNumber); // Release the dispenser

            nDispenser.decrement(); // Decrement the number of active dispensers
            dispenserAvail.signal(); // Signal a waiting thread that a dispenser is available

            // If there is a next client and all dispensers are free, change the coffee type
            if (nextClient != null && nDispenser.get() == 0 && noClientServed.get()!=0) {  // If no dispensers are in use and no client after this
                typeLock.lock(); // Lock the type for switching
                try {
                    // Switch coffee type based on the current client's type
                    if (type.startsWith("H")) {
                        cm.setCurrentType("Cold"); // Switch to cold coffee if the current type is hot
                    } else {
                        cm.setCurrentType("Hot"); // Switch to hot coffee if the current type is cold
                    }
                    coffeeType.signalAll(); // Notify all waiting threads that the coffee type has changed
                    time.addNumber(timeServe); // Update the time counter
                } finally {
                    typeLock.unlock(); // Release the type lock
                }
            }

            // If all dispensers are free and no clients remain to be served, print "DONE"
            if (nDispenser.get() == 0 && Thread.activeCount() == 2 && noClientServed.get() == 0) {
                time.addNumber(timeServe);
                System.out.println("(" + time.get() + ") DONE");
            }

        } finally {
            dispenserLock.unlock(); // Release the dispenser lock
        }
    }
}
