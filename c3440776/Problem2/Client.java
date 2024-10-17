/*
 * COMP2240 - Operating Systems
 * Assignment 2 - Answers
 * @author  Jimmy Nguyen - c3440776
 * Last edit: 25/9/2024
 * 
 * This is the file for object client
 */

public class Client {
    private String type, id;
    private int time;

    Client(String id, int time){
        this.id = id;
        this.time = time;
        if(id.startsWith("H")){
            type = "Hot";
        }
        else{
            type = "Cold";
        }
    }

    public String getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
