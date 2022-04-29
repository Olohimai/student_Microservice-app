package uk.ac.leedsbeckett.student.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("Could not find course " + id);
    }
    public CourseNotFoundException() {
        super("Could not find course.");
    }
}
