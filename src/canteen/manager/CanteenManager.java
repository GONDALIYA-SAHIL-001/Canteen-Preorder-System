package canteen.manager;
import static canteen.Printer.*;
import canteen.exceptions.*;
import canteen.interfaces.Reportable;
import canteen.filehandler.FileHandler;
import canteen.model.*;
import java.util.*;

public class CanteenManager implements Reportable {

    private ArrayList<FoodItem> menu;
    private ArrayList<Order> allOrders;
    private HashMap<String, Student> students;
    private HashMap<Integer, Order> tokenOrderMap;
    private Queue<WaitlistEntry> waitlist;
    private HashMap<String, Integer> slotCapacity;

    public CanteenManager() {
    tokenOrderMap = new HashMap<>();
    waitlist      = new LinkedList<>();
    slotCapacity  = new HashMap<>();

    students  = FileHandler.loadStudents();
    menu      = FileHandler.loadMenu();
    allOrders = FileHandler.loadOrders(menu);

    if (menu.isEmpty()) {
        loadSampleMenu();
    }

   
    rebuildTokenOrderMap();
    setupSlots();
}

private void rebuildTokenOrderMap() {
    for (Order order : allOrders) {
        tokenOrderMap.put(order.getToken().getTokenNumber(), order);
        Student s = students.get(order.getStudentId());
        if (s != null && !s.getOrderHistory().contains(order.getOrderId())) {
            s.addOrderToHistory(order.getOrderId());
        }
    }
}

    private void loadSampleMenu() {
        menu.add(new VegItem("Dal Chawal", 50, false));
        menu.add(new VegItem("Paneer Sabji Roti", 70, false));
        menu.add(new VegItem("Jain Thali", 65, true));
        menu.add(new VegItem("Aloo Paratha", 45, false));
        menu.add(new VegItem("Chole Rice", 55, false));
        menu.add(new NonVegItem("Chicken Curry Rice", 100, "Chicken"));
        menu.add(new NonVegItem("Egg Bhurji Roti", 60, "Egg"));
        menu.add(new NonVegItem("Mutton Curry Rice", 120, "Mutton"));
        menu.add(new Beverage("Chai", 15, "hot"));
        menu.add(new Beverage("Cold Coffee", 40, "cold"));
        menu.add(new Beverage("Lassi", 30, "cold"));
        menu.add(new Beverage("Fresh Lime Soda", 25, "cold"));
    }

    private void setupSlots() {
        slotCapacity.put("11:00 AM", 20);
        slotCapacity.put("11:30 AM", 20);
        slotCapacity.put("12:00 PM", 20);
        slotCapacity.put("12:30 PM", 20);
        slotCapacity.put("01:00 PM", 20);
        slotCapacity.put("01:30 PM", 20);
        slotCapacity.put("07:00 PM", 15);
        slotCapacity.put("07:30 PM", 15);
        slotCapacity.put("08:00 PM", 20);
    }

    public void registerStudent(String id, String password, String name, double balance) {
        if (students.containsKey(id)) {
            return;
        }
        Student s = new Student(id, password, name, balance);
        students.put(id, s);
        System.out.println("Student registered: " + name);
    }

    public Student getStudent(String studentId) {
        return students.get(studentId);
    }

    public void displayMenu() {
    System.out.println();
    printLine();
    System.out.println("             CANTEEN MENU               ");
    printLine();

    System.out.println("\n   VEG ITEMS ");
    for (FoodItem item : menu) {
        if (item.getCategory() == FoodCategory.VEG && item.isAvailable()) {
            System.out.println(item.getName() +
                "  Rs." + String.format("%.2f", item.getPrice()) +
                "  |  Prep: " + item.getPrepTime() + " mins");
        }
    }

    System.out.println("\n   NON-VEG ITEMS ");
    for (FoodItem item : menu) {
        if (item.getCategory() == FoodCategory.NON_VEG && item.isAvailable()) {
            System.out.println(item.getName() +
                "  Rs." + String.format("%.2f", item.getPrice()) +
                "  |  Prep: " + item.getPrepTime() + " mins");
        }
    }

    System.out.println("\n   BEVERAGES ");
    for (FoodItem item : menu) {
        if (item.getCategory() == FoodCategory.BEVERAGE && item.isAvailable()) {
            System.out.println(item.getName() +
                "  Rs." + String.format("%.2f", item.getPrice()) +
                "  |  Prep: " + item.getPrepTime() + " mins");
        }
    }

    System.out.println();
    printLine();
}

    public ArrayList<String> getSortedSlots() {
        ArrayList<String> sorted = new ArrayList<>(slotCapacity.keySet());
        sorted.sort((a, b) -> {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
                return sdf.parse(a).compareTo(sdf.parse(b));
            } catch (Exception e) {
                return a.compareTo(b);
            }
        });
        return sorted;
    }

    public String getSlotByNumber(int number) {
        ArrayList<String> sorted = getSortedSlots();
        if (number < 1 || number > sorted.size()) return null;
        return sorted.get(number - 1);
    }

    public int getSlotSeats(String slot) {
        return slotCapacity.getOrDefault(slot, 0);
    }

    public void displaySlots() {
        System.out.println();
        printLine();
        System.out.println("          AVAILABLE TIME SLOTS          ");
        printLine();
        ArrayList<String> sorted = getSortedSlots();
        for (int i = 0; i < sorted.size(); i++) {
            String slot  = sorted.get(i);
            int    seats = slotCapacity.get(slot);
            String seatsColor = "";
            String fullLabel  = seats > 0 ? (seatsColor + seats + " seats left")
                                           : ("FULL");
            System.out.println(
                "  " + String.format("%2d", i + 1) + "." +
                "  " + slot +
                "     " + fullLabel);
        }
        printLine();
    }

    public FoodItem findItem(String name) throws ItemNotFoundException {
        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(name) && item.isAvailable()) {
                return item;
            }
        }
        throw new ItemNotFoundException("Item not found or unavailable: " + name);
    }

    public void addMenuItem(FoodItem item) {
        menu.add(item);
        System.out.println("Item added to menu: " + item.getName());
    }

    public void removeMenuItem(String name) throws ItemNotFoundException {
        FoodItem item = findItem(name);
        item.setAvailable(false);
        System.out.println("Item removed from menu: " + name);
    }

    public Order placeOrder(String studentId,
                            ArrayList<FoodItem> items,
                            String timeSlot)
            throws InsufficientBalanceException,
                   ItemNotFoundException,
                   SlotFullException,
                   InvalidInputException {

        if (items == null || items.isEmpty()) {
            throw new InvalidInputException("No items selected for order!");
        }

        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not registered!");
            return null;
        }

        if (!slotCapacity.containsKey(timeSlot)) {
            throw new InvalidInputException("Invalid time slot: " + timeSlot);
        }

        if (slotCapacity.get(timeSlot) <= 0) {
            waitlist.add(new WaitlistEntry(student, items, timeSlot));
            throw new SlotFullException(
                "Slot " + timeSlot + " is full! " +
                student.getName() + " added to waitlist."
            );
        }

        Token token = new Token(timeSlot);
        Order order = new Order(studentId, items, token);

        double discountedAmount = order.applyDiscount(order.getTotalAmount());
        order.setTotalAmount(discountedAmount);

        student.deductBalance(discountedAmount);
        student.addOrderToHistory(order.getOrderId());

        allOrders.add(order);
        tokenOrderMap.put(token.getTokenNumber(), order);

        slotCapacity.put(timeSlot, slotCapacity.get(timeSlot) - 1);

        System.out.println("\nOrder placed successfully!");
        order.printOrderDetails();
        return order;
    }

    public void updateOrderStatus(int orderId, OrderStatus status)
            throws OrderNotFoundException {
        for (Order order : allOrders) {
            if (order.getOrderId() == orderId) {
                order.setOrderStatus(status);
                System.out.println("Order #" + orderId +
                                   " status updated to: " + status);
                return;
            }
        }
        throw new OrderNotFoundException("Order #" + orderId + " not found!");
    }

    public void cancelOrder(int orderId, String studentId)
            throws OrderNotFoundException {
        for (Order order : allOrders) {
            if (order.getOrderId() == orderId &&
                order.getStudentId().equals(studentId)) {

                if (order.getOrderStatus() == OrderStatus.PENDING) {
                    order.setOrderStatus(OrderStatus.CANCELLED);
                    
                    // 5% Cancellation Fee
                    double fee = order.getTotalAmount() * 0.05;
                    double refund = order.getTotalAmount() - fee;
                    
                    students.get(studentId).addBalance(refund);
                    String slot = order.getToken().getTimeSlot();
                    slotCapacity.put(slot, slotCapacity.get(slot) + 1);
                    
                    System.out.println("Order #" + orderId + " cancelled.");
                    System.out.println("5% cancellation fee applied (Rs." + String.format("%.2f", fee) + ").");
                    System.out.println("Refund of Rs." + String.format("%.2f", refund) + " added to wallet.");
                    
                    processWaitlist(slot);
                } else {
                    System.out.println("Cannot cancel. Order status is: " +
                                       order.getOrderStatus());
                }
                return;
            }
        }
        throw new OrderNotFoundException("Order not found for cancellation!");
    }

    private void processWaitlist(String timeSlot) {
        Iterator<WaitlistEntry> it = waitlist.iterator();
        while (it.hasNext()) {
            WaitlistEntry entry = it.next();
            if (entry.getTimeSlot().equals(timeSlot)) {
                try {
                    it.remove(); // Remove them from waitlist
                    // We directly call placeOrder. Since slotCapacity is now > 0, it will succeed.
                    placeOrder(entry.getStudent().getStudentId(), entry.getItems(), timeSlot);
                    System.out.println("Waitlist order automatically fulfilled for " + 
                                       entry.getStudent().getName() + "!");
                    return; // Slot filled, stop processing waitlist
                } catch (InsufficientBalanceException e) {
                    System.out.println("Waitlist skipped for " + entry.getStudent().getName() + 
                                       " due to insufficient balance.");
                } catch (Exception e) {
                    System.out.println("Failed to process waitlist entry: " + e.getMessage());
                }
            }
        }
    }

    public void trackOrder(int tokenNumber) throws OrderNotFoundException {
        Order order = tokenOrderMap.get(tokenNumber);
        if (order == null) {
            throw new OrderNotFoundException(
                "No order found for Token #" + tokenNumber);
        }
        System.out.println("\n--- Tracking Order ---");
        order.printOrderDetails();
    }

    public void viewOrderHistory(String studentId) {
        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        System.out.println("\n=== Order History: " + student.getName() + " ===");
        if (student.getOrderHistory().isEmpty()) {
            System.out.println("No orders placed yet.");
            return;
        }
        for (int orderId : student.getOrderHistory()) {
            for (Order order : allOrders) {
                if (order.getOrderId() == orderId) {
                    order.printOrderDetails();
                }
            }
        }
    }

    public void viewAllOrders() {
        System.out.println("\n===== ALL ORDERS =====");
        if (allOrders.isEmpty()) {
            System.out.println("No orders yet.");
            return;
        }
        for (Order order : allOrders) {
            System.out.println(order);
        }
    }

    public void viewWaitlist() {
        System.out.println("\n===== WAITLIST =====");
        if (waitlist.isEmpty()) {
            System.out.println("Waitlist is empty.");
            return;
        }
        for (WaitlistEntry e : waitlist) {
            System.out.println(e.getStudent().getName() + " -> waiting for slot: " + e.getTimeSlot());
        }
    }

    @Override
    public void generateReport() {
    System.out.println();
    printLine();
    System.out.println("          DAILY SALES REPORT            ");
    printLine();

    double totalRevenue = 0;
    int totalOrders = 0;
    int cancelledOrders = 0;
    HashMap<String, Integer> itemCount = new HashMap<>();

    for (Order order : allOrders) {
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            cancelledOrders++;
            continue;
        }
        totalRevenue += order.getTotalAmount();
        totalOrders++;
        for (FoodItem item : order.getItemList()) {
            itemCount.put(item.getName(),
                itemCount.getOrDefault(item.getName(), 0) + 1);
        }
    }

    System.out.println("  Total Orders   : " +
                       totalOrders);
    System.out.println("  Cancelled      : " +
                       cancelledOrders);
    System.out.println("  Total Revenue  : " +
                       "Rs." + String.format("%.2f", totalRevenue));

    System.out.println("\n  Item-wise Sales:");
    if (itemCount.isEmpty()) {
        warning("  No items sold yet.");
    } else {
        for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
            System.out.println("     " +
                               entry.getKey() + "    " +
                               entry.getValue() + " orders");
        }
    }

    System.out.println("\n  Slot-wise Capacity:");
    for (Map.Entry<String, Integer> entry : slotCapacity.entrySet()) {
        String c = "";
        System.out.println("     " +
                           entry.getKey() + "    " +
                           c + entry.getValue() + " seats left");
    }

    String reportContent =
        "Total Orders: "   + totalOrders + "\n" +
        "Cancelled: "      + cancelledOrders + "\n" +
        "Total Revenue: Rs." + String.format("%.2f", totalRevenue);
    FileHandler.saveReport(reportContent);

    System.out.println();
    printLine();
}

    public void saveAll() {
        FileHandler.saveStudents(students);
        FileHandler.saveMenu(menu);
        FileHandler.saveOrders(allOrders);
    }
}