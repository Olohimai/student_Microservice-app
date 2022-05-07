package uk.ac.leedsbeckett.student.service;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
