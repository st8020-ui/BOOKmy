import java.util.*;

/**
 * Project: Book My Stay
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 * Goal: Use synchronization to prevent race conditions during simultaneous bookings.
 * * @version 1.1
 */

class Room {
    private int roomNumber;
    private boolean isAvailable = true;

    public Room(int roomNumber) { this.roomNumber = roomNumber; }
    public int getRoomNumber() { return roomNumber; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}

class BookingProcessor implements Runnable {
    private String guestName;
    private int requestedRoom;
    private static List<Room> inventory;
    private static Set<Integer> allocatedRooms = new HashSet<>();

    public BookingProcessor(String name, int room, List<Room> inv) {
        this.guestName = name;
        this.requestedRoom = room;
        inventory = inv;
    }

    @Override
    public void run() {
        processBooking();
    }

    /**
     * Use Case 11: The Critical Section
     * The 'synchronized' keyword ensures only one thread enters this block at a time.
     */
    private void processBooking() {
        System.out.println(guestName + " is attempting to book Room " + requestedRoom + "...");

        synchronized (inventory) { // Locking the shared resource
            boolean success = false;

            // Check if already allocated (Race condition protection)
            if (allocatedRooms.contains(requestedRoom)) {
                System.err.println(">>> CONCURRENCY ALERT: " + guestName + " failed. Room " + requestedRoom + " already taken.");
                return;
            }

            for (Room r : inventory) {
                if (r.getRoomNumber() == requestedRoom && r.isAvailable()) {
                    // Atomic-like update
                    r.setAvailable(false);
                    allocatedRooms.add(requestedRoom);
                    success = true;
                    break;
                }
            }

            if (success) {
                System.out.println(">>> SUCCESS: " + guestName + " secured Room " + requestedRoom);
            } else {
                System.out.println(">>> FAILED: Room " + requestedRoom + " is unavailable for " + guestName);
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== BOOK MY STAY: CONCURRENT SIMULATION ===\n");

        // 1. Setup Shared Inventory
        List<Room> sharedInventory = Collections.synchronizedList(new ArrayList<>());
        sharedInventory.add(new Room(101));
        sharedInventory.add(new Room(102));

        // 2. Simulate Simultaneous Guests for the SAME Room (101)
        // If thread safety fails, both might get the room!
        Thread t1 = new Thread(new BookingProcessor("Alice", 101, sharedInventory));
        Thread t2 = new Thread(new BookingProcessor("Bob", 101, sharedInventory));
        Thread t3 = new Thread(new BookingProcessor("Charlie", 102, sharedInventory));

        // 3. Start Threads (Parallel Execution)
        t1.start();
        t2.start();
        t3.start();

        // Wait for all to finish
        t1.join();
        t2.join();
        t3.join();

        System.out.println("\n--- Final System Audit ---");
        for (Room r : sharedInventory) {
            System.out.println("Room " + r.getRoomNumber() + " Status: " + (r.isAvailable() ? "Available" : "Occupied"));
        }
    }
}