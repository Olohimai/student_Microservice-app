package uk.ac.leedsbeckett.student.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.ac.leedsbeckett.student.model.Course;
import uk.ac.leedsbeckett.student.model.Enrolment;
import uk.ac.leedsbeckett.student.repository.EnrolmentRepository;
import uk.ac.leedsbeckett.student.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class EnrolmentServiceTestBase {

    @MockBean
    protected EnrolmentRepository enrolmentRepository;
    @Autowired
    protected EnrolmentService enrolmentService;

    protected Course course1, course2, course3, course4, course5;
    protected List<Enrolment> enrolmentList;
    protected Student studentManyEnrolments;
    protected Student studentOneEnrolment;
    protected Student studentNoEnrolments;
    protected Enrolment enrolment1;
    protected Enrolment enrolment2;
    protected Enrolment enrolment3;
    protected Enrolment enrolment4;
    protected Enrolment enrolment5;

    @BeforeEach
    public void setUp() {
        createTestObjects();
        defineMockingBehaviour();
    }

    protected void createTestObjects() {

        Faker faker = new Faker();
        course1 = new Course(faker.educator().course(), faker.lorem().paragraph(), faker.number().randomDouble(2, 0, 5000));
        course2 = new Course(faker.educator().course(), faker.lorem().paragraph(), faker.number().randomDouble(2, 0, 5000));
        course3 = new Course(faker.educator().course(), faker.lorem().paragraph(), faker.number().randomDouble(2, 0, 5000));
        course4 = new Course(faker.educator().course(), faker.lorem().paragraph(), faker.number().randomDouble(2, 0, 5000));
        course5 = new Course(faker.educator().course(), faker.lorem().paragraph(), faker.number().randomDouble(2, 0, 5000));

        course1.setId(1L);
        course2.setId(2L);
        course3.setId(3L);
        course4.setId(4L);
        course5.setId(5L);

        studentManyEnrolments = new Student(faker.name().firstName(), faker.name().lastName());
        studentOneEnrolment = new Student(faker.name().firstName(), faker.name().lastName());
        studentNoEnrolments = new Student(faker.name().firstName(), faker.name().lastName());

        enrolment1 = new Enrolment(studentManyEnrolments, course1);
        enrolment2 = new Enrolment(studentManyEnrolments, course2);
        enrolment3 = new Enrolment(studentManyEnrolments, course3);
        enrolment4 = new Enrolment(studentManyEnrolments, course4);
        enrolment5 = new Enrolment(studentOneEnrolment, course1);

        enrolmentList = Arrays.asList(enrolment1, enrolment2, enrolment3, enrolment4);
    }

    protected void defineMockingBehaviour() {
        Mockito.when(enrolmentRepository.findEnrolmentByCourseAndStudent(course1, studentManyEnrolments))
                .thenReturn(enrolment1);
        Mockito.when(enrolmentRepository.findEnrolmentByCourseAndStudent(course2, studentManyEnrolments))
                .thenReturn(enrolment2);
        Mockito.when(enrolmentRepository.findEnrolmentByCourseAndStudent(course3, studentManyEnrolments))
                .thenReturn(enrolment3);
        Mockito.when(enrolmentRepository.findEnrolmentByCourseAndStudent(course4, studentManyEnrolments))
                .thenReturn(enrolment4);
        Mockito.when(enrolmentRepository.findEnrolmentByCourseAndStudent(course1, studentOneEnrolment))
                .thenReturn(enrolment5);
        Mockito.when(enrolmentRepository.findEnrolmentByCourseAndStudent(eq(course5), any()))
                .thenReturn(null);
        Mockito.when(enrolmentRepository.findEnrolmentByStudent(studentManyEnrolments))
                        .thenReturn(enrolmentList);
        Mockito.when(enrolmentRepository.findEnrolmentByStudent(studentOneEnrolment))
                        .thenReturn(List.of(enrolment5));
        Mockito.when(enrolmentRepository.findEnrolmentByStudent(studentNoEnrolments))
                        .thenReturn(new ArrayList<>());
        Mockito.when(enrolmentRepository.save(any(Enrolment.class)))
                .then(returnsFirstArg());
    }

    @AfterEach
    public void tearDown() {
        enrolmentList.forEach(enrolment -> enrolment = null);
        enrolmentList = null;
        studentManyEnrolments = null;
        studentOneEnrolment = null;
        studentNoEnrolments = null;
        course1 = null;
        course2 = null;
        course3 = null;
        course4 = null;
        course5 = null;
        enrolmentRepository = null;
        enrolmentService = null;
    }
}
