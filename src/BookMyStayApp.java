import java.util.*;

/**
 * Project: Book My Stay
 * Use Case 9: Error Handling & Validation
 * Goal: Use Custom Exceptions and "Fail-Fast" validation to protect system state.
 * * @author YourName
 * @version 1.0
 */

// --- CUSTOM EXCEPTION ---
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// --- ENTITIES (Room, Service, Request, Record) ---

class AddOnService {
    private String name;
    private double price;
    public AddOnService(String name, double price) { this.name = name; this.price = price; }
    public double getPrice() { return price; }
    @Override
    public String toString() { return name + " ($" + price + ")"; }
}

class Room {
    private int roomNumber;
    private String category;
    private boolean isAvailable;
    public Room(int roomNumber, String category) { this.roomNumber = roomNumber; this.category = category; this.isAvailable = true; }
    public int getRoomNumber() { return roomNumber; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}

class ConfirmedBooking {
    private String guestName;
    private int roomNumber;
    private double totalBill;
    public ConfirmedBooking(String guestName, int roomNumber, double totalBill) {
        this.guestName = guestName; this.roomNumber = roomNumber; this.totalBill = totalBill;
    }
    @Override
    public String toString() { return String.format("[Archive] %s stayed in Room %d (Paid: $%.2f)", guestName, roomNumber, totalBill); }
}

class ReservationRequest {
    private String guestName;
    private int requestedRoomNumber;
    public ReservationRequest(String guestName, int requestedRoomNumber) { this.guestName = guestName; this.requestedRoomNumber = requestedRoomNumber; }
    public String getGuestName() { return guestName; }
    public int getRequestedRoomNumber() { return requestedRoomNumber; }
}

// --- MAIN APPLICATION ---

public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("=== BOOK MY STAY: STABLE VERSION (UC 9) ===\n");

        // 1. Setup Data
        List<Room> inventory = new ArrayList<>();
        inventory.add(new Room(101, "Standard"));
        inventory.add(new Room(102, "Deluxe"));

        Queue<ReservationRequest> bookingQueue = new LinkedList<>();
        bookingQueue.add(new ReservationRequest("Alice", 101));
        bookingQueue.add(new ReservationRequest("Bob", 999)); // INVALID ROOM NUMBER!
        bookingQueue.add(new ReservationRequest("Charlie", 102));

        List<ConfirmedBooking> history = new ArrayList<>();
        Set<Integer> allocatedRooms = new HashSet<>();

        // 2. Processing with Error Handling
        System.out.println("--- Starting Allocation Engine ---");

        while (!bookingQueue.isEmpty()) {
            ReservationRequest req = bookingQueue.poll();

            try {
                // FAIL-FAST VALIDATION
                validateRequest(req, inventory, allocatedRooms);

                // If validation passes, proceed to allocation
                allocatedRooms.add(req.getRequestedRoomNumber());
                history.add(new ConfirmedBooking(req.getGuestName(), req.getRequestedRoomNumber(), 0.0));
                System.out.println("SUCCESS: Confirmed " + req.getGuestName());

            } catch (InvalidBookingException e) {
                // Graceful Failure Handling
                System.err.println("VALIDATION ERROR: " + e.getMessage() + " [Guest: " + req.getGuestName() + "]");
            }
        }

        // 3. Final Report
        System.out.println("\n--- Final Operational History ---");
        history.forEach(System.out::println);
    }

    /**
     * Use Case 9 Logic: Guarding the System State.
     * Detects errors before any data structure mutation occurs.
     */
    private static void validateRequest(ReservationRequest req, List<Room> inventory, Set<Integer> allocated) throws InvalidBookingException {
        // Rule 1: Room must exist in inventory
        boolean exists = inventory.stream().anyMatch(r -> r.getRoomNumber() == req.getRequestedRoomNumber());
        if (!exists) {
            throw new InvalidBookingException("Room " + req.getRequestedRoomNumber() + " does not exist in inventory.");
        }

        // Rule 2: Prevent Double Booking (Re-checking Set)
        if (allocated.contains(req.getRequestedRoomNumber())) {
            throw new InvalidBookingException("Room " + req.getRequestedRoomNumber() + " is already occupied.");
        }
    }
}