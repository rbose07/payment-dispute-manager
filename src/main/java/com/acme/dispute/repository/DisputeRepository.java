package com.acme.dispute.repository;

import com.acme.dispute.entity.Dispute;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisputeRepository extends JpaRepository<Dispute, Long> {
    Optional<Dispute> findByIdAndAssignedAnalyst(Long id, String assignedAnalyst);
    List<Dispute> findByCustomerEmailAndAssignedAnalyst(String customerEmail, String assignedAnalyst);
    List<Dispute> findByCustomerEmail(String customerEmail);
}
