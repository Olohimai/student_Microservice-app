package uk.ac.leedsbeckett.student.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.CourseNotFoundException;
import uk.ac.leedsbeckett.student.model.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Validated
@Component
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrolmentService enrolmentService;
    private final UserService userService;
    private final IntegrationService integrationService;
    private Student student;
    private Course course;

    public CourseService(CourseRepository courseRepository, UserService userService, EnrolmentService enrolmentService, IntegrationService integrationService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.enrolmentService = enrolmentService;
        this.integrationService = integrationService;
    }

    public ModelAndView getCourses() {
        ModelAndView modelAndView = new ModelAndView("courses");
        List<Course> courses = courseRepository.findAll();
        modelAndView.addObject("courses", courses);
        return modelAndView;
    }

    public ModelAndView getCourse(@NotNull Long id, @NotNull User user) {
        populateStudentAndCourse(user, id);
        Enrolment existingEnrolment = enrolmentService.findEnrolment(course, student);
        return getModelAndView(existingEnrolment != null, null);
    }

    public ModelAndView enrolInCourse(@NotNull Long id, @NotNull User user) {
        populateStudentAndCourse(user, id);
        if (student == null) {
            userService.createStudentFromUser(user);
        }
        student = userService.findStudentFromUser(user);
        enrolmentService.createEnrolment(course, student);
        Invoice invoice = notifySubscribers(student, course);
        return getModelAndView(true, invoice);
    }

    public ModelAndView searchCourses(String searchString) {
        ModelAndView modelAndView = new ModelAndView("courses");
        List<Course> courses = courseRepository.search(searchString);
        modelAndView.addObject("courses", courses);
        return modelAndView;
    }

    private ModelAndView getModelAndView(Boolean isEnrolled, @Nullable Invoice invoice) {
        ModelAndView modelAndView = new ModelAndView("course");
        modelAndView.addObject("course", course);
        modelAndView.addObject("student", student);
        modelAndView.addObject("isEnrolled", isEnrolled);
        StringBuilder message = new StringBuilder("You are enrolled in this course.");
        if (invoice != null && invoice.getReference() != null && !invoice.getReference().isEmpty()) {
            message.append(" Please log into the Payment Portal to pay the invoice reference: ")
                    .append(invoice.getReference())
                    .append(".");
        }
        if (isEnrolled) {
            modelAndView.addObject("message", message.toString());
        }
        return modelAndView;
    }

    private void populateStudentAndCourse(User user, Long courseId) {
        student = userService.findStudentFromUser(user);
        course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
    }

    private Invoice notifySubscribers(Student student, Course course) {
        Account account = new Account();
        account.setStudentId(student.getStudentId());
        Invoice invoice = new Invoice();
        invoice.setAccount(account);
        invoice.setType(Invoice.Type.TUITION_FEES);
        invoice.setAmount(course.getFee());
        invoice.setDueDate(LocalDate.now().plusMonths(1));
        return integrationService.createCourseFeeInvoice(invoice);
    }

}
