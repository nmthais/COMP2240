import java.util.ArrayList;
import java.util.Comparator;

public class Algorithm {
    static double avgFCFSTTime, avgFCFSWTime, avgSRTTTime, avgSRTWTime, avgFBVTTime, avgFBVWTime, avgLTRTTime, avgLTRWTime;
   
    public String FCFS(ArrayList<Process> processes, int dISP){   
        String output;
        int finalTime, totalTTime=0, totalWTime=0;
        
        output = "FCFS:"+"\n";
        for(int i=0; i< processes.size(); i++){
            output += "T" + dISP + ":" + processes.get(i).getProcessId() + "\n";
            finalTime = dISP + processes.get(i).getServiceTime();
            dISP += processes.get(i).getServiceTime() + 1;                       // calculate next time where dispatcher switch to another process
            processes.get(i).setFTime(finalTime);               //set finalTime to complete calculation of turnaround time and wait time
        }

        for(int i=0; i<processes.size();i++){
            totalTTime +=processes.get(i).getTurnTime();
            totalWTime +=processes.get(i).getWaitTime();
        }
        avgFCFSTTime =(double)totalTTime / processes.size();
        avgFCFSWTime =(double)totalWTime / processes.size();

        output = output + String.format("%-10s %-16s %-15s%n","Process", "Turnaround Time", "Waiting Time");
        for(int i=0; i< processes.size(); i++){
            output = output + String.format("%-10s %-16d %-15d%n", processes.get(i).getProcessId(), processes.get(i).getTurnTime(), processes.get(i).getWaitTime());            
        } // return a string containing info
        return output;
    }


    public String SRT(ArrayList<Process> processes, int dISP){              // return a string containing info instead of print in this class
        String output;
        int completedProcess=0, totalTTime=0, totalWTime=0;
        ArrayList<Process> readyQueue = new ArrayList<>();
        ArrayList<Process> arrivedProcesses = new ArrayList<>();   
        Process currentProcess =null;
        int time=0, timeDisp; 
        
        //there should be a ready queue, prioritizing shorter remaining time
        // initiate completed process, simulate time runnning using while loop-> calculate remaining time
        output= "SRT:" + "\n";
        
        arrivedProcesses.addAll(processes);

        while(completedProcess < processes.size()){ 
            
            for(int i=0; i< arrivedProcesses.size(); i++)
            {
                if(time >= arrivedProcesses.get(i).getArrivalTime() && !readyQueue.contains(arrivedProcesses.get(i))){
                    readyQueue.add(arrivedProcesses.get(i));                     // add process in queue if they arrived
                    arrivedProcesses.remove(i);
                    i--;
                }
            }

            //sorting all the process in the queue
            readyQueue.sort(Comparator.comparingInt(Process::getRemainingTime));

            if(currentProcess ==null && !readyQueue.isEmpty())              //if there arent any process running, get the process from the index 0 of the queue
            {
                time+=dISP;
                currentProcess =readyQueue.get(0);
                readyQueue.remove(0);
                timeDisp = time;
                output += "T" + timeDisp + ":" + currentProcess.getProcessId() + "\n";
            }
            if(currentProcess !=null)                                       // run the process if there is one
            {
                // dispatch time instead of cal, service time
                //time++;
                int curRemainTime = currentProcess.getRemainingTime();
                for (int i=0; i< readyQueue.size();i++){                        //check if the current process has the shortest time remaining by comparing with all the other process in the readyqueue
                    if(Integer.compare(readyQueue.get(i).getRemainingTime(), curRemainTime) < 0)        //if no, swap to the one with STR in the ready queue
                    {
                        time+=dISP;
                        Process temp = currentProcess;
                        currentProcess =readyQueue.get(i);
                        readyQueue.remove(i);
                        readyQueue.add(temp);
                        timeDisp = time;
                        output += "T" + timeDisp + ":" + currentProcess.getProcessId() + "\n";
                    }
                }
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                if(currentProcess.getRemainingTime() ==0)
                {
                    currentProcess.setFTime(time + dISP);
                    currentProcess=null;
                    completedProcess++;
                }
            }
            time++;
        }    

        for(int i=0; i<processes.size();i++){
            totalTTime +=processes.get(i).getTurnTime();
            totalWTime +=processes.get(i).getWaitTime();
        }
        avgSRTTTime =(double)totalTTime / processes.size();
        avgSRTWTime =(double)totalWTime / processes.size();

        output = output + String.format("%-10s %-16s %-15s%n","Process", "Turnaround Time", "Waiting Time");
        for(int i=0; i< processes.size(); i++){
            output = output + String.format("%-10s %-16d %-15d%n", processes.get(i).getProcessId(), processes.get(i).getTurnTime(), processes.get(i).getWaitTime());            
        } // return a string containing info
        return output;
        // create an array saving the ftime, tatime, wtime
    }
        
    public String FBV(ArrayList<Process> processes, int dISP){
        String output="";
        int completedProcess=0, time=0, timeDisp=time, totalTTime=0, totalWTime=0;
        Process currentProcess =null;
        ArrayList<Process> topQueue = new ArrayList<>();
        ArrayList<Process> midQueue = new ArrayList<>();
        ArrayList<Process> botQueue = new ArrayList<>();
        ArrayList<Process> arrivedProcesses = new ArrayList<>();
        
        output="FBV: "+ "\n";

        arrivedProcesses.addAll(processes);
        
        while(completedProcess < processes.size()){
            
            for(int i=0; i< arrivedProcesses.size(); i++){
                if(time >= arrivedProcesses.get(i).getArrivalTime() && !topQueue.contains(arrivedProcesses.get(i))){
                    topQueue.add(arrivedProcesses.get(i));                     // add process in queue if they arrived
                    arrivedProcesses.remove(i);
                    i--;
                }
            }

            if(currentProcess ==null){
                if(!topQueue.isEmpty())
                    currentProcess = topQueue.get(0);
                else if(!midQueue.isEmpty())
                    currentProcess = midQueue.get(0);
                else if(!botQueue.isEmpty())
                    currentProcess = botQueue.get(0);

                if(currentProcess !=null){
                    time+= dISP;
                    timeDisp =time;
                    output += "T" + timeDisp + ":" + currentProcess.getProcessId() + "\n"; 
                }
            }
            if(currentProcess !=null){ // need to address when theres is current process but no process in topqueue
                int timeUnit=0;
                
                if(topQueue.contains(currentProcess)){
                    timeUnit = Math.min(2, currentProcess.getRemainingTime());
                } 
                else if(midQueue.contains(currentProcess)){
                    timeUnit = Math.min(4, currentProcess.getRemainingTime());
                } 
                else if(botQueue.contains(currentProcess)){
                    timeUnit = Math.min(4, currentProcess.getRemainingTime());
                    currentProcess.setBotQueueTime(currentProcess.getBotQueueTime() + timeUnit);
                }
                time+=timeUnit;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeUnit);

                if(currentProcess.getRemainingTime() <=0){
                    if(topQueue.contains(currentProcess))
                        topQueue.remove(0);
                    else if(midQueue.contains(currentProcess))
                        midQueue.remove(0);
                    else if(botQueue.contains(currentProcess))
                        botQueue.remove(0);
                    currentProcess.setFTime(time);
                    currentProcess=null;
                    completedProcess++;
                }
                else if(botQueue.contains(currentProcess) && currentProcess.getBotQueueTime() >= 16){
                    botQueue.remove(0);
                    currentProcess.setBotQueueTime(0);
                    topQueue.add(currentProcess);
                    currentProcess =null;
                }

                else{
                    if(topQueue.contains(currentProcess)){
                        topQueue.remove(0);
                        midQueue.add(currentProcess);
                    }
                    else if(midQueue.contains(currentProcess)){
                        midQueue.remove(0);
                        botQueue.add(currentProcess);
                    }
                    currentProcess =null;
                }
            }
            //time++;
        }
        for(int i=0; i<processes.size();i++){
            totalTTime +=processes.get(i).getTurnTime();
            totalWTime +=processes.get(i).getWaitTime();
        }
        avgFBVTTime =(double)totalTTime / processes.size();
        avgFBVWTime =(double)totalWTime / processes.size();

        output = output + String.format("%-10s %-16s %-15s%n","Process", "Turnaround Time", "Waiting Time");
        for(int i=0; i< processes.size(); i++){
            output = output + String.format("%-10s %-16d %-15d%n", processes.get(i).getProcessId(), processes.get(i).getTurnTime(), processes.get(i).getWaitTime());            
        } // return a string containing info

        return output;
        //save for average calc
    }

    public String LTR(ArrayList<Process> processes, ArrayList<Integer> loteryInput, int dISP){
        String output="LTR: \n";
        int winner, completedProcess=0, time=0, timeDisp=0, x=0, totalTTime=0, totalWTime=0;
        Process currentProcess =null;
        ArrayList<Process> readyQueue = new ArrayList<>();
        ArrayList<Process> arrivedProcesses = new ArrayList<>();

        arrivedProcesses.addAll(processes);
        
        while(completedProcess < processes.size()){
            for(int i=0; i< arrivedProcesses.size(); i++){
                if(time >= arrivedProcesses.get(i).getArrivalTime() && !readyQueue.contains(arrivedProcesses.get(i))){
                    readyQueue.add(arrivedProcesses.get(i));
                    arrivedProcesses.remove(i);
                    i--;
                }
            }

            int y=0, counter=0;
            winner = loteryInput.get(x);
            currentProcess= readyQueue.get(y);
            while(currentProcess !=null){
                counter += currentProcess.getNoOfTickets();
                if(counter > winner){
                    break;
                }
                y++;
                if(y >=readyQueue.size()){
                    y=0;
                }
                currentProcess =readyQueue.get(y);
            }
            readyQueue.remove(y);
            time += dISP;
            timeDisp =time;
            output += "T" + timeDisp + ":" + currentProcess.getProcessId() + "\n";
            int timeUnit = Math.min(3, currentProcess.getRemainingTime());
            currentProcess.setBotQueueTime(currentProcess.getBotQueueTime() + timeUnit);
            time+=timeUnit;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 3);
            if(currentProcess.getRemainingTime() <=0){
                currentProcess.setFTime(time);
                currentProcess=null;
                completedProcess++;
            }
            else{
                arrivedProcesses.add(currentProcess);
                currentProcess=null;
            }
            x++;
        }

        for(int i=0; i<processes.size();i++){
            totalTTime +=processes.get(i).getTurnTime();
            totalWTime +=processes.get(i).getWaitTime();
        }
        avgLTRTTime = (double)totalTTime / processes.size();
        avgLTRWTime = (double)totalWTime / processes.size();

        output = output + String.format("%-10s %-16s %-15s%n","Process", "Turnaround Time", "Waiting Time");
        for(int i=0; i< processes.size(); i++){
            output = output + String.format("%-10s %-16d %-15d%n", processes.get(i).getProcessId(), processes.get(i).getTurnTime(), processes.get(i).getWaitTime());            
        } // return a string containing info
    
        return output;
    }


    public String Summary() {
        String output = "Summary: \n";
        output += String.format("%-10s %-30s %-20s%n", "Algorithm", "Average Turnaround Time", "Waiting Time");
        output += String.format("%-10s %-30.2f %-20.2f%n", "FCFS", avgFCFSTTime, avgFCFSWTime);
        output += String.format("%-10s %-30.2f %-20.2f%n", "SRT", avgSRTTTime, avgSRTWTime);
        output += String.format("%-10s %-30.2f %-20.2f%n", "FBV", avgFBVTTime, avgFBVWTime);
        output += String.format("%-10s %-30.2f %-20.2f%n", "LTR", avgLTRTTime, avgLTRWTime);
        return output;
    }
}


