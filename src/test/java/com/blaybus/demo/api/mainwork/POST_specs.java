package com.blaybus.demo.api.mainwork;

import com.blaybus.demo.api.DemoApiTest;
import com.blaybus.demo.api.TestFixture;
import com.blaybus.demo.command.RegisterMainWorkCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.UUID;
import java.util.function.Predicate;

import static com.blaybus.demo.RegisterMainWorkGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

@DemoApiTest
@DisplayName("POST /api/main-work")
public class POST_specs {

    @Test
    void 올바르게_요청하면_201_Created_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        RegisterMainWorkCommand command = generateRegisterMainWorkCommand();
        // Act
        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/api/main-work",
            command,
            Void.class
        );
        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void 제목이_지정되지_않을_경우_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();

        // Act
        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/api/main-work",
            generateRegisterMainWorkCommandWithTitle(null),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 제목이_빈_문자열인_경우_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();

        // Act
        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/api/main-work",
            generateRegisterMainWorkCommandWithTitle(""),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 설명이_지정되지_않을_경우_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();

        // Act
        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/api/main-work",
            generateRegisterMainWorkCommandWithDescription(null),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 설명이_빈_문자열인_경우_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();

        // Act
        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/api/main-work",
            generateRegisterMainWorkCommandWithDescription(""),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 설명이_1000자_이상인_경우_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        String longDescription = "a".repeat(1001); // 1001 characters

        // Act
        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/api/main-work",
            generateRegisterMainWorkCommandWithDescription(longDescription),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 올바르게_요청하면_등록된_대표_작품_정보에_접근하는_Location_헤더를_반환한다(
        @Autowired TestFixture testFixture
    ) {
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        RegisterMainWorkCommand command = generateRegisterMainWorkCommand();

        // Act
        ResponseEntity<Void> response = testFixture.client().postForEntity(
            "/api/main-work",
            command,
            Void.class
        );
        // Assert
        URI actual = response.getHeaders().getLocation();
        assertThat(actual).isNotNull();
        assertThat(actual.isAbsolute()).isFalse();
        assertThat(actual.getPath())
            .startsWith("/api/main-work/")
            .matches(endWithUUID());

    }

    private Predicate<? super String> endWithUUID() {
        return path -> {
            String[] segments = path.split("/");
            String lastSegment = segments[segments.length - 1];
            try {
                UUID.fromString(lastSegment);
                return true;
            } catch (IllegalArgumentException exception) {
                return false;
            }
        };
    }
}
