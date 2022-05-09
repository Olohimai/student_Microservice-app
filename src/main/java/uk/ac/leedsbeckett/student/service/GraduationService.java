package uk.ac.leedsbeckett.student.service;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.StudentNotFoundException;
import uk.ac.leedsbeckett.student.model.Account;
import uk.ac.leedsbeckett.student.model.Student;

@Validated
@Component
public class GraduationService {

    private final IntegrationService integrationService;

    public GraduationService(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    public ModelAndView getGraduationStatus(Student student) {
        if (student == null) {
            throw new StudentNotFoundException();
        }
        Account account = integrationService.getStudentPaymentStatus(student);
        ModelAndView modelAndView = new ModelAndView("graduation");
        modelAndView.addObject("balanceOutstanding", account.isHasOutstandingBalance());
        modelAndView.addObject("message", account.isHasOutstandingBalance() ? "Sorry you ineligible to graduate at the moment" : "Congratulations you are eligible to graduate");
        return modelAndView;
    }
}
