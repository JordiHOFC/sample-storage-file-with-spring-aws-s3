package br.com.jordihofc.fileuploder.file;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {
    private final S3Template template;
    private final String bucket;

    public FileStorageService(S3Template template, @Value("${s3.bucket.name}") String bucket) {
        this.template = template;
        this.bucket = bucket;
    }

    public String upload(FileUpload fileUpload) {
        try (var file = fileUpload.data().getInputStream()) {
            String key = UUID.randomUUID().toString();
            S3Resource uploaded = template.upload(bucket, key, file);
            return key;
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possivel realizar o upload do documento");
        }
    }
}
