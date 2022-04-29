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
class CourseServiceTest extends CourseServiceTestBase {

    @Test
    void testGetCourses_returnsExistingCourses() {
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
    void testGetCourseWithoutEnrolment_userIsStudent_returnsExistingCourseAndStudent() {
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
    void testGetCourseWithEnrolment_userIsStudent_returnsExistingCourseAndStudent() {
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
    void testGetCourseWithoutEnrolment_userIsNotStudent_returnsExistingCourseAndNoStudent() {
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
    void testGetCourse_idIsZero_throwsCourseNotFoundException() {
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourse(0L, userNotStudent),
                "Exception was not thrown.");
    }

    @Test
    void testGetCourse_idDoesNotExist_throwsCourseNotFoundException() {
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourse(999L, userNotStudent),
                "Exception was not thrown.");
    }

    @Test
    void testGetCourse_idIsNull_throwsCourseNotFoundException() {
        assertThrows(ConstraintViolationException.class, () -> courseService.getCourse(null, userNotStudent),
                "Exception was not thrown.");
    }

    @Test
    void testGetCourse_userIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> courseService.getCourse(1L, null),
                "Exception was not thrown.");
    }

    @Test
    void testGetCourse_userAndIdAreNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> courseService.getCourse(null, null),
                "Exception was not thrown.");
    }

    @Test
    void testEnrolInCourse_userIsStudent_createsEnrolment() {
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
    void testEnrolInCourse_userIsNotStudent_createsStudentAndEnrolment() {
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
    void testEnrolInCourse_userIsStudent_alreadyEnrolled_throwsException() {
        assertThrows(EnrolmentAlreadyExistsException.class, () -> courseService.enrolInCourse(course2.getId(), userStudent),
                "Exception was not thrown.");
    }

    @Test
    void testEnrolInCourse_userIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> courseService.enrolInCourse(course2.getId(), null),
                "Exception was not thrown.");
    }

    @Test
    void testEnrolInCourse_courseIdIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> courseService.enrolInCourse(null, userStudent),
                "Exception was not thrown.");
    }

    @Test
    void testEnrolInCourse_courseIdAndUserAreNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> courseService.enrolInCourse(null, null),
                "Exception was not thrown.");
    }

    @Test
    void testSearch_withFullTitle_returnsCourse() {
        ModelAndView result = courseService.searchCourses("Software Engineering for Service Computing");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void testSearch_withTitleSubstring_returnsCourse() {
        ModelAndView result = courseService.searchCourses("Service Computing");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void testSearch_withDescriptionSubstring_returnsCourse() {
        ModelAndView result = courseService.searchCourses("This module provides an in-depth");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void testSearch_withWordFromDescription_returnsCourse() {
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
    void testSearch_withWordsFromTitleAndDescription_returnsCourse() {
        ModelAndView result = courseService.searchCourses("Engineering recent modular");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).contains(course1));
        }
    }

    @Test
    void testSearch_withWordNotInTitleOrDescription_returnsNothing() {
        ModelAndView result = courseService.searchCourses("Rampage");
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).isEmpty());
        }
    }

    @Test
    void testSearch_withEmptyString_returnsNothing() {
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
    void testSearch_withNull_returnsNothing() {
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