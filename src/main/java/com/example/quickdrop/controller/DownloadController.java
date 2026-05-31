package com.example.quickdrop.controller;

import com.example.quickdrop.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class DownloadController {
    private final FileService fileService;

    public DownloadController(FileService fileService){
        this.fileService = fileService;
    }

    @GetMapping("/download/{shortCode}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String shortCode) throws IOException {
        return fileService.downloadFile(shortCode);
    }
}
