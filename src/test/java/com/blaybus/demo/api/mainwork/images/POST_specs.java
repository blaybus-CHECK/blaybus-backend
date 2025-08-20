package com.blaybus.demo.api.mainwork.images;

import com.blaybus.demo.api.DemoApiTest;
import com.blaybus.demo.api.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DemoApiTest
@DisplayName("POST /api/main-work/images/{id}")
public class POST_specs {

    @LocalServerPort
    private int port;


    @Test
    void 올바르게_요청하면_201_Created_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createMemberThenSetAsDefaultUser();
        UUID mainWorkId = fixture.registerMainWork();
        ByteArrayResource[] files = new ByteArrayResource[]{
            generateByteArrayResourceWithFileType(".jpg")
        };
        HttpEntity<MultiValueMap<String, Object>> requestEntity = generateMultiValueMapHttpEntityWithFiles(files);

        // Act
        ResponseEntity<Void> response = fixture.client().exchange(
            "http://localhost:" + port + "/api/main-work/" + mainWorkId + "/images",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        deleteImages();
    }

    private static void deleteImages() {
        String path = "main-work";
        File main = new File(path);
        File[] mainWorkIds = main.listFiles();
        for (File idFile: Objects.requireNonNull(mainWorkIds)){
            File[] imageFolders = idFile.listFiles();
            for(File imageFolder: Objects.requireNonNull(imageFolders)) {
                File[] image = imageFolder.listFiles();
                for(File imageFile: Objects.requireNonNull(image)) {
                    imageFile.delete();
                }
                imageFolder.delete();
            }
            idFile.delete();
        }
        main.delete();
    }

    @Test
    void 대표_작품_식별자가_UUID_형식이_아니면_404_Not_Found_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createMemberThenSetAsDefaultUser();
        ByteArrayResource[] files = new ByteArrayResource[]{
            generateByteArrayResourceWithFileType(".jpg")
        };
        HttpEntity<MultiValueMap<String, Object>> requestEntity = generateMultiValueMapHttpEntityWithFiles(files);

        // Act
        ResponseEntity<Void> response = fixture.client().exchange(
            "http://localhost:" + port + "/api/main-work/" + 1 + "/images",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );


        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 대표_작품이_없는_경우_404_Not_Found_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createMemberThenSetAsDefaultUser();
        UUID mainWorkId = UUID.randomUUID(); // 존재하지 않는 ID
        ByteArrayResource[] files = new ByteArrayResource[]{
            generateByteArrayResourceWithFileType(".jpg")
        };
        HttpEntity<MultiValueMap<String, Object>> requestEntity = generateMultiValueMapHttpEntityWithFiles(files);

        // Act
        ResponseEntity<Void> response = fixture.client().exchange(
            "http://localhost:" + port + "/api/main-work/" + mainWorkId + "/images",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void 이미지가_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createMemberThenSetAsDefaultUser();
        UUID mainWorkId = fixture.registerMainWork();
        ByteArrayResource[] files = new ByteArrayResource[]{};
        HttpEntity<MultiValueMap<String, Object>> requestEntity = generateMultiValueMapHttpEntityWithFiles(files);

        // Act
        ResponseEntity<Void> response = fixture.client().exchange(
            "http://localhost:" + port + "/api/main-work/" + mainWorkId + "/images",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 한_번에_이미지_등록은_최대_4개까지만_허용_가능하다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        UUID mainWorkId = testFixture.registerMainWork();
        ByteArrayResource[] files = new ByteArrayResource[]{
            generateByteArrayResourceWithFileType(".jpg"),
            generateByteArrayResourceWithFileType(".jpg"),
            generateByteArrayResourceWithFileType(".jpg"),
            generateByteArrayResourceWithFileType(".jpg"),
            generateByteArrayResourceWithFileType(".jpg"),
        };
        HttpEntity<MultiValueMap<String, Object>> requestEntity = generateMultiValueMapHttpEntityWithFiles(files);

        // Act
        ResponseEntity<Void> response = testFixture.client().exchange(
            "http://localhost:" + port + "/api/main-work/" + mainWorkId + "/images",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 이미지가_5MB를_초과하면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        UUID mainWorkId = testFixture.registerMainWork();
        ByteArrayResource[] files = new ByteArrayResource[]{
            generateByteArrayResourceWithFileType(".jpg"),
            generateByteArrayResourceWithFileType(".jpg"),
            generateByteArrayResourceWithFileType(".jpg"),
            generateByteArrayResourceWithFileType(".jpg"),
            generateByteArrayResourceWithFileType(".jpg"),
        };
        HttpEntity<MultiValueMap<String, Object>> requestEntity = generateMultiValueMapHttpEntityWithFiles(files);
        // Act
        ResponseEntity<Void> response = testFixture.client().exchange(
            "http://localhost:" + port + "/api/main-work/" + mainWorkId + "/images",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        ".txt",
        ".pdf",
        ".docx",
    })
    void 이미지가_허용되지_않는_MIME_타입이면_400_Bad_Request_상태코드를_반환한다(
        String type,
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        UUID mainWorkId = testFixture.registerMainWork();
        ByteArrayResource[] files = new ByteArrayResource[]{
            generateByteArrayResourceWithFileSize(new byte[5 * 1024 * 1024 + 1]) // 5 MB
        };
        HttpEntity<MultiValueMap<String, Object>> requestEntity = generateMultiValueMapHttpEntityWithFiles(files);
        // Act
        ResponseEntity<Void> response = testFixture.client().exchange(
            "http://localhost:" + port + "/api/main-work/" + mainWorkId + "/images",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );
        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 올바르게_요청하면_파일을_저장하고_등록된_대표_작품_사진_정보에_접근하는_Location_헤더를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createMemberThenSetAsDefaultUser();
        UUID mainWorkId = fixture.registerMainWork();
        ByteArrayResource[] files = new ByteArrayResource[]{
            generateByteArrayResourceWithFileType(".jpg")
        };
        HttpEntity<MultiValueMap<String, Object>> requestEntity = generateMultiValueMapHttpEntityWithFiles(files);

        // Act
        ResponseEntity<Void> response = fixture.client().exchange(
            "http://localhost:" + port + "/api/main-work/" + mainWorkId + "/images",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );

        // Assert
        URI actual = response.getHeaders().getLocation();
        assertThat(actual).isNotNull();
        assertThat(actual.isAbsolute()).isFalse();
        assertThat(actual.getPath()).isEqualTo("main-work/" + mainWorkId + "/images");
    }

    private static HttpEntity<MultiValueMap<String, Object>> generateMultiValueMapHttpEntityWithFiles(ByteArrayResource[] files) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (ByteArrayResource file : files) {
            body.add("files", file);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return requestEntity;
    }

    private static ByteArrayResource generateByteArrayResourceWithFileType(String type) {
        ByteArrayResource fakeImage = new ByteArrayResource("fake-image-data".getBytes()) {
            @Override
            public String getFilename() {
                return "fake-image" + type;
            }
        };
        return fakeImage;
    }

    private static ByteArrayResource generateByteArrayResourceWithFileSize(byte[] fileSize) {
        ByteArrayResource fakeImage = new ByteArrayResource(fileSize) {
            @Override
            public String getFilename() {
                return "fake-image.jpg";
            }
        };
        return fakeImage;
    }
}
