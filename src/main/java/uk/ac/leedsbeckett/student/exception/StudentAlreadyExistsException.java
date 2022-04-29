package uk.ac.leedsbeckett.student.exception;

public class StudentAlreadyExistsException extends RuntimeException {

    public StudentAlreadyExistsException() {
        super("A student already exists for this user.");
    }
    public StudentAlreadyExistsException(String username) {
        super("A student already exists for user " + username);
    }
}
