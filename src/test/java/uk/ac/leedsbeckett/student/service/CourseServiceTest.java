package uk.ac.leedsbeckett.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.CourseNotFoundException;
import uk.ac.leedsbeckett.student.exception.EnrolmentAlreadyExistsException;
import uk.ac.leedsbeckett.student.model.Course;
import uk.ac.leedsbeckett.student.model.Invoice;
import uk.ac.leedsbeckett.student.model.Student;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CourseServiceTest extends CourseServiceUnitTest {

    @Test
    void CourseId() {
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourse(0L, userNotStudent),
            "Exception was not thrown.");
    }

    @Test
    void ExistingCourses() {
        ModelAndView result = courseService.getCourses();
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).containsAll(courseList));
        }
    }

    @Test
    void ExistingCourseAndStudent() {
        ModelAndView result = courseService.getCourse(course1.getId(), userStudent);
        assertEquals(result.getViewName(), "course");
        assertNotNull(result.getModel());
        assertEquals(3, result.getModel().size());
        Object courseReturned = result.getModel().get("course");
        if (courseReturned instanceof Course) {
            assertEquals(course1, courseReturned);
        }
        Object studentReturned = result.getModel().get("student");
        if (studentReturned instanceof Student) {
            assertEquals(student, studentReturned);
        }
        assertFalse((Boolean) result.getModel().get("isEnrolled"));
    }

    @Test
    void UserIsAStudent() {
        ModelAndView result = courseService.getCourse(course2.getId(), userStudent);
        assertEquals(result.getViewName(), "course");
        assertNotNull(result.getModel());
        assertEquals(4, result.getModel().size());
        Object courseReturned = result.getModel().get("course");
        if (courseReturned instanceof Course) {
            assertEquals(course2, courseReturned);
        }
        Object studentReturned = result.getModel().get("student");
        if (studentReturned instanceof Student) {
            assertEquals(student, studentReturned);
        }
        assertTrue((Boolean) result.getModel().get("isEnrolled"));
        assertEquals("You are enrolled in this course.", result.getModel().get("message"));
    }

    @Test
    void UserIsNotAStudent() {
        ModelAndView result = courseService.getCourse(course1.getId(), userNotStudent);
        assertEquals(result.getViewName(), "course");
        assertNotNull(result.getModel());
        assertEquals(3, result.getModel().size());
        Object courseReturned = result.getModel().get("course");
        if (courseReturned instanceof Course) {
            assertEquals(course1, courseReturned);
        }
        Object studentReturned = result.getModel().get("student");
        assertNull(studentReturned);
        assertFalse((Boolean) result.getModel().get("isEnrolled"));
    }

    @Test
    void StudentidDoesNotExist() {
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourse(999L, userNotStudent),
                "Exception was not thrown.");
    }

    @Test
    void idIsNull() {
        assertThrows(ConstraintViolationException.class, () -> courseService.getCourse(null, userNotStudent),
                "Exception was not thrown.");
    }

    @Test
    void userIsNull() {
        assertThrows(ConstraintViolationException.class, () -> courseService.getCourse(1L, null),
                "Exception was not thrown.");
    }

    @Test
    void UserAndIdAreNull() {
        assertThrows(ConstraintViolationException.class, () -> courseService.getCourse(null, null),
                "Exception was not thrown.");
    }

    @Test
    void AnExistingStudentId() {
        ModelAndView result = courseService.enrolInCourse(course3.getId(), userStudent);
        assertEquals(result.getViewName(), "course");
        assertNotNull(result.getModel());
        assertEquals(4, result.getModel().size());
        Object courseReturned = result.getModel().get("course");
        if (courseReturned instanceof Course) {
            assertEquals(course3, courseReturned);
        }
        Object studentReturned = result.getModel().get("student");
        if (studentReturned instanceof Student) {
            assertEquals(student, studentReturned);
        }
        assertTrue((Boolean) result.getModel().get("isEnrolled"));
        assertTrue(result.getModel().get("message").toString().contains("Please log into the Payment Portal to pay the invoice reference: "));
        verify(enrolmentService, times(1)).createEnrolment(course3, student);
        verify(integrationService, times(1)).createCourseFeeInvoice(any(Invoice.class));
    }

    @Test
    void NotAStudent() {
        ModelAndView result = courseService.enrolInCourse(course3.getId(), userToBecomeStudent);
        assertEquals(result.getViewName(), "course");
        assertNotNull(result.getModel());
        assertEquals(4, result.getModel().size());
        Object courseReturned = result.getModel().get("course");
        if (courseReturned instanceof Course) {
            assertEquals(course3, courseReturned);
        }
        assertNotNull(result.getModel().get("student"));
        assertTrue((Boolean) result.getModel().get("isEnrolled"));
        verify(userService, times(1)).createStudentFromUser(userToBecomeStudent);
        verify(enrolmentService, times(1)).createEnrolment(course3, (Student) result.getModel().get("student"));
        assertTrue(result.getModel().get("message").toString().contains("Please log into the Payment Portal to pay the invoice reference: "));
        verify(integrationService, times(1)).createCourseFeeInvoice(any(Invoice.class));
    }

    @Test
    void AlreadyEnrolled() {
        assertThrows(EnrolmentAlreadyExistsException.class, () -> courseService.enrolInCourse(course2.getId(), userStudent),
                "Exception was not thrown.");
    }

    @Test
    void UserIsNull() {
        assertThrows(ConstraintViolationException.class, () -> courseService.enrolInCourse(course2.getId(), null),
                "Exception was not thrown.");
    }

    @Test
    void CourseIdIsNull() {
        assertThrows(ConstraintViolationException.class, () -> courseService.enrolInCourse(null, userStudent),
                "Exception was not thrown.");
    }

    @Test
    void CourseIdAndUserAreNull() {
        assertThrows(ConstraintViolationException.class, () -> courseService.enrolInCourse(null, null),
                "Exception was not thrown.");
    }

    @Test
    void Search() {
        ModelAndView result = courseService.searchCourses("Cloud Computing Development");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertFalse(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void SearchTitle() {
        ModelAndView result = courseService.searchCourses("Cloud Computing Development");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertFalse(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void SearchDescription() {
        ModelAndView result = courseService.searchCourses("The module provides an understanding of Cloud Computing");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertFalse(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void SearchFromDescription() {
        ModelAndView result = courseService.searchCourses("specifically");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void SearchTheTitleAndDescription() {
        ModelAndView result = courseService.searchCourses("software processes and methods for Cloud Computing");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertFalse(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void SearchNotInTitleOrDescription() {
        ModelAndView result = courseService.searchCourses("England");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).isEmpty());
        }
    }

    @Test
    void SearchEmptyString() {
        ModelAndView result = courseService.searchCourses("");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).isEmpty());
        }
    }

    @Test
    void testSearchNull() {
        ModelAndView result = courseService.searchCourses(null);
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).isEmpty());
        }
    }

}
