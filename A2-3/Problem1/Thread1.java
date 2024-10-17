//import java.util.*;
import java.util.concurrent.*;

public class Thread1 extends Thread{
    private int trail;
    private int crossingTime;
    private String id,state,destination;
    private Semaphore lock;
    private Counter ctr1, ctr2;

    public Thread1(MAC m, Semaphore lock, Counter ctr1, Counter ctr2){
        this.id = m.getName();
        this.state = m.getState();
        this.destination = m.getDestination();
        this.crossingTime = m.getCrossTime();
        this.trail = m.getTrail();
        this.lock = lock;
        this.ctr1 = ctr1;
        this.ctr2 = ctr2;
        
    }
    //@override
    public void run(){

        try {
            while(crossingTime>0){  // switch state and destination
                System.out.println(id + " (" + state + "):" + " Waiting at the intersection. Going towards " + destination );

                lock.acquire();
                //System.out.println(id + " acquired the lock.");
                crossingIntersection();
                System.out.println("Total crossed in Trail1: " +  ctr1.get() + " Trail2: " + ctr2.get());
                lock.release();
                if(state.startsWith("E")){
                    state = "Stock";
                    if(trail==1){
                        destination = "ED" + trail;
                    }
                    else{
                        destination = "ED" + trail;
                    }
                }
                else{
                    state = "Empty";
                    if(trail==1){
                        destination = "CSR" + trail;
                    }
                    else{
                        destination = "CSR" + trail;
                    }
                }
            }
            if(crossingTime ==0){
                System.out.println(id + " (" + state + "):" + " Finished" );
            }
            
        } 
        catch (Exception e) {
            System.out.println("Somethings wrong:D");
        }
    }

    public void crossingIntersection(){
        try {
            for(int i=1; i<=3; i++){
                Thread.sleep(50);
                System.out.println(id + " (" + state + "):" + " Crossing intersection Checkpoint "+ i + ".");
            }
            System.out.println(id + " (" + state + "):" + " Crossed the intersection.");
            if(trail==1){
                ctr1.add();
            }
            else{
                ctr2.add();
            }
            crossingTime--;
            //decrementCrossTime, while crosstime !=0, continue crossing
            

        } catch (InterruptedException e) {
            System.out.println("Somethings wrong:D");
        }
        
    }

}


