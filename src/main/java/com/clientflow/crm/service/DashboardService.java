
package com.clientflow.crm.service;

import com.clientflow.crm.entity.Customer;
import com.clientflow.crm.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;

    public DashboardService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public DashboardData getDashboardData() {
        LocalDateTime monthStart = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();

        long total = customerRepository.countByArchivedFalse();
        long active = customerRepository
                .countByArchivedFalseAndStatus("ACTIVE");
        long inactive = customerRepository
                .countByArchivedFalseAndStatus("INACTIVE");
        long newThisMonth = customerRepository
                .countByArchivedFalseAndCreatedAtGreaterThanEqual(monthStart);

        List<Customer> recentCustomers = customerRepository
                .findTop5ByArchivedFalseOrderByCreatedAtDesc();

        return new DashboardData(
                total,
                active,
                inactive,
                newThisMonth,
                recentCustomers
        );
    }

    public record DashboardData(
            long totalCustomers,
            long activeCustomers,
            long inactiveCustomers,
            long newCustomersThisMonth,
            List<Customer> recentCustomers
    ) {
    }
}
