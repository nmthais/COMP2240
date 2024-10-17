/*
 * COMP2240 - Operating Systems
 * Assignment 2 - Answers
 * @author  Jimmy Nguyen - c3440776
 * Last edit: 25/9/2024
 * 
 * This is the file for object counter, keeping track of numbers
 */

public class Counter {
    private int i;

    Counter(){
        this.i = 0;
    }
    Counter(int number){
        this.i = number;
    }

    public int get() {
        return i;
    }

    public void increment(){
        i++;
    }

    public void decrement(){
        i--;
    }

    public void addNumber(int number){
        i += number;
    }

    
}
