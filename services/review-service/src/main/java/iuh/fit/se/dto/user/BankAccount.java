package iuh.fit.se.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BankAccount {
    private String bankNumber;
    private LocalDate dueDate;
    private String ownerName;
    private String address;
    private Integer zipCode;
}
