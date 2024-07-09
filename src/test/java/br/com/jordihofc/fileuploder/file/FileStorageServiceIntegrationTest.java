package br.com.jordihofc.fileuploder.file;

import br.com.jordihofc.fileuploder.base.S3BasedTest;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class FileStorageServiceIntegrationTest extends S3BasedTest {
    @Autowired
    private S3Template s3Template;
    @Value("${s3.bucket.name}")
    private String bucketName;
    @Value("classpath:uploads/file.txt")
    private Resource file;
    @Autowired
    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        s3Template.createBucket(bucketName);
    }

    @AfterEach
    void tearDown() {
        s3Template.deleteBucket(bucketName);
    }

    @Test
    @DisplayName("Deve fazer o upload de um arquivo")
    void t0() throws IOException {
        //cenario
        MockMultipartFile fileRequest = new MockMultipartFile("data", file.getInputStream());
        FileUpload fileUpload = new FileUpload(fileRequest);
        //acao
        String keyAcessResource = fileStorageService.upload(fileUpload);
        //validacao
        assertTrue(s3Template.objectExists(bucketName, keyAcessResource));
        s3Template.deleteObject(bucketName, keyAcessResource);
    }

    @Test
    @DisplayName("Nao deve fazer o upload de um arquivo caso tenha algum problema em sua abertura")
    void t1() throws Exception {
        //cenario
        MultipartFile fileRequest = createCorruptedFile();
        FileUpload fileUpload = new FileUpload(fileRequest);
        //acao e validacao
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.upload(fileUpload);
        });
        //validacao
        assertEquals("Não foi possivel realizar o upload do documento",exception.getMessage());
    }
}