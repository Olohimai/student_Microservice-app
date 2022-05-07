package uk.ac.leedsbeckett.student.exception;

public class EnrolmentAlreadyExistsException extends RuntimeException {
    public EnrolmentAlreadyExistsException() {
        super("This enrolment already exists Enrol to a new course.");
    }
    public EnrolmentAlreadyExistsException(String message) {
        super(message);
    }
}
