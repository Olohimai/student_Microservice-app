package uk.ac.leedsbeckett.student.model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
    Enrolment findEnrolmentByCourseAndStudent(Course course, Student student);
    List<Enrolment> findEnrolmentByStudent(Student student);
}
