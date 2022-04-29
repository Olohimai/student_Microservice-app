package uk.ac.leedsbeckett.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.StudentNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class GraduationServiceTest extends GraduationServiceTestBase {

    @Test
    void testGetGraduationStatus_whenEligible_returnsCorrectModelAndView() {
        ModelAndView modelAndView = graduationService.getGraduationStatus(student);
        assertEquals("graduation", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertFalse((Boolean) modelAndView.getModel().get("balanceOutstanding"));
        assertEquals("eligible to graduate", modelAndView.getModel().get("message"));
    }

    @Test
    void testGetGraduationStatus_whenNotEligible_returnsCorrectModelAndView() {
        account.setHasOutstandingBalance(true);
        ModelAndView modelAndView = graduationService.getGraduationStatus(student);
        assertEquals("graduation", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertTrue((Boolean) modelAndView.getModel().get("balanceOutstanding"));
        assertEquals("ineligible to graduate", modelAndView.getModel().get("message"));
    }

    @Test
    void testGetGraduationStatus_whenStudentIsNull_throwsStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> graduationService.getGraduationStatus(null),
                "Exception was not thrown.");
    }

}