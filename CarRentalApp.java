import javax.swing.*;
	import java.io.*;
	import java.util.*;

	// Make Car implement Serializable for object serialization
	class Car implements Serializable {
	    private static final long serialVersionUID = 1L;

	    String model;
	    String number;
	    double ratePerDay;
	    boolean available;

	    Car(String model, String number, double ratePerDay) {
	        this.model = model;
	        this.number = number;
	        this.ratePerDay = ratePerDay;
	        this.available = true;
	    }

	    @Override
	    public String toString() {
	        return model + " (" + number + ") - ₹" + ratePerDay + "/day - " + (available ? "Available" : "Rented");
	    }
	}

	public class CarRentalApp {
	    static ArrayList<Car> cars = new ArrayList<>();

	    public static void main(String[] args) {
	        loadData();

	        while (true) {
	            String menu = """
	                    🚗 CAR RENTAL SYSTEM
	                    1. Add Car
	                    2. View Cars
	                    3. Rent Car
	                    4. Return Car
	                    5. Exit
	                    Enter your choice:
	                    """;

	            String choice = JOptionPane.showInputDialog(menu);
	            if (choice == null) break; // user cancelled

	            switch (choice) {
	                case "1" -> addCar();
	                case "2" -> viewCars();
	                case "3" -> rentCar();
	                case "4" -> returnCar();
	                case "5" -> {
	                    saveData();
	                    JOptionPane.showMessageDialog(null, "Data saved. Goodbye!");
	                    System.exit(0);
	                }
	                default -> JOptionPane.showMessageDialog(null, "Invalid choice!");
	            }
	        }
	    }

	    static void addCar() {
	        String model = JOptionPane.showInputDialog("Enter Car Model:");
	        if (model == null) return;
	        String number = JOptionPane.showInputDialog("Enter Car Number:");
	        if (number == null) return;
	        String rateStr = JOptionPane.showInputDialog("Enter Rate per Day:");
	        try {
	            double rate = Double.parseDouble(rateStr);
	            cars.add(new Car(model, number, rate));
	            JOptionPane.showMessageDialog(null, "Car added successfully!");
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null, "Invalid input!");
	        }
	    }

	    static void viewCars() {
	        if (cars.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "No cars available.");
	            return;
	        }
	        StringBuilder sb = new StringBuilder("Cars:\n");
	        for (int i = 0; i < cars.size(); i++) {
	            sb.append(i + 1).append(". ").append(cars.get(i)).append("\n");
	        }
	        JOptionPane.showMessageDialog(null, sb.toString());
	    }

	    static void rentCar() {
	        if (cars.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "No cars to rent.");
	            return;
	        }
	        String numStr = JOptionPane.showInputDialog("Enter car number to rent:");
	        if (numStr == null) return;
	        Car car = findCar(numStr);
	        if (car == null) {
	            JOptionPane.showMessageDialog(null, "Car not found!");
	            return;
	        }
	        if (!car.available) {
	            JOptionPane.showMessageDialog(null, "Car is already rented!");
	            return;
	        }
	        String daysStr = JOptionPane.showInputDialog("Enter number of rental days:");
	        try {
	            int days = Integer.parseInt(daysStr);
	            double total = days * car.ratePerDay;
	            car.available = false;
	            JOptionPane.showMessageDialog(null, "Car rented successfully!\nTotal: ₹" + total);
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null, "Invalid input!");
	        }
	    }

	    static void returnCar() {
	        String numStr = JOptionPane.showInputDialog("Enter car number to return:");
	        if (numStr == null) return;
	        Car car = findCar(numStr);
	        if (car == null) {
	            JOptionPane.showMessageDialog(null, "Car not found!");
	            return;
	        }
	        if (car.available) {
	            JOptionPane.showMessageDialog(null, "Car is already available!");
	            return;
	        }
	        car.available = true;
	        JOptionPane.showMessageDialog(null, "Car returned successfully!");
	    }

	    static Car findCar(String number) {
	        for (Car c : cars) {
	            if (c.number.equalsIgnoreCase(number)) return c;
	        }
	        return null;
	    }

	    static void saveData() {
	        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cars.dat"))) {
	            out.writeObject(cars);
	        } catch (Exception e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage());
	        }
	    }

	    @SuppressWarnings("unchecked")
	    static void loadData() {
	        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("cars.dat"))) {
	            cars = (ArrayList<Car>) in.readObject();
	        } catch (FileNotFoundException fnf) {
	            // first run - file not present is okay
	        } catch (Exception e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
	        }
	    }
	}