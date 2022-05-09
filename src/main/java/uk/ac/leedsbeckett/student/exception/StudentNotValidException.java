package uk.ac.leedsbeckett.student.exception;

public class StudentNotValidException extends RuntimeException {

    public StudentNotValidException() {

        super("Invalid student.");
    }
    public StudentNotValidException(String message) {
        super(message);
    }
}
