package uk.ac.leedsbeckett.student.model;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserId(Long userId);
}
