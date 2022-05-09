package uk.ac.leedsbeckett.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.EnrolmentAlreadyExistsException;
import uk.ac.leedsbeckett.student.model.Enrolment;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class EnrolmentServiceTest extends EnrolmentServiceUnitTest {

    @Test
    void OneEnrolment() {
        ModelAndView result = enrolmentService.getEnrolments(studentOneEnrolment);
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertEquals(1, ((List<?>) coursesReturned).size());
            assertTrue(((List<?>) coursesReturned).contains(course1));
            assertFalse(((List<?>) coursesReturned).contains(course2));
            assertFalse(((List<?>) coursesReturned).contains(course3));
            assertFalse(((List<?>) coursesReturned).contains(course4));
            assertFalse(((List<?>) coursesReturned).contains(course5));
        }
    }
    @Test
    void ManyEnrolments() {
        ModelAndView result = enrolmentService.getEnrolments(studentManyEnrolments);
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertEquals(4, ((List<?>) coursesReturned).size());
            assertTrue(((List<?>) coursesReturned).contains(course1));
            assertTrue(((List<?>) coursesReturned).contains(course2));
            assertTrue(((List<?>) coursesReturned).contains(course3));
            assertTrue(((List<?>) coursesReturned).contains(course4));
            assertFalse(((List<?>) coursesReturned).contains(course5));
        }
    }
    @Test
    void EnrolmentExist() {
        Enrolment result = enrolmentService.findEnrolment(course1, studentOneEnrolment);
        assertEquals(enrolment5, result);
    }

    @Test
    void NoEnrolmentsExist() {
        Enrolment result = enrolmentService.findEnrolment(course1, studentNoEnrolments);
        assertNull(result);
    }
    @Test
    void Enrolment() {
        Enrolment result = enrolmentService.findEnrolment(course1, studentManyEnrolments);
        assertEquals(enrolment1, result);
    }

    @Test
    void NoEnrolments() {
        ModelAndView result = enrolmentService.getEnrolments(studentNoEnrolments);
        assertEquals(result.getViewName(), "courses");
        assertNotNull(result.getModel());
        assertEquals(1, result.getModel().size());
        Object coursesReturned = result.getModel().get("courses");
        if (coursesReturned instanceof List<?>) {
            assertTrue(((List<?>) coursesReturned).isEmpty());
        }
    }

    @Test
    void CourseIsNull() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.findEnrolment(null, studentNoEnrolments),
                "Exception was not thrown.");
    }

    @Test
    void StudentIsNull() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.getEnrolments(null),
                "Exception was not thrown.");
    }

    @Test
    void DoesNotExistEnrolment() {
        Enrolment result = enrolmentService.createEnrolment(course1, studentNoEnrolments);
        verify(enrolmentRepository, times(1)).save(result);
        assertEquals(studentNoEnrolments, result.getStudent());
        assertEquals(course1, result.getCourse());
    }

    @Test
    void EnrolmentAlreadyExistsEnrolment() {
        assertThrows(EnrolmentAlreadyExistsException.class, () -> enrolmentService.createEnrolment(course1, studentOneEnrolment),
                "Exception was not thrown.");
    }

    @Test
    void StudentIsNullCreateEnrolment() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.createEnrolment(course1, null),
                "Exception was not thrown.");
    }

    @Test
    void CourseIsNullCreateEnrolment() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.createEnrolment(null, studentNoEnrolments),
                "Exception was not thrown.");
    }

    @Test
    void StudentAndCourseAreNull() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.createEnrolment(null, null),
                "Exception was not thrown.");
    }


}
