package uk.ac.leedsbeckett.student.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.StudentNotFoundException;
import uk.ac.leedsbeckett.student.model.Student;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class ProfileServiceTest extends ProfileServiceIntTest {

    @Test
    void testProfile() {
        profileService.getProfile(user, existingStudent, "test");
        verify(portalService, times(1)).loadPortalUserDetails(user, existingStudent, "test");
    }

    @Test
    void testNull() {
        assertThrows(ConstraintViolationException.class, () -> profileService.getProfile(user, existingStudent, null),
                "Throws an exception.");
    }

    @Test
    void testEmptyProfile() {
        assertThrows(ConstraintViolationException.class, () -> profileService.getProfile(user, existingStudent, ""),
                "Throws an exception.");
    }

    @Test
    void testExistingStudentID() {
        ModelAndView modelAndView = profileService.getProfileToEdit(existingStudent.getId());
        assertEquals(modelAndView.getViewName(), "update");
        assertNotNull(modelAndView.getModel());
        assertEquals(1, modelAndView.getModel().size());
        assertEquals(existingStudent, modelAndView.getModel().get("student"));
    }

    @Test
    void testNewStudentID() {
        assertThrows(StudentNotFoundException.class, () -> profileService.getProfileToEdit(9999L),
                "Throws an exception.");
    }

    @Test
    void testStudentIdEqualNull() {
        assertThrows(StudentNotFoundException.class, () -> profileService.getProfileToEdit(null),
                "Throws an exception");
    }

    @Test
    void testEditProfile() {
        dataStudent.setFirstname("Peace");
        dataStudent.setSurname("Akalumhe");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertEquals("Peace", returnedStudent.getFirstname());
        assertEquals("Akalumhe", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testDetailsCreated() {
        dataStudent.setFirstname("Peace");
        dataStudent.setSurname("Akalumhe");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertEquals("Peace", returnedStudent.getFirstname());
        assertEquals("Akalumhe", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditFirstname() {
        dataStudent.setFirstname("Peace");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getSurname());
        assertEquals("Peace", returnedStudent.getFirstname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditFirstnameAndSurnameEmpty() {
        dataStudent.setFirstname("Peace");
        dataStudent.setSurname("");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getSurname());
        assertEquals("Peace", returnedStudent.getFirstname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditSurname() {
        dataStudent.setSurname("Akalumhe");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getFirstname());
        assertEquals("Akalumhe", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfileSurname() {
        dataStudent.setSurname("Akalumhe");
        dataStudent.setFirstname("");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getFirstname());
        assertEquals("Akalumhe", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfileStudentID() {
        dataStudent.setStudentId("c1234567");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotEquals("c1234567", returnedStudent.getStudentId());
        assertStudentNotUpdated(modelAndView, returnedStudent);
    }


    private void assertStudentUpdated(ModelAndView modelAndView, Student returnedStudent) {
        assertCorrectModelAndView(modelAndView, returnedStudent);
        assertTrue((Boolean) modelAndView.getModel().get("updated"));
        assertEquals("You have Successfully Updated your Profile", modelAndView.getModel().get("message"));
    }

    private void assertStudentNotUpdated(ModelAndView modelAndView, Student returnedStudent) {
        assertCorrectModelAndView(modelAndView, returnedStudent);
        assertNotNull(returnedStudent.getFirstname());
        assertNotNull(returnedStudent.getSurname());
        assertFalse((Boolean) modelAndView.getModel().get("updated"));
        assertEquals("Your Profile Did not update because name already in use, Enter a new name!!!", modelAndView.getModel().get("message"));
    }

    private void assertCorrectModelAndView(ModelAndView modelAndView, Student returnedStudent) {
        assertEquals(modelAndView.getViewName(), "profile");
        assertNotNull(modelAndView.getModel());
        assertEquals(4, modelAndView.getModel().size());
        assertNotNull(returnedStudent.getStudentId());
        assertNotNull(returnedStudent.getId());
        assertTrue((Boolean) modelAndView.getModel().get("isStudent"));
    }

}
