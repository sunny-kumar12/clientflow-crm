
package com.clientflow.crm.repository;

import com.clientflow.crm.entity.Lead;
import com.clientflow.crm.entity.Lead.LeadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeadRepository extends JpaRepository<Lead, Long> {

    @Query("""
        SELECT l FROM Lead l
        WHERE l.archived = false
          AND (
            :keyword IS NULL
            OR LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(l.company) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(l.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
          AND (:status IS NULL OR l.status = :status)
        """)
    Page<Lead> searchLeads(
            @Param("keyword") String keyword,
            @Param("status") LeadStatus status,
            Pageable pageable);

    java.util.Optional<Lead> findByIdAndArchivedFalse(Long id);

    long countByArchivedFalse();

    long countByArchivedFalseAndStatus(LeadStatus status);


    java.util.List<Lead> findAllByArchivedFalseOrderByCreatedAtDesc();

}
