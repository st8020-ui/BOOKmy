import java.util.*;
import java.io.*;

/**
 * Project: Book My Stay
 * Use Case 12: Data Persistence & System Recovery
 * Goal: Use Serialization to save/load system state to a file (data.ser).
 * * @version 1.2
 */

// --- Entity: Room (Must be Serializable) ---
class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private int roomNumber;
    private boolean isAvailable;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.isAvailable = true;
    }
    public void setAvailable(boolean available) { isAvailable = available; }
    @Override
    public String toString() { return "Room " + roomNumber + " [" + (isAvailable ? "Free" : "Sold") + "]"; }
}

public class BookMyStayApp {
    private static final String FILE_NAME = "hotel_state.ser";
    private static List<Room> inventory = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== BOOK MY STAY: PERSISTENCE ENGINE ===\n");

        // 1. Attempt Recovery (Deserialization)
        if (loadState()) {
            System.out.println(">>> RECOVERY SUCCESSFUL: Previous state loaded.");
        } else {
            System.out.println(">>> NO PREVIOUS STATE FOUND: Initializing new inventory.");
            inventory.add(new Room(101));
            inventory.add(new Room(102));
        }

        // 2. Display Current State
        System.out.println("Current Inventory:");
        inventory.forEach(System.out::println);

        // 3. Simulate a change in state
        System.out.println("\n--- Action: Booking Room 101 ---");
        inventory.get(0).setAvailable(false);

        // 4. Persist State before shutdown (Serialization)
        saveState();
        System.out.println("\n>>> SYSTEM SHUTDOWN: State persisted to " + FILE_NAME);
        System.out.println("Run the app again to see Room 101 remain 'Sold'!");
    }

    /**
     * Use Case 12: Serialization (Save to Disk)
     */
    private static void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            System.err.println("Persistence Error: " + e.getMessage());
        }
    }

    /**
     * Use Case 12: Deserialization (Load from Disk)
     */
    @SuppressWarnings("unchecked")
    private static boolean loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            inventory = (List<Room>) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Recovery Error: " + e.getMessage());
            return false;
        }
    }
}