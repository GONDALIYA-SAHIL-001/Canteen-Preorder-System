package canteen.exceptions;

public class SlotFullException extends Exception {
    public SlotFullException(String message) {
        super(message);
    }
}