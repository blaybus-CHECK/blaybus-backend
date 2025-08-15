package com.blaybus.demo.api.member.issutoken;

import com.blaybus.demo.DemoApplication;
import com.blaybus.demo.command.CreateMemberCommand;
import com.blaybus.demo.command.IssueMemberToken;
import com.blaybus.demo.result.AccessTokenCarrier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.blaybus.demo.EmailGenerator.generateEmail;
import static com.blaybus.demo.JwtAssertions.conformsToJwtFormat;
import static com.blaybus.demo.PasswordGenerator.generatePassword;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = { DemoApplication.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /api/member/issueToken")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
            "/api/member/signUp",
            new CreateMemberCommand(email, password),
            Void.class
        );

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/api/member/issueToken",
            new IssueMemberToken(email, password),
            AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 올바르게_요청하면_접근_토큰을_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
            "/api/member/signUp",
            new CreateMemberCommand(email, password),
            Void.class
        );

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/api/member/issueToken",
            new IssueMemberToken(email, password),
            AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isNotNull();
    }

    @Test
    void 접근_토큰은_JWT_형식을_따른다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
            "/api/member/signUp",
            new CreateMemberCommand(email, password),
            Void.class
        );

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/api/member/issueToken",
            new IssueMemberToken(email, password),
            AccessTokenCarrier.class
        );

        // Assert
        String actual = requireNonNull(response.getBody()).accessToken();
        assertThat(actual).satisfies(conformsToJwtFormat());
    }

    @Test
    void 존재하지_않는_이메일_주소가_사용되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/api/member/issueToken",
            new IssueMemberToken(email, password),
            AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 잘못된_비밀번호가_사용되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        String wrongPassword = generatePassword();

        client.postForEntity(
            "/api/member/signUp",
            new CreateMemberCommand(email, password),
            Void.class
        );

        // Act
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
            "/api/member/issueToken",
            new IssueMemberToken(email, wrongPassword),
            AccessTokenCarrier.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
