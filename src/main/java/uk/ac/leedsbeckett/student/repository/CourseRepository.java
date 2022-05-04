package uk.ac.leedsbeckett.student.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.ac.leedsbeckett.student.model.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE CONCAT(c.title, c.description) LIKE %?1%")
    public List<Course> search(String keyword);
}
