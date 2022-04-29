package uk.ac.leedsbeckett.student.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.ac.leedsbeckett.student.model.*;

public class GraduationServiceTestBase {

    @MockBean
    protected IntegrationService integrationService;
    @Autowired
    protected GraduationService graduationService;

    protected Student student;
    protected Account account;


    @BeforeEach
    public void setUp() {
        createTestObjects();
        defineMockingBehaviour();
    }

    protected void createTestObjects() {
        Faker faker = new Faker();
        student = new Student(faker.name().firstName(), faker.name().lastName());
        student.setId(1L);
        student.setStudentId("c6666666");
        account = new Account();
        account.setStudentId("c6666666");
        account.setHasOutstandingBalance(false);
    }

    protected void defineMockingBehaviour() {
        Mockito.when(integrationService.getStudentPaymentStatus(student))
                .thenReturn(account);
    }

    @AfterEach
    public void tearDown() {
        student = null;
        account = null;
        integrationService = null;
        graduationService = null;
    }
}
