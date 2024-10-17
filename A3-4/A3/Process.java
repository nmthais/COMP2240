/*
* COMP2240 - Operating Systems
* Assignment 3 - Sub
* @author  Minh Thai Nguyen - c3440776 
* @version 1.0
* 
* This file read inputs, files as agrs and call the methods to enter simulation
*/

import java.util.ArrayList;

public class Process{
    private String name;
    private ArrayList<Integer> pageList;
    private int faultTime;
    private ArrayList<Integer> faultList;
    private int blockTime, turnaroundTime;

    public Process(String pname, ArrayList<Integer> pageList) {
        this.name = pname;
        this.pageList = pageList;
        this.faultTime=0;
        this.faultList = new ArrayList<>();
        this.blockTime=0;
        this.turnaroundTime=0;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void incrementFaultTime(){
        faultTime++;
    }

    public void setBlockingTime(int time){
        blockTime= time;
    }

    public void addBlockTime(int num){
        blockTime +=num;
    }

    public void incrementBlockTime(){
        blockTime++;
    }

    public void decrementBlockTime(){
        blockTime--;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getPageList() {
        return pageList;
    }
    
    public int getFaultTime() {
        return faultTime;
    }

    public ArrayList<Integer> getFaultList() {
        return faultList;
    }

    public int getBlockTime() {
        return blockTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }
}