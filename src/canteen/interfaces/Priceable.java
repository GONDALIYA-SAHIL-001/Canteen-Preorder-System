package canteen.interfaces;

public interface Priceable {
    double calculateTotal();
    double applyDiscount(double amount);
}