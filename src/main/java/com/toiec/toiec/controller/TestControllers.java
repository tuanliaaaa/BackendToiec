package com.toiec.toiec.controller;


import com.toiec.toiec.utils.WordToHtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/docx-to-html")
public class TestControllers {


    @PostMapping("/convert")
    public ResponseEntity<String> convertDocxToHtml(@RequestParam("file") MultipartFile file) {
        try {
            String htmlContent = WordToHtmlUtils.covertToHtml(file);
            return ResponseEntity.ok(htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }
}
