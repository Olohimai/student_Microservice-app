package uk.ac.leedsbeckett.student.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.model.Role;
import uk.ac.leedsbeckett.student.model.Student;
import uk.ac.leedsbeckett.student.repository.StudentRepository;
import uk.ac.leedsbeckett.student.model.User;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

public class ProfileServiceTestBase {

    @MockBean
    protected StudentRepository studentRepository;
    @MockBean
    protected PortalService portalService;
    @Autowired
    protected ProfileService profileService;

    protected Student dataStudent;
    protected Student existingStudent;
    protected User user;

    @BeforeEach
    public void setUp() {
        createTestObjects();
        defineMockingBehaviour();
    }

    protected void createTestObjects() {

        Faker faker = new Faker();
        existingStudent = new Student(faker.name().firstName(), faker.name().lastName());
        existingStudent.setId(1L);
        user = new User(faker.name().username(), Role.STUDENT, "user@gmail.com", faker.ancient().god());
        dataStudent = new Student();
        dataStudent.setId(1L);
    }

    protected void defineMockingBehaviour() {
        Mockito.when(portalService.loadPortalUserDetails(any(User.class), any(Student.class), any(String.class)))
                .thenReturn(new ModelAndView());
        Mockito.when(studentRepository.findById(dataStudent.getId()))
                .thenReturn(Optional.of(dataStudent));
        Mockito.when(studentRepository.findById(existingStudent.getId()))
                .thenReturn(Optional.of(existingStudent));
        Mockito.when(studentRepository.saveAndFlush(any(Student.class)))
                .then(returnsFirstArg());
    }

    @AfterEach
    public void tearDown() {
        dataStudent = null;
        existingStudent = null;
        studentRepository = null;
        portalService = null;
        profileService = null;
    }
}
