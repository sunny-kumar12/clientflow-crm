
package com.clientflow.crm.dto;

import com.clientflow.crm.entity.Lead.LeadStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class LeadForm {

    @NotBlank(message = "Lead name is required")
    @Size(max = 120, message = "Name cannot exceed 120 characters")
    private String name;

    @Size(max = 150)
    private String company;

    @Email(message = "Enter a valid email address")
    @Size(max = 150)
    private String email;

    @Size(max = 25)
    private String phone;

    @Size(max = 50)
    private String source;

    @NotNull(message = "Select a lead status")
    private LeadStatus status = LeadStatus.NEW;

    @NotNull(message = "Estimated value is required")
    @DecimalMin(value = "0.00", message = "Value cannot be negative")
    private BigDecimal estimatedValue = BigDecimal.ZERO;

    @Size(max = 2000)
    private String notes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LeadStatus getStatus() { return status; }
    public void setStatus(LeadStatus status) { this.status = status; }

    public BigDecimal getEstimatedValue() { return estimatedValue; }
    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
