package org.example.model;

import lombok.Data;
import java.util.Date;

@Data
public class BankAccount {
    private String accountNumber;
    private Date expiryDate;
    private String accountHolderName;
    private String billingAddress;
    private double balance;
}
