package canteen.interfaces;

public interface Orderable {
    void placeOrder();
    void cancelOrder(int orderId);
}