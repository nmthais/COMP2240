/*
* COMP2240 - Operating Systems
* Assignment 2 - Answers
* @author Jimmy Nguyen - c3440776
* Last edit: 27/9/2024
* 
* This is the file that takes input and stores the information, it also contains the main method.
*/

import java.util.*;
import java.io.*;

public class P2 {
    static String fileName; // Static variable to hold the filename
    
    public static void main(String[] args) throws InterruptedException {
        fileName = args[0]; // Takes filename as command line argument
        P2 p2 = new P2(); // Creates an instance of P2
        Queue<Client> clients = p2.Input(); // Calls Input method to read clients from file
        int noClient = clients.size(); // Number of clients in the queue
        Counter time = new Counter(); // Counter for time
        Counter nDispenserAvail = new Counter(); // Counter for available dispensers
        Counter noClientServed = new Counter(noClient); // Counter for number of clients served
        CoffeeMachine cm = new CoffeeMachine(nDispenserAvail); // Coffee machine with dispenser availability
        int i = 0; // Counter for tracking clients
        int highestTime = 0; // Variable to track the highest time

        // Loop through the clients queue
        while (!clients.isEmpty()) {
            Client c = clients.poll(); // Dequeue the next client
            Client nextC = clients.peek(); // Peek at the next client
            Thread2 thread2 = new Thread2(c, nextC, time, nDispenserAvail, cm, noClientServed); // Create new Thread2 instance
            thread2.start(); // Start the thread
            i++; // Increment client counter

            // Check if the next client is of a different type or if 3 clients have been processed
            if (nextC != null && (!nextC.getType().equals(c.getType()) || i == 3)) {
                thread2.join(); // Wait for thread to finish
                i = 0; // Reset client counter
            }
        }
    }

    // Method to read input and populate client queue
    public Queue<Client> Input() {
        Queue<Client> queue = new LinkedList<>(); // Queue to store clients
        
        try {
            File fileRead = new File(fileName); // Create a file object with the specified filename
            Scanner reader = new Scanner(fileRead); // Create a scanner to read from the file
            String lineCheck; // Variable to hold each line of input
            int noClient = 0; // Variable to hold number of clients
            
            // Read each line of the file
            while (reader.hasNext()) {
                lineCheck = reader.next(); // Read the next token from the file
                
                // Check if the token represents a client (starts with "H" or "C")
                if (lineCheck.startsWith("H") || lineCheck.startsWith("C")) {
                    Client client = new Client(lineCheck, Integer.parseInt(reader.next())); // Create a new Client object
                    queue.add(client); // Add client to the queue
                } else {
                    noClient = Integer.parseInt(lineCheck); // Parse the token as number of clients
                }
            }

            reader.close(); // Close the scanner
            
            // Check if the number of clients in the queue matches the expected number
            if (queue.size() == noClient) {
                return queue; // Return the populated queue
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("Something is wrong :D"); // Handle file not found exception
        }

        return queue; // Return the queue (possibly empty)
    }
}
