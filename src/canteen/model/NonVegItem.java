package canteen.model;

public class NonVegItem extends FoodItem {

    private String meatType;

    public NonVegItem(String name, double price, String meatType) {
        super(name, price, FoodCategory.NON_VEG);
        this.meatType = meatType;
    }

    public String getMeatType() {
        return meatType;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    @Override
    public double getPrice() {
        return getBasePrice();
    }

    @Override
    public int getPrepTime() {
        return 15;
    }

    @Override
    public String toString() {
        return super.toString() + " | Meat: " + meatType;
    }
}