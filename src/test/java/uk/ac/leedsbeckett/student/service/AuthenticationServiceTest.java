package uk.ac.leedsbeckett.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class AuthenticationServiceTest extends AuthenticationServiceTestBase {

    @Test
    void testShowLoginError_returnsCorrectView() {
        Model model = new ExtendedModelMap();
        String result = authenticationService.showLoginError(model);
        assertEquals("login", result);
        assertNotNull(model.getAttribute("loginError"));
        assertTrue((Boolean) model.getAttribute("loginError"));
    }

    @Test
    void testShowRegistrationForm_returnsCorrectView() {
        Model model = new ExtendedModelMap();
        String result = authenticationService.showRegistrationForm(model);
        assertEquals("register", result);
        assertNotNull(model.getAttribute("user"));
        assertTrue(model.getAttribute("user") instanceof User);
    }

    @Test
    void testRegisterNewUser_createsUser_andReturnsCorrectView() {
        ModelAndView result = authenticationService.registerNewUser(user);
        assertEquals("register-success", result.getViewName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterNewUserWithExistingUsername_doesNotCreateUser_andDisplaysCorrectMessage() {
        ModelAndView result = authenticationService.registerNewUser(invalidUserSameUsername);
        assertEquals("register", result.getViewName());
        verify(userRepository, times(0)).save(invalidUserSameUsername);
        assertEquals("There is already an account with the username: " +
                invalidUserSameUsername.getUserName() +
                ".", result.getModel().get("message"));
    }

    @Test
    void testRegisterNewUserWithExistingEmail_doesNotCreateUser_andDisplaysCorrectMessage() {
        ModelAndView result = authenticationService.registerNewUser(invalidUserSameEmail);
        assertEquals("register", result.getViewName());
        verify(userRepository, times(0)).save(invalidUserSameEmail);
        assertEquals("There is already an account with the e-mail address: " +
                invalidUserSameEmail.getEmail() +
                ".", result.getModel().get("message"));
    }

    @Test
    void testRegisterNewUserWithExistingUsernameAndEmail_doesNotCreateUser_andDisplaysCorrectMessage() {
        ModelAndView result = authenticationService.registerNewUser(invalidUserSameUsernameAndEmail);
        assertEquals("register", result.getViewName());
        verify(userRepository, times(0)).save(invalidUserSameUsernameAndEmail);
        assertEquals("There is already an account with the username: " +
                invalidUserSameUsernameAndEmail.getUserName() +
                " and the e-mail address: " + invalidUserSameUsernameAndEmail.getEmail() +
                ".", result.getModel().get("message"));
    }

}