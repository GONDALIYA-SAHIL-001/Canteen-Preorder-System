package canteen.model;

public abstract class FoodItem {

    private String name;
    private double price;
    private boolean isAvailable;
    private FoodCategory category;

    public FoodItem(String name, double price, FoodCategory category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.isAvailable = true;
    }

    public abstract double getPrice();
    public abstract int getPrepTime();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public FoodCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        
        return name + " | Rs." + String.format("%.2f", getPrice()) + " | " + category + " | Prep Time: " + getPrepTime() + " mins" + " | Available: " + isAvailable;
    }
}