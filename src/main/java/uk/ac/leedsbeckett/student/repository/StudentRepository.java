package uk.ac.leedsbeckett.student.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.leedsbeckett.student.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserId(Long userId);
}
