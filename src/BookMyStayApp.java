/**
 * Book My Stay App - UC4
 *
 * Room Search & Availability Check
 *
 * Demonstrates:
 * - Read-only access to centralized inventory
 * - Separation of concerns (search logic vs inventory updates)
 * - Defensive programming to filter unavailable rooms
 * - Using Room objects for descriptive info
 *
 * Author: YourName
 * Version: 1.0
 */

import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory (UC3 structure)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 0); // No suites available

        // Initialize room objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Display available rooms
        System.out.println("----- Available Rooms -----");
        searchService.displayAvailableRoom(singleRoom);
        searchService.displayAvailableRoom(doubleRoom);
        searchService.displayAvailableRoom(suiteRoom);
        System.out.println("---------------------------");
    }
}

// Room search service (read-only access)
class RoomSearchService {

    private final RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Display room details only if available
    public void displayAvailableRoom(Room room) {
        String roomType = room.getTypeName(); // User-friendly name
        int available = inventory.getAvailability(roomType);
        if (available > 0) {
            System.out.println(room.getDetails() + " | Available: " + available);
        }
    }
}

// Abstract Room class
abstract class Room {
    protected String typeName; // e.g., "Single Room"
    protected int beds;
    protected int size; // in sqm
    protected double price;

    public String getTypeName() {
        return typeName;
    }

    public abstract String getDetails();
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        this.typeName = "Single Room";
        this.beds = 1;
        this.size = 15;
        this.price = 50.0;
    }

    @Override
    public String getDetails() {
        return typeName + " | Beds: " + beds + " | Size: " + size + " sqm | Price: $" + price;
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        this.typeName = "Double Room";
        this.beds = 2;
        this.size = 25;
        this.price = 80.0;
    }

    @Override
    public String getDetails() {
        return typeName + " | Beds: " + beds + " | Size: " + size + " sqm | Price: $" + price;
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        this.typeName = "Suite Room";
        this.beds = 3;
        this.size = 50;
        this.price = 150.0;
    }

    @Override
    public String getDetails() {
        return typeName + " | Beds: " + beds + " | Size: " + size + " sqm | Price: $" + price;
    }
}

// Centralized inventory management
class RoomInventory {
    private final Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    // Updates inventory (used elsewhere, not by search)
    public void updateAvailability(String roomType, int change) {
        if (roomAvailability.containsKey(roomType)) {
            int updated = roomAvailability.get(roomType) + change;
            if (updated < 0) updated = 0;
            roomAvailability.put(roomType, updated);
        }
    }
}