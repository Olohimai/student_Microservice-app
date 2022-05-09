package uk.ac.leedsbeckett.student.exception;

public class StudentAlreadyExistsException extends RuntimeException {

    public StudentAlreadyExistsException() {
        super("Not a valid student.");
    }
    public StudentAlreadyExistsException(String username) {
        super("A student already exists for this user " + username);
    }
}
