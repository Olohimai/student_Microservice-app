package uk.ac.leedsbeckett.student.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {
    private Long id;
    private String reference;
    private Double amount;
    private LocalDate dueDate;
    private Type type;
    private Status status;
    private Account account;

    public enum Type {
        LIBRARY_FINE,
        TUITION_FEES
    }

    enum Status {
        OUTSTANDING,
        PAID,
        CANCELLED
    }
}