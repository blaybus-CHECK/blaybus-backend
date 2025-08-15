package com.blaybus.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity

            .ok("{ name: 이세현 }");
    }

    @GetMapping("/api/user")
    public String user(){
        return "user";
    }
}
