package uk.ac.leedsbeckett.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class PortalServiceTest extends PortalServiceTestBase {

    @Test
    void testLoadUserByUsername_whenExists_returnsUser() {
        UserDetails details = portalService.loadUserByUsername(userStudent.getUserName());
        assertEquals(userStudent.getUserName(), details.getUsername());
        assertEquals(userStudent.getPassword(), details.getPassword());
    }

    @Test
    void testLoadUserByUsername_whenStudentDoesNotExist_throwsUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> portalService.loadUserByUsername("Rampage the blue parrotlet"),
                "Exception was not thrown.");
    }

    @Test
    void testLoadUserByUsername_whenUsernameIsNull_throwsUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> portalService.loadUserByUsername(null),
                "Exception was not thrown.");
    }

    @Test
    void testLoadUserByUsername_whenUsernameIsEmpty_throwsUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> portalService.loadUserByUsername(""),
                "Exception was not thrown.");
    }

    @Test
    void testLoadPortalUserDetails_whenUserIsStudent_returnsCorrectModelAndView() {
        ModelAndView modelAndView = portalService.loadPortalUserDetails(userStudent, student, "home");
        assertEquals(modelAndView.getViewName(), "home");
        assertNotNull(modelAndView.getModel());
        assertEquals(3, modelAndView.getModel().size());
        assertEquals(userStudent, modelAndView.getModel().get("user"));
        assertEquals(student, modelAndView.getModel().get("student"));
        assertTrue((Boolean) modelAndView.getModel().get("showFirstName"));
    }

    @Test
    void testLoadPortalUserDetails_whenUserIsNotStudent_returnsCorrectModelAndView() {
        ModelAndView modelAndView = portalService.loadPortalUserDetails(userNotStudent, null, "home");
        assertEquals(modelAndView.getViewName(), "home");
        assertNotNull(modelAndView.getModel());
        assertEquals(2, modelAndView.getModel().size());
        assertEquals(userNotStudent, modelAndView.getModel().get("user"));
        assertFalse((Boolean) modelAndView.getModel().get("showFirstName"));
    }

    @Test
    void testLoadPortalUserDetails_whenUserIsStudent_andFirstNameIsNull_returnsCorrectModelAndView() {
        student.setForename(null);
        ModelAndView modelAndView = portalService.loadPortalUserDetails(userStudent, student, "home");
        assertEquals(modelAndView.getViewName(), "home");
        assertNotNull(modelAndView.getModel());
        assertEquals(3, modelAndView.getModel().size());
        assertEquals(userStudent, modelAndView.getModel().get("user"));
        assertEquals(student, modelAndView.getModel().get("student"));
        assertFalse((Boolean) modelAndView.getModel().get("showFirstName"));
    }

    @Test
    void testLoadPortalUserDetails_whenUserIsNull_redirectsToLoginPage() {
        ModelAndView modelAndView = portalService.loadPortalUserDetails(null, student, "home");
        assertEquals(modelAndView.getViewName(), "redirect:/login");
    }

    @Test
    void testLoadPortalUserDetails_whenViewIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> portalService.loadPortalUserDetails(userStudent, student, null),
                "Exception was not thrown.");
    }

}