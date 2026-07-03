package canteen.model;

import canteen.exceptions.InsufficientBalanceException;
import canteen.interfaces.Orderable;
import java.util.ArrayList;

public class Student implements Orderable {

    private String studentId;
    private String password;
    private String name;
    private double walletBalance;
    private ArrayList<Integer> orderHistory;

    public Student(String studentId, String password, String name, double walletBalance) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.walletBalance = walletBalance;
        this.orderHistory = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public ArrayList<Integer> getOrderHistory() {
        return orderHistory;
    }

    public void addBalance(double amount) {
        this.walletBalance += amount;
        System.out.println("Rs." + String.format("%.2f", amount) +
                           " added. New balance: Rs." +
                           String.format("%.2f", walletBalance));
    }

    public void deductBalance(double amount) throws InsufficientBalanceException {
        if (amount > walletBalance) {
            throw new InsufficientBalanceException(
                "Insufficient balance! Required: Rs." + String.format("%.2f", amount) +
                ", Available: Rs." + String.format("%.2f", walletBalance)
            );
        }
        this.walletBalance -= amount;
    }

    public void addOrderToHistory(int orderId) {
        orderHistory.add(orderId);
    }

    @Override
    public void placeOrder() {
        System.out.println("Order placed by: " + name);
    }

    @Override
    public void cancelOrder(int orderId) {
        System.out.println("Order #" + orderId +
                           " cancellation requested by: " + name);
    }

    @Override
    public String toString() {
        return "Student[" + studentId + "] " + name +
               " | Balance: Rs." + String.format("%.2f", walletBalance);
    }
}