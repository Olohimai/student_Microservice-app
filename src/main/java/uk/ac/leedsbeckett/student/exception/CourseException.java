package uk.ac.leedsbeckett.student.exception;

public class CourseException extends RuntimeException {
    public CourseException(Long id) {
        super("Sorry Couldn't find course you are looking for " + id);
    }
    public CourseException() {
        super("Sorry Couldn't find course you are looking for.");
    }
}
