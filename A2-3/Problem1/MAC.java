public class MAC {
    private String macNum, state, destination;
    private int trail, crossTime;
    
    public MAC(String name){
        this.macNum = name;
        this.trail =0;
        this.destination ="";
        this.state ="";
        this.crossTime =0;
    }

    public void setCrossIntersection(int crossIntersection){
        this.crossTime = crossIntersection;
    }

    public void setDestination(String source){
        this.destination = source;
        if(source.endsWith("1")){
            trail =1;
        }
        else{
            trail =2;
        }   
    }

    public void setState(String sentFrom){
        if(destination.startsWith("ED")){
            this.state = "Stock";
        }
        else{
            this.state = "Empty";
        }
    }


    public String getState(){
        return state;
    }

    public String getName(){
        return macNum;
    }
    
    public String getDestination(){
        return destination;
    }

    public int getTrail(){
        return trail;
    }
    
    public int getCrossTime(){
        return crossTime;
    }
    
}
