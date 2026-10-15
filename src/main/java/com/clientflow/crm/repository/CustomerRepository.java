package com.clientflow.crm.repository;

import com.clientflow.crm.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(
            String email,
            Long id
    );

    Optional<Customer> findByIdAndArchivedFalse(Long id);

    @Query("""
        SELECT c FROM Customer c
        WHERE c.archived = false
          AND (
            :keyword IS NULL
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.company) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR c.phone LIKE CONCAT('%', :keyword, '%')
          )
          AND (:status IS NULL OR c.status = :status)
        """)
    Page<Customer> searchCustomers(
            @Param("keyword") String keyword,
            @Param("status") String status,
            Pageable pageable
    );


    ///


    long countByArchivedFalse();

    long countByArchivedFalseAndStatus(String status);

    long countByArchivedFalseAndCreatedAtGreaterThanEqual(
            java.time.LocalDateTime start);

    java.util.List<Customer>
    findTop5ByArchivedFalseOrderByCreatedAtDesc();


}