package uk.ac.leedsbeckett.student.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.leedsbeckett.student.model.Student;

/**
 * Inherit database interaction functionality from JpaRepository for Student class, of ID type Long
 * Create new Student *
 * Update existing Student *
 * Delete Student *
 * Find Student (one, all, or search by simple or complex properties)
 */
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserId(Long userId);
}
