package com.acme.dispute.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@RestController
public class FileUploadController {
	@PostMapping("/api/evidence/upload")
	public String upload(@RequestParam MultipartFile file) throws Exception {
		file.transferTo(new File("uploads/" + file.getOriginalFilename()));
		return "uploaded";
	}
}
