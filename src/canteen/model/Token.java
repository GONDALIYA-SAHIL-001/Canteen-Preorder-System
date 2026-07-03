package canteen.model;

public class Token {
    
    private static int counter = 1;

    private int tokenNumber;
    private String timeSlot;
    private boolean isUsed;

    public Token(String timeSlot) {
        this.tokenNumber = counter++;
        this.timeSlot = timeSlot;
        this.isUsed = false;
    }

    // Used by FileHandler to restore token number from saved data
    public void setTokenNumber(int number) {
        this.tokenNumber = number;
    }

    // Sync static counter after loading from file
    public static void setTokenCounter(int val) {
        counter = val;
    }

    public int getTokenNumber() {
        return tokenNumber;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void markUsed() {
        this.isUsed = true;
    }

    @Override
    public String toString() {
        return "Token #" + tokenNumber +
               " | Slot: " + timeSlot +
               " | Used: " + isUsed;
    }
}