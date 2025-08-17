package com.blaybus.demo.api;

import com.blaybus.demo.command.CreateMemberCommand;
import com.blaybus.demo.command.IssueMemberToken;
import com.blaybus.demo.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static com.blaybus.demo.EmailGenerator.generateEmail;
import static com.blaybus.demo.PasswordGenerator.generatePassword;

public record TestFixture(
    TestRestTemplate client
) {
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
}
