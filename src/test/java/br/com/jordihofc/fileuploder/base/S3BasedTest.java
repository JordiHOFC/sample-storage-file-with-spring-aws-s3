package br.com.jordihofc.fileuploder.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

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
}
