package com.easybili.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/testenv")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }
}
