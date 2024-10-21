/*
* COMP2240 - Operating Systems
* Assignment 3 - Sub
* @author  Minh Thai Nguyen - c3440776 
* @version 1.0
* 
* This file simalate variable allocation - global replacement
*/

import java.util.ArrayList;
import java.util.Iterator;

public class VariableGobal {
    private ArrayList<Integer> mainMem;
    private ArrayList<Process> pList;
    private Counter time, processCountdown;
    private int numF, timeQ;
    private ArrayList<Process> readyQueue, blockedQueue;
    private final int timeAddPage;
    
    public VariableGobal(ArrayList<Process> pList, int numFrames, int tQuantum){
        this.mainMem = new ArrayList<>(); // should not exceed numFrames
        this.pList = pList;
        this.numF = numFrames;
        this.timeQ = tQuantum;
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
                Process p = readyQueue.removeFirst();       //check readyQueue
                String pN = p.getName();
                ArrayList<Integer> pageL = p.getPageList();  
                for(int i=0; i< timeQ;i++){   
                    if(!pageL.isEmpty()){           // if theres still page for execution
                        int pageExecute = pageL.getFirst();
                        if(!mainMem.contains(pageExecute)){                     // if page is not in main mem -> page fault
                            // System.out.println("page fault " + pageExecute+ " by " + pN + " at time " + time.get());
                            addPage(p, pN, pageExecute);            //swap the page needed in, takes 4 timeU
                            break;
                        }
                        else{           //execute the instruction
                            //System.out.println(pN + " Executed " + pageExecute + " at time " + time.get());
                            modidyPage(p, pN, pageExecute);
                            pageL.removeFirst();
                            time.increment();
                            roundRobin++;
                            checkBlockingProcess();             // free blocked process first
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
                        processCountdown.decrement();    
                        p.setTurnaroundTime(time.get());
                        i=3;
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
        if(mainMem.size() == numF){
            //System.out.println("current number of frames of "+pName+": " + mainMem.get(pName).size());
            mainMem.removeFirst();
            //System.out.println(pageRemoved + " removed from " + pName );
        }
        mainMem.add(page);
        blockedQueue.add(p);
    }
    public void modidyPage(Process p, String pName, int page){          // remove the page, then add it again -> least recently used page will be the first in the list
        mainMem.remove(Integer.valueOf(page));
        mainMem.add(page);
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

    public void printResult() {
        // Print header
        System.out.println("LRU - Variable-Global Replacement:");
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

        System.out.println("------------------------------------------------------------");
    }
}