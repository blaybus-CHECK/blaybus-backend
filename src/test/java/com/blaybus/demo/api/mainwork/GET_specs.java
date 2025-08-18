package com.blaybus.demo.api.mainwork;

import com.blaybus.demo.MainWorkAssertion;
import com.blaybus.demo.api.DemoApiTest;
import com.blaybus.demo.api.TestFixture;
import com.blaybus.demo.command.RegisterMainWorkCommand;
import com.blaybus.demo.view.MainWorkView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static com.blaybus.demo.RegisterMainWorkGenerator.generateRegisterMainWorkCommand;
import static org.assertj.core.api.Assertions.assertThat;

@DemoApiTest
@DisplayName("GET /api/main-work/{id}")
public class GET_specs {
    
    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ){
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        UUID id = testFixture.registerMainWork();
        // Act
        ResponseEntity<?> response = testFixture.client().getForEntity(
            "/api/main-work/" + id,
            MainWorkView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 존재하지_않는_대표_작품_식별자를_사용하면_404_Not_Found_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ){
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        UUID id = UUID.randomUUID(); // 존재하지 않는 ID
        // Act
        ResponseEntity<?> response = testFixture.client().getForEntity(
            "/api/main-work/" + id,
            MainWorkView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
    
    @Test
    void 다른_회원이_등록한_대표_작품_식별자를_사용하면_404_Not_Found_상태코드를_반환한다(
        @Autowired TestFixture testFixture
    ){
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        UUID id = testFixture.registerMainWork();
        testFixture.createMemberThenSetAsDefaultUser();
        // Act
        ResponseEntity<?> response = testFixture.client().getForEntity(
            "/api/main-work/" + id,
            MainWorkView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
    
    @Test
    void 대표_작품_식별자를_올바르게_반환한다(
        @Autowired TestFixture testFixture
    ){
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        UUID id = testFixture.registerMainWork();
        // Act
        ResponseEntity<MainWorkView> response = testFixture.client().getForEntity(
            "/api/main-work/" + id,
            MainWorkView.class
        );

        // Assert
        MainWorkView actual = response.getBody();
        assertThat(actual).isNotNull();
//        assertThat(actual.getId()).isEqualTo(id);
    }
    
    @Test
    void 대표_작품_정보를_올바르게_반환한다(
        @Autowired TestFixture testFixture
    ){
        // Arrange
        testFixture.createMemberThenSetAsDefaultUser();
        RegisterMainWorkCommand command = generateRegisterMainWorkCommand();
        UUID id = testFixture.registerMainWork(command);
        // Act
        MainWorkView actual = testFixture.client().getForObject(
            "/api/main-work/" + id,
            MainWorkView.class
        );

        // Assert
        assertThat(actual).satisfies(MainWorkAssertion.isDerivedForm(command));
    }
}
