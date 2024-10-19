/*
* COMP2240 - Operating Systems
* Assignment 3 - Sub
* @author  Minh Thai Nguyen - c3440776 
* @version 1.0
* 
* This file simalate fixed allocation - local replacement
*/
import java.util.*;

public class FixedLocal {
    private HashMap<String,ArrayList<Integer>> mainMem;
    private ArrayList<Process> pList;
    private Counter time, processComplete;
    private int numF, timeQ;
    private ArrayList<Process> readyQueue, blockedQueue;
    private final int timeAddPage;
    
    public FixedLocal(HashMap<String, ArrayList<Integer>> mainMem, ArrayList<Process> pList, int fixedNumF, int tQuantum){
        this.mainMem = mainMem;
        this.pList = pList;
        this.time = new Counter();
        this.processComplete = new Counter(pList.size());
        this.numF = fixedNumF;
        this.timeQ = tQuantum;
        this.readyQueue = new ArrayList<>();
        this.timeAddPage =4;
        blockedQueue = new ArrayList<>();
    }

    public void run(){
        for(int i=0; i<pList.size();i++){           //pageFault at time 0
            Process p = pList.get(i);
            mainMem.get(p.getName()).add(p.getPageList().get(0));              //add first page to main
            System.out.println("page fault " +p.getPageList().get(0)+ " by " + p.getName() + " at time " + time.get());
            p.incrementFaultTime();
            p.getFaultList().add(time.get());
            //p.addBlockTime(timeAddPage);
            readyQueue.add(p);                         //add to readyQueue
        }
        time.addNumber(timeAddPage);                  // add time for 4 processes
        
        while(processComplete.get()!=0){
            checkBlockingProcess();             // stuck at time 18 with p2, 1, 4 in blocked queue
            if(!readyQueue.isEmpty()){                      //process first in ready Queue
                int roundRobin = 0;
                Process p = readyQueue.removeFirst();       //check readyQueue
                String pN = p.getName();
                ArrayList<Integer> pageL = p.getPageList();  
                for(int i=0; i< timeQ;i++){       //when removeFirst(), cant finish cause i< pageL.size()   
                    if(!pageL.isEmpty()){
                        if(time.get() ==25){
                            System.out.println();
                        }
                        int pageExecute = pageL.getFirst();
                        if(!mainMem.get(pN).contains(pageExecute)){                     // if page is not in main mem -> page fault
                            System.out.println("page fault " + pageExecute+ " by " + pN + " at time " + time.get());
                            p.incrementFaultTime();
                            p.getFaultList().add(time.get());
                            p.setBlockingTime(time.get() + timeAddPage -1);
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
                    else{
                        //System.out.println(pN +" finished at " + time.get());
                        processComplete.decrement();
                        p.setTurnaroundTime(time.get());
                        i=3;
                        break;
                    }
                    //readyQueue.add(p);
                }
            }
            else{
                time.increment();
            }
        }
        printResult();
    }

    public void addPage(Process p, String pName, int page){
        if(mainMem.get(pName).size() == numF){
            //System.out.println("current number of frames of "+pName+": " + mainMem.get(pName).size());
            // keep track of least recently used by removing the page using and add it again when used
            /*int pageRemoved = */mainMem.get(pName).removeFirst();
            //System.out.println(pageRemoved + " removed from " + pName );
        }
        mainMem.get(pName).add(page);
        blockedQueue.add(p);
    }

    public void modidyPage(Process p, String pName, int page){          // remove the item, then add it again -> it will be the last item in the list
        mainMem.get(pName).remove(Integer.valueOf(page));
        mainMem.get(pName).add(page);
    }

    public void checkBlockingProcess(){
        Iterator<Process> iterator = blockedQueue.iterator();
        while(iterator.hasNext()){
            Process blockedProcess = iterator.next();
            //blockedProcess.decrementBlockTime();
            if(blockedProcess.getBlockTime() <time.get()){
                System.out.println(blockedProcess.getName() + " joined ready queue from blocked queue at time " + time.get());
                readyQueue.add(blockedProcess);
                iterator.remove();
            }
        }
    }

    public void printResult() {
        // Print header
        System.out.println("LRU - Fixed-Local Replacement:");
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
