package com.blaybus.demo.api;

import com.blaybus.demo.command.CreateMemberCommand;
import com.blaybus.demo.command.IssueMemberToken;
import com.blaybus.demo.command.RegisterMainWorkCommand;
import com.blaybus.demo.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import static com.blaybus.demo.EmailGenerator.generateEmail;
import static com.blaybus.demo.PasswordGenerator.generatePassword;
import static com.blaybus.demo.RegisterMainWorkGenerator.generateRegisterMainWorkCommand;

public record TestFixture(
    TestRestTemplate client
) {
    public static TestFixture create(Environment environment) {
        var client = new TestRestTemplate();
        LocalHostUriTemplateHandler uriTemplateHandler = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateHandler);
        return new TestFixture(client);
    }

    public String createMemberThenIssueToken() {
        String email = generateEmail();
        String password = generatePassword();
        createMember(email, password);
        return issueMemberToken(email, password);
    }

    public void createMember(String email, String password) {
        client.postForEntity(
            "/api/member/signUp",
            new CreateMemberCommand(email, password),
            Void.class);
    }

    public String issueMemberToken(String email, String password) {
        AccessTokenCarrier accessTokenCarrier = client.postForObject(
            "/api/member/issueToken",
            new IssueMemberToken(email, password),
            AccessTokenCarrier.class
        );
        return accessTokenCarrier.accessToken();
    }

    public void setMemberAsDefaultUser(String email, String password) {
        String token = issueMemberToken(email, password);
        client.getRestTemplate().getInterceptors().add((request, body, execution) -> {
                request.getHeaders().setBearerAuth(token);
                return execution.execute(request, body);
            }
        );
    }

    public void createMemberThenSetAsDefaultUser() {
        String email = generateEmail();
        String password = generatePassword();
        createMember(email, password);
        setMemberAsDefaultUser(email, password);
    }

    public UUID registerMainWork() {
        return registerMainWork(generateRegisterMainWorkCommand());
    }

    public UUID registerMainWork(RegisterMainWorkCommand command) {
        ResponseEntity<Void> response = client.postForEntity(
            "/api/main-work",
            command,
            Void.class
        );
        URI location = response.getHeaders().getLocation();
        String path = Objects.requireNonNull(location).getPath();
        String id = path.substring("/api/main-work/".length());
        return UUID.fromString(id);
    }
}
