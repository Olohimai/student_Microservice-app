package uk.ac.leedsbeckett.student.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Could not find student " + id);
    }
    public StudentNotFoundException() {
        super("Could not find student.");
    }
}
