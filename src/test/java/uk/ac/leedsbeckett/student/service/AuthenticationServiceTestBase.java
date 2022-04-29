package uk.ac.leedsbeckett.student.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.ac.leedsbeckett.student.model.*;
import uk.ac.leedsbeckett.student.repository.UserRepository;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

public class AuthenticationServiceTestBase {

    @MockBean
    protected UserRepository userRepository;
    @Autowired
    protected AuthenticationService authenticationService;

    protected User user;
    protected User invalidUserSameUsername;
    protected User invalidUserSameEmail;
    protected User invalidUserSameUsernameAndEmail;

    @BeforeEach
    public void setUp() {
        createTestObjects();
        defineMockingBehaviour();
    }

    protected void createTestObjects() {
        Faker faker = new Faker();
        user = new User(faker.name().username(), Role.STUDENT, "user@gmail.com", faker.animal().name());
        user.setId(1L);
        invalidUserSameUsername = new User(faker.name().username(), Role.STUDENT, "invalidUser1@gmail.com", faker.animal().name());
        invalidUserSameEmail = new User(faker.name().username(), Role.STUDENT, "invalidUser2@gmail.com", faker.animal().name());
        invalidUserSameUsernameAndEmail = new User(faker.name().username(), Role.STUDENT, "invalidUser3@gmail.com", faker.animal().name());
    }

    protected void defineMockingBehaviour() {
        Mockito.when(userRepository.save(any(User.class)))
                .then(returnsFirstArg());
        Mockito.when(userRepository.findUserByUserName(user.getUserName()))
                .thenReturn(null);
        Mockito.when(userRepository.findUserByUserName(invalidUserSameUsername.getUserName()))
                .thenReturn(invalidUserSameUsername);
        Mockito.when(userRepository.findUserByEmail(invalidUserSameEmail.getEmail()))
                .thenReturn(invalidUserSameEmail);
        Mockito.when(userRepository.findUserByUserName(invalidUserSameUsernameAndEmail.getUserName()))
                .thenReturn(invalidUserSameUsernameAndEmail);
        Mockito.when(userRepository.findUserByEmail(invalidUserSameUsernameAndEmail.getEmail()))
                .thenReturn(invalidUserSameUsernameAndEmail);
    }

    @AfterEach
    public void tearDown() {
        user = null;
        userRepository = null;
        authenticationService = null;
    }
}
