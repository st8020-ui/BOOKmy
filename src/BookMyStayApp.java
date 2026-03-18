/**
 * Book My Stay App - UC3
 *
 * Centralized Room Inventory Management
 *
 * Demonstrates:
 * - HashMap for centralized state
 * - Encapsulation of inventory logic
 * - Single source of truth for room availability
 * - Controlled updates and retrievals
 *
 * Author: YourName
 * Version: 1.0
 */

import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize centralized room inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        // Display initial availability
        System.out.println("----- Initial Room Inventory -----");
        inventory.displayInventory();

        // Update availability (simulate a booking)
        inventory.updateAvailability("Single Room", -1); // one single room booked
        inventory.updateAvailability("Suite Room", 1);   // one suite room added

        // Display updated inventory
        System.out.println("----- Updated Room Inventory -----");
        inventory.displayInventory();
    }
}

// Centralized inventory management class
class RoomInventory {

    // HashMap to store room type -> available count
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        this.roomAvailability = new HashMap<>();
    }

    // Add a new room type to inventory
    public void addRoomType(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    // Update availability for a given room type
    public void updateAvailability(String roomType, int change) {
        if (roomAvailability.containsKey(roomType)) {
            int current = roomAvailability.get(roomType);
            int updated = current + change;
            if (updated < 0) {
                System.out.println("Cannot reduce below zero for " + roomType);
                return;
            }
            roomAvailability.put(roomType, updated);
        } else {
            System.out.println("Room type " + roomType + " does not exist.");
        }
    }

    // Retrieve current availability for a room type
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    // Display the entire inventory
    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}