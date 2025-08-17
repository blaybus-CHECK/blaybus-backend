package com.blaybus.demo.api.member.me;

import com.blaybus.demo.api.DemoApiTest;
import com.blaybus.demo.api.TestFixture;
import com.blaybus.demo.view.MemberMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.blaybus.demo.EmailGenerator.generateEmail;
import static com.blaybus.demo.PasswordGenerator.generatePassword;
import static java.util.Objects.*;
import static org.assertj.core.api.Assertions.assertThat;

@DemoApiTest
@DisplayName("GET /api/member/me")
public class GET_specs {
    
    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ){
        // Arrange
        String token = fixture.createMemberThenIssueToken();
        // Act
        ResponseEntity<Void> response = fixture.client().exchange(
            RequestEntity.get("/api/member/me")
                .header("Authorization", "Bearer " + token)
                .build(),
            Void.class
        );
        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
    
    @Test
    void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ){
        // Act
        ResponseEntity<Void> response = fixture.client().exchange(
            RequestEntity.get("/api/member/me").build(),
            Void.class
        );
        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }
    
    @Test
    void 서로_다른_회원의_식별자는_서로_다르다(
        @Autowired TestFixture fixture
    ){
        // Arrange
        String token1 = fixture.createMemberThenIssueToken();
        String token2 = fixture.createMemberThenIssueToken();

        // Act
        ResponseEntity<MemberMeView> response1 = fixture.client().exchange(
            RequestEntity.get("/api/member/me")
                .header("Authorization", "Bearer " + token1)
                .build(),
            MemberMeView.class
        );

        ResponseEntity<MemberMeView> response2 = fixture.client().exchange(
            RequestEntity.get("/api/member/me")
                .header("Authorization", "Bearer " + token2)
                .build(),
            MemberMeView.class
        );

        // Assert
        assertThat(requireNonNull(response1.getBody()).id()).isNotEqualTo(requireNonNull(response2.getBody()).id());
    }
    
    @Test
    void 같은_회원의_식별자는_항상_같다(
        @Autowired TestFixture fixture
    ){
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        fixture.createMember(email, password);

        String token1 = fixture.issueMemberToken(email, password);
        String token2 = fixture.issueMemberToken(email, password);

        // Act
        ResponseEntity<MemberMeView> response1 = fixture.client().exchange(
            RequestEntity.get("/api/member/me")
                .header("Authorization", "Bearer " + token1)
                .build(),
            MemberMeView.class
        );

        ResponseEntity<MemberMeView> response2 = fixture.client().exchange(
            RequestEntity.get("/api/member/me")
                .header("Authorization", "Bearer " + token2)
                .build(),
            MemberMeView.class
        );

        // Assert
        assertThat(requireNonNull(response1.getBody()).id()).isEqualTo(requireNonNull(response2.getBody()).id());
    }
    
    @Test
    void 회원의_기본_정보가_올바르게_설정된다(
        @Autowired TestFixture fixture
    ){
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        fixture.createMember(email, password);
        fixture.setMemberAsDefaultUser(email, password);
        // Act
        ResponseEntity<MemberMeView> response = fixture.client().getForEntity(
            "/api/member/me",
            MemberMeView.class
        );
        // Assert
        MemberMeView actual = response.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual.email()).isEqualTo(email);
    }
}
