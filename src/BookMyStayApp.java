/**
 * Book My Stay App - UC2
 *
 * Basic Room Types & Static Availability
 *
 * Demonstrates:
 * - Abstract classes
 * - Inheritance
 * - Polymorphism
 * - Encapsulation
 * - Static availability representation
 *
 * Author: YourName
 * Version: 1.0
 */
public class BookMyStayApp {

    // Static availability for each room type
    static int singleRoomAvailable = 5;
    static int doubleRoomAvailable = 3;
    static int suiteRoomAvailable = 2;

    public static void main(String[] args) {

        // Create room objects using polymorphism
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Display room details and availability
        System.out.println("----- Book My Stay App: Room Types & Availability -----");
        System.out.println(singleRoom.getDetails() + " | Available: " + singleRoomAvailable);
        System.out.println(doubleRoom.getDetails() + " | Available: " + doubleRoomAvailable);
        System.out.println(suiteRoom.getDetails() + " | Available: " + suiteRoomAvailable);
        System.out.println("------------------------------------------------------");
    }
}

// Abstract Room class
abstract class Room {
    protected int beds;
    protected int size; // in square meters
    protected double price; // per night

    // Abstract method to get room details
    public abstract String getDetails();
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        this.beds = 1;
        this.size = 15;
        this.price = 50.0;
    }

    @Override
    public String getDetails() {
        return "Single Room | Beds: " + beds + " | Size: " + size + " sqm | Price: $" + price;
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        this.beds = 2;
        this.size = 25;
        this.price = 80.0;
    }

    @Override
    public String getDetails() {
        return "Double Room | Beds: " + beds + " | Size: " + size + " sqm | Price: $" + price;
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        this.beds = 3;
        this.size = 50;
        this.price = 150.0;
    }

    @Override
    public String getDetails() {
        return "Suite Room | Beds: " + beds + " | Size: " + size + " sqm | Price: $" + price;
    }
}