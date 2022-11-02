package com.web.springboot.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public String healthCheck() {
        return "Service is running...";
    }
}
