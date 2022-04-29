package uk.ac.leedsbeckett.student.exception;

public class StudentNotValidException extends RuntimeException {

    public StudentNotValidException() {
        super("Not a valid student.");
    }
    public StudentNotValidException(String message) {
        super(message);
    }
}