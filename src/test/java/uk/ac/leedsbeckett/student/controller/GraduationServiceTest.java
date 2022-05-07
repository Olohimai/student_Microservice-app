package uk.ac.leedsbeckett.student.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.StudentException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class GraduationServiceTest extends GraduationServiceIntTest {

    @Test
    void testGraduationStatus() {
        assertThrows(StudentException.class, () -> graduationService.getGraduationStatus(null),
            "Exception was not thrown.");
    }
    @Test
    void testEligibleNotToGraduate() {
        account.setHasOutstandingBalance(true);
        ModelAndView modelAndView = graduationService.getGraduationStatus(student);
        assertEquals("graduation", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertTrue((Boolean) modelAndView.getModel().get("balanceOutstanding"));
        assertEquals("You have an outstanding fee therefore, you are ineligible to graduate ", modelAndView.getModel().get("message"));
    }
    @Test
    void testEligibilityToGraduate() {
        ModelAndView modelAndView = graduationService.getGraduationStatus(student);
        assertEquals("graduation", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertFalse((Boolean) modelAndView.getModel().get("balanceOutstanding"));
        assertEquals("Congratulations You are eligible to graduate no outstanding fees left", modelAndView.getModel().get("message"));
    }





}
