package uk.ac.leedsbeckett.student.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("Sorry Couldn't find course you are looking for " + id);
    }
    public CourseNotFoundException() {
        super("Sorry Couldn't find course you are looking for.");
    }
}
