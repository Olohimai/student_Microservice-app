package uk.ac.leedsbeckett.student.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.leedsbeckett.student.model.Course;
import uk.ac.leedsbeckett.student.model.Enrolment;
import uk.ac.leedsbeckett.student.model.Student;

import java.util.List;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
    Enrolment findEnrolmentByCourseAndStudent(Course course, Student student);
    List<Enrolment> findEnrolmentByStudent(Student student);
}
