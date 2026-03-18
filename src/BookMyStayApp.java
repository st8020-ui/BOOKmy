import java.util.*;

/**
 * Project: Book My Stay
 * Use Case 6: Reservation Confirmation & Room Allocation
 * Goal: Assign rooms safely and prevent double-booking using Set and HashMap.
 * * @author YourName
 * @version 1.0
 */

class Room {
    private int roomNumber;
    private String category;
    private boolean isAvailable;

    public Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isAvailable = true;
    }

    public int getRoomNumber() { return roomNumber; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Room [" + roomNumber + " | " + category + " | Available: " + isAvailable + "]";
    }
}

class ReservationRequest {
    private String guestName;
    private int requestedRoomNumber;

    public ReservationRequest(String guestName, int requestedRoomNumber) {
        this.guestName = guestName;
        this.requestedRoomNumber = requestedRoomNumber;
    }

    public String getGuestName() { return guestName; }
    public int getRequestedRoomNumber() { return requestedRoomNumber; }

    @Override
    public String toString() {
        return "Request [Guest: " + guestName + " | Room: " + requestedRoomNumber + "]";
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // --- Setup (From Previous Use Cases) ---
        System.out.println("=== Book My Stay: Allocation System ===");

        List<Room> inventory = new ArrayList<>();
        inventory.add(new Room(101, "Standard"));
        inventory.add(new Room(102, "Deluxe"));
        inventory.add(new Room(201, "Suite"));

        Queue<ReservationRequest> bookingQueue = new LinkedList<>();
        bookingQueue.add(new ReservationRequest("Alice", 101));
        bookingQueue.add(new ReservationRequest("Bob", 102));
        bookingQueue.add(new ReservationRequest("Charlie", 101)); // Conflicts with Alice

        // --- Use Case 6: Allocation & Uniqueness Enforcement ---

        // Set to prevent reuse of Room IDs (Double Booking Prevention)
        Set<Integer> allocatedRooms = new HashSet<>();

        System.out.println("\n--- Processing Allocation Queue ---");

        while (!bookingQueue.isEmpty()) {
            // 1. Dequeue request (FIFO)
            ReservationRequest currentRequest = bookingQueue.poll();
            int roomNum = currentRequest.getRequestedRoomNumber();

            System.out.println("\nProcessing: " + currentRequest);

            // 2. Uniqueness Enforcement Check
            if (allocatedRooms.contains(roomNum)) {
                System.out.println(">> FAILED: Room " + roomNum + " is already allocated. Double-booking prevented!");
            } else {
                // 3. Find room in inventory and update status
                boolean found = false;
                for (Room r : inventory) {
                    if (r.getRoomNumber() == roomNum && r.isAvailable()) {
                        // 4. Atomic Logical Operation: Assignment + Update
                        r.setAvailable(false);
                        allocatedRooms.add(roomNum); // Record to prevent reuse
                        System.out.println(">> SUCCESS: Reservation confirmed for " + currentRequest.getGuestName());
                        found = true;
                        break;
                    }
                }
                if (!found) System.out.println(">> ERROR: Room not found or unavailable.");
            }
        }

        // --- Final Inventory Consistency Check ---
        System.out.println("\n--- Final Inventory State ---");
        for (Room r : inventory) {
            System.out.println(r);
        }
    }
}