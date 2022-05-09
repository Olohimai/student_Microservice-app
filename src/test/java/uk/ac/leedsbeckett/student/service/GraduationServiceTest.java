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
class GraduationServiceTest extends GraduationServiceUnitTest {

    @Test
    void GraduationStatus() {
        ModelAndView modelAndView = graduationService.getGraduationStatus(student);
        assertEquals("graduation", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertFalse((Boolean) modelAndView.getModel().get("balanceOutstanding"));
        assertEquals("Congratulations you are eligible to graduate", modelAndView.getModel().get("message"));
    }

    @Test
    void NotEligibleToGraduate() {
        account.setHasOutstandingBalance(true);
        ModelAndView modelAndView = graduationService.getGraduationStatus(student);
        assertEquals("graduation", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertTrue((Boolean) modelAndView.getModel().get("balanceOutstanding"));
        assertEquals("Sorry you ineligible to graduate at the moment", modelAndView.getModel().get("message"));
    }

    @Test
    void StudentIsNull() {
        assertThrows(StudentNotFoundException.class, () -> graduationService.getGraduationStatus(null),
                "Exception was not thrown.");
    }

}
