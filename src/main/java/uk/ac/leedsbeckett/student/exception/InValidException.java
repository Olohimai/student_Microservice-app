package uk.ac.leedsbeckett.student.exception;

public class InValidException extends RuntimeException {

    public InValidException() {
        super("Not a valid student.");
    }
    public InValidException(String message) {
        super(message);
    }
}
