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
class UserServiceTest extends UserServiceTestBase {

    @Test
    void testGetLoggedInUser_whenAuthenticated_returnsUser() {
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(true);
        userService.setSecurityContext(securityContext);
        User userFound = userService.getLoggedInUser();
        assertEquals(userStudent, userFound);
    }

    @Test
    void testGetLoggedInUser_whenNotAuthenticated_returnsNull() {
        Mockito.when(authentication.isAuthenticated())
                .thenReturn(false);
        userService.setSecurityContext(securityContext);
        User userFound = userService.getLoggedInUser();
        assertNull(userFound);
    }

    @Test
    void testFindStudentFromUser_whenStudentExists_returnsStudent() {
        Student studentFound = userService.findStudentFromUser(userStudent);
        assertEquals(student, studentFound);
    }

    @Test
    void testFindStudentFromUser_whenStudentDoesNotExist_returnsNull() {
        Student studentFound = userService.findStudentFromUser(userNotStudent);
        assertNull(studentFound);
    }

    @Test
    void testFindStudentFromUser_whenUserIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> userService.findStudentFromUser(null),
                "Exception was not thrown.");
    }

    @Test
    void testCreateStudentFromUser_whenUserIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> userService.createStudentFromUser(null),
                "Exception was not thrown.");
    }

    @Test
    void testCreateStudentFromUser_whenAlreadyStudent_throwsStudentAlreadyExistsException() {
        assertThrows(StudentAlreadyExistsException.class, () -> userService.createStudentFromUser(userStudent),
                "Exception was not thrown.");
    }

}