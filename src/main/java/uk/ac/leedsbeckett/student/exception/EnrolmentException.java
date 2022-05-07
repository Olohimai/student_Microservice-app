package uk.ac.leedsbeckett.student.exception;

public class EnrolmentException extends RuntimeException {
    public EnrolmentException() {
        super("This enrolment already exists Enrol to a new course.");
    }
    public EnrolmentException(String message) {
        super(message);
    }
}
