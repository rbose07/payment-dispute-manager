package com.acme.dispute.dto;

public record DisputeResponse(
        Long id,
        String transactionId,
        String cardLastFour,
        String customerEmail,
        String assignedAnalyst) {
}
