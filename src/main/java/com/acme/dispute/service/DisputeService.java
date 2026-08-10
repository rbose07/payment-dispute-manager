package com.acme.dispute.service;

import com.acme.dispute.dto.DisputeResponse;
import com.acme.dispute.entity.Dispute;
import com.acme.dispute.repository.DisputeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisputeService {
    private final DisputeRepository repo;
    private final SecurityAuditService audit;

    public DisputeResponse findAuthorized(Long id, String username, boolean admin) {
        Dispute dispute = admin
                ? repo.findById(id).orElseThrow()
                : repo.findByIdAndAssignedAnalyst(id, username)
                        .orElseThrow(() -> new AccessDeniedException("Dispute access denied"));
        audit.record(username, "READ_DISPUTE", "dispute:" + id, "SUCCESS");
        return toResponse(dispute);
    }

    public List<DisputeResponse> searchAuthorized(String email, String username, boolean admin) {
        List<Dispute> disputes = admin
                ? repo.findByCustomerEmail(email)
                : repo.findByCustomerEmailAndAssignedAnalyst(email, username);
        audit.record(username, "SEARCH_DISPUTE", "resultCount:" + disputes.size(), "SUCCESS");
        return disputes.stream().map(this::toResponse).toList();
    }

    private DisputeResponse toResponse(Dispute dispute) {
        return new DisputeResponse(dispute.getId(), dispute.getTransactionId(),
                dispute.getCardLastFour(), dispute.getCustomerEmail(), dispute.getAssignedAnalyst());
    }
}
