import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class P1 {
    static String fileName;
    public static void main(String[] args) {
        fileName = args[0];
        P1 p1 = new P1();
        List<MAC> threadList = p1.Input();
        Semaphore lock = new Semaphore(1);
        Counter ctr1 = new Counter();
        Counter ctr2 = new Counter();

        int threadCount = threadList.size();
        for(int i=0; i<threadCount;i++){
            Thread1 thread1 = new Thread1(threadList.get(i), lock, ctr1, ctr2);
            thread1.start();
        }
    }
    
    public List<MAC> Input(){
        ArrayList<Integer> threadList = new ArrayList<>();
        String linecheck="";
        int CSR1=-1, CSR2=-1, ED1=-1, ED2=-1, N=-1;
        try{
            File fileRead = new File(fileName);
            Scanner reader = new Scanner(fileRead);
            while(reader.hasNext()){
                linecheck= reader.next();
                if(linecheck.startsWith("CSR1=")){
                    CSR1 = Integer.parseInt(linecheck.substring(5, linecheck.length()-1));
                    threadList.add(CSR1);
                }
                if(linecheck.startsWith("CSR2=")){
                    CSR2 = Integer.parseInt(linecheck.substring(5, linecheck.length()-1));
                    threadList.add(CSR2);
                }
                if(linecheck.startsWith("ED1=")){
                    ED1 = Integer.parseInt(linecheck.substring(4, linecheck.length()-1));
                    threadList.add(ED1);
                }
                if(linecheck.startsWith("ED2=")){
                    ED2 = Integer.parseInt(linecheck.substring(4, linecheck.length()-1));
                    threadList.add(ED2);
                }
                if(linecheck.startsWith("N=")){
                    N = Integer.parseInt(linecheck.substring(2));   
                    break;                          //break here since this is the last thing thats needed to be read
                }
                
            }
            reader.close();
            if(threadList.size()!=4){
                System.out.println("input was not recorded.");
                return (new ArrayList<>());
            }
            
           
        }
        catch(Exception e)
        {
            System.out.println("Somethings wrong:D");
        }

        return getThreadsList(threadList, N);
    }

    public List<MAC> getThreadsList(ArrayList<Integer> macList, int N){
        List<MAC> threadQueue = new ArrayList<>();


        int n=1;

        for(int i=0; i<macList.size(); i++){
            int num =macList.get(i);
            while (num>0) {
                String macName = "MAC-" + n;
                MAC m = new MAC(macName);
                m.setCrossIntersection(N);
                threadQueue.add(m);
                if(i<2){
                    if(i==0){
                        m.setDestination("ED1");}
                    if(i==1){
                        m.setDestination("ED2");}
                    m.setState("CSR");
                }
                else{
                    if(i==2){
                        m.setDestination("CSR1");}    
                    if(i==3){
                        m.setDestination("CSR2");}
                    m.setState("ED");
                }
                num--;
                n++;
            }
        }
        return threadQueue;
    }

}
