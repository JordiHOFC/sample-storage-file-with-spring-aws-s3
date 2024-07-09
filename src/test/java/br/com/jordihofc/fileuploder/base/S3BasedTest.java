package br.com.jordihofc.fileuploder.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = BEFORE_CLASS)
public abstract class S3BasedTest {
    @Container
    static LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack")
    ).withServices(S3);

    @DynamicPropertySource
    static void registerS3Properties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.endpoint",
                () -> LOCALSTACK_CONTAINER.getEndpointOverride(S3).toString());
        registry.add("spring.cloud.aws.region.static",
                () -> LOCALSTACK_CONTAINER.getRegion().toString());
        registry.add("spring.cloud.aws.credentials.access-key",
                () -> LOCALSTACK_CONTAINER.getAccessKey().toString());
        registry.add("spring.cloud.aws.credentials.secret-key",
                () -> LOCALSTACK_CONTAINER.getSecretKey().toString());
    }

    protected MultipartFile createCorruptedFile() {
        return new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                throw new IOException("Error ao abrir o arquivo");
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
            }
        };
    }
}
