import java.util.*;

/**
 * Project: Book My Stay
 * Use Case 8: Booking History & Reporting
 * Goal: Use List to provide a chronological audit trail and generate reports.
 * * @author YourName
 * @version 1.0
 */

// --- Entity: Service (Use Case 7) ---
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
    public String toString() { return name + " ($" + price + ")"; }
}

// --- Entity: Room (Use Case 2/4/6) ---
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
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    @Override
    public String toString() { return "Room [" + roomNumber + " | " + category + "]"; }
}

// --- Entity: Final Reservation Record (Use Case 8) ---
class ConfirmedBooking {
    private String guestName;
    private int roomNumber;
    private List<AddOnService> services;
    private double totalBill;

    public ConfirmedBooking(String guestName, int roomNumber, List<AddOnService> services, double totalBill) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.services = services;
        this.totalBill = totalBill;
    }

    @Override
    public String toString() {
        return String.format("History Record: [Guest: %-8s | Room: %d | Bill: $%.2f | Services: %d]",
                guestName, roomNumber, totalBill, services.size());
    }
}

// --- Entity: Request (Use Case 5) ---
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

        // 1. Initial Setup
        System.out.println("=== BOOK MY STAY: FINAL VERSION (UC 8) ===\n");

        List<Room> inventory = new ArrayList<>();
        inventory.add(new Room(101, "Standard"));
        inventory.add(new Room(102, "Deluxe"));

        Queue<ReservationRequest> bookingQueue = new LinkedList<>();
        bookingQueue.add(new ReservationRequest("Alice", 101));
        bookingQueue.add(new ReservationRequest("Bob", 102));

        // Use Case 8: The Historical List (Persistence Mindset)
        List<ConfirmedBooking> bookingHistory = new ArrayList<>();

        // Service Catalog (Use Case 7)
        AddOnService breakfast = new AddOnService("Breakfast", 20.0);
        AddOnService spa = new AddOnService("Spa", 50.0);

        // 2. Processing and Persistence Logic
        System.out.println("--- Processing Active Bookings ---");
        Set<Integer> allocatedRooms = new HashSet<>();

        while (!bookingQueue.isEmpty()) {
            ReservationRequest req = bookingQueue.poll();
            int roomNum = req.getRequestedRoomNumber();

            if (!allocatedRooms.contains(roomNum)) {
                allocatedRooms.add(roomNum);

                // Add default services for demo
                List<AddOnService> guestServices = new ArrayList<>();
                if (req.getGuestName().equals("Alice")) guestServices.add(breakfast);
                if (req.getGuestName().equals("Bob")) guestServices.add(spa);

                double total = guestServices.stream().mapToDouble(AddOnService::getPrice).sum();

                // SAVE TO HISTORY (Crucial Step for Use Case 8)
                ConfirmedBooking record = new ConfirmedBooking(req.getGuestName(), roomNum, guestServices, total);
                bookingHistory.add(record);

                System.out.println("Successfully Confirmed & Archived: " + req.getGuestName());
            }
        }

        // 3. Reporting Service
        generateAdminReport(bookingHistory);
    }

    private static void generateAdminReport(List<ConfirmedBooking> history) {
        System.out.println("\n--- ADMIN OPERATIONAL REPORT ---");
        System.out.println("Total Bookings Processed: " + history.size());

        double totalRevenue = 0;
        for (ConfirmedBooking record : history) {
            System.out.println(record);
            // In a real app, revenue would include room price + services
        }
        System.out.println("\nReport Status: SUCCESS (Audit Trail Maintained)");
    }
}