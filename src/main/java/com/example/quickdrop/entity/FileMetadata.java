package com.example.quickdrop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "files",
        indexes = {
                @Index(name = "idx_shortcode", columnList = "shortCode")
        }
)
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalFileName;

    private String storedFileName;

    @Column(unique = true, nullable = false)
    private String shortCode;

    private String contentType;

    private Long fileSize;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    private Long downloadCount;

    private Boolean oneTimeDownload;

    private Boolean deleteAfterDownload;
}
