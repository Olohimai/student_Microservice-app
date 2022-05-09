package uk.ac.leedsbeckett.student.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student not found" + id);
    }
    public StudentNotFoundException() {

        super("Student not found ");
    }
}
