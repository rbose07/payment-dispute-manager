package com.acme.dispute.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Dispute {
    @Id
    @GeneratedValue
    private Long id;
    private String transactionId;
    private String cardLastFour;
    private String customerEmail;
    private String assignedAnalyst;
}
