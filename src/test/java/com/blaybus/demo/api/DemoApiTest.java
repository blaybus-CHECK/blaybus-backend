package com.blaybus.demo.api;

import com.blaybus.demo.DemoApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        DemoApplication.class,
        TestFixtureConfiguration.class,
        PasswordEncoderConfiguration.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public @interface DemoApiTest {

}
