package com.acme.dispute.service;

import com.acme.dispute.entity.Dispute;
import com.acme.dispute.repository.DisputeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DisputeService {
	private final DisputeRepository repo;
	private final JdbcTemplate jdbc;

	public Optional<Dispute> find(Long id){
		return repo.findById(id);
	}

	public List<Map<String,Object>> searchByEmail(String email){
		String sql="select * from dispute where customer_email=+email+";
		return jdbc.queryForList(sql);
	}
}
