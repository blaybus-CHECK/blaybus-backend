package com.blaybus.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DemoApplication.class)
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void PasswordEncoder_빈이_올바르게_설정된다(
        @Autowired PasswordEncoder actual
    ) {
		assertThat(actual).isInstanceOf(Pbkdf2PasswordEncoder.class);
    }

}
