package canteen;
import static canteen.Printer.*;

import canteen.exceptions.*;
import canteen.manager.CanteenManager;
import canteen.model.*;
import java.util.*;

public class Main {

    static CanteenManager manager = new CanteenManager();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        printWelcomeBanner();

        manager.registerStudent("202403052", "1234", "Jenish Devganiya", 5000);
        manager.registerStudent("202403053", "1234", "Vidur Dudhat", 5000);
        manager.registerStudent("202401058", "1234", "SAHIL GONDALIYA", 10000);
        manager.registerStudent("202401055", "1234", "Sumit Gauswami", 1000);
        manager.registerStudent("202401021", "1234", "Utsav Bechara", 5000);
        manager.registerStudent("202401201", "1234", "OM SHILOJA", 5200);

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1: studentMenu();   break;
                case 2: adminMenu();     break;
                case 3:
                    running = false;
                    System.out.println();
                    printLine();
                    manager.saveAll();
                    success("All data saved successfully!");
                    printLine();
                    System.out.println("  Thank you for using Canteen System!");
                    printLine();
                    break;
                default:
                    error("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // 
    //  WELCOME BANNER
    // 
    static void printWelcomeBanner() {
        System.out.println();
        printLine();
        System.out.println("       COLLEGE CANTEEN PRE-ORDER SYSTEM       ");
        System.out.println("         Fast Food. No Queue. Just Order.      ");
        printLine();
        System.out.println();
    }
    

    // 
    //  MAIN MENU
    // 
    static void printMainMenu() {
        System.out.println();
        printLine();
        System.out.println("             MAIN MENU                  ");
        printLine();
        System.out.println("  1." + " Student Panel");
        System.out.println("  2." + " Admin Panel");
        System.out.println("  3." + " Exit");
        printLine();
        System.out.print("  Enter choice: ");
    }

    // 
    //  STUDENT PANEL
    // 
    static void studentMenu() {
        System.out.println();
        System.out.print("  Enter your Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = manager.getStudent(studentId);

        if (student == null) {
            warning("Student ID not found!");
            System.out.print("  Register now? (yes/no): ");
            String ans = scanner.nextLine().trim();
            if (ans.equalsIgnoreCase("yes")) {
                System.out.print("  Enter name: ");
                String name = scanner.nextLine().trim();
                System.out.print("  Set a Password: ");
                String pass = scanner.nextLine().trim();
                System.out.print("  Enter initial wallet balance: Rs.");
                double bal = getDoubleInput();
                manager.registerStudent(studentId, pass, name, bal);
                success("Student registered successfully! Please log in.");
            }
            return;
        }

        System.out.print("  Enter Password: ");
        String pass = scanner.nextLine().trim();
        if (!student.getPassword().equals(pass)) {
            error("Incorrect password! Access denied.");
            return;
        }

        System.out.println();
        printLine();
        System.out.println("  Welcome, " + student.getName() + "!" +
                           "   " + "Wallet: Rs." +
                           String.format("%.2f", student.getWalletBalance()));
        printLine();

        boolean back = false;
        while (!back) {
            System.out.println();
            printLine();
            System.out.println("           STUDENT PANEL                ");
            printLine();
            System.out.println("  1." + " View Menu");
            System.out.println("  2." + " Place Order");
            System.out.println("  3." + " Track Order by Token");
            System.out.println("  4." + " Cancel Order");
            System.out.println("  5." + " View Order History");
            System.out.println("  6." + " Add Wallet Balance");
            System.out.println("  7." + " Reset Password");
            System.out.println("  8." + " Back to Main Menu");
            printLine();
            System.out.print("  Choice: ");
            int ch = getIntInput();

            switch (ch) {
                case 1: manager.displayMenu();               break;
                case 2: handlePlaceOrder(studentId);         break;
                case 3: handleTrackOrder();                  break;
                case 4: handleCancelOrder(studentId);        break;
                case 5: manager.viewOrderHistory(studentId); break;
                case 6: handleAddBalance(studentId);         break;
                case 7: handleResetPassword(student);        break;
                case 8: back = true;                         break;
                default: error("Invalid choice. Try again.");
            }
        }
    }

    private static void handleResetPassword(Student student) {
        System.out.println();
        System.out.print("  Enter current password: ");
        String currentPass = scanner.nextLine().trim();

        if (!currentPass.equals(student.getPassword())) {
            error("Incorrect current password! Reset failed.");
            return;
        }

        System.out.print("  Enter new password: ");
        String pass1 = scanner.nextLine().trim();
        System.out.print("  Confirm new password: ");
        String pass2 = scanner.nextLine().trim();

        if (pass1.isEmpty()) {
            error("Password cannot be empty.");
            return;
        }

        if (pass1.equals(student.getPassword())) {
            error("New password cannot be the same as the current password.");
            return;
        }

        if (pass1.equals(pass2)) {
            student.setPassword(pass1);
            success("Password updated successfully!");
        } else {
            error("Passwords do not match! Reset failed.");
        }
    }

    // 
    //  ADMIN PANEL
    // 
    static void adminMenu() {
        System.out.println();
        System.out.print("  Enter Admin Password: ");
        String pass = scanner.nextLine().trim();

        if (!pass.equals("admin123")) {
            error("Wrong password! Access denied.");
            return;
        }

        success("Admin access granted!");

        boolean back = false;
        while (!back) {
            System.out.println();
            printLine();
            System.out.println("            ADMIN PANEL                 ");
            printLine();
            System.out.println("  1." + " View All Orders");
            System.out.println("  2." + " Update Order Status");
            System.out.println("  3." + " Add Menu Item");
            System.out.println("  4." + " Remove Menu Item");
            System.out.println("  5." + " View Waitlist");
            System.out.println("  6." + " Generate Daily Report");
            System.out.println("  7." + " Back to Main Menu");
            printLine();
            System.out.print("  Choice: ");
            int ch = getIntInput();

            switch (ch) {
                case 1: manager.viewAllOrders();  break;
                case 2: handleUpdateStatus();     break;
                case 3: handleAddMenuItem();      break;
                case 4: handleRemoveMenuItem();   break;
                case 5: manager.viewWaitlist();   break;
                case 6: manager.generateReport(); break;
                case 7: back = true;              break;
                default: error("Invalid choice. Try again.");
            }
        }
    }

    // 
    //  PLACE ORDER
    // 
    static void handlePlaceOrder(String studentId) {
        try {
            manager.displayMenu();
            manager.displaySlots();

            ArrayList<FoodItem> selectedItems = new ArrayList<>();

            System.out.println();
            info("Enter item names one by one. Type 'done' when finished.");
            System.out.println();

            while (true) {
                System.out.print("  Item name: ");
                String itemName = scanner.nextLine().trim();
                if (itemName.equalsIgnoreCase("done")) break;

                try {
                    FoodItem item = manager.findItem(itemName);
                    System.out.print("  Quantity for " +
                                     item.getName() + ": ");
                    int quantity = getIntInput();

                    if (quantity <= 0) {
                        warning("Quantity must be at least 1.");
                        continue;
                    }

                    for (int i = 0; i < quantity; i++) {
                        selectedItems.add(item);
                    }

                    double totalItemPrice = item.getPrice() * quantity;
                    success("Added: " + quantity + " x " + item.getName() +
                            "    Rs." + String.format("%.2f", totalItemPrice));

                } catch (ItemNotFoundException e) {
                    error(e.getMessage() + " Try again.");
                }
            }

            if (selectedItems.isEmpty()) {
                warning("No items selected. Order cancelled.");
                return;
            }

            //  Slot selection: user picks a number, not types the time 
            String slot = null;
            while (slot == null) {
                System.out.print("  Select slot number: ");
                int slotChoice = getIntInput();
                slot = manager.getSlotByNumber(slotChoice);
                if (slot == null) {
                    error("Invalid slot number. Please choose from the list above.");
                } else if (manager.getSlotSeats(slot) <= 0) {
                    warning("That slot is FULL. Please choose another.");
                    slot = null;
                }
            }

            //  Order Confirmation 
            double total = 0;
            for (FoodItem item : selectedItems) total += item.getPrice();
            if (total > 100) total = total - (total * 0.05); // 5% discount

            Student student = manager.getStudent(studentId);
            System.out.println();
            printLine();
            System.out.println("  Order Summary:");
            System.out.println("  Total Amount : Rs." + String.format("%.2f", total));
            System.out.println("  Current Bal  : Rs." + String.format("%.2f", student.getWalletBalance()));
            System.out.println("  Remaining Bal: Rs." + String.format("%.2f", student.getWalletBalance() - total));
            printLine();
            System.out.print("  Confirm order? (yes/no): ");
            
            String confirm = scanner.nextLine().trim();
            if (!confirm.equalsIgnoreCase("yes")) {
                warning("Order cancelled by user.");
                return;
            }

            manager.placeOrder(studentId, selectedItems, slot);
        } catch (InsufficientBalanceException e) {
            error("Payment failed: " + e.getMessage());
        } catch (SlotFullException e) {
            warning(e.getMessage());
        } catch (InvalidInputException e) {
            error("Invalid input: " + e.getMessage());
        } catch (ItemNotFoundException e) {
            error(e.getMessage());
        }
    }

    // 
    //  TRACK ORDER
    // 
    static void handleTrackOrder() {
        try {
            System.out.print("  Enter Token Number: ");
            int token = getIntInput();
            manager.trackOrder(token);
        } catch (OrderNotFoundException e) {
            error(e.getMessage());
        }
    }

    // 
    //  CANCEL ORDER
    // 
    static void handleCancelOrder(String studentId) {
        try {
            System.out.print("  Enter Order ID to cancel: ");
            int orderId = getIntInput();
            manager.cancelOrder(orderId, studentId);
        } catch (OrderNotFoundException e) {
            error(e.getMessage());
        }
    }

    // 
    //  ADD WALLET BALANCE
    // 
    static void handleAddBalance(String studentId) {
        Student student = manager.getStudent(studentId);
        if (student == null) return;
        System.out.print("  Enter amount to add: Rs.");
        double amount = getDoubleInput();
        if (amount <= 0) {
            error("Invalid amount. Must be greater than 0.");
            return;
        }
        student.addBalance(amount);
    }

    // 
    //  UPDATE ORDER STATUS (ADMIN)
    // 
    static void handleUpdateStatus() {
        try {
            System.out.print("  Enter Order ID: ");
            int orderId = getIntInput();

            System.out.println();
            System.out.println("  Select new status:");
            System.out.println("  1." + " PREPARING");
            System.out.println("  2." + " READY");
            System.out.println("  3." + " DELIVERED");
            System.out.print("  Choice: ");
            int ch = getIntInput();

            OrderStatus status;
            switch (ch) {
                case 1: status = OrderStatus.PREPARING; break;
                case 2: status = OrderStatus.READY;     break;
                case 3: status = OrderStatus.DELIVERED; break;
                default:
                    error("Invalid choice.");
                    return;
            }
            manager.updateOrderStatus(orderId, status);

        } catch (OrderNotFoundException e) {
            error(e.getMessage());
        }
    }

    // 
    //  ADD MENU ITEM (ADMIN)
    // 
    static void handleAddMenuItem() {
        System.out.println();
        System.out.println("  Select category:");
        System.out.println("  1." + " Veg");
        System.out.println("  2." + " Non-Veg");
        System.out.println("  3." + " Beverage");
        System.out.print("  Choice: ");
        int cat = getIntInput();

        System.out.print("  Item name: ");
        String name = scanner.nextLine().trim();
        System.out.print("  Price: Rs.");
        double price = getDoubleInput();

        switch (cat) {
            case 1:
                System.out.print("  Is Jain? (yes/no): ");
                boolean isJain = scanner.nextLine().trim().equalsIgnoreCase("yes");
                manager.addMenuItem(new VegItem(name, price, isJain));
                break;
            case 2:
                System.out.print("  Meat type (chicken/egg/mutton): ");
                String meat = scanner.nextLine().trim();
                manager.addMenuItem(new NonVegItem(name, price, meat));
                break;
            case 3:
                System.out.print("  Temperature (hot/cold): ");
                String temp = scanner.nextLine().trim();
                manager.addMenuItem(new Beverage(name, price, temp));
                break;
            default:
                error("Invalid category.");
        }
    }

    // 
    //  REMOVE MENU ITEM (ADMIN)
    // 
    static void handleRemoveMenuItem() {
        System.out.print("  Enter item name to remove: ");
        String name = scanner.nextLine().trim();
        try {
            manager.removeMenuItem(name);
        } catch (ItemNotFoundException e) {
            error(e.getMessage());
        }
    }

    // 
    //  INPUT HELPERS
    // 
    static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Invalid! Enter a number: ");
            }
        }
    }

    static double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Invalid! Enter a valid amount: ");
            }
        }
    }
}