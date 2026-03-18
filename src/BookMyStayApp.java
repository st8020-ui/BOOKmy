import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Project: Book My Stay
 * Use Case 5: Booking Request (First-Come-First-Served)
 * * This class serves as the entry point to demonstrate fair request handling
 * using the FIFO principle via a Queue data structure.
 * * @author YourName
 * @version 1.0
 */

// --- Entity: Room (From Use Case 2/4) ---
class Room {
    private int roomNumber;
    private String category;
    private boolean isAvailable;

    public Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "Room [" + roomNumber + " | " + category + " | Available: " + isAvailable + "]";
    }
}

// --- Entity: Reservation (From Use Case 5) ---
class ReservationRequest {
    private String guestName;
    private int requestedRoomNumber;

    public ReservationRequest(String guestName, int requestedRoomNumber) {
        this.guestName = guestName;
        this.requestedRoomNumber = requestedRoomNumber;
    }

    @Override
    public String toString() {
        return "Request [Guest: " + guestName + " | Room: " + requestedRoomNumber + "]";
    }
}

public class BookMyStayApp {

    /**
     * Main method to execute the application flow.
     * 1. Initialize Inventory
     * 2. Accept Booking Requests into a Queue
     */
    public static void main(String[] args) {

        // --- Use Case 1: Welcome Message ---
        System.out.println("=================================");
        System.out.println(" Welcome to Book My Stay ");
        System.out.println(" Hotel Booking System v1.0 ");
        System.out.println("=================================");

        // --- Use Case 2: Room Inventory Initialization ---
        // Using ArrayList to maintain real-time inventory consistency
        List<Room> inventory = new ArrayList<>();
        inventory.add(new Room(101, "Standard"));
        inventory.add(new Room(102, "Deluxe"));
        inventory.add(new Room(201, "Suite"));

        System.out.println("\n--- Current Room Inventory ---");
        for (Room r : inventory) {
            System.out.println(r);
        }

        // --- Use Case 5: Booking Request (FCFS) ---
        // Decoupling intake from allocation using a Queue (FIFO)
        Queue<ReservationRequest> bookingQueue = new LinkedList<>();

        System.out.println("\n--- Receiving Booking Requests ---");

        // Adding requests in arrival order (Alice -> Bob -> Charlie)
        bookingQueue.add(new ReservationRequest("Alice", 101));
        bookingQueue.add(new ReservationRequest("Bob", 102));
        bookingQueue.add(new ReservationRequest("Charlie", 101));

        // Displaying the queue to verify FIFO order
        System.out.println("Current Request Queue (Arrival Order):");
        for (ReservationRequest req : bookingQueue) {
            System.out.println(" >> " + req);
        }

        // Final Verification
        System.out.println("\nTotal Queued Requests: " + bookingQueue.size());
        System.out.println("Next Request to Process: " + bookingQueue.peek());

        System.out.println("\nApplication Terminated Successfully.");
    }
}