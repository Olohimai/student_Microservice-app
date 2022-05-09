package uk.ac.leedsbeckett.student.service;

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
class ProfileServiceTest extends ProfileServiceUnitTest {

    @Test
    void GetProfile() {
        profileService.getProfile(user, existingStudent, "test");
        verify(portalService, times(1)).loadPortalUserDetails(user, existingStudent, "test");
    }

    @Test
    void Null() {
        assertThrows(ConstraintViolationException.class, () -> profileService.getProfile(user, existingStudent, null),
                "Exception was not thrown.");
    }

    @Test
    void Empty() {
        assertThrows(ConstraintViolationException.class, () -> profileService.getProfile(user, existingStudent, ""),
                "Exception was not thrown.");
    }

    @Test
    void ExistingStudentId() {
        ModelAndView modelAndView = profileService.getProfileToEdit(existingStudent.getId());
        assertEquals(modelAndView.getViewName(), "profile-edit");
        assertNotNull(modelAndView.getModel());
        assertEquals(1, modelAndView.getModel().size());
        assertEquals(existingStudent, modelAndView.getModel().get("student"));
    }

    @Test
    void NonExistingStudentId() {
        assertThrows(StudentNotFoundException.class, () -> profileService.getProfileToEdit(9999L),
                "Exception was not thrown.");
    }

    @Test
    void NullStudentId() {
        assertThrows(StudentNotFoundException.class, () -> profileService.getProfileToEdit(null),
                "Exception was not thrown.");
    }

    @Test
    void DetailsUpdated() {
        dataStudent.setForename("Olohimai");
        dataStudent.setSurname("Akalumhe");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertEquals("Olohimai", returnedStudent.getForename());
        assertEquals("Akalumhe", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void EditProfile() {
        dataStudent.setForename("Olohimai");
        dataStudent.setSurname("Akalumhe");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertEquals("Olohimai", returnedStudent.getForename());
        assertEquals("Akalumhe", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void EditForename() {
        dataStudent.setForename("Olohimai");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getSurname());
        assertEquals("Olohimai", returnedStudent.getForename());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void EditForenameAndSurname() {
        dataStudent.setForename("Olohimai");
        dataStudent.setSurname("");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getSurname());
        assertEquals("Olohimai", returnedStudent.getForename());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void EditProfileSurname() {
        dataStudent.setSurname("Akalumhe");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getForename());
        assertEquals("Akalumhe", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfileBlankForename() {
        dataStudent.setSurname("Akalumhe");
        dataStudent.setForename("");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getForename());
        assertEquals("Akalumhe", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void updateStudentId() {
        dataStudent.setStudentId("c7268697");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotEquals("c7268697", returnedStudent.getStudentId());
        assertStudentNotUpdated(modelAndView, returnedStudent);
    }


    private void assertStudentUpdated(ModelAndView modelAndView, Student returnedStudent) {
        assertCorrectModelAndView(modelAndView, returnedStudent);
        assertTrue((Boolean) modelAndView.getModel().get("updated"));
        assertEquals("Profile updated", modelAndView.getModel().get("message"));
    }

    private void assertStudentNotUpdated(ModelAndView modelAndView, Student returnedStudent) {
        assertCorrectModelAndView(modelAndView, returnedStudent);
        assertNotNull(returnedStudent.getForename());
        assertNotNull(returnedStudent.getSurname());
        assertFalse((Boolean) modelAndView.getModel().get("updated"));
        assertEquals("Profile not updated", modelAndView.getModel().get("message"));
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
