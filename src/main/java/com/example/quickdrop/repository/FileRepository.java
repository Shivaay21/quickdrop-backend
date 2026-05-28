package com.example.quickdrop.repository;

import com.example.quickdrop.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface FileRepository extends JpaRepository<FileMetadata,Long> {
    Optional<FileMetadata> findByShortCode(String shortcode);
    List<FileMetadata> findByExpiresAtBefore(LocalDateTime time);
}
