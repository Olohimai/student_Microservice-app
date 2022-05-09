package uk.ac.leedsbeckett.student.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.leedsbeckett.student.exception.StudentAlreadyExistsException;
import uk.ac.leedsbeckett.student.model.Student;
import uk.ac.leedsbeckett.student.model.User;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest extends UserServiceUnitTest {

    @Test
    void Login() {
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(true);
        userService.setSecurityContext(securityContext);
        User userFound = userService.getLoggedInUser();
        assertEquals(userStudent, userFound);
    }

    @Test
    void NoAuthentication() {
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(false);
        userService.setSecurityContext(securityContext);
        User userFound = userService.getLoggedInUser();
        assertNull(userFound);
    }

    @Test
    void Search() {
        Student studentFound = userService.findStudentFromUser(userStudent);
        assertEquals(student, studentFound);
    }

    @Test
    void CanNotValue() {
        Student studentFound = userService.findStudentFromUser(userNotStudent);
        assertNull(studentFound);
    }

    @Test
    void FindStudent() {
        assertThrows(ConstraintViolationException.class, () -> userService.findStudentFromUser(null),
                "Exception was not thrown.");
    }

    @Test
    void CreateStudent() {
        assertThrows(ConstraintViolationException.class, () -> userService.createStudentFromUser(null),
                "Exception was not thrown.");
    }

    @Test
    void AlreadyStudent() {
        assertThrows(StudentAlreadyExistsException.class, () -> userService.createStudentFromUser(userStudent),
                "Exception was not thrown.");
    }

}
