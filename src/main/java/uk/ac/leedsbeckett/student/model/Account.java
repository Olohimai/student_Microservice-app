package uk.ac.leedsbeckett.student.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    private Long id;
    private String studentId;
    private boolean hasOutstandingBalance;
}
