package com.example.jobbug;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping
    public String welcome() {
        return "Welcome to JOBBUG!";
    }
}
