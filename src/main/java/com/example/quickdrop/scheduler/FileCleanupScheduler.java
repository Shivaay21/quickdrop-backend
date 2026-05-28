package com.example.quickdrop.scheduler;

import com.example.quickdrop.entity.FileMetadata;
import com.example.quickdrop.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class FileCleanupScheduler {
    private final FileRepository fileRepository;

    public FileCleanupScheduler(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    private static final Path UPLOAD_DIR =
            Paths.get("uploads").toAbsolutePath().normalize();

    @Scheduled(cron = "${app.cleanup.cron}")
    public void cleanupExpiredFiles(){
        List<FileMetadata> expiredFiles =
                fileRepository.findByExpiresAtBefore(LocalDateTime.now());

        for(FileMetadata file : expiredFiles){
            try {
                Path filePath = UPLOAD_DIR.resolve(file.getStoredFileName());

                Files.deleteIfExists(filePath);

                fileRepository.delete(file);

                log.info("Deleted expired file: {}", file.getStoredFileName());
            }catch (IOException e){
                log.error("Failed to delete file: {}",
                        file.getStoredFileName());
            }
        }
        List<FileMetadata> oneTimeFiles =  fileRepository.findAll()
                .stream()
                .filter(FileMetadata::getDeleteAfterDownload)
                .toList();

        for (FileMetadata file : oneTimeFiles){
            try {
                Path filePath = UPLOAD_DIR.resolve((file.getStoredFileName()));
                Files.deleteIfExists(filePath);
                fileRepository.delete(file);
                log.info("Deleted one time file: {}",file.getStoredFileName());
            }catch (IOException e){
                log.error("Failed to deleted one-time file: {}", file.getStoredFileName());
            }
        }
    }
}
