package org.itmo.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;
    
    private volatile boolean simulateFailure = false;
    
    public void setSimulateFailure(boolean simulate) {
        this.simulateFailure = simulate;
        log.warn("MinIO failure simulation: {}", simulate ? "ON" : "OFF");
    }
    
    public boolean isSimulateFailure() {
        return simulateFailure;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (simulateFailure) {
            throw new IOException("Simulated MinIO failure");
        }
        String fileKey = generateFileKey(file.getOriginalFilename());
        
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return fileKey;
        } catch (Exception e) {
            throw new IOException("Failed to upload file to MinIO", e);
        }
    }

    public String uploadBytes(String fileName, byte[] content, String contentType) throws IOException {
        if (simulateFailure) {
            throw new IOException("Simulated MinIO failure");
        }
        String fileKey = generateFileKey(fileName);
        
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .stream(inputStream, content.length, -1)
                            .contentType(contentType)
                            .build()
            );
            return fileKey;
        } catch (Exception e) {
            throw new IOException("Failed to upload bytes to MinIO", e);
        }
    }

    public InputStream downloadFile(String fileKey) throws IOException {
        if (simulateFailure) {
            throw new IOException("Simulated MinIO failure");
        }
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Failed to download file from MinIO", e);
        }
    }

    public void deleteFile(String fileKey) throws IOException {
        if (simulateFailure) {
            throw new IOException("Simulated MinIO failure");
        }
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Failed to delete file from MinIO", e);
        }
    }

    public boolean fileExists(String fileKey) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public StatObjectResponse getFileMetadata(String fileKey) throws IOException {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileKey)
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Failed to get file metadata from MinIO", e);
        }
    }

    private String generateFileKey(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        if (originalFilename == null || originalFilename.isEmpty()) {
            return uuid;
        }
        return uuid + "-" + originalFilename;
    }
}
