package uk.ac.leedsbeckett.student.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import uk.ac.leedsbeckett.student.model.*;
import uk.ac.leedsbeckett.student.repository.StudentRepository;
import uk.ac.leedsbeckett.student.repository.UserRepository;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTestBase {

    @MockBean
    protected StudentRepository studentRepository;
    @MockBean
    protected UserRepository userRepository;
    @MockBean
    protected SecurityContext securityContext;
    @MockBean
    protected Authentication authentication;
    @Autowired
    protected UserService userService;

    protected User userStudent;
    protected User userNotStudent;
    protected Student student;
    protected Student newStudent;

    @BeforeEach
    public void setUp() {
        createTestObjects();
        defineMockingBehaviour();
    }

    protected void createTestObjects() {
        Faker faker = new Faker();
        userStudent = new User(faker.name().username(), Role.STUDENT, "user@gmail.com", faker.animal().name());
        userStudent.setId(1L);
        student = new Student(faker.name().firstName(), faker.name().lastName());
        userStudent.setStudent(student);
        userNotStudent = new User(faker.name().username(), Role.STUDENT, "uns@gmail.com", faker.animal().name());
        userNotStudent.setId(2L);
        newStudent = new Student(faker.name().firstName(), faker.name().lastName());
    }

    protected void defineMockingBehaviour() {
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(authentication);
        Mockito.when(userRepository.findUserByUserName(userStudent.getUserName()))
                .thenReturn(userStudent);
        Mockito.when(authentication.getName())
                .thenReturn(userStudent.getUserName());
        Mockito.when(studentRepository.findByUserId(1L))
                .thenReturn(student);
        Mockito.when(studentRepository.findByUserId(2L))
                .thenReturn(null);
        Mockito.when(userRepository.saveAndFlush(any(User.class)))
                .then(returnsFirstArg());
    }

    @AfterEach
    public void tearDown() {
        userStudent = null;
        userNotStudent = null;
        student = null;
        newStudent = null;
        userService = null;
    }
}
