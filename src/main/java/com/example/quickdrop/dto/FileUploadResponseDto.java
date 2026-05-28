package com.example.quickdrop.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponseDto {

    private String originalFileName;

    private String message;

    private String shortCode;

    private String downloadUrl;

    private LocalDateTime expiresAt;
}
