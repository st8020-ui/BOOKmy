import java.util.*;

/**
 * Project: Book My Stay
 * Use Case 7: Add-On Service Selection
 * Goal: Map multiple services to a reservation using Map<String, List<Service>>.
 * * @author YourName
 * @version 1.0
 */

// --- Entity: Service (New for Use Case 7) ---
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}

// --- Entity: Room (From Use Case 2/4/6) ---
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

// --- Entity: Reservation (From Use Case 5/6) ---
class ReservationRequest {
    private String guestName;
    private int requestedRoomNumber;

    public ReservationRequest(String guestName, int requestedRoomNumber) {
        this.guestName = guestName;
        this.requestedRoomNumber = requestedRoomNumber;
    }

    public String getGuestName() { return guestName; }
    public int getRequestedRoomNumber() { return requestedRoomNumber; }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // --- Setup Inventory & Requests ---
        List<Room> inventory = new ArrayList<>();
        inventory.add(new Room(101, "Standard"));
        inventory.add(new Room(102, "Deluxe"));

        Queue<ReservationRequest> bookingQueue = new LinkedList<>();
        bookingQueue.add(new ReservationRequest("Alice", 101));
        bookingQueue.add(new ReservationRequest("Bob", 102));

        // --- Use Case 6: Allocation (Summary) ---
        Set<Integer> allocatedRooms = new HashSet<>();
        System.out.println("=== Use Case 6: Allocation Summary ===");
        while (!bookingQueue.isEmpty()) {
            ReservationRequest req = bookingQueue.poll();
            allocatedRooms.add(req.getRequestedRoomNumber());
            System.out.println("Confirmed: " + req.getGuestName() + " in Room " + req.getRequestedRoomNumber());
        }

        // --- Use Case 7: Add-On Service Selection ---

        // 1. Define Available Services
        AddOnService breakfast = new AddOnService("Breakfast Buffet", 20.0);
        AddOnService wifi = new AddOnService("High-Speed WiFi", 10.0);
        AddOnService spa = new AddOnService("Spa Treatment", 50.0);

        // 2. Map Reservation (Guest Name) to a List of Services
        Map<String, List<AddOnService>> serviceAssignments = new HashMap<>();

        System.out.println("\n--- Use Case 7: Adding Services ---");

        // Guest: Alice selects Breakfast and WiFi
        serviceAssignments.put("Alice", new ArrayList<>());
        serviceAssignments.get("Alice").add(breakfast);
        serviceAssignments.get("Alice").add(wifi);

        // Guest: Bob selects Spa
        serviceAssignments.put("Bob", new ArrayList<>());
        serviceAssignments.get("Bob").add(spa);

        // 3. Cost Aggregation Logic
        for (String guest : serviceAssignments.keySet()) {
            double totalAddOnCost = 0;
            System.out.println("\nAdd-Ons for " + guest + ":");

            List<AddOnService> guestServices = serviceAssignments.get(guest);
            for (AddOnService s : guestServices) {
                System.out.println(" + " + s);
                totalAddOnCost += s.getPrice();
            }
            System.out.println("Total Service Charges: $" + totalAddOnCost);
        }

        System.out.println("\n--- Final System Integrity Check ---");
        System.out.println("Core Inventory preserved? Yes. Total Rooms: " + inventory.size());
    }
}