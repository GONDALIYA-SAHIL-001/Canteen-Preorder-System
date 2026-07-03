package canteen.model;

import canteen.interfaces.Priceable;
import java.util.ArrayList;

public class Order implements Priceable {

    private static int orderCounter = 1;

    private int orderId;
    private String studentId;
    private ArrayList<FoodItem> itemList;
    private double totalAmount;
    private OrderStatus orderStatus;
    private Token token;

    public Order(String studentId, ArrayList<FoodItem> itemList, Token token) {
        this.orderId = orderCounter++;
        this.studentId = studentId;
        this.itemList = new ArrayList<>(itemList);
        this.token = token;
        this.orderStatus = OrderStatus.PENDING;
        this.totalAmount = calculateTotal();
    }

    // Used by FileHandler when loading saved orders
    public void setOrderId(int id) {
        this.orderId = id;
    }

    // Sync static counter so new orders don't reuse old IDs
    public static void setOrderCounter(int val) {
        orderCounter = val;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStudentId() {
        return studentId;
    }

    public ArrayList<FoodItem> getItemList() {
        return itemList;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double amount) {
        this.totalAmount = amount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public double calculateTotal() {
        double total = 0;
        for (FoodItem item : itemList) {
            total += item.getPrice();
        }
        return total;
    }

    @Override
    public double applyDiscount(double amount) {
        if (amount > 100) {
            return amount - (amount * 0.05);
        }
        return amount;
    }

    public void printOrderDetails() {
        System.out.println("\n  ------ Order Details ------");
        System.out.println("  Order ID  : #" + orderId);
        System.out.println("  Student ID: "  + studentId);
        System.out.println("  Status    : "  + orderStatus);
        System.out.println("  Token     : Token #" + token.getTokenNumber()
                         + "  |  Slot: " + token.getTimeSlot());
        System.out.println("  Items:");

        // Group items by name so same item doesn't print 10 times
        java.util.LinkedHashMap<String, int[]> countMap = new java.util.LinkedHashMap<>();
        for (FoodItem item : itemList) {
            countMap.computeIfAbsent(item.getName(), k -> new int[]{0, 0});
            countMap.get(item.getName())[0]++;                      // count
            countMap.get(item.getName())[1] = (int)(item.getPrice() * 100); // price (paise)
        }
        for (java.util.Map.Entry<String, int[]> e : countMap.entrySet()) {
            int    qty       = e.getValue()[0];
            double unitPrice = e.getValue()[1] / 100.0;
            double lineTotal = unitPrice * qty;
            System.out.println("    " + qty + " x " + e.getKey()
                             + "  @Rs." + String.format("%.2f", unitPrice)
                             + "  =  Rs." + String.format("%.2f", lineTotal));
        }

        System.out.println("  --------------------------");
        System.out.println("  Total     : Rs." + String.format("%.2f", totalAmount));
        System.out.println("  --------------------------");
    }

    @Override
    public String toString() {
        return "Order #" + orderId +
               " | Student: " + studentId +
               " | Total: Rs." + String.format("%.2f", totalAmount) +
               " | Status: " + orderStatus +
               " | Token #" + token.getTokenNumber() +
               " | Slot: " + token.getTimeSlot();
    }
}