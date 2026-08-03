package com.acme.dispute.controller;

import com.acme.dispute.entity.Dispute;
import com.acme.dispute.service.DisputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/disputes")
@RequiredArgsConstructor
public class DisputeController {
	private final DisputeService service;

	@GetMapping("/{id}")
	public Dispute get(@PathVariable Long id){
		return service.find(id).orElseThrow();
	}

	@GetMapping("/search")
	public List<Map<String,Object>> search(@RequestParam String email){
		return service.searchByEmail(email);
	}
}
