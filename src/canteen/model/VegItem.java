package canteen.model;

public class VegItem extends FoodItem {

    private boolean isJain;
    private static final double STUDENT_DISCOUNT = 0.10;

    public VegItem(String name, double price, boolean isJain) {
        super(name, price, FoodCategory.VEG);
        this.isJain = isJain;
    }

    public boolean isJain() {
        return isJain;
    }

    public void setJain(boolean jain) {
        this.isJain = jain;
    }

    @Override
    public double getPrice() {
        double basePrice = getBasePrice();
        return basePrice - (basePrice * STUDENT_DISCOUNT);
    }

    @Override
    public int getPrepTime() {
        return 10;
    }

    @Override
    public String toString() {
        return super.toString() + " | Jain: " + isJain;
    }
}