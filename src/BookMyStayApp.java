import java.util.*;

/**
 * Project: Book My Stay
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * Goal: Use a Stack (LIFO) to manage state reversal and restore inventory.
 * * @version 1.0
 */

// --- CUSTOM EXCEPTION ---
class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) { super(message); }
}

// --- ENTITIES ---

class Room {
    private int roomNumber;
    private boolean isAvailable;
    public Room(int roomNumber) { this.roomNumber = roomNumber; this.isAvailable = true; }
    public int getRoomNumber() { return roomNumber; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    @Override
    public String toString() { return "Room " + roomNumber + " [Available: " + isAvailable + "]"; }
}

class ConfirmedBooking {
    private String guestName;
    private int roomNumber;
    public ConfirmedBooking(String guestName, int roomNumber) { this.guestName = guestName; this.roomNumber = roomNumber; }
    public String getGuestName() { return guestName; }
    public int getRoomNumber() { return roomNumber; }
}

public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("=== BOOK MY STAY: FULL LIFECYCLE (UC 10) ===\n");

        // 1. Setup Environment
        List<Room> inventory = new ArrayList<>(Arrays.asList(new Room(101), new Room(102)));
        Map<String, ConfirmedBooking> activeBookings = new HashMap<>();
        Stack<Integer> rollbackStack = new Stack<>(); // The LIFO Rollback Structure

        // 2. Simulate a Booking (Alice takes Room 101)
        System.out.println("--- Action: Booking ---");
        activeBookings.put("Alice", new ConfirmedBooking("Alice", 101));
        inventory.get(0).setAvailable(false);
        System.out.println("Alice booked Room 101.");

        // 3. Use Case 10: Cancellation & Rollback
        System.out.println("\n--- Action: Cancellation (Use Case 10) ---");
        try {
            cancelBooking("Alice", activeBookings, inventory, rollbackStack);
        } catch (InvalidOperationException e) {
            System.err.println("Cancellation Failed: " + e.getMessage());
        }

        // 4. Verification
        System.out.println("\n--- Final System State ---");
        System.out.println("Rollback Stack (Recently Released): " + rollbackStack);
        inventory.forEach(System.out::println);
    }

    /**
     * Use Case 10 Logic: Controlled State Reversal
     */
    private static void cancelBooking(String guestName,
                                      Map<String, ConfirmedBooking> activeBookings,
                                      List<Room> inventory,
                                      Stack<Integer> rollbackStack) throws InvalidOperationException {

        // 1. Validate Existence
        if (!activeBookings.containsKey(guestName)) {
            throw new InvalidOperationException("No active booking found for guest: " + guestName);
        }

        // 2. Retrieve Booking Data
        ConfirmedBooking booking = activeBookings.remove(guestName);
        int roomToRelease = booking.getRoomNumber();

        // 3. Inventory Restoration (Increment/Set Available)
        for (Room r : inventory) {
            if (r.getRoomNumber() == roomToRelease) {
                r.setAvailable(true);
                break;
            }
        }

        // 4. Push to Rollback Structure (LIFO)
        rollbackStack.push(roomToRelease);

        System.out.println("SUCCESS: Inventory rolled back. Room " + roomToRelease + " is now free.");
    }
}