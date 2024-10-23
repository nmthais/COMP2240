/*
* COMP2240 - Operating Systems
* Assignment 3 - Sub
* @author  Minh Thai Nguyen - c3440776 
* @version 1.0
* 
* This file simalate variable allocation - global replacement
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VariableGobal {
    private HashMap<String,ArrayList<Integer>> processes;
    private ArrayList<String> cleanPageL;
    private ArrayList<Process> pList;
    private Counter time, processCountdown,frameCount;
    private int numF, timeQ;
    private ArrayList<Process> readyQueue, blockedQueue;
    private final int timeAddPage;
    
    public VariableGobal(ArrayList<Process> pList, int numFrames, int tQuantum){
        this.processes = new HashMap<>();
        for(int i=0;i<pList.size();i++){                        //setting up main memory
            String pName = pList.get(i).getName();
            processes.putIfAbsent(pName, new ArrayList<>());
        }
        this.numF = numFrames;
        this.timeQ = tQuantum;
        this.frameCount = new Counter(numF);
        this.cleanPageL = new ArrayList<>();
        for(int i=0;i<numF;i++){
            cleanPageL.add(null);
        }
        this.pList = pList;
        this.time = new Counter();
        this.processCountdown = new Counter(pList.size());
        this.readyQueue = new ArrayList<>();
        this.blockedQueue = new ArrayList<>();
        this.timeAddPage =4;
    }

    public void Global(){
        for(int i=0; i<pList.size();i++){           //pageFault at time 0
            Process p = pList.get(i);
            readyQueue.add(p);                         //add to readyQueue
        }
        while(processCountdown.get()>0){
            checkBlockingProcess();             // stuck at time 18 with p2, 1, 4 in blocked queue
            if(!readyQueue.isEmpty()){                      //process first in ready Queue
                int roundRobin = 0;
                Process p = readyQueue.remove(0);       //check readyQueue
                String pN = p.getName();
                ArrayList<Integer> pageL = p.getPageList();  
                for(int i=0; i< timeQ;i++){   
                    if(!pageL.isEmpty()){           // if theres still page for execution
                        int pageExecute = pageL.get(0);
                        if(!processes.get(pN).contains(pageExecute)){                     // if page is not in main mem -> page fault
                            // System.out.println("page fault " + pageExecute+ " by " + pN + " at time " + time.get());
                            checkNumFrame(pN, pageExecute);
                            addPage(p, pN, pageExecute);            //swap the page needed in, takes 4 timeU
                            break;
                        }
                        else{           //execute the instruction
                            //System.out.println(pN + " Executed " + pageExecute + " at time " + time.get());
                            modidyPage(pN, pageExecute);
                            pageL.remove(0);
                            time.increment();
                            roundRobin++;
                            checkBlockingProcess();             // free blocked process first
                            if(pageL.isEmpty()){
                                processFinish(p);
                                break;
                            }
                            if(roundRobin==timeQ){                  // time quantum expired process after
                                roundRobin=0;
                                //System.out.println(p.getName() + " joined ready queue from time quantum at time " + time.get());
                                readyQueue.add(p);
                                break;
                            }
                        }
                    }
                    else{           // if not the process is completed
                        //System.out.println(pN +" finished at " + time.get());
                        freeFrame(pN);
                        processFinish(p);
                        break;
                    }
                }
            }
            else{
                time.increment();
            }
        }
        printResult();
    }
    
    public void addPage(Process p, String pName, int page){
        p.incrementFaultTime();
        p.getFaultList().add(time.get());
        p.setBlockingTime(time.get() + timeAddPage -1);
        processes.get(pName).add(page);
        blockedQueue.add(p);
    }
    public void modidyPage(String pageName, int page){          // remove the page, then add it again -> least recently used page will be the first in the list
        cleanPageL.remove(pageName + ": " + page);
        cleanPageL.add(pageName + ": " + page);
    }

    public void checkBlockingProcess(){
        Iterator<Process> iterator = blockedQueue.iterator();
        while(iterator.hasNext()){
            Process blockedProcess = iterator.next();
            if(blockedProcess.getBlockTime() <time.get()){
                //System.out.println(blockedProcess.getName() + " joined ready queue from blocked queue at time " + time.get());
                readyQueue.add(blockedProcess);
                iterator.remove();
            }
        }
    }

    public void checkNumFrame(String pageName, int page){
        if(frameCount.get() >0){            //if theres still page avail, get that page, allocate it
            cleanPageL.add(pageName + ": " + page);
            cleanPageL.remove(0);
            frameCount.decrement();
        }
        else if(frameCount.get() ==0){
            /*String pageDrop= */cleanPageL.remove(0);
            //System.out.println("Dropped page "+ pageDrop);
        }
    }

    public void freeFrame(String pName){
        Iterator<String> pCheck = cleanPageL.iterator();
        while (pCheck.hasNext()) {
            String nameCheck = pCheck.next();
            if(nameCheck.startsWith(pName)){
                pCheck.remove();
            }
        }
    }

    public void processFinish(Process p){
        // String pN = p.getName();
        // System.out.println(pN +" finished at " + time.get());
        processCountdown.decrement();    
        p.setTurnaroundTime(time.get());
    }

    public void printResult() {
        // Print header
        System.out.println(" \n" + "LRU - Variable-Global Replacement:");
        System.out.println("PID  Process Name      Turnaround Time  # Faults  Fault Times");
        
        // Iterate through the list of processes
        for (int i = 0; i < pList.size(); i++) {
            Process process = pList.get(i);
            int pid = i + 1;  // Assuming PID starts from 1
            
            // Format the output for each process
            System.out.printf("%-4d %-16s %-17d %-9d %s\n",
                              pid,
                              process.getName(),
                              process.getTurnaroundTime(),
                              process.getFaultTime(),
                              process.getFaultList().toString());
        }
    }
}