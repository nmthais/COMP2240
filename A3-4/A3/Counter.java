/*
* COMP2240 - Operating Systems
* Assignment 3 - Sub
* @author  Minh Thai Nguyen - c3440776 
* @version 1.0
* 
* This file define an object counter
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