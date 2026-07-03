package canteen.model;

public class Beverage extends FoodItem {

    private String temperature;
    private static final double COMBO_DISCOUNT = 0.05;

    public Beverage(String name, double price, String temperature) {
        super(name, price, FoodCategory.BEVERAGE);
        this.temperature = temperature;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public double getPrice() {
        double basePrice = getBasePrice();
        return basePrice - (basePrice * COMBO_DISCOUNT);
    }

    @Override
    public int getPrepTime() {
        return 2;
    }

    @Override
    public String toString() {
        return super.toString() + " | Temp: " + temperature;
    }
}