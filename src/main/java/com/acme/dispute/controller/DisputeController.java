package com.acme.dispute.controller;

import com.acme.dispute.dto.DisputeResponse;
import com.acme.dispute.service.DisputeService;
import jakarta.validation.constraints.Email;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/disputes")
@RequiredArgsConstructor
@Validated
public class DisputeController {
    private final DisputeService service;

    @GetMapping("/{id}")
    public DisputeResponse get(@PathVariable Long id, Authentication authentication) {
        return service.findAuthorized(id, authentication.getName(), isAdmin(authentication));
    }

    @GetMapping("/search")
    public List<DisputeResponse> search(@RequestParam @Email String email, Principal principal,
                                        Authentication authentication) {
        return service.searchAuthorized(email, principal.getName(), isAdmin(authentication));
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
