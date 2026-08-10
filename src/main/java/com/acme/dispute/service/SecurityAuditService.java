package com.acme.dispute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SecurityAuditService {
    private static final Logger AUDIT = LoggerFactory.getLogger("SECURITY_AUDIT");

    public void record(String user, String action, String resource, String outcome) {
        AUDIT.info("security_event user={} action={} resource={} outcome={}",
                safe(user), safe(action), safe(resource), safe(outcome));
    }

    private String safe(String value) {
        return value == null ? "unknown" : value.replaceAll("[\\r\\n\\t]", "_");
    }
}
