package com.clientflow.crm.service;

import com.clientflow.crm.dto.CustomerForm;
import com.clientflow.crm.entity.Customer;
import com.clientflow.crm.repository.CustomerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Page<Customer> searchCustomers(
            String keyword,
            String status,
            Pageable pageable) {

        String normalizedKeyword = normalize(keyword);
        String normalizedStatus = normalize(status);

        if (normalizedStatus != null
                && !normalizedStatus.matches("ACTIVE|PENDING|INACTIVE")) {
            normalizedStatus = null;
        }

        return customerRepository.searchCustomers(
                normalizedKeyword,
                normalizedStatus,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll()
                .stream()
                .filter(customer -> !customer.isArchived())
                .toList();
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(Long id) {
        return customerRepository.findByIdAndArchivedFalse(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Customer not found.")
                );
    }

    @Transactional
    public Customer createCustomer(CustomerForm form) {
        String email = normalize(form.getEmail());

        if (customerRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateCustomerEmailException();
        }

        Customer customer = new Customer();
        applyForm(customer, form);

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, CustomerForm form) {
        Customer customer = getCustomer(id);
        String email = normalize(form.getEmail());

        if (customerRepository.existsByEmailIgnoreCaseAndIdNot(
                email, id)) {
            throw new DuplicateCustomerEmailException();
        }

        applyForm(customer, form);

        return customerRepository.save(customer);
    }

    @Transactional
    public void archiveCustomer(Long id) {
        Customer customer = getCustomer(id);

        customer.setArchived(true);

        customerRepository.save(customer);
    }

    private void applyForm(Customer customer, CustomerForm form) {
        customer.setName(form.getName().trim());
        customer.setCompany(normalize(form.getCompany()));
        customer.setEmail(normalize(form.getEmail()));
        customer.setPhone(normalize(form.getPhone()));

        String status = normalize(form.getStatus());

        customer.setStatus(
                status == null
                        ? "ACTIVE"
                        : status.toUpperCase(Locale.ROOT)
        );
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    public static class DuplicateCustomerEmailException
            extends RuntimeException {
    }
}