package com.blaybus.demo.api;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

public class TestFixtureConfiguration {

    @Bean
    TestFixture testFixture(TestRestTemplate client) {
        return new TestFixture(client);
    }
}
