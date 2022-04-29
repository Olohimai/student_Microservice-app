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
class ProfileServiceTest extends ProfileServiceTestBase {

    @Test
    void testGetProfile_callsPortalServiceMethod_once() {
        profileService.getProfile(user, existingStudent, "dummy");
        verify(portalService, times(1)).loadPortalUserDetails(user, existingStudent, "dummy");
    }

    @Test
    void testGetProfile_whenViewIsNull_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> profileService.getProfile(user, existingStudent, null),
                "Exception was not thrown.");
    }

    @Test
    void testGetProfile_whenViewIsEmpty_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> profileService.getProfile(user, existingStudent, ""),
                "Exception was not thrown.");
    }

    @Test
    void testGetProfileToEdit_withExistingStudentId_returnsCorrectModelAndView() {
        ModelAndView modelAndView = profileService.getProfileToEdit(existingStudent.getId());
        assertEquals(modelAndView.getViewName(), "profile-edit");
        assertNotNull(modelAndView.getModel());
        assertEquals(1, modelAndView.getModel().size());
        assertEquals(existingStudent, modelAndView.getModel().get("student"));
    }

    @Test
    void testGetProfileToEdit_withNonExistingStudentId_throwsStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> profileService.getProfileToEdit(9999L),
                "Exception was not thrown.");
    }

    @Test
    void testGetProfileToEdit_withNullStudentId_throwsStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> profileService.getProfileToEdit(null),
                "Exception was not thrown.");
    }

    @Test
    void testEditProfile_withStudentDetailsModified_returnsCorrectModelAndView() {
        dataStudent.setForename("Mohammed");
        dataStudent.setSurname("Salah");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertEquals("Mohammed", returnedStudent.getForename());
        assertEquals("Salah", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfile_withStudentDetailsCreated_returnsCorrectModelAndView() {
        dataStudent.setForename("Mohammed");
        dataStudent.setSurname("Salah");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertEquals("Mohammed", returnedStudent.getForename());
        assertEquals("Salah", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfile_withOnlyForenameSupplied_onlyUpdatesForename_andReturnsCorrectModelAndView() {
        dataStudent.setForename("Mohammed");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getSurname());
        assertEquals("Mohammed", returnedStudent.getForename());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfile_withForenameSuppliedAndSurnameBlank_onlyUpdatesForename_andReturnsCorrectModelAndView() {
        dataStudent.setForename("Mohammed");
        dataStudent.setSurname("");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getSurname());
        assertEquals("Mohammed", returnedStudent.getForename());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfile_withOnlySurnameSupplied_returnsCorrectModelAndView() {
        dataStudent.setSurname("Salah");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getForename());
        assertEquals("Salah", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfile_withOnlySurnameSuppliedAndForenameBlank_onlyUpdatesSurname_andReturnsCorrectModelAndView() {
        dataStudent.setSurname("Salah");
        dataStudent.setForename("");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotNull(returnedStudent.getForename());
        assertEquals("Salah", returnedStudent.getSurname());
        assertStudentUpdated(modelAndView, returnedStudent);
    }

    @Test
    void testEditProfile_withStudentIdSupplied_doesNotUpdateStudent_andReturnsCorrectModelAndView() {
        dataStudent.setStudentId("c1234567");
        ModelAndView modelAndView = profileService.editProfile(dataStudent);
        Student returnedStudent = (Student) modelAndView.getModel().get("student");
        assertNotEquals("c1234567", returnedStudent.getStudentId());
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