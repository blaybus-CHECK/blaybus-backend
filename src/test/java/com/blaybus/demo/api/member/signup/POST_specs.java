package com.blaybus.demo.api.member.signup;

import com.blaybus.demo.DemoApplication;
import com.blaybus.demo.command.CreateMemberCommand;
import com.blaybus.demo.entity.Member;
import com.blaybus.demo.entity.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.blaybus.demo.EmailGenerator.generateEmail;
import static com.blaybus.demo.PasswordGenerator.generatePassword;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = { DemoApplication.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /api/member/signUp")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        var command = new CreateMemberCommand(
            generateEmail(),
            "password"
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/api/member/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void email_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        var command = new CreateMemberCommand(
            null,
            "password"
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/api/member/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "invalid-email@",
        "invalid-email@test",
        "invalid-email@test.",
        "invalid-email@.com"
    })
    void email_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
        String email,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        var command = new CreateMemberCommand(
            email,
            "password"
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/api/member/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void password_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        var command = new CreateMemberCommand(
            generateEmail(),
            null
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/api/member/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @MethodSource("com.blaybus.demo.TestDataSource#invalidPasswords")
    void password_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        var command = new CreateMemberCommand(
            generateEmail(),
            password
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/api/member/signUp",
            command,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void email_속성에_이미_존재하는_이메일_주소가_지정되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();

        client.postForEntity(
            "/api/member/signUp",
            new CreateMemberCommand(email, "password"),
            Void.class
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/api/member/signUp",
            new CreateMemberCommand(email, "password"),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 비밀번호를_올바르게_암호화한다(
        @Autowired TestRestTemplate client,
        @Autowired MemberRepository repository,
        @Autowired PasswordEncoder encoder
    ) {
        // Arrange
        var command = new CreateMemberCommand(
            generateEmail(),
            generatePassword()
        );

        // Act
        client.postForEntity("/api/member/signUp", command, Void.class);

        // Assert
        Member member = repository
            .findAll()
            .stream()
            .filter(x -> x.getEmail().equals(command.email()))
            .findFirst()
            .orElseThrow();
        String actual = member.getHashedPassword();
        assertThat(actual).isNotNull();
        assertThat(encoder.matches(command.password(), actual)).isTrue();
    }
}
