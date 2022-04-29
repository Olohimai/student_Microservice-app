package uk.ac.leedsbeckett.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import uk.ac.leedsbeckett.student.repository.CourseRepository;
import uk.ac.leedsbeckett.student.repository.EnrolmentRepository;
import uk.ac.leedsbeckett.student.repository.StudentRepository;
import uk.ac.leedsbeckett.student.repository.UserRepository;

import java.util.Locale;

@Configuration
class MiscellaneousBeans {

    private static final Logger log = LoggerFactory.getLogger(MiscellaneousBeans.class);

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.UK);
        return sessionLocaleResolver;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    CommandLineRunner initDatabase(StudentRepository studentRepository, UserRepository userRepository, CourseRepository courseRepository, EnrolmentRepository enrolmentRepository) {
        return args -> {
//            Student jesse = new Student("Jesse", "Pinkman");
//            User walterWhite = new User("walterwhite",  Role.STUDENT, "w.white@gmail.com", "password");
//            Student walter = new Student("Walter", "White");
//            walterWhite.setStudent(walter);
//            userRepository.save(walterWhite);
//            studentRepository.save(jesse);
//            userRepository.findAll().forEach(user -> log.info("Preloaded " + user));
//            studentRepository.findAll().forEach(student -> log.info("Preloaded " + student));
//
//            Course programming = new Course("Introduction to Programming", "An introductory course covering the fundamentals of object-oriented programming.", 150.00);
//            Course ocpjp = new Course("OCPJP Exam Preparation", "A course covering the learning objectives for the latest OCPJP certification.", 350.00);
//            Course springBoot = new Course("Spring Boot", "An intermediate course on how to make the most of Spring Boot's latest functionality.", 200.00);
//            Course chemistry = new Course("Advanced Chemistry", "An advanced course covering the in-lab production of medical-grade synthetic substances.", 1150.00);
//            courseRepository.save(programming);
//            courseRepository.save(ocpjp);
//            courseRepository.save(springBoot);
//            courseRepository.save(chemistry);
//            courseRepository.findAll().forEach(course -> log.info("Preloaded " + course));
//
//            enrolmentRepository.save(new Enrolment(jesse, programming));
//            enrolmentRepository.save(new Enrolment(walter, springBoot));
//            enrolmentRepository.save(new Enrolment(walter, chemistry));
//            courseRepository.findAll().forEach(enrolment -> log.info("Preloaded " + enrolment));
        };
    }
}