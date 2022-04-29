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
class EnrolmentServiceTest extends EnrolmentServiceTestBase {

    @Test
    void testFindEnrolment_whenManyEnrolmentsExist_returnsCorrectEnrolment() {
        Enrolment result = enrolmentService.findEnrolment(course1, studentManyEnrolments);
        assertEquals(enrolment1, result);
    }

    @Test
    void testFindEnrolment_whenOneEnrolmentExist_returnsCorrectEnrolment() {
        Enrolment result = enrolmentService.findEnrolment(course1, studentOneEnrolment);
        assertEquals(enrolment5, result);
    }

    @Test
    void testFindEnrolment_whenNoEnrolmentsExist_returnsNoEnrolment() {
        Enrolment result = enrolmentService.findEnrolment(course1, studentNoEnrolments);
        assertNull(result);
    }

    @Test
    void testFindEnrolment_whenCourseIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.findEnrolment(null, studentNoEnrolments),
                "Exception was not thrown.");
    }

    @Test
    void testGetEnrolments_whenManyEnrolmentsExist_returnsCorrectEnrolments() {
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
    void testGetEnrolments_whenOneEnrolmentExists_returnsCorrectEnrolment() {
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
    void testGetEnrolments_whenNoEnrolmentsExist_returnsNoEnrolments() {
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
    void testGetEnrolments_whenStudentIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.getEnrolments(null),
                "Exception was not thrown.");
    }

    @Test
    void testCreateEnrolment_whenEnrolmentDoesNotExist_createsEnrolment() {
        Enrolment result = enrolmentService.createEnrolment(course1, studentNoEnrolments);
        verify(enrolmentRepository, times(1)).save(result);
        assertEquals(studentNoEnrolments, result.getStudent());
        assertEquals(course1, result.getCourse());
    }

    @Test
    void testCreateEnrolment_whenEnrolmentAlreadyExists_throwsEnrolmentAlreadyExistsException() {
        assertThrows(EnrolmentAlreadyExistsException.class, () -> enrolmentService.createEnrolment(course1, studentOneEnrolment),
                "Exception was not thrown.");
    }

    @Test
    void testCreateEnrolment_whenStudentIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.createEnrolment(course1, null),
                "Exception was not thrown.");
    }

    @Test
    void testCreateEnrolment_whenCourseIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.createEnrolment(null, studentNoEnrolments),
                "Exception was not thrown.");
    }

    @Test
    void testCreateEnrolment_whenStudentAndCourseAreNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> enrolmentService.createEnrolment(null, null),
                "Exception was not thrown.");
    }


}