package com.clientflow.crm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerForm {

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 120, message = "Company must not exceed 120 characters")
    private String company;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Pattern(
            regexp = "^$|^[0-9+() .-]{7,20}$",
            message = "Enter a valid phone number"
    )
    private String phone;

    @NotBlank(message = "Select a customer status")
    @Pattern(
            regexp = "ACTIVE|PENDING|INACTIVE",
            message = "Select a valid customer status"
    )
    private String status = "ACTIVE";

    public CustomerForm() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}