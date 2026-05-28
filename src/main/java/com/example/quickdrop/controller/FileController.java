package com.example.quickdrop.controller;

import com.example.quickdrop.dto.FileInfoResponseDto;
import com.example.quickdrop.dto.FileUploadResponseDto;
import com.example.quickdrop.entity.ExpiryTime;
import com.example.quickdrop.entity.FileMetadata;
import com.example.quickdrop.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public FileUploadResponseDto uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "oneTimeDownload",
                    defaultValue = "false")
            Boolean oneTimeDownload,
            @RequestParam(value = "expiryTime",
                    defaultValue = "ONE_HOUR")
            ExpiryTime expiryTime
    ) throws IOException{
        return fileService.uploadFile(file, oneTimeDownload, expiryTime);
    }

    @GetMapping("/info/{shortCode}")
    public FileInfoResponseDto getFileInfo(@PathVariable String shortCode){
        return fileService.getFileInfo(shortCode);
    }
}
