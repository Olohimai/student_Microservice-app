package uk.ac.leedsbeckett.student.exception;

public class StudentException extends RuntimeException {
    public StudentException(Long id) {
        super("Could not find student " + id);
    }
    public StudentException() {
        super("Could not find student.");
    }
}
