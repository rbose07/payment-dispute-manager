package com.acme.dispute.entity;

import jakarta.persistence.*;
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
	private String cardNumber;
	private String customerEmail;
	private String assignedAnalyst;
}
