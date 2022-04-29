package uk.ac.leedsbeckett.student.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import uk.ac.leedsbeckett.student.exception.StudentAlreadyExistsException;
import uk.ac.leedsbeckett.student.model.*;
import uk.ac.leedsbeckett.student.repository.StudentRepository;
import uk.ac.leedsbeckett.student.repository.UserRepository;

import javax.validation.constraints.NotNull;

@Validated
@Component
public class UserService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final IntegrationService integrationService;
    SecurityContext securityContext;

    public UserService(StudentRepository studentRepository, UserRepository userRepository, IntegrationService integrationService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.integrationService = integrationService;
    }

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    public User getLoggedInUser() {
        if (securityContext == null || securityContext.getAuthentication() == null) {
            setSecurityContext(SecurityContextHolder.getContext());
        }
        Authentication authentication = securityContext.getAuthentication();
        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            user = userRepository.findUserByUserName(authentication.getName());
        }
        return user;
    }

    public Student findStudentFromUser(@NotNull User user) {
        return studentRepository.findByUserId(user.getId());
    }

    public User createStudentFromUser(@NotNull User user) {
        if (studentRepository.findByUserId(user.getId()) != null) {
            throw new StudentAlreadyExistsException(user.getUserName());
        }
        Student student = new Student();
        student.populateStudentId();
        user.setStudent(student);
        userRepository.saveAndFlush(user);
        notifySubscribers(student);
        return user;
    }

    private void notifySubscribers(Student student) {
        Account account = new Account();
        account.setStudentId(student.getStudentId());
        integrationService.notifyStudentCreated(account);
    }
}
