package uk.ac.leedsbeckett.student.service;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.EnrolmentAlreadyExistsException;
import uk.ac.leedsbeckett.student.model.Course;
import uk.ac.leedsbeckett.student.model.Enrolment;
import uk.ac.leedsbeckett.student.repository.EnrolmentRepository;
import uk.ac.leedsbeckett.student.model.Student;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Component
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final IntegrationService integrationService;

    public EnrolmentService(EnrolmentRepository enrolmentRepository, IntegrationService integrationService) {
        this.enrolmentRepository = enrolmentRepository;
        this.integrationService = integrationService;
    }

    public Enrolment createEnrolment(@NotNull Course course, @NotNull Student student) {
        if (enrolmentRepository.findEnrolmentByCourseAndStudent(course, student) != null) {
            throw new EnrolmentAlreadyExistsException("Student " + student.getStudentId() + " is already enrolled in course " + course.getTitle());
        }
        Enrolment enrolment = new Enrolment(student, course);
        return enrolmentRepository.save(enrolment);
    }

    public Enrolment findEnrolment(@NotNull Course course, @Nullable Student student) {
        return enrolmentRepository.findEnrolmentByCourseAndStudent(course, student);
    }

    public ModelAndView getEnrolments(@NotNull Student student) {
        ModelAndView modelAndView = new ModelAndView("courses");
        List<Course> courses = enrolmentRepository.findEnrolmentByStudent(student)
                .stream()
                .map(Enrolment::getCourse)
                .collect(Collectors.toList());
        modelAndView.addObject("courses", courses);
        return modelAndView;
    }
}
