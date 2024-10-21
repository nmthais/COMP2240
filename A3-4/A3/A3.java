/*
* COMP2240 - Operating Systems
* Assignment 3 - Main
* @author  Minh Thai Nguyen - c3440776 
* @version 1.0
* 
* This file read inputs, files as agrs and call the methods to enter simulation
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class A3 {
    
    public static void main(String[] args) {
        A3 a3 = new A3();
        int numberFrames = Integer.parseInt(args[0]);
        int timeQuantum = Integer.parseInt(args[1]);
        ArrayList<Process> pListLocal = a3.Input(args);
        ArrayList<Process> pListGlobal = a3.Input(args);
        if(pListLocal !=null && pListGlobal!= null){
            final int fixedNumF =numberFrames/pListLocal.size();
            FixedLocal f = new FixedLocal(pListLocal, fixedNumF, timeQuantum);
            f.Local();
            VariableGobal v = new VariableGobal(pListGlobal, numberFrames, timeQuantum);
            v.Global();
        }
    }

    public ArrayList<Process> Input(String[] args){
        String pname="";
        int pageNum;
        ArrayList<Process> processList = new ArrayList<>();
        for(int i=2; i<args.length;i++){
            String fileName = args[i];
            ArrayList<Integer> pageList = new ArrayList<>();
            String lineCheck;
            try {
                File file = new File(fileName);
                Scanner reader = new Scanner(file);
                while(reader.hasNext()){
                    lineCheck = reader.nextLine();
                    if(lineCheck.startsWith("name")){
                        pname = lineCheck.substring(6,lineCheck.length()-1);
                        //System.out.println(pname);
                    }
                    else if(lineCheck.startsWith("page")){
                        pageNum = Integer.parseInt(lineCheck.substring(6, lineCheck.length()-1));
                        pageList.add(pageNum);
                        //System.out.println(pageNum);
                    }
                    else if(lineCheck.startsWith("end")){
                        Process p = new Process(pname, pageList);
                        processList.add(p);
                    }
                }
                reader.close(); 
            } 
            catch (FileNotFoundException e) {
                System.err.println("Something is wrong :D");
            }
        }
        return processList;
    }

}