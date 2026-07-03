package canteen.model;

import java.util.ArrayList;

public class WaitlistEntry {
    private Student student;
    private ArrayList<FoodItem> items;
    private String timeSlot;

    public WaitlistEntry(Student student, ArrayList<FoodItem> items, String timeSlot) {
        this.student = student;
        this.items = items;
        this.timeSlot = timeSlot;
    }

    public Student getStudent() {
        return student;
    }

    public ArrayList<FoodItem> getItems() {
        return items;
    }

    public String getTimeSlot() {
        return timeSlot;
    }
}
