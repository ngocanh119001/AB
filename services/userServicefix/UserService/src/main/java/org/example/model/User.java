package org.example.model;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class User {
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private List<String> roles;
    private Date createdAt;
    private Date updatedAt;
}
