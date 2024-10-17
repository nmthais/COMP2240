import java.util.*;
import java.io.*;
import java.lang.Integer;



public class A1 {
    static String fileName;
    String processId;
    int dISP, arrivalTime, serviceTime, noOfTickets, trTime, wTime, oAvgTTime, oAvgWTIMe, noOfProcess=0;
    //int currentTime, completedProcess;
    Scanner keyboard = new Scanner(System.in);
    Algorithm a = new Algorithm();
    ArrayList<Process> processes = new ArrayList<>();                                             //set up an array containing processes
    ArrayList<Integer> loteryInput = new ArrayList<>();

    BufferedReader inputReader;
    public static void main(String[] args){
        fileName = args[0];
        A1 a1 = new A1();
        a1.dispatcher();
    }

    public void dispatcher(){

        String lineCheck="";

        // take input of p1-pn, its PID, arrivalTime, serviceTime, noOfTickets
        // list the order and time that the process was loaded into the CPU, 
        // compute turnaround time tR, waiting time tW for every process, the average turnaround time  and average waiting time. 
        // The average values will be consolidated in a table

        // System.out.println("Enter file name: ");

        try{

            inputReader = new BufferedReader(new FileReader(fileName));

                while((lineCheck = inputReader.readLine()) != null){
                    if(lineCheck.startsWith("DISP")){
                        dISP = Integer.parseInt(lineCheck.split(":")[1].trim());                             //split the line to 2 parts, before and after the ':', take the latter part
                    }
                    if(lineCheck.startsWith("PID: ")){
                        processId = lineCheck.split(":")[1];
                    }
                    if(lineCheck.startsWith("ArrTime: ")){
                        arrivalTime = Integer.parseInt(lineCheck.split(":")[1].trim());
                    }
                    if(lineCheck.startsWith("SrvTime: ")){
                        serviceTime = Integer.parseInt(lineCheck.split(":")[1].trim());
                    }
                    if(lineCheck.startsWith("Tickets: ")){
                        noOfTickets = Integer.parseInt(lineCheck.split(":")[1].trim());
                    }
                    if(lineCheck.startsWith("END") && processId !=null && !lineCheck.startsWith("ENDRANDOM") ){
                        Process p = new Process();
                        p.setProcess(processId, arrivalTime, serviceTime, noOfTickets);
                        processes.add(p);  
                        noOfProcess++;
                    }
                    if(lineCheck.startsWith("BEGINRANDOM")){       
                        lineCheck = inputReader.readLine(); 
                        while(!lineCheck.startsWith("ENDRANDOM") && lineCheck.matches("\\d++")){
                            int loteryInt = Integer.parseInt(lineCheck);
                            loteryInput.add(loteryInt);
                            lineCheck = inputReader.readLine(); 
                        }
                    }
                    
                    // save these info into an arraylist, call function to execute algorithms
            }
            System.out.println(a.FCFS(copyProcesses(processes), dISP));
            System.out.println(a.SRT(copyProcesses(processes), dISP));
            System.out.println(a.FBV(copyProcesses(processes), dISP));
            System.out.println(a.LTR(copyProcesses(processes), loteryInput, dISP));
            System.out.println(a.Summary());

        }
        catch(Exception e)
        {
            System.out.println("Somethings wrong:D");
        }
    }

    public ArrayList<Process> copyProcesses(List<Process> original) {
        ArrayList<Process> copy = new ArrayList<>();
        for (Process p : original) {
            copy.add(new Process(p)); // Create a deep copy using the copy constructor
        }
        return copy;
    }
}

