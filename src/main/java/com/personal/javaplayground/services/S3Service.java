package com.personal.javaplayground.services;

import com.personal.javaplayground.properties.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
public class S3Service {

    private final AwsProperties awsProperties;
    private final S3Client s3Client;

    @Autowired
    public S3Service(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                awsProperties.getAccessKeyId(),
                awsProperties.getSecretAccessKey()
        );

        this.s3Client = S3Client.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    public String uploadJsonToS3(String json) throws Exception {
        String fileName = UUID.randomUUID() + ".json";

        // Save JSON to a temporary file
        Path tempFile = Files.createTempFile(fileName, ".xml");
        Files.writeString(tempFile, json, StandardOpenOption.WRITE);

        // Upload file to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getS3BucketName())
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, tempFile);

        // Clean up the temporary file
        Files.delete(tempFile);

        return fileName;
    }
}