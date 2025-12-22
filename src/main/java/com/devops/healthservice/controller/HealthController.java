package com.devops.healthservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Value("${APP_VERSION:1.0}")
    private String version;

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/version")
    public String version() {
        return version;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from DevOps Spring Boot App";
    }
}
