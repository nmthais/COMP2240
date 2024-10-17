/*
 * COMP2240 - Operating Systems
 * Assignment 2 - Answers
 * @author  Jimmy Nguyen - c3440776
 * 
 * 
 * This is the file containing run() method, overriding the run() from Thread class
 */

//import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Thread2 extends Thread {
    String id, type;
    int timeServe, dispenserNumber;
    Counter time, nDispenser;
    CoffeeMachine cm;
    Client nextClient;
    static int timeAdd;
    static Lock lock = new ReentrantLock();
    static Lock typeLock = new ReentrantLock();
    static Lock dispenserLock = new ReentrantLock();
    static Condition coffeeType = typeLock.newCondition();
    static Condition dispenserAvail = dispenserLock.newCondition();

    Thread2(Client c, Client nextC, Counter time, Counter nDispenser, CoffeeMachine cm){ //take client, the next client, number of available dispenser and object coffee machine as parmeter
        this.id = c.getId();
        this.type = c.getType();
        this.timeServe = c.getTime();
        this.nextClient = nextC;
        this.nDispenser = nDispenser;
        this.time = time;
        this.cm = cm;
    }

    //@override
    public void run() {

        dispenserLock.lock();
        try{
            while(nDispenser.get()==3) {
                dispenserAvail.await();
            }
        }
        catch (InterruptedException e) {
            System.out.println("Something is wrong :D");
        }
        finally{
            dispenserLock.unlock();
        }

        typeLock.lock();
        try {
            while (!cm.getCurrentType().equals(type)) {
                if (cm.getCurrentType().equals("")) {
                    cm.setCurrentType(type);
                } 
                else {
                    coffeeType.await();
                }
            }
            typeLock.unlock();

            cm.useDispenser();
            dispenserNumber = cm.getDispenserNo();

            nDispenser.increment();
            
            System.out.println("("+ time.get() + ") " + id + " uses dispenser "+ dispenserNumber + " (time:"+ timeServe + ")");
            Thread.sleep(timeServe * 100);
        }
        catch (InterruptedException e) {
            System.out.println("Something is wrong :D");
        }
        
        dispenserLock.lock();
        try {
            cm.freeDispenser(dispenserNumber);

            nDispenser.decrement(); // Free a dispenser
            dispenserAvail.signal(); // Notify one waiting thread about dispenser availability
            // If all dispensers are available, allow switching coffee type
            if (nextClient!=null){
                if (nDispenser.get() == 0) {
                    typeLock.lock();
                    try{
                        if(type.startsWith("H")){
                            cm.setCurrentType("Cold");
                        }
                        else{
                            cm.setCurrentType("Hot");
                        }                        
                        coffeeType.signalAll(); // Notify all waiting clients that coffee type changed
                        time.addNumber(timeServe);
                    }
                    finally{
                        typeLock.unlock();
                    }
                }
            }
            

            if(nextClient==null){
                time.addNumber(timeServe); 
                System.out.println("(" + time.get() + ") DONE");
            }

        }
        finally {
            dispenserLock.unlock();
        }
        
    }
}
