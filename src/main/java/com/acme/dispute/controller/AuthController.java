package com.acme.dispute.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password){
		if("admin".equals(username) && "admin123".equals(password))
			return "SUCCESS";
		return "FAILED";
	}
}
