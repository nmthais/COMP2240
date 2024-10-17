/*
 * COMP2240 - Operating Systems
 * Assignment 2 - Answers
 * @author  Jimmy Nguyen - c3440776
 * Last edit: 25/9/2024
 * 
 * This is the file is used to keep track of which number should be added to counter time
 */
public class TimeCalc{
    private int highestTime, lowestTime;
    TimeCalc(int time){
        this.highestTime =time;
        this.lowestTime =time;
    }
    
    public int getHighestTime() {
        return highestTime;
    }
    public int getLowestTime() {
        return lowestTime;
    }
    public void setHighestTime(int time) {
        if(highestTime < time){
            highestTime = time;    
        }
    }
    public void setLowestTime(int time) {
        if (lowestTime > time) {
            lowestTime = time;   
        }
    }
}