package com.example.quickdrop.service;

import com.example.quickdrop.dto.FileInfoResponseDto;
import com.example.quickdrop.dto.FileUploadResponseDto;
import com.example.quickdrop.entity.ExpiryTime;
import com.example.quickdrop.entity.FileMetadata;
import com.example.quickdrop.exception.FileExpiredException;
import com.example.quickdrop.exception.InvalidFileException;
import com.example.quickdrop.repository.FileRepository;
import com.example.quickdrop.util.Base62Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.quickdrop.exception.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;
    @Value("${app.file.max-size}")
    private long maxFileSize;

    @Value("${app.file.expiry-hours}")
    private long expiryHours;

    @Value("${app.base-url}")
    private String baseUrl;

    public FileService(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    private static final List<String> ALLOWED_TYPES = List.of(
            "application/pdf",
            "image/png",
            "image/jpeg"
    );

    private static final Path UPLOAD_DIR =
            Paths.get("uploads").toAbsolutePath().normalize();

    public FileUploadResponseDto uploadFile(MultipartFile file, Boolean oneTimeDownload, ExpiryTime expiryTime) throws IOException{

        if(file.isEmpty()){
            throw new InvalidFileException("File is empty");
        }

        if(file.getSize() > maxFileSize){
            throw new InvalidFileException("File size exceeds 25 MB");
        }

        if(!ALLOWED_TYPES.contains((file.getContentType()))){
            throw new InvalidFileException("Unsupported file type");
        }

        String originalFileName = file.getOriginalFilename();

        String extension = "";

        if(originalFileName != null && originalFileName.contains(".")){
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String storedFileName = UUID.randomUUID() + extension;

        Files.createDirectories(UPLOAD_DIR);

        Path filePath = UPLOAD_DIR.resolve(storedFileName);

        file.transferTo(filePath.toFile());

        FileMetadata fileMetadata = FileMetadata.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(getExpiryMinutes(expiryTime)))
                .downloadCount(0L)
                .oneTimeDownload(oneTimeDownload)
                .build();

        FileMetadata saved = fileRepository.save(fileMetadata);

        String shortCode = Base62Util.encode(saved.getId());

        saved.setShortCode(shortCode);

        fileRepository.save(saved);

        return FileUploadResponseDto.builder()
                .message("File uploaded successfully")
                .originalFileName(originalFileName)
                .shortCode(shortCode)
                .downloadUrl(baseUrl+ "/f/" + shortCode)
                .build();
    }

    public ResponseEntity<Resource> downloadFile(String shortCode) throws IOException {
        FileMetadata fileMetadata = fileRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        if (fileMetadata.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new FileExpiredException("Link Expired");
        }

        Path filePath = UPLOAD_DIR.resolve(fileMetadata.getStoredFileName());

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found on server");
        }

        Resource resource = new UrlResource(filePath.toUri());

        fileMetadata.setDownloadCount(
                fileMetadata.getDownloadCount() == null ? 1L : fileMetadata.getDownloadCount() + 1
        );

        fileRepository.save(fileMetadata);

        if (Boolean.TRUE.equals(fileMetadata.getOneTimeDownload())) {
            fileMetadata.setDeleteAfterDownload(true);
            fileRepository.save(fileMetadata);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileMetadata.getOriginalFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE,
                        fileMetadata.getContentType())
                .body(resource);
    }

    private long getExpiryMinutes(ExpiryTime expiryTime){

        return switch (expiryTime){
            case TEN_MINUTES -> 10;
            case ONE_HOUR -> 60;
            case TWENTY_FOUR_HOURS -> 1440;
        };
    }

    public FileInfoResponseDto getFileInfo(String shortCode){
        FileMetadata fileMetadata = fileRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        return FileInfoResponseDto.builder()
                .fileName(fileMetadata.getOriginalFileName())
                .fileSize(fileMetadata.getFileSize())
                .expiresAt(fileMetadata.getExpiresAt().toString())
                .downloadUrl(baseUrl+ "/f/" +shortCode)
                .build();
    }
}
