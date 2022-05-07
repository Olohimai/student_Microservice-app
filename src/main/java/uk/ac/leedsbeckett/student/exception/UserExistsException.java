package uk.ac.leedsbeckett.student.exception;

public class UserExistsException extends RuntimeException {

    public UserExistsException() {
        super("A student already exists for this user registration.");
    }
    public UserExistsException(String username) {
        super("A student already exists " + username);
    }
}
