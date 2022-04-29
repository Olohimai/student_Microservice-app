package uk.ac.leedsbeckett.student.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.ac.leedsbeckett.student.YamlSourceFactory;
import uk.ac.leedsbeckett.student.model.Account;
import uk.ac.leedsbeckett.student.model.Invoice;
import uk.ac.leedsbeckett.student.model.Student;

import javax.validation.constraints.NotNull;

@Component
@PropertySource(value = "classpath:integrations.yml", factory = YamlSourceFactory.class)
public class IntegrationService {

    private final RestTemplate restTemplate;
    @Value("${finance.host}")
    private String financeHost;
    @Value("${finance.student.create}")
    private String studentCreatedFinanceSubscriber;
    @Value("${library.host}")
    private String libraryHost;
    @Value("${library.student.create}")
    private String studentCreatedLibrarySubscriber;
    @Value("${finance.course.enrol}")
    private String courseEnrolmentFinanceSubscriber;
    @Value("${finance.account.status}")
    private String accountStatusPublisher;

    public IntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void notifyStudentCreated(@NotNull Account account) {
        restTemplate.postForObject(financeHost + studentCreatedFinanceSubscriber, account, Account.class);
        restTemplate.postForObject(libraryHost + studentCreatedLibrarySubscriber, account, Account.class);
    }

    public Invoice createCourseFeeInvoice(@NotNull Invoice invoice) {
        return restTemplate.postForObject(financeHost + courseEnrolmentFinanceSubscriber, invoice, Invoice.class);
    }

    public Account getStudentPaymentStatus(@NotNull Student student) {
        return restTemplate.getForObject(financeHost + accountStatusPublisher + student.getStudentId(), Account.class);
    }
}
