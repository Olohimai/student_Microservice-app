package uk.ac.leedsbeckett.student.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.ac.leedsbeckett.student.model.Role;
import uk.ac.leedsbeckett.student.model.Student;
import uk.ac.leedsbeckett.student.model.User;
import uk.ac.leedsbeckett.student.model.UserRepository;
import uk.ac.leedsbeckett.student.service.PortalService;

public class PortalServiceUnitTest {

    @MockBean
    protected UserRepository userRepository;
    @Autowired
    protected PortalService portalService;

    protected User userStudent;
    protected User userNotStudent;
    protected Student student;


    @BeforeEach
    public void setUp() {
        create();
        MockTheBehaviour();
    }

    protected void create() {

        Faker faker = new Faker();
        userStudent = new User(faker.name().username(), Role.STUDENT, "user@gmail.com", faker.animal().name());
        userStudent.setId(1L);
        student = new Student(faker.name().firstName(), faker.name().lastName());
        userStudent.setStudent(student);
        userNotStudent = new User(faker.name().username(), Role.STUDENT, "uns@gmail.com", faker.animal().name());
        userNotStudent.setId(2L);
    }

    protected void MockTheBehaviour() {
        Mockito.when(userRepository.findUserByUserName(userStudent.getUserName()))
                .thenReturn(userStudent);
        Mockito.when(userRepository.findUserByUserName(userNotStudent.getUserName()))
                .thenReturn(userNotStudent);
    }

    @AfterEach
    public void tearDown() {
        userStudent = null;
        userNotStudent = null;
        student = null;
        userRepository = null;
        portalService = null;
    }
}
