package br.com.jordihofc.fileuploder.file;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FileStorageServiceUnitTest {
    @Mock
    private S3Template s3Template;
    @Mock
    private MultipartFile file;
    @Mock
    private S3Resource uploaded;
    private String bucketName = "myBucketName";

    @Test
    @DisplayName("Deve fazer o upload de um arquivo")
    void t0() throws IOException {
        //cenario
        FileUpload fileUpload = new FileUpload(file);
        FileStorageService fileStorageService = new FileStorageService(s3Template, bucketName);
        when(s3Template.upload(any(), any(), any())).thenReturn(uploaded);

        //acao
        String acesseKeyFile = fileStorageService.upload(fileUpload);

        //validacao
        assertNotNull(acesseKeyFile);
    }

    @Test
    @DisplayName("Nao deve fazer o upload de um arquivo caso tenha algum problema em sua abertura")
    void t1() throws IOException {
        //cenario
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(IOException.class);
        FileUpload fileUpload = new FileUpload(file);
        FileStorageService fileStorageService = new FileStorageService(s3Template, bucketName);

        //acao e validacao
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            String acesseKeyFile = fileStorageService.upload(fileUpload);

        });
        assertEquals("Não foi possivel realizar o upload do documento", runtimeException.getMessage());
    }
}