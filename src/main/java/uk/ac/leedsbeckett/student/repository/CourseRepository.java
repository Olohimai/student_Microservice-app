package uk.ac.leedsbeckett.student.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.ac.leedsbeckett.student.model.Course;

import java.util.List;

/**
 * Inherit database interaction functionality from JpaRepository for Course class, of ID type Long
 * Create new Course *
 * Update existing Course *
 * Delete Course *
 * Find Course (one, all, or search by simple or complex properties)
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE CONCAT(c.title, c.description) LIKE %?1%")
    public List<Course> search(String keyword);
}
