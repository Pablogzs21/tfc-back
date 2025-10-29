package com.tuproject.tfcback.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
            "status", "OK",
            "service", "tfc-back",
            "message", "Backend desplegado correctamente 🚀"
        );
    }
}
