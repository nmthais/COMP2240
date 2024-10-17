public class Process {
    private String processId;
    private int arrivalTime, serviceTime, noOfTickets, remainingTime, turnTime, waitTime, finalTime, botQueueTime;

    public Process(){
        processId ="";
        arrivalTime =0;
        serviceTime =0;
        noOfTickets =0; 
    }

    public Process(Process other) {
        this.processId = other.processId;
        this.arrivalTime = other.arrivalTime;
        this.serviceTime = other.serviceTime;
        this.noOfTickets = other.noOfTickets;
        this.remainingTime = other.serviceTime;
        this.botQueueTime = 0;
    }

    public void setProcess(String processId, int arrTime, int srvTime, int noOfTickets){
        this.processId = processId;
        this.arrivalTime = arrTime;
        this.serviceTime = srvTime;
        this.noOfTickets = noOfTickets;
        this.remainingTime = srvTime;
    }
    public void setRemainingTime(int remainingTime){
        this.remainingTime = remainingTime;
    }
    public void setFTime(int fTime){
        this.finalTime = fTime;
        this.turnTime = fTime - arrivalTime;
        this.waitTime = turnTime - serviceTime;
    }
    public void setBotQueueTime(int queueTime){
        this.botQueueTime = queueTime;
    }

    public int getBotQueueTime(){
        return botQueueTime;
    }
    public int getFinalTime(){
        return finalTime;
    }
    public int getTurnTime(){
        return turnTime;
    }
    public int getWaitTime(){
        return waitTime;
    }
    public String getProcessId(){
        return processId;
    }
    public int getArrivalTime(){
        return arrivalTime;
    }
    public int getServiceTime(){
        return serviceTime;
    }
    public int getNoOfTickets(){
        return noOfTickets;
    }
    public int getRemainingTime(){
        return remainingTime;
    }

}
