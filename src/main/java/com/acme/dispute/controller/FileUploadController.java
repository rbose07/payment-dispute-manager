package com.acme.dispute.controller;

import com.acme.dispute.service.SecurityAuditService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/evidence")
@RequiredArgsConstructor
public class FileUploadController {
    private static final long MAX_BYTES = 5 * 1024 * 1024;
    private static final Map<String, String> EXTENSIONS = Map.of(
            MediaType.APPLICATION_PDF_VALUE, ".pdf",
            MediaType.IMAGE_JPEG_VALUE, ".jpg",
            MediaType.IMAGE_PNG_VALUE, ".png");

    private final SecurityAuditService audit;

    @Value("${app.upload-dir:./secure-uploads}")
    private String uploadDirectory;

    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam MultipartFile file, Principal principal) throws IOException {
        if (file.isEmpty() || file.getSize() > MAX_BYTES || !EXTENSIONS.containsKey(file.getContentType())) {
            audit.record(principal.getName(), "UPLOAD_EVIDENCE", "rejected", "REJECTED");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported evidence file");
        }

        Path root = Path.of(uploadDirectory).toAbsolutePath().normalize();
        Files.createDirectories(root);
        String storageId = UUID.randomUUID() + EXTENSIONS.get(file.getContentType());
        Path destination = root.resolve(storageId).normalize();
        if (!destination.startsWith(root)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid storage path");
        }
        file.transferTo(destination);
        audit.record(principal.getName(), "UPLOAD_EVIDENCE", "evidence:" + storageId, "SUCCESS");
        return Map.of("evidenceId", storageId);
    }
}
