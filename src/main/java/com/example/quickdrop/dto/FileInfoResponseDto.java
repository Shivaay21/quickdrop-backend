package com.example.quickdrop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfoResponseDto {
    private String fileName;

    private Long fileSize;

    private String expiresAt;

    private String downloadUrl;
}
